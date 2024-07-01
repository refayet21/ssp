package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AgencyLicenseTypeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AgencyLicenseType;
import com.mycompany.myapp.repository.AgencyLicenseTypeRepository;
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
 * Integration tests for the {@link AgencyLicenseTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgencyLicenseTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/agency-license-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgencyLicenseTypeRepository agencyLicenseTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgencyLicenseTypeMockMvc;

    private AgencyLicenseType agencyLicenseType;

    private AgencyLicenseType insertedAgencyLicenseType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyLicenseType createEntity(EntityManager em) {
        AgencyLicenseType agencyLicenseType = new AgencyLicenseType().name(DEFAULT_NAME).isActive(DEFAULT_IS_ACTIVE);
        return agencyLicenseType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyLicenseType createUpdatedEntity(EntityManager em) {
        AgencyLicenseType agencyLicenseType = new AgencyLicenseType().name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);
        return agencyLicenseType;
    }

    @BeforeEach
    public void initTest() {
        agencyLicenseType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAgencyLicenseType != null) {
            agencyLicenseTypeRepository.delete(insertedAgencyLicenseType);
            insertedAgencyLicenseType = null;
        }
    }

    @Test
    @Transactional
    void createAgencyLicenseType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AgencyLicenseType
        var returnedAgencyLicenseType = om.readValue(
            restAgencyLicenseTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyLicenseType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgencyLicenseType.class
        );

        // Validate the AgencyLicenseType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAgencyLicenseTypeUpdatableFieldsEquals(returnedAgencyLicenseType, getPersistedAgencyLicenseType(returnedAgencyLicenseType));

        insertedAgencyLicenseType = returnedAgencyLicenseType;
    }

    @Test
    @Transactional
    void createAgencyLicenseTypeWithExistingId() throws Exception {
        // Create the AgencyLicenseType with an existing ID
        agencyLicenseType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyLicenseTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyLicenseType)))
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agencyLicenseType.setName(null);

        // Create the AgencyLicenseType, which fails.

        restAgencyLicenseTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyLicenseType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgencyLicenseTypes() throws Exception {
        // Initialize the database
        insertedAgencyLicenseType = agencyLicenseTypeRepository.saveAndFlush(agencyLicenseType);

        // Get all the agencyLicenseTypeList
        restAgencyLicenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencyLicenseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getAgencyLicenseType() throws Exception {
        // Initialize the database
        insertedAgencyLicenseType = agencyLicenseTypeRepository.saveAndFlush(agencyLicenseType);

        // Get the agencyLicenseType
        restAgencyLicenseTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, agencyLicenseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agencyLicenseType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAgencyLicenseType() throws Exception {
        // Get the agencyLicenseType
        restAgencyLicenseTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgencyLicenseType() throws Exception {
        // Initialize the database
        insertedAgencyLicenseType = agencyLicenseTypeRepository.saveAndFlush(agencyLicenseType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyLicenseType
        AgencyLicenseType updatedAgencyLicenseType = agencyLicenseTypeRepository.findById(agencyLicenseType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgencyLicenseType are not directly saved in db
        em.detach(updatedAgencyLicenseType);
        updatedAgencyLicenseType.name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restAgencyLicenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgencyLicenseType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAgencyLicenseType))
            )
            .andExpect(status().isOk());

        // Validate the AgencyLicenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgencyLicenseTypeToMatchAllProperties(updatedAgencyLicenseType);
    }

    @Test
    @Transactional
    void putNonExistingAgencyLicenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicenseType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyLicenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyLicenseType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyLicenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgencyLicenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicenseType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyLicenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyLicenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgencyLicenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicenseType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyLicenseTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyLicenseType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyLicenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgencyLicenseTypeWithPatch() throws Exception {
        // Initialize the database
        insertedAgencyLicenseType = agencyLicenseTypeRepository.saveAndFlush(agencyLicenseType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyLicenseType using partial update
        AgencyLicenseType partialUpdatedAgencyLicenseType = new AgencyLicenseType();
        partialUpdatedAgencyLicenseType.setId(agencyLicenseType.getId());

        partialUpdatedAgencyLicenseType.name(UPDATED_NAME);

        restAgencyLicenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyLicenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgencyLicenseType))
            )
            .andExpect(status().isOk());

        // Validate the AgencyLicenseType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyLicenseTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgencyLicenseType, agencyLicenseType),
            getPersistedAgencyLicenseType(agencyLicenseType)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgencyLicenseTypeWithPatch() throws Exception {
        // Initialize the database
        insertedAgencyLicenseType = agencyLicenseTypeRepository.saveAndFlush(agencyLicenseType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyLicenseType using partial update
        AgencyLicenseType partialUpdatedAgencyLicenseType = new AgencyLicenseType();
        partialUpdatedAgencyLicenseType.setId(agencyLicenseType.getId());

        partialUpdatedAgencyLicenseType.name(UPDATED_NAME).isActive(UPDATED_IS_ACTIVE);

        restAgencyLicenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyLicenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgencyLicenseType))
            )
            .andExpect(status().isOk());

        // Validate the AgencyLicenseType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyLicenseTypeUpdatableFieldsEquals(
            partialUpdatedAgencyLicenseType,
            getPersistedAgencyLicenseType(partialUpdatedAgencyLicenseType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAgencyLicenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicenseType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyLicenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agencyLicenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyLicenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgencyLicenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicenseType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyLicenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyLicenseType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgencyLicenseType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicenseType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyLicenseTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agencyLicenseType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyLicenseType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgencyLicenseType() throws Exception {
        // Initialize the database
        insertedAgencyLicenseType = agencyLicenseTypeRepository.saveAndFlush(agencyLicenseType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agencyLicenseType
        restAgencyLicenseTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, agencyLicenseType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agencyLicenseTypeRepository.count();
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

    protected AgencyLicenseType getPersistedAgencyLicenseType(AgencyLicenseType agencyLicenseType) {
        return agencyLicenseTypeRepository.findById(agencyLicenseType.getId()).orElseThrow();
    }

    protected void assertPersistedAgencyLicenseTypeToMatchAllProperties(AgencyLicenseType expectedAgencyLicenseType) {
        assertAgencyLicenseTypeAllPropertiesEquals(expectedAgencyLicenseType, getPersistedAgencyLicenseType(expectedAgencyLicenseType));
    }

    protected void assertPersistedAgencyLicenseTypeToMatchUpdatableProperties(AgencyLicenseType expectedAgencyLicenseType) {
        assertAgencyLicenseTypeAllUpdatablePropertiesEquals(
            expectedAgencyLicenseType,
            getPersistedAgencyLicenseType(expectedAgencyLicenseType)
        );
    }
}
