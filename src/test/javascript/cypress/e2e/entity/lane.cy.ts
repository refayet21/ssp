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

describe('Lane e2e test', () => {
  const lanePageUrl = '/lane';
  const lanePageUrlPattern = new RegExp('/lane(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const laneSample = {};

  let lane;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/lanes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lanes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lanes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (lane) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lanes/${lane.id}`,
      }).then(() => {
        lane = undefined;
      });
    }
  });

  it('Lanes menu should load Lanes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lane');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Lane').should('exist');
    cy.url().should('match', lanePageUrlPattern);
  });

  describe('Lane page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(lanePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Lane page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/lane/new$'));
        cy.getEntityCreateUpdateHeading('Lane');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', lanePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lanes',
          body: laneSample,
        }).then(({ body }) => {
          lane = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lanes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [lane],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(lanePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Lane page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('lane');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', lanePageUrlPattern);
      });

      it('edit button click should load edit Lane page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Lane');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', lanePageUrlPattern);
      });

      it('edit button click should load edit Lane page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Lane');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', lanePageUrlPattern);
      });

      it('last delete button click should delete instance of Lane', () => {
        cy.intercept('GET', '/api/lanes/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('lane').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', lanePageUrlPattern);

        lane = undefined;
      });
    });
  });

  describe('new Lane page', () => {
    beforeEach(() => {
      cy.visit(`${lanePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Lane');
    });

    it('should create an instance of Lane', () => {
      cy.get(`[data-cy="name"]`).type('instead rotating');
      cy.get(`[data-cy="name"]`).should('have.value', 'instead rotating');

      cy.get(`[data-cy="shortName"]`).type('death');
      cy.get(`[data-cy="shortName"]`).should('have.value', 'death');

      cy.get(`[data-cy="direction"]`).select('IN');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        lane = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', lanePageUrlPattern);
    });
  });
});
