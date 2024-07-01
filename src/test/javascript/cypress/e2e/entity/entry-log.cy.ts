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

describe('EntryLog e2e test', () => {
  const entryLogPageUrl = '/entry-log';
  const entryLogPageUrlPattern = new RegExp('/entry-log(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const entryLogSample = {};

  let entryLog;
  let lane;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/lanes',
      body: { name: 'upwardly euphonium', shortName: 'dioxide nor brr', direction: 'IN', isActive: true },
    }).then(({ body }) => {
      lane = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/entry-logs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/entry-logs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/entry-logs/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/passes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/lanes', {
      statusCode: 200,
      body: [lane],
    });
  });

  afterEach(() => {
    if (entryLog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/entry-logs/${entryLog.id}`,
      }).then(() => {
        entryLog = undefined;
      });
    }
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

  it('EntryLogs menu should load EntryLogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('entry-log');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EntryLog').should('exist');
    cy.url().should('match', entryLogPageUrlPattern);
  });

  describe('EntryLog page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(entryLogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EntryLog page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/entry-log/new$'));
        cy.getEntityCreateUpdateHeading('EntryLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', entryLogPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/entry-logs',
          body: {
            ...entryLogSample,
            lane: lane,
          },
        }).then(({ body }) => {
          entryLog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/entry-logs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/entry-logs?page=0&size=20>; rel="last",<http://localhost/api/entry-logs?page=0&size=20>; rel="first"',
              },
              body: [entryLog],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(entryLogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EntryLog page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('entryLog');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', entryLogPageUrlPattern);
      });

      it('edit button click should load edit EntryLog page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntryLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', entryLogPageUrlPattern);
      });

      it('edit button click should load edit EntryLog page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EntryLog');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', entryLogPageUrlPattern);
      });

      it('last delete button click should delete instance of EntryLog', () => {
        cy.intercept('GET', '/api/entry-logs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('entryLog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', entryLogPageUrlPattern);

        entryLog = undefined;
      });
    });
  });

  describe('new EntryLog page', () => {
    beforeEach(() => {
      cy.visit(`${entryLogPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EntryLog');
    });

    it('should create an instance of EntryLog', () => {
      cy.get(`[data-cy="eventTime"]`).type('2024-07-01T03:45');
      cy.get(`[data-cy="eventTime"]`).blur();
      cy.get(`[data-cy="eventTime"]`).should('have.value', '2024-07-01T03:45');

      cy.get(`[data-cy="direction"]`).select('OUT');

      cy.get(`[data-cy="passStatus"]`).select('PRINTED');

      cy.get(`[data-cy="actionType"]`).select('ALLOW');

      cy.get(`[data-cy="lane"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        entryLog = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', entryLogPageUrlPattern);
    });
  });
});
