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

describe('CityCorpPoura e2e test', () => {
  const cityCorpPouraPageUrl = '/city-corp-poura';
  const cityCorpPouraPageUrlPattern = new RegExp('/city-corp-poura(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cityCorpPouraSample = { name: 'staid once' };

  let cityCorpPoura;
  let rMO;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/rmos',
      body: { name: 'because reef catalysis', code: 'following but' },
    }).then(({ body }) => {
      rMO = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/city-corp-pouras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/city-corp-pouras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/city-corp-pouras/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/wards', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/addresses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/districts', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/upazilas', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/rmos', {
      statusCode: 200,
      body: [rMO],
    });
  });

  afterEach(() => {
    if (cityCorpPoura) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/city-corp-pouras/${cityCorpPoura.id}`,
      }).then(() => {
        cityCorpPoura = undefined;
      });
    }
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

  it('CityCorpPouras menu should load CityCorpPouras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('city-corp-poura');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CityCorpPoura').should('exist');
    cy.url().should('match', cityCorpPouraPageUrlPattern);
  });

  describe('CityCorpPoura page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cityCorpPouraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CityCorpPoura page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/city-corp-poura/new$'));
        cy.getEntityCreateUpdateHeading('CityCorpPoura');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cityCorpPouraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/city-corp-pouras',
          body: {
            ...cityCorpPouraSample,
            rmo: rMO,
          },
        }).then(({ body }) => {
          cityCorpPoura = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/city-corp-pouras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cityCorpPoura],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cityCorpPouraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CityCorpPoura page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cityCorpPoura');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cityCorpPouraPageUrlPattern);
      });

      it('edit button click should load edit CityCorpPoura page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CityCorpPoura');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cityCorpPouraPageUrlPattern);
      });

      it('edit button click should load edit CityCorpPoura page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CityCorpPoura');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cityCorpPouraPageUrlPattern);
      });

      it('last delete button click should delete instance of CityCorpPoura', () => {
        cy.intercept('GET', '/api/city-corp-pouras/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cityCorpPoura').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cityCorpPouraPageUrlPattern);

        cityCorpPoura = undefined;
      });
    });
  });

  describe('new CityCorpPoura page', () => {
    beforeEach(() => {
      cy.visit(`${cityCorpPouraPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CityCorpPoura');
    });

    it('should create an instance of CityCorpPoura', () => {
      cy.get(`[data-cy="name"]`).type('bewitched');
      cy.get(`[data-cy="name"]`).should('have.value', 'bewitched');

      cy.get(`[data-cy="rmo"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cityCorpPoura = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', cityCorpPouraPageUrlPattern);
    });
  });
});
