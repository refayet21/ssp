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

describe('Upazila e2e test', () => {
  const upazilaPageUrl = '/upazila';
  const upazilaPageUrlPattern = new RegExp('/upazila(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const upazilaSample = {"name":"adventurously provided"};

  let upazila;
  // let district;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/districts',
      body: {"name":"high-rise halt foolhardy"},
    }).then(({ body }) => {
      district = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/upazilas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/upazilas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/upazilas/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/unions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/city-corp-pouras', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/districts', {
      statusCode: 200,
      body: [district],
    });

  });
   */

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

  /* Disabled due to incompatibility
  afterEach(() => {
    if (district) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/districts/${district.id}`,
      }).then(() => {
        district = undefined;
      });
    }
  });
   */

  it('Upazilas menu should load Upazilas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('upazila');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Upazila').should('exist');
    cy.url().should('match', upazilaPageUrlPattern);
  });

  describe('Upazila page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(upazilaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Upazila page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/upazila/new$'));
        cy.getEntityCreateUpdateHeading('Upazila');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/upazilas',
          body: {
            ...upazilaSample,
            district: district,
          },
        }).then(({ body }) => {
          upazila = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/upazilas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [upazila],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(upazilaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(upazilaPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Upazila page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('upazila');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);
      });

      it('edit button click should load edit Upazila page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Upazila');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);
      });

      it('edit button click should load edit Upazila page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Upazila');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Upazila', () => {
        cy.intercept('GET', '/api/upazilas/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('upazila').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);

        upazila = undefined;
      });
    });
  });

  describe('new Upazila page', () => {
    beforeEach(() => {
      cy.visit(`${upazilaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Upazila');
    });

    it.skip('should create an instance of Upazila', () => {
      cy.get(`[data-cy="name"]`).type('stable problem gah');
      cy.get(`[data-cy="name"]`).should('have.value', 'stable problem gah');

      cy.get(`[data-cy="district"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        upazila = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', upazilaPageUrlPattern);
    });
  });
});
