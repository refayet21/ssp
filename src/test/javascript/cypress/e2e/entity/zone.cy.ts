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

describe('Zone e2e test', () => {
  const zonePageUrl = '/zone';
  const zonePageUrlPattern = new RegExp('/zone(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const zoneSample = {};

  let zone;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/zones+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/zones').as('postEntityRequest');
    cy.intercept('DELETE', '/api/zones/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (zone) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/zones/${zone.id}`,
      }).then(() => {
        zone = undefined;
      });
    }
  });

  it('Zones menu should load Zones page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('zone');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Zone').should('exist');
    cy.url().should('match', zonePageUrlPattern);
  });

  describe('Zone page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(zonePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Zone page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/zone/new$'));
        cy.getEntityCreateUpdateHeading('Zone');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', zonePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/zones',
          body: zoneSample,
        }).then(({ body }) => {
          zone = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/zones+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [zone],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(zonePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Zone page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('zone');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', zonePageUrlPattern);
      });

      it('edit button click should load edit Zone page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Zone');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', zonePageUrlPattern);
      });

      it('edit button click should load edit Zone page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Zone');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', zonePageUrlPattern);
      });

      it('last delete button click should delete instance of Zone', () => {
        cy.intercept('GET', '/api/zones/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('zone').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', zonePageUrlPattern);

        zone = undefined;
      });
    });
  });

  describe('new Zone page', () => {
    beforeEach(() => {
      cy.visit(`${zonePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Zone');
    });

    it('should create an instance of Zone', () => {
      cy.get(`[data-cy="name"]`).type('author astride');
      cy.get(`[data-cy="name"]`).should('have.value', 'author astride');

      cy.get(`[data-cy="shortName"]`).type('fritter');
      cy.get(`[data-cy="shortName"]`).should('have.value', 'fritter');

      cy.get(`[data-cy="location"]`).type('once galumph prime');
      cy.get(`[data-cy="location"]`).should('have.value', 'once galumph prime');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        zone = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', zonePageUrlPattern);
    });
  });
});
