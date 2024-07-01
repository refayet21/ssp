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

describe('AgencyLicense e2e test', () => {
  const agencyLicensePageUrl = '/agency-license';
  const agencyLicensePageUrlPattern = new RegExp('/agency-license(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const agencyLicenseSample = {};

  let agencyLicense;
  let agencyLicenseType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/agency-license-types',
      body: { name: 'whenever hmph', isActive: false },
    }).then(({ body }) => {
      agencyLicenseType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agency-licenses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agency-licenses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agency-licenses/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/agency-license-types', {
      statusCode: 200,
      body: [agencyLicenseType],
    });

    cy.intercept('GET', '/api/agencies', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (agencyLicense) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agency-licenses/${agencyLicense.id}`,
      }).then(() => {
        agencyLicense = undefined;
      });
    }
  });

  afterEach(() => {
    if (agencyLicenseType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agency-license-types/${agencyLicenseType.id}`,
      }).then(() => {
        agencyLicenseType = undefined;
      });
    }
  });

  it('AgencyLicenses menu should load AgencyLicenses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agency-license');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AgencyLicense').should('exist');
    cy.url().should('match', agencyLicensePageUrlPattern);
  });

  describe('AgencyLicense page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agencyLicensePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AgencyLicense page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/agency-license/new$'));
        cy.getEntityCreateUpdateHeading('AgencyLicense');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicensePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agency-licenses',
          body: {
            ...agencyLicenseSample,
            agencyLicenseType: agencyLicenseType,
          },
        }).then(({ body }) => {
          agencyLicense = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agency-licenses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/agency-licenses?page=0&size=20>; rel="last",<http://localhost/api/agency-licenses?page=0&size=20>; rel="first"',
              },
              body: [agencyLicense],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(agencyLicensePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AgencyLicense page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agencyLicense');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicensePageUrlPattern);
      });

      it('edit button click should load edit AgencyLicense page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgencyLicense');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicensePageUrlPattern);
      });

      it('edit button click should load edit AgencyLicense page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgencyLicense');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicensePageUrlPattern);
      });

      it('last delete button click should delete instance of AgencyLicense', () => {
        cy.intercept('GET', '/api/agency-licenses/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('agencyLicense').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', agencyLicensePageUrlPattern);

        agencyLicense = undefined;
      });
    });
  });

  describe('new AgencyLicense page', () => {
    beforeEach(() => {
      cy.visit(`${agencyLicensePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AgencyLicense');
    });

    it('should create an instance of AgencyLicense', () => {
      cy.get(`[data-cy="filePath"]`).type('quiet');
      cy.get(`[data-cy="filePath"]`).should('have.value', 'quiet');

      cy.get(`[data-cy="serialNo"]`).type('stonework ripen');
      cy.get(`[data-cy="serialNo"]`).should('have.value', 'stonework ripen');

      cy.get(`[data-cy="issueDate"]`).type('2024-07-01');
      cy.get(`[data-cy="issueDate"]`).blur();
      cy.get(`[data-cy="issueDate"]`).should('have.value', '2024-07-01');

      cy.get(`[data-cy="expiryDate"]`).type('2024-07-01');
      cy.get(`[data-cy="expiryDate"]`).blur();
      cy.get(`[data-cy="expiryDate"]`).should('have.value', '2024-07-01');

      cy.get(`[data-cy="agencyLicenseType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        agencyLicense = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', agencyLicensePageUrlPattern);
    });
  });
});
