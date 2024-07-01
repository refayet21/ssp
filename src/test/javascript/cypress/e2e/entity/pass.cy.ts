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

describe('Pass e2e test', () => {
  const passPageUrl = '/pass';
  const passPageUrlPattern = new RegExp('/pass(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const passSample = {};

  let pass;
  // let passType;
  // let user;
  // let assignment;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/pass-types',
      body: {"name":"poised since for","shortName":"regret","isActive":false,"printedName":"realistic stiff bridge","issueFee":14295.29,"renewFee":28816.48,"cancelFee":17103.37,"minimumDuration":25408,"maximumDuration":3208,"issueChannelType":"THIRD_PARTY","taxCode":"VAT","passMediaType":"BIOMETRIC","passUserType":"HUMAN"},
    }).then(({ body }) => {
      passType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"pMU@Nfj\\joHHR\\Jrnu895\\]3-EBfE","firstName":"Mazie","lastName":"Kerluke","email":"Roderick_Stroman@yahoo.com","imageUrl":"vascular utility tug","langKey":"justly"},
    }).then(({ body }) => {
      user = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/assignments',
      body: {"startDate":"2024-07-01","endDate":"2024-07-01","isPrimary":false},
    }).then(({ body }) => {
      assignment = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/passes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/passes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/passes/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/entry-logs', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/pass-types', {
      statusCode: 200,
      body: [passType],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/assignments', {
      statusCode: 200,
      body: [assignment],
    });

    cy.intercept('GET', '/api/vehicle-assignments', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (pass) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/passes/${pass.id}`,
      }).then(() => {
        pass = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (passType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pass-types/${passType.id}`,
      }).then(() => {
        passType = undefined;
      });
    }
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
    if (assignment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/assignments/${assignment.id}`,
      }).then(() => {
        assignment = undefined;
      });
    }
  });
   */

  it('Passes menu should load Passes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pass');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Pass').should('exist');
    cy.url().should('match', passPageUrlPattern);
  });

  describe('Pass page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(passPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Pass page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pass/new$'));
        cy.getEntityCreateUpdateHeading('Pass');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/passes',
          body: {
            ...passSample,
            passType: passType,
            requestedBy: user,
            assignment: assignment,
          },
        }).then(({ body }) => {
          pass = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/passes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/passes?page=0&size=20>; rel="last",<http://localhost/api/passes?page=0&size=20>; rel="first"',
              },
              body: [pass],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(passPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(passPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Pass page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pass');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passPageUrlPattern);
      });

      it('edit button click should load edit Pass page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pass');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passPageUrlPattern);
      });

      it('edit button click should load edit Pass page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pass');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Pass', () => {
        cy.intercept('GET', '/api/passes/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('pass').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passPageUrlPattern);

        pass = undefined;
      });
    });
  });

  describe('new Pass page', () => {
    beforeEach(() => {
      cy.visit(`${passPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Pass');
    });

    it.skip('should create an instance of Pass', () => {
      cy.get(`[data-cy="collectedFee"]`).type('3283.97');
      cy.get(`[data-cy="collectedFee"]`).should('have.value', '3283.97');

      cy.get(`[data-cy="fromDate"]`).type('2024-07-01T06:49');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2024-07-01T06:49');

      cy.get(`[data-cy="endDate"]`).type('2024-06-30T21:26');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-06-30T21:26');

      cy.get(`[data-cy="status"]`).select('REQUESTED');

      cy.get(`[data-cy="passNumber"]`).type('14782');
      cy.get(`[data-cy="passNumber"]`).should('have.value', '14782');

      cy.get(`[data-cy="mediaSerial"]`).type('circa');
      cy.get(`[data-cy="mediaSerial"]`).should('have.value', 'circa');

      cy.get(`[data-cy="passType"]`).select(1);
      cy.get(`[data-cy="requestedBy"]`).select(1);
      cy.get(`[data-cy="assignment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pass = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', passPageUrlPattern);
    });
  });
});
