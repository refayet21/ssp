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

describe('AccessProfile e2e test', () => {
  const accessProfilePageUrl = '/access-profile';
  const accessProfilePageUrlPattern = new RegExp('/access-profile(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const accessProfileSample = {};

  let accessProfile;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/access-profiles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/access-profiles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/access-profiles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accessProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/access-profiles/${accessProfile.id}`,
      }).then(() => {
        accessProfile = undefined;
      });
    }
  });

  it('AccessProfiles menu should load AccessProfiles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('access-profile');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccessProfile').should('exist');
    cy.url().should('match', accessProfilePageUrlPattern);
  });

  describe('AccessProfile page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(accessProfilePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AccessProfile page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/access-profile/new$'));
        cy.getEntityCreateUpdateHeading('AccessProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accessProfilePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/access-profiles',
          body: accessProfileSample,
        }).then(({ body }) => {
          accessProfile = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/access-profiles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [accessProfile],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(accessProfilePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AccessProfile page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accessProfile');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accessProfilePageUrlPattern);
      });

      it('edit button click should load edit AccessProfile page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccessProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accessProfilePageUrlPattern);
      });

      it('edit button click should load edit AccessProfile page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AccessProfile');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accessProfilePageUrlPattern);
      });

      it('last delete button click should delete instance of AccessProfile', () => {
        cy.intercept('GET', '/api/access-profiles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('accessProfile').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', accessProfilePageUrlPattern);

        accessProfile = undefined;
      });
    });
  });

  describe('new AccessProfile page', () => {
    beforeEach(() => {
      cy.visit(`${accessProfilePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AccessProfile');
    });

    it('should create an instance of AccessProfile', () => {
      cy.get(`[data-cy="name"]`).type('sponsor bridge hasty');
      cy.get(`[data-cy="name"]`).should('have.value', 'sponsor bridge hasty');

      cy.get(`[data-cy="description"]`).type('which vaguely');
      cy.get(`[data-cy="description"]`).should('have.value', 'which vaguely');

      cy.get(`[data-cy="startTimeOfDay"]`).type('5145');
      cy.get(`[data-cy="startTimeOfDay"]`).should('have.value', '5145');

      cy.get(`[data-cy="endTimeOfDay"]`).type('6616');
      cy.get(`[data-cy="endTimeOfDay"]`).should('have.value', '6616');

      cy.get(`[data-cy="dayOfWeek"]`).type('9867');
      cy.get(`[data-cy="dayOfWeek"]`).should('have.value', '9867');

      cy.get(`[data-cy="action"]`).select('ALLOW');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        accessProfile = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', accessProfilePageUrlPattern);
    });
  });
});
