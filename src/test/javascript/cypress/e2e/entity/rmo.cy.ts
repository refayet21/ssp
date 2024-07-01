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

describe('RMO e2e test', () => {
  const rMOPageUrl = '/rmo';
  const rMOPageUrlPattern = new RegExp('/rmo(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rMOSample = { name: 'around uh-huh', code: 'advanced resonate' };

  let rMO;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rmos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rmos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rmos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rMO) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rmos/${rMO.id}`,
      }).then(() => {
        rMO = undefined;
      });
    }
  });

  it('RMOS menu should load RMOS page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rmo');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RMO').should('exist');
    cy.url().should('match', rMOPageUrlPattern);
  });

  describe('RMO page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rMOPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RMO page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/rmo/new$'));
        cy.getEntityCreateUpdateHeading('RMO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', rMOPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rmos',
          body: rMOSample,
        }).then(({ body }) => {
          rMO = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rmos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [rMO],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(rMOPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RMO page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rMO');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', rMOPageUrlPattern);
      });

      it('edit button click should load edit RMO page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RMO');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', rMOPageUrlPattern);
      });

      it('edit button click should load edit RMO page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RMO');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', rMOPageUrlPattern);
      });

      it('last delete button click should delete instance of RMO', () => {
        cy.intercept('GET', '/api/rmos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('rMO').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', rMOPageUrlPattern);

        rMO = undefined;
      });
    });
  });

  describe('new RMO page', () => {
    beforeEach(() => {
      cy.visit(`${rMOPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RMO');
    });

    it('should create an instance of RMO', () => {
      cy.get(`[data-cy="name"]`).type('however and into');
      cy.get(`[data-cy="name"]`).should('have.value', 'however and into');

      cy.get(`[data-cy="code"]`).type('elementary how strictly');
      cy.get(`[data-cy="code"]`).should('have.value', 'elementary how strictly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        rMO = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', rMOPageUrlPattern);
    });
  });
});
