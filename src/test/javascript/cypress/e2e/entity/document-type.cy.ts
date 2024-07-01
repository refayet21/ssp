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

describe('DocumentType e2e test', () => {
  const documentTypePageUrl = '/document-type';
  const documentTypePageUrlPattern = new RegExp('/document-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const documentTypeSample = { name: 'periodic upright carefully', documentMasterType: 'AGENCYDOC' };

  let documentType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/document-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/document-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/document-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (documentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/document-types/${documentType.id}`,
      }).then(() => {
        documentType = undefined;
      });
    }
  });

  it('DocumentTypes menu should load DocumentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('document-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocumentType').should('exist');
    cy.url().should('match', documentTypePageUrlPattern);
  });

  describe('DocumentType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(documentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DocumentType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/document-type/new$'));
        cy.getEntityCreateUpdateHeading('DocumentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', documentTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/document-types',
          body: documentTypeSample,
        }).then(({ body }) => {
          documentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/document-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [documentType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(documentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DocumentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('documentType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', documentTypePageUrlPattern);
      });

      it('edit button click should load edit DocumentType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocumentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', documentTypePageUrlPattern);
      });

      it('edit button click should load edit DocumentType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocumentType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', documentTypePageUrlPattern);
      });

      it('last delete button click should delete instance of DocumentType', () => {
        cy.intercept('GET', '/api/document-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('documentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', documentTypePageUrlPattern);

        documentType = undefined;
      });
    });
  });

  describe('new DocumentType page', () => {
    beforeEach(() => {
      cy.visit(`${documentTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DocumentType');
    });

    it('should create an instance of DocumentType', () => {
      cy.get(`[data-cy="name"]`).type('whenever within inasmuch');
      cy.get(`[data-cy="name"]`).should('have.value', 'whenever within inasmuch');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="description"]`).type('yuck yowza');
      cy.get(`[data-cy="description"]`).should('have.value', 'yuck yowza');

      cy.get(`[data-cy="documentMasterType"]`).select('PERSONDOC');

      cy.get(`[data-cy="requiresVerification"]`).should('not.be.checked');
      cy.get(`[data-cy="requiresVerification"]`).click();
      cy.get(`[data-cy="requiresVerification"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        documentType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', documentTypePageUrlPattern);
    });
  });
});
