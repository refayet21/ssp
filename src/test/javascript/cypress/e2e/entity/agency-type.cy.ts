import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('AgencyType e2e test', () => {
  const agencyTypePageUrl = '/agency-type';
  const agencyTypePageUrlPattern = new RegExp('/agency-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const agencyTypeSample = {};

  let agencyType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agency-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agency-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agency-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (agencyType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agency-types/${agencyType.id}`,
      }).then(() => {
        agencyType = undefined;
      });
    }
  });

  it('AgencyTypes menu should load AgencyTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agency-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AgencyType').should('exist');
    cy.url().should('match', agencyTypePageUrlPattern);
  });

  describe('AgencyType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agencyTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AgencyType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/agency-type/new$'));
        cy.getEntityCreateUpdateHeading('AgencyType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agency-types',
          body: agencyTypeSample,
        }).then(({ body }) => {
          agencyType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agency-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [agencyType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(agencyTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AgencyType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agencyType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyTypePageUrlPattern);
      });

      it('edit button click should load edit AgencyType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgencyType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyTypePageUrlPattern);
      });

      it('edit button click should load edit AgencyType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgencyType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyTypePageUrlPattern);
      });

      it('last delete button click should delete instance of AgencyType', () => {
        cy.intercept('GET', '/api/agency-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('agencyType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyTypePageUrlPattern);

        agencyType = undefined;
      });
    });
  });

  describe('new AgencyType page', () => {
    beforeEach(() => {
      cy.visit(`${agencyTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AgencyType');
    });

    it('should create an instance of AgencyType', () => {
      cy.get(`[data-cy="name"]`).type('hello rod');
      cy.get(`[data-cy="name"]`).should('have.value', 'hello rod');

      cy.get(`[data-cy="shortName"]`).type('rescue');
      cy.get(`[data-cy="shortName"]`).should('have.value', 'rescue');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        agencyType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', agencyTypePageUrlPattern);
    });
  });
});
