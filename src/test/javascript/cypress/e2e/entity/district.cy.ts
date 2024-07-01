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

describe('District e2e test', () => {
  const districtPageUrl = '/district';
  const districtPageUrlPattern = new RegExp('/district(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const districtSample = { name: 'gazunder democratize attempt' };

  let district;
  let division;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/divisions',
      body: { name: 'whoever shred' },
    }).then(({ body }) => {
      division = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/districts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/districts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/districts/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/upazilas', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/city-corp-pouras', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/post-offices', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/divisions', {
      statusCode: 200,
      body: [division],
    });
  });

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

  afterEach(() => {
    if (division) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/divisions/${division.id}`,
      }).then(() => {
        division = undefined;
      });
    }
  });

  it('Districts menu should load Districts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('district');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('District').should('exist');
    cy.url().should('match', districtPageUrlPattern);
  });

  describe('District page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(districtPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create District page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/district/new$'));
        cy.getEntityCreateUpdateHeading('District');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/districts',
          body: {
            ...districtSample,
            division: division,
          },
        }).then(({ body }) => {
          district = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/districts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [district],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(districtPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details District page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('district');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);
      });

      it('edit button click should load edit District page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('District');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);
      });

      it('edit button click should load edit District page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('District');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);
      });

      it('last delete button click should delete instance of District', () => {
        cy.intercept('GET', '/api/districts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('district').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);

        district = undefined;
      });
    });
  });

  describe('new District page', () => {
    beforeEach(() => {
      cy.visit(`${districtPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('District');
    });

    it('should create an instance of District', () => {
      cy.get(`[data-cy="name"]`).type('amber barring after');
      cy.get(`[data-cy="name"]`).should('have.value', 'amber barring after');

      cy.get(`[data-cy="division"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        district = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', districtPageUrlPattern);
    });
  });
});
