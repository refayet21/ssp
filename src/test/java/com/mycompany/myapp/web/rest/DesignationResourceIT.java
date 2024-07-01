package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DesignationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Designation;
import com.mycompany.myapp.repository.DesignationRepository;
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
 * Integration tests for the {@link DesignationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DesignationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/designations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDesignationMockMvc;

    private Designation designation;

    private Designation insertedDesignation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Designation createEntity(EntityManager em) {
        Designation designation = new Designation().name(DEFAULT_NAME).shortName(DEFAULT_SHORT_NAME).isActive(DEFAULT_IS_ACTIVE);
        return designation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Designation createUpdatedEntity(EntityManager em) {
        Designation designation = new Designation().name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).isActive(UPDATED_IS_ACTIVE);
        return designation;
    }

    @BeforeEach
    public void initTest() {
        designation = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDesignation != null) {
            designationRepository.delete(insertedDesignation);
            insertedDesignation = null;
        }
    }

    @Test
    @Transactional
    void createDesignation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Designation
        var returnedDesignation = om.readValue(
            restDesignationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(designation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Designation.class
        );

        // Validate the Designation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDesignationUpdatableFieldsEquals(returnedDesignation, getPersistedDesignation(returnedDesignation));

        insertedDesignation = returnedDesignation;
    }

    @Test
    @Transactional
    void createDesignationWithExistingId() throws Exception {
        // Create the Designation with an existing ID
        designation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDesignationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(designation)))
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        designation.setName(null);

        // Create the Designation, which fails.

        restDesignationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(designation)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDesignations() throws Exception {
        // Initialize the database
        insertedDesignation = designationRepository.saveAndFlush(designation);

        // Get all the designationList
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(designation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getDesignation() throws Exception {
        // Initialize the database
        insertedDesignation = designationRepository.saveAndFlush(designation);

        // Get the designation
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL_ID, designation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(designation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDesignation() throws Exception {
        // Get the designation
        restDesignationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDesignation() throws Exception {
        // Initialize the database
        insertedDesignation = designationRepository.saveAndFlush(designation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the designation
        Designation updatedDesignation = designationRepository.findById(designation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDesignation are not directly saved in db
        em.detach(updatedDesignation);
        updatedDesignation.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).isActive(UPDATED_IS_ACTIVE);

        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDesignation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDesignationToMatchAllProperties(updatedDesignation);
    }

    @Test
    @Transactional
    void putNonExistingDesignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        designation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, designation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(designation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDesignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        designation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(designation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDesignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        designation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(designation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Designation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDesignationWithPatch() throws Exception {
        // Initialize the database
        insertedDesignation = designationRepository.saveAndFlush(designation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the designation using partial update
        Designation partialUpdatedDesignation = new Designation();
        partialUpdatedDesignation.setId(designation.getId());

        partialUpdatedDesignation.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME);

        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDesignationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDesignation, designation),
            getPersistedDesignation(designation)
        );
    }

    @Test
    @Transactional
    void fullUpdateDesignationWithPatch() throws Exception {
        // Initialize the database
        insertedDesignation = designationRepository.saveAndFlush(designation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the designation using partial update
        Designation partialUpdatedDesignation = new Designation();
        partialUpdatedDesignation.setId(designation.getId());

        partialUpdatedDesignation.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).isActive(UPDATED_IS_ACTIVE);

        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDesignationUpdatableFieldsEquals(partialUpdatedDesignation, getPersistedDesignation(partialUpdatedDesignation));
    }

    @Test
    @Transactional
    void patchNonExistingDesignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        designation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, designation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(designation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDesignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        designation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(designation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDesignation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        designation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(designation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Designation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDesignation() throws Exception {
        // Initialize the database
        insertedDesignation = designationRepository.saveAndFlush(designation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the designation
        restDesignationMockMvc
            .perform(delete(ENTITY_API_URL_ID, designation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return designationRepository.count();
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

    protected Designation getPersistedDesignation(Designation designation) {
        return designationRepository.findById(designation.getId()).orElseThrow();
    }

    protected void assertPersistedDesignationToMatchAllProperties(Designation expectedDesignation) {
        assertDesignationAllPropertiesEquals(expectedDesignation, getPersistedDesignation(expectedDesignation));
    }

    protected void assertPersistedDesignationToMatchUpdatableProperties(Designation expectedDesignation) {
        assertDesignationAllUpdatablePropertiesEquals(expectedDesignation, getPersistedDesignation(expectedDesignation));
    }
}
