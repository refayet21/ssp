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

describe('Ward e2e test', () => {
  const wardPageUrl = '/ward';
  const wardPageUrlPattern = new RegExp('/ward(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const wardSample = { name: 'apud clearly' };

  let ward;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/wards+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/wards').as('postEntityRequest');
    cy.intercept('DELETE', '/api/wards/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ward) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/wards/${ward.id}`,
      }).then(() => {
        ward = undefined;
      });
    }
  });

  it('Wards menu should load Wards page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ward');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ward').should('exist');
    cy.url().should('match', wardPageUrlPattern);
  });

  describe('Ward page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(wardPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ward page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ward/new$'));
        cy.getEntityCreateUpdateHeading('Ward');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wardPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/wards',
          body: wardSample,
        }).then(({ body }) => {
          ward = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/wards+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ward],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(wardPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ward page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ward');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wardPageUrlPattern);
      });

      it('edit button click should load edit Ward page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ward');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wardPageUrlPattern);
      });

      it('edit button click should load edit Ward page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ward');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wardPageUrlPattern);
      });

      it('last delete button click should delete instance of Ward', () => {
        cy.intercept('GET', '/api/wards/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ward').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', wardPageUrlPattern);

        ward = undefined;
      });
    });
  });

  describe('new Ward page', () => {
    beforeEach(() => {
      cy.visit(`${wardPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ward');
    });

    it('should create an instance of Ward', () => {
      cy.get(`[data-cy="name"]`).type('oh inasmuch');
      cy.get(`[data-cy="name"]`).should('have.value', 'oh inasmuch');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        ward = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', wardPageUrlPattern);
    });
  });
});
