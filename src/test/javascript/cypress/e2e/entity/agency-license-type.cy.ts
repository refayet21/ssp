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

describe('AgencyLicenseType e2e test', () => {
  const agencyLicenseTypePageUrl = '/agency-license-type';
  const agencyLicenseTypePageUrlPattern = new RegExp('/agency-license-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const agencyLicenseTypeSample = { name: 'violent spattering until' };

  let agencyLicenseType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agency-license-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agency-license-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agency-license-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (agencyLicenseType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agency-license-types/${agencyLicenseType.id}`,
      }).then(() => {
        agencyLicenseType = undefined;
      });
    }
  });

  it('AgencyLicenseTypes menu should load AgencyLicenseTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agency-license-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AgencyLicenseType').should('exist');
    cy.url().should('match', agencyLicenseTypePageUrlPattern);
  });

  describe('AgencyLicenseType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agencyLicenseTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AgencyLicenseType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/agency-license-type/new$'));
        cy.getEntityCreateUpdateHeading('AgencyLicenseType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicenseTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agency-license-types',
          body: agencyLicenseTypeSample,
        }).then(({ body }) => {
          agencyLicenseType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agency-license-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [agencyLicenseType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(agencyLicenseTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AgencyLicenseType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agencyLicenseType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicenseTypePageUrlPattern);
      });

      it('edit button click should load edit AgencyLicenseType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgencyLicenseType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicenseTypePageUrlPattern);
      });

      it('edit button click should load edit AgencyLicenseType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgencyLicenseType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicenseTypePageUrlPattern);
      });

      it('last delete button click should delete instance of AgencyLicenseType', () => {
        cy.intercept('GET', '/api/agency-license-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('agencyLicenseType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicenseTypePageUrlPattern);

        agencyLicenseType = undefined;
      });
    });
  });

  describe('new AgencyLicenseType page', () => {
    beforeEach(() => {
      cy.visit(`${agencyLicenseTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AgencyLicenseType');
    });

    it('should create an instance of AgencyLicenseType', () => {
      cy.get(`[data-cy="name"]`).type('clump shyly');
      cy.get(`[data-cy="name"]`).should('have.value', 'clump shyly');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        agencyLicenseType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', agencyLicenseTypePageUrlPattern);
    });
  });
});
