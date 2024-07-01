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

describe('Designation e2e test', () => {
  const designationPageUrl = '/designation';
  const designationPageUrlPattern = new RegExp('/designation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const designationSample = { name: 'pierce majestically' };

  let designation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/designations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/designations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/designations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (designation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/designations/${designation.id}`,
      }).then(() => {
        designation = undefined;
      });
    }
  });

  it('Designations menu should load Designations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('designation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Designation').should('exist');
    cy.url().should('match', designationPageUrlPattern);
  });

  describe('Designation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(designationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Designation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/designation/new$'));
        cy.getEntityCreateUpdateHeading('Designation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', designationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/designations',
          body: designationSample,
        }).then(({ body }) => {
          designation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/designations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [designation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(designationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Designation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('designation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', designationPageUrlPattern);
      });

      it('edit button click should load edit Designation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Designation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', designationPageUrlPattern);
      });

      it('edit button click should load edit Designation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Designation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', designationPageUrlPattern);
      });

      it('last delete button click should delete instance of Designation', () => {
        cy.intercept('GET', '/api/designations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('designation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', designationPageUrlPattern);

        designation = undefined;
      });
    });
  });

  describe('new Designation page', () => {
    beforeEach(() => {
      cy.visit(`${designationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Designation');
    });

    it('should create an instance of Designation', () => {
      cy.get(`[data-cy="name"]`).type('zany');
      cy.get(`[data-cy="name"]`).should('have.value', 'zany');

      cy.get(`[data-cy="shortName"]`).type('boastfully barring');
      cy.get(`[data-cy="shortName"]`).should('have.value', 'boastfully barring');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        designation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', designationPageUrlPattern);
    });
  });
});
