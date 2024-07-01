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

describe('VehicleType e2e test', () => {
  const vehicleTypePageUrl = '/vehicle-type';
  const vehicleTypePageUrlPattern = new RegExp('/vehicle-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleTypeSample = {};

  let vehicleType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-types/${vehicleType.id}`,
      }).then(() => {
        vehicleType = undefined;
      });
    }
  });

  it('VehicleTypes menu should load VehicleTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleType').should('exist');
    cy.url().should('match', vehicleTypePageUrlPattern);
  });

  describe('VehicleType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-type/new$'));
        cy.getEntityCreateUpdateHeading('VehicleType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-types',
          body: vehicleTypeSample,
        }).then(({ body }) => {
          vehicleType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [vehicleType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTypePageUrlPattern);
      });

      it('edit button click should load edit VehicleType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTypePageUrlPattern);
      });

      it('edit button click should load edit VehicleType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTypePageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleType', () => {
        cy.intercept('GET', '/api/vehicle-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTypePageUrlPattern);

        vehicleType = undefined;
      });
    });
  });

  describe('new VehicleType page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleType');
    });

    it('should create an instance of VehicleType', () => {
      cy.get(`[data-cy="name"]`).type('oof nor');
      cy.get(`[data-cy="name"]`).should('have.value', 'oof nor');

      cy.get(`[data-cy="numberOfOperators"]`).type('29839');
      cy.get(`[data-cy="numberOfOperators"]`).should('have.value', '29839');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleTypePageUrlPattern);
    });
  });
});
