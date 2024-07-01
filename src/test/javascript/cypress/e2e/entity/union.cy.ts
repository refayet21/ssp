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

describe('Union e2e test', () => {
  const unionPageUrl = '/union';
  const unionPageUrlPattern = new RegExp('/union(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const unionSample = {"name":"finally bricklaying whoever"};

  let union;
  // let upazila;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/upazilas',
      body: {"name":"boo midst"},
    }).then(({ body }) => {
      upazila = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/unions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/unions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/unions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
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

    cy.intercept('GET', '/api/upazilas', {
      statusCode: 200,
      body: [upazila],
    });

  });
   */

  afterEach(() => {
    if (union) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/unions/${union.id}`,
      }).then(() => {
        union = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (upazila) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/upazilas/${upazila.id}`,
      }).then(() => {
        upazila = undefined;
      });
    }
  });
   */

  it('Unions menu should load Unions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('union');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Union').should('exist');
    cy.url().should('match', unionPageUrlPattern);
  });

  describe('Union page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(unionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Union page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/union/new$'));
        cy.getEntityCreateUpdateHeading('Union');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', unionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/unions',
          body: {
            ...unionSample,
            upazila: upazila,
          },
        }).then(({ body }) => {
          union = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/unions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [union],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(unionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(unionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Union page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('union');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', unionPageUrlPattern);
      });

      it('edit button click should load edit Union page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Union');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', unionPageUrlPattern);
      });

      it('edit button click should load edit Union page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Union');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', unionPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Union', () => {
        cy.intercept('GET', '/api/unions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('union').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', unionPageUrlPattern);

        union = undefined;
      });
    });
  });

  describe('new Union page', () => {
    beforeEach(() => {
      cy.visit(`${unionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Union');
    });

    it.skip('should create an instance of Union', () => {
      cy.get(`[data-cy="name"]`).type('across');
      cy.get(`[data-cy="name"]`).should('have.value', 'across');

      cy.get(`[data-cy="upazila"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        union = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', unionPageUrlPattern);
    });
  });
});
