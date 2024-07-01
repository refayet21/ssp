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

describe('Gate e2e test', () => {
  const gatePageUrl = '/gate';
  const gatePageUrlPattern = new RegExp('/gate(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const gateSample = {};

  let gate;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/gates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/gates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/gates/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (gate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/gates/${gate.id}`,
      }).then(() => {
        gate = undefined;
      });
    }
  });

  it('Gates menu should load Gates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('gate');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Gate').should('exist');
    cy.url().should('match', gatePageUrlPattern);
  });

  describe('Gate page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(gatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Gate page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/gate/new$'));
        cy.getEntityCreateUpdateHeading('Gate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', gatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/gates',
          body: gateSample,
        }).then(({ body }) => {
          gate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/gates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [gate],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(gatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Gate page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('gate');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', gatePageUrlPattern);
      });

      it('edit button click should load edit Gate page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Gate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', gatePageUrlPattern);
      });

      it('edit button click should load edit Gate page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Gate');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', gatePageUrlPattern);
      });

      it('last delete button click should delete instance of Gate', () => {
        cy.intercept('GET', '/api/gates/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('gate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', gatePageUrlPattern);

        gate = undefined;
      });
    });
  });

  describe('new Gate page', () => {
    beforeEach(() => {
      cy.visit(`${gatePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Gate');
    });

    it('should create an instance of Gate', () => {
      cy.get(`[data-cy="name"]`).type('probate aspire about');
      cy.get(`[data-cy="name"]`).should('have.value', 'probate aspire about');

      cy.get(`[data-cy="shortName"]`).type('correctly');
      cy.get(`[data-cy="shortName"]`).should('have.value', 'correctly');

      cy.get(`[data-cy="lat"]`).type('20003.96');
      cy.get(`[data-cy="lat"]`).should('have.value', '20003.96');

      cy.get(`[data-cy="lon"]`).type('14726.5');
      cy.get(`[data-cy="lon"]`).should('have.value', '14726.5');

      cy.get(`[data-cy="gateType"]`).select('HUMAN');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        gate = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', gatePageUrlPattern);
    });
  });
});
