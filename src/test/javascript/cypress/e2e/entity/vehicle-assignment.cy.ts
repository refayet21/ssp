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

describe('VehicleAssignment e2e test', () => {
  const vehicleAssignmentPageUrl = '/vehicle-assignment';
  const vehicleAssignmentPageUrlPattern = new RegExp('/vehicle-assignment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleAssignmentSample = { startDate: '2024-07-01' };

  let vehicleAssignment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-assignments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-assignments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-assignments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleAssignment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-assignments/${vehicleAssignment.id}`,
      }).then(() => {
        vehicleAssignment = undefined;
      });
    }
  });

  it('VehicleAssignments menu should load VehicleAssignments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-assignment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleAssignment').should('exist');
    cy.url().should('match', vehicleAssignmentPageUrlPattern);
  });

  describe('VehicleAssignment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleAssignmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleAssignment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-assignment/new$'));
        cy.getEntityCreateUpdateHeading('VehicleAssignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleAssignmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-assignments',
          body: vehicleAssignmentSample,
        }).then(({ body }) => {
          vehicleAssignment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-assignments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-assignments?page=0&size=20>; rel="last",<http://localhost/api/vehicle-assignments?page=0&size=20>; rel="first"',
              },
              body: [vehicleAssignment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleAssignmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleAssignment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleAssignment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleAssignmentPageUrlPattern);
      });

      it('edit button click should load edit VehicleAssignment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleAssignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleAssignmentPageUrlPattern);
      });

      it('edit button click should load edit VehicleAssignment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleAssignment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleAssignmentPageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleAssignment', () => {
        cy.intercept('GET', '/api/vehicle-assignments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('vehicleAssignment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleAssignmentPageUrlPattern);

        vehicleAssignment = undefined;
      });
    });
  });

  describe('new VehicleAssignment page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleAssignmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleAssignment');
    });

    it('should create an instance of VehicleAssignment', () => {
      cy.get(`[data-cy="startDate"]`).type('2024-06-30');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-06-30');

      cy.get(`[data-cy="endDate"]`).type('2024-07-01');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-07-01');

      cy.get(`[data-cy="isPrimary"]`).should('not.be.checked');
      cy.get(`[data-cy="isPrimary"]`).click();
      cy.get(`[data-cy="isPrimary"]`).should('be.checked');

      cy.get(`[data-cy="isRental"]`).should('not.be.checked');
      cy.get(`[data-cy="isRental"]`).click();
      cy.get(`[data-cy="isRental"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vehicleAssignment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleAssignmentPageUrlPattern);
    });
  });
});
