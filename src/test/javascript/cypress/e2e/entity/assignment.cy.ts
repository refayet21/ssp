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

describe('Assignment e2e test', () => {
  const assignmentPageUrl = '/assignment';
  const assignmentPageUrlPattern = new RegExp('/assignment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const assignmentSample = {"startDate":"2024-07-01"};

  let assignment;
  // let person;
  // let designation;
  // let agency;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/people',
      body: {"name":"hence","shortName":"zowie alibi","dob":"2024-06-30","email":"Emilie.Pouros79@gmail.com","isBlackListed":false,"fatherName":"action violently","motherName":"unless polyp","logStatus":"PENDING"},
    }).then(({ body }) => {
      person = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/designations',
      body: {"name":"engorge of gently","shortName":"even eek sharply","isActive":false},
    }).then(({ body }) => {
      designation = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/agencies',
      body: {"name":"price","shortName":"fiery huzzah","isInternal":true,"isDummy":true},
    }).then(({ body }) => {
      agency = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/assignments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/assignments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/assignments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/people', {
      statusCode: 200,
      body: [person],
    });

    cy.intercept('GET', '/api/designations', {
      statusCode: 200,
      body: [designation],
    });

    cy.intercept('GET', '/api/agencies', {
      statusCode: 200,
      body: [agency],
    });

  });
   */

  afterEach(() => {
    if (assignment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assignments/${assignment.id}`,
      }).then(() => {
        assignment = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (person) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/people/${person.id}`,
      }).then(() => {
        person = undefined;
      });
    }
    if (designation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/designations/${designation.id}`,
      }).then(() => {
        designation = undefined;
      });
    }
    if (agency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agencies/${agency.id}`,
      }).then(() => {
        agency = undefined;
      });
    }
  });
   */

  it('Assignments menu should load Assignments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('assignment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Assignment').should('exist');
    cy.url().should('match', assignmentPageUrlPattern);
  });

  describe('Assignment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assignmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Assignment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/assignment/new$'));
        cy.getEntityCreateUpdateHeading('Assignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assignmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/assignments',
          body: {
            ...assignmentSample,
            person: person,
            designation: designation,
            agency: agency,
          },
        }).then(({ body }) => {
          assignment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/assignments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/assignments?page=0&size=20>; rel="last",<http://localhost/api/assignments?page=0&size=20>; rel="first"',
              },
              body: [assignment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assignmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(assignmentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Assignment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assignment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assignmentPageUrlPattern);
      });

      it('edit button click should load edit Assignment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Assignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assignmentPageUrlPattern);
      });

      it('edit button click should load edit Assignment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Assignment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assignmentPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Assignment', () => {
        cy.intercept('GET', '/api/assignments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('assignment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', assignmentPageUrlPattern);

        assignment = undefined;
      });
    });
  });

  describe('new Assignment page', () => {
    beforeEach(() => {
      cy.visit(`${assignmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Assignment');
    });

    it.skip('should create an instance of Assignment', () => {
      cy.get(`[data-cy="startDate"]`).type('2024-07-01');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-07-01');

      cy.get(`[data-cy="endDate"]`).type('2024-07-01');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-07-01');

      cy.get(`[data-cy="isPrimary"]`).should('not.be.checked');
      cy.get(`[data-cy="isPrimary"]`).click();
      cy.get(`[data-cy="isPrimary"]`).should('be.checked');

      cy.get(`[data-cy="person"]`).select(1);
      cy.get(`[data-cy="designation"]`).select(1);
      cy.get(`[data-cy="agency"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        assignment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', assignmentPageUrlPattern);
    });
  });
});
