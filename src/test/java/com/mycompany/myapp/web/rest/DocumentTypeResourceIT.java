package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DocumentTypeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DocumentType;
import com.mycompany.myapp.domain.enumeration.DocumentMasterType;
import com.mycompany.myapp.repository.DocumentTypeRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DocumentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final DocumentMasterType DEFAULT_DOCUMENT_MASTER_TYPE = DocumentMasterType.PERSONDOC;
    private static final DocumentMasterType UPDATED_DOCUMENT_MASTER_TYPE = DocumentMasterType.AGENCYDOC;

    private static final Boolean DEFAULT_REQUIRES_VERIFICATION = false;
    private static final Boolean UPDATED_REQUIRES_VERIFICATION = true;

    private static final String ENTITY_API_URL = "/api/document-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentTypeMockMvc;

    private DocumentType documentType;

    private DocumentType insertedDocumentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createEntity(EntityManager em) {
        DocumentType documentType = new DocumentType()
            .name(DEFAULT_NAME)
            .isActive(DEFAULT_IS_ACTIVE)
            .description(DEFAULT_DESCRIPTION)
            .documentMasterType(DEFAULT_DOCUMENT_MASTER_TYPE)
            .requiresVerification(DEFAULT_REQUIRES_VERIFICATION);
        return documentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createUpdatedEntity(EntityManager em) {
        DocumentType documentType = new DocumentType()
            .name(UPDATED_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .documentMasterType(UPDATED_DOCUMENT_MASTER_TYPE)
            .requiresVerification(UPDATED_REQUIRES_VERIFICATION);
        return documentType;
    }

    @BeforeEach
    public void initTest() {
        documentType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDocumentType != null) {
            documentTypeRepository.delete(insertedDocumentType);
            insertedDocumentType = null;
        }
    }

    @Test
    @Transactional
    void createDocumentType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentType
        var returnedDocumentType = om.readValue(
            restDocumentTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentType.class
        );

        // Validate the DocumentType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDocumentTypeUpdatableFieldsEquals(returnedDocumentType, getPersistedDocumentType(returnedDocumentType));

        insertedDocumentType = returnedDocumentType;
    }

    @Test
    @Transactional
    void createDocumentTypeWithExistingId() throws Exception {
        // Create the DocumentType with an existing ID
        documentType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentType)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentType.setName(null);

        // Create the DocumentType, which fails.

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentMasterTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentType.setDocumentMasterType(null);

        // Create the DocumentType, which fails.

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentTypes() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentMasterType").value(hasItem(DEFAULT_DOCUMENT_MASTER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].requiresVerification").value(hasItem(DEFAULT_REQUIRES_VERIFICATION.booleanValue())));
    }

    @Test
    @Transactional
    void getDocumentType() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get the documentType
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, documentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.documentMasterType").value(DEFAULT_DOCUMENT_MASTER_TYPE.toString()))
            .andExpect(jsonPath("$.requiresVerification").value(DEFAULT_REQUIRES_VERIFICATION.booleanValue()));
    }

    @Test
    @Transactional
    void getDocumentTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        Long id = documentType.getId();

        defaultDocumentTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name equals to
        defaultDocumentTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name in
        defaultDocumentTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name is not null
        defaultDocumentTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name contains
        defaultDocumentTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name does not contain
        defaultDocumentTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where isActive equals to
        defaultDocumentTypeFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where isActive in
        defaultDocumentTypeFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where isActive is not null
        defaultDocumentTypeFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where description equals to
        defaultDocumentTypeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where description in
        defaultDocumentTypeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where description is not null
        defaultDocumentTypeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where description contains
        defaultDocumentTypeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where description does not contain
        defaultDocumentTypeFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypesByDocumentMasterTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentMasterType equals to
        defaultDocumentTypeFiltering(
            "documentMasterType.equals=" + DEFAULT_DOCUMENT_MASTER_TYPE,
            "documentMasterType.equals=" + UPDATED_DOCUMENT_MASTER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypesByDocumentMasterTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentMasterType in
        defaultDocumentTypeFiltering(
            "documentMasterType.in=" + DEFAULT_DOCUMENT_MASTER_TYPE + "," + UPDATED_DOCUMENT_MASTER_TYPE,
            "documentMasterType.in=" + UPDATED_DOCUMENT_MASTER_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypesByDocumentMasterTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where documentMasterType is not null
        defaultDocumentTypeFiltering("documentMasterType.specified=true", "documentMasterType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByRequiresVerificationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where requiresVerification equals to
        defaultDocumentTypeFiltering(
            "requiresVerification.equals=" + DEFAULT_REQUIRES_VERIFICATION,
            "requiresVerification.equals=" + UPDATED_REQUIRES_VERIFICATION
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypesByRequiresVerificationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where requiresVerification in
        defaultDocumentTypeFiltering(
            "requiresVerification.in=" + DEFAULT_REQUIRES_VERIFICATION + "," + UPDATED_REQUIRES_VERIFICATION,
            "requiresVerification.in=" + UPDATED_REQUIRES_VERIFICATION
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypesByRequiresVerificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where requiresVerification is not null
        defaultDocumentTypeFiltering("requiresVerification.specified=true", "requiresVerification.specified=false");
    }

    private void defaultDocumentTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentTypeShouldBeFound(shouldBeFound);
        defaultDocumentTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentTypeShouldBeFound(String filter) throws Exception {
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentMasterType").value(hasItem(DEFAULT_DOCUMENT_MASTER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].requiresVerification").value(hasItem(DEFAULT_REQUIRES_VERIFICATION.booleanValue())));

        // Check, that the count call also returns 1
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentTypeShouldNotBeFound(String filter) throws Exception {
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentType() throws Exception {
        // Get the documentType
        restDocumentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentType() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentType
        DocumentType updatedDocumentType = documentTypeRepository.findById(documentType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentType are not directly saved in db
        em.detach(updatedDocumentType);
        updatedDocumentType
            .name(UPDATED_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .documentMasterType(UPDATED_DOCUMENT_MASTER_TYPE)
            .requiresVerification(UPDATED_REQUIRES_VERIFICATION);

        restDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentTypeToMatchAllProperties(updatedDocumentType);
    }

    @Test
    @Transactional
    void putNonExistingDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentType using partial update
        DocumentType partialUpdatedDocumentType = new DocumentType();
        partialUpdatedDocumentType.setId(documentType.getId());

        partialUpdatedDocumentType
            .name(UPDATED_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .documentMasterType(UPDATED_DOCUMENT_MASTER_TYPE);

        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the DocumentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentType, documentType),
            getPersistedDocumentType(documentType)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentType using partial update
        DocumentType partialUpdatedDocumentType = new DocumentType();
        partialUpdatedDocumentType.setId(documentType.getId());

        partialUpdatedDocumentType
            .name(UPDATED_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .description(UPDATED_DESCRIPTION)
            .documentMasterType(UPDATED_DOCUMENT_MASTER_TYPE)
            .requiresVerification(UPDATED_REQUIRES_VERIFICATION);

        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the DocumentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTypeUpdatableFieldsEquals(partialUpdatedDocumentType, getPersistedDocumentType(partialUpdatedDocumentType));
    }

    @Test
    @Transactional
    void patchNonExistingDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentType() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentType
        restDocumentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected DocumentType getPersistedDocumentType(DocumentType documentType) {
        return documentTypeRepository.findById(documentType.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentTypeToMatchAllProperties(DocumentType expectedDocumentType) {
        assertDocumentTypeAllPropertiesEquals(expectedDocumentType, getPersistedDocumentType(expectedDocumentType));
    }

    protected void assertPersistedDocumentTypeToMatchUpdatableProperties(DocumentType expectedDocumentType) {
        assertDocumentTypeAllUpdatablePropertiesEquals(expectedDocumentType, getPersistedDocumentType(expectedDocumentType));
    }
}
