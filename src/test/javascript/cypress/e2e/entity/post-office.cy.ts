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

describe('PostOffice e2e test', () => {
  const postOfficePageUrl = '/post-office';
  const postOfficePageUrlPattern = new RegExp('/post-office(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const postOfficeSample = {"name":"quicken astride","code":"miscommunication"};

  let postOffice;
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
      body: {"name":"periodic powerfully"},
    }).then(({ body }) => {
      district = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/post-offices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-offices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-offices/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/addresses', {
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
    if (postOffice) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-offices/${postOffice.id}`,
      }).then(() => {
        postOffice = undefined;
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

  it('PostOffices menu should load PostOffices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('post-office');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostOffice').should('exist');
    cy.url().should('match', postOfficePageUrlPattern);
  });

  describe('PostOffice page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postOfficePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostOffice page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/post-office/new$'));
        cy.getEntityCreateUpdateHeading('PostOffice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postOfficePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-offices',
          body: {
            ...postOfficeSample,
            district: district,
          },
        }).then(({ body }) => {
          postOffice = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/post-offices+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [postOffice],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(postOfficePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(postOfficePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PostOffice page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postOffice');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postOfficePageUrlPattern);
      });

      it('edit button click should load edit PostOffice page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostOffice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postOfficePageUrlPattern);
      });

      it('edit button click should load edit PostOffice page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostOffice');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postOfficePageUrlPattern);
      });

      it.skip('last delete button click should delete instance of PostOffice', () => {
        cy.intercept('GET', '/api/post-offices/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('postOffice').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postOfficePageUrlPattern);

        postOffice = undefined;
      });
    });
  });

  describe('new PostOffice page', () => {
    beforeEach(() => {
      cy.visit(`${postOfficePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostOffice');
    });

    it.skip('should create an instance of PostOffice', () => {
      cy.get(`[data-cy="name"]`).type('how whereas nicely');
      cy.get(`[data-cy="name"]`).should('have.value', 'how whereas nicely');

      cy.get(`[data-cy="code"]`).type('equally inconsequential');
      cy.get(`[data-cy="code"]`).should('have.value', 'equally inconsequential');

      cy.get(`[data-cy="district"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        postOffice = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', postOfficePageUrlPattern);
    });
  });
});
