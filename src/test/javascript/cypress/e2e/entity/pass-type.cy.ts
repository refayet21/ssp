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

describe('PassType e2e test', () => {
  const passTypePageUrl = '/pass-type';
  const passTypePageUrlPattern = new RegExp('/pass-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const passTypeSample = {};

  let passType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pass-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pass-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pass-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (passType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pass-types/${passType.id}`,
      }).then(() => {
        passType = undefined;
      });
    }
  });

  it('PassTypes menu should load PassTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pass-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PassType').should('exist');
    cy.url().should('match', passTypePageUrlPattern);
  });

  describe('PassType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(passTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PassType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pass-type/new$'));
        cy.getEntityCreateUpdateHeading('PassType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pass-types',
          body: passTypeSample,
        }).then(({ body }) => {
          passType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pass-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [passType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(passTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PassType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('passType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passTypePageUrlPattern);
      });

      it('edit button click should load edit PassType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PassType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passTypePageUrlPattern);
      });

      it('edit button click should load edit PassType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PassType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passTypePageUrlPattern);
      });

      it('last delete button click should delete instance of PassType', () => {
        cy.intercept('GET', '/api/pass-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('passType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passTypePageUrlPattern);

        passType = undefined;
      });
    });
  });

  describe('new PassType page', () => {
    beforeEach(() => {
      cy.visit(`${passTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PassType');
    });

    it('should create an instance of PassType', () => {
      cy.get(`[data-cy="name"]`).type('defiantly');
      cy.get(`[data-cy="name"]`).should('have.value', 'defiantly');

      cy.get(`[data-cy="shortName"]`).type('lumbering likewise');
      cy.get(`[data-cy="shortName"]`).should('have.value', 'lumbering likewise');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="printedName"]`).type('monopoly');
      cy.get(`[data-cy="printedName"]`).should('have.value', 'monopoly');

      cy.get(`[data-cy="issueFee"]`).type('13858.1');
      cy.get(`[data-cy="issueFee"]`).should('have.value', '13858.1');

      cy.get(`[data-cy="renewFee"]`).type('5313.81');
      cy.get(`[data-cy="renewFee"]`).should('have.value', '5313.81');

      cy.get(`[data-cy="cancelFee"]`).type('15005.27');
      cy.get(`[data-cy="cancelFee"]`).should('have.value', '15005.27');

      cy.get(`[data-cy="minimumDuration"]`).type('30291');
      cy.get(`[data-cy="minimumDuration"]`).should('have.value', '30291');

      cy.get(`[data-cy="maximumDuration"]`).type('3299');
      cy.get(`[data-cy="maximumDuration"]`).should('have.value', '3299');

      cy.get(`[data-cy="issueChannelType"]`).select('THIRD_PARTY');

      cy.get(`[data-cy="taxCode"]`).select('NOVAT');

      cy.get(`[data-cy="passMediaType"]`).select('QRCODE');

      cy.get(`[data-cy="passUserType"]`).select('BOTH');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        passType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', passTypePageUrlPattern);
    });
  });
});
