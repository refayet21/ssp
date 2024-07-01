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

describe('Person e2e test', () => {
  const personPageUrl = '/person';
  const personPageUrlPattern = new RegExp('/person(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const personSample = { name: 'even ick abandon', dob: '2024-06-30' };

  let person;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/people+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/people').as('postEntityRequest');
    cy.intercept('DELETE', '/api/people/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (person) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/people/${person.id}`,
      }).then(() => {
        person = undefined;
      });
    }
  });

  it('People menu should load People page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('person');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Person').should('exist');
    cy.url().should('match', personPageUrlPattern);
  });

  describe('Person page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(personPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Person page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/person/new$'));
        cy.getEntityCreateUpdateHeading('Person');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/people',
          body: personSample,
        }).then(({ body }) => {
          person = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/people+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/people?page=0&size=20>; rel="last",<http://localhost/api/people?page=0&size=20>; rel="first"',
              },
              body: [person],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(personPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Person page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('person');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personPageUrlPattern);
      });

      it('edit button click should load edit Person page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Person');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personPageUrlPattern);
      });

      it('edit button click should load edit Person page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Person');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personPageUrlPattern);
      });

      it('last delete button click should delete instance of Person', () => {
        cy.intercept('GET', '/api/people/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('person').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', personPageUrlPattern);

        person = undefined;
      });
    });
  });

  describe('new Person page', () => {
    beforeEach(() => {
      cy.visit(`${personPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Person');
    });

    it('should create an instance of Person', () => {
      cy.get(`[data-cy="name"]`).type('sans perky');
      cy.get(`[data-cy="name"]`).should('have.value', 'sans perky');

      cy.get(`[data-cy="shortName"]`).type('popular');
      cy.get(`[data-cy="shortName"]`).should('have.value', 'popular');

      cy.get(`[data-cy="dob"]`).type('2024-07-01');
      cy.get(`[data-cy="dob"]`).blur();
      cy.get(`[data-cy="dob"]`).should('have.value', '2024-07-01');

      cy.get(`[data-cy="email"]`).type('Phoebe_Durgan92@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Phoebe_Durgan92@hotmail.com');

      cy.get(`[data-cy="isBlackListed"]`).should('not.be.checked');
      cy.get(`[data-cy="isBlackListed"]`).click();
      cy.get(`[data-cy="isBlackListed"]`).should('be.checked');

      cy.get(`[data-cy="fatherName"]`).type('per');
      cy.get(`[data-cy="fatherName"]`).should('have.value', 'per');

      cy.get(`[data-cy="motherName"]`).type('comment');
      cy.get(`[data-cy="motherName"]`).should('have.value', 'comment');

      cy.get(`[data-cy="logStatus"]`).select('APPROVED');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        person = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', personPageUrlPattern);
    });
  });
});
