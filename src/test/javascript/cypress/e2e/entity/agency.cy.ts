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

describe('Agency e2e test', () => {
  const agencyPageUrl = '/agency';
  const agencyPageUrlPattern = new RegExp('/agency(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const agencySample = { name: 'catalyze' };

  let agency;
  let agencyType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/agency-types',
      body: { name: 'however supposing down', shortName: 'worth', isActive: true },
    }).then(({ body }) => {
      agencyType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agencies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agencies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agencies/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/addresses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/documents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/assignments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/agency-licenses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/departments', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/agency-types', {
      statusCode: 200,
      body: [agencyType],
    });

    cy.intercept('GET', '/api/zones', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/pass-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (agency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agencies/${agency.id}`,
      }).then(() => {
        agency = undefined;
      });
    }
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

  it('Agencies menu should load Agencies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agency');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Agency').should('exist');
    cy.url().should('match', agencyPageUrlPattern);
  });

  describe('Agency page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agencyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Agency page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/agency/new$'));
        cy.getEntityCreateUpdateHeading('Agency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agencies',
          body: {
            ...agencySample,
            agencyType: agencyType,
          },
        }).then(({ body }) => {
          agency = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agencies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/agencies?page=0&size=20>; rel="last",<http://localhost/api/agencies?page=0&size=20>; rel="first"',
              },
              body: [agency],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(agencyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Agency page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agency');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);
      });

      it('edit button click should load edit Agency page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Agency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);
      });

      it('edit button click should load edit Agency page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Agency');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);
      });

      it('last delete button click should delete instance of Agency', () => {
        cy.intercept('GET', '/api/agencies/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('agency').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyPageUrlPattern);

        agency = undefined;
      });
    });
  });

  describe('new Agency page', () => {
    beforeEach(() => {
      cy.visit(`${agencyPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Agency');
    });

    it('should create an instance of Agency', () => {
      cy.get(`[data-cy="name"]`).type('rope');
      cy.get(`[data-cy="name"]`).should('have.value', 'rope');

      cy.get(`[data-cy="shortName"]`).type('breadfruit localise hypothesize');
      cy.get(`[data-cy="shortName"]`).should('have.value', 'breadfruit localise hypothesize');

      cy.get(`[data-cy="isInternal"]`).should('not.be.checked');
      cy.get(`[data-cy="isInternal"]`).click();
      cy.get(`[data-cy="isInternal"]`).should('be.checked');

      cy.get(`[data-cy="isDummy"]`).should('not.be.checked');
      cy.get(`[data-cy="isDummy"]`).click();
      cy.get(`[data-cy="isDummy"]`).should('be.checked');

      cy.get(`[data-cy="agencyType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        agency = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', agencyPageUrlPattern);
    });
  });
});
