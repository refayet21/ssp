package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AgencyTypeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AgencyType;
import com.mycompany.myapp.repository.AgencyTypeRepository;
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
 * Integration tests for the {@link AgencyTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgencyTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/agency-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgencyTypeRepository agencyTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgencyTypeMockMvc;

    private AgencyType agencyType;

    private AgencyType insertedAgencyType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyType createEntity(EntityManager em) {
        AgencyType agencyType = new AgencyType().name(DEFAULT_NAME).shortName(DEFAULT_SHORT_NAME).isActive(DEFAULT_IS_ACTIVE);
        return agencyType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyType createUpdatedEntity(EntityManager em) {
        AgencyType agencyType = new AgencyType().name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).isActive(UPDATED_IS_ACTIVE);
        return agencyType;
    }

    @BeforeEach
    public void initTest() {
        agencyType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAgencyType != null) {
            agencyTypeRepository.delete(insertedAgencyType);
            insertedAgencyType = null;
        }
    }

    @Test
    @Transactional
    void createAgencyType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AgencyType
        var returnedAgencyType = om.readValue(
            restAgencyTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgencyType.class
        );

        // Validate the AgencyType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAgencyTypeUpdatableFieldsEquals(returnedAgencyType, getPersistedAgencyType(returnedAgencyType));

        insertedAgencyType = returnedAgencyType;
    }

    @Test
    @Transactional
    void createAgencyTypeWithExistingId() throws Exception {
        // Create the AgencyType with an existing ID
        agencyType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyType)))
            .andExpect(status().isBadRequest());

        // Validate the AgencyType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgencyTypes() throws Exception {
        // Initialize the database
        insertedAgencyType = agencyTypeRepository.saveAndFlush(agencyType);

        // Get all the agencyTypeList
        restAgencyTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getAgencyType() throws Exception {
        // Initialize the database
        insertedAgencyType = agencyTypeRepository.saveAndFlush(agencyType);

        // Get the agencyType
        restAgencyTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, agencyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agencyType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAgencyType() throws Exception {
        // Get the agencyType
        restAgencyTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgencyType() throws Exception {
        // Initialize the database
        insertedAgencyType = agencyTypeRepository.saveAndFlush(agencyType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyType
        AgencyType updatedAgencyType = agencyTypeRepository.findById(agencyType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgencyType are not directly saved in db
        em.detach(updatedAgencyType);
        updatedAgencyType.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).isActive(UPDATED_IS_ACTIVE);

        restAgencyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgencyType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAgencyType))
            )
            .andExpect(status().isOk());

        // Validate the AgencyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgencyTypeToMatchAllProperties(updatedAgencyType);
    }

    @Test
    @Transactional
    void putNonExistingAgencyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgencyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgencyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgencyTypeWithPatch() throws Exception {
        // Initialize the database
        insertedAgencyType = agencyTypeRepository.saveAndFlush(agencyType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyType using partial update
        AgencyType partialUpdatedAgencyType = new AgencyType();
        partialUpdatedAgencyType.setId(agencyType.getId());

        partialUpdatedAgencyType.shortName(UPDATED_SHORT_NAME).isActive(UPDATED_IS_ACTIVE);

        restAgencyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgencyType))
            )
            .andExpect(status().isOk());

        // Validate the AgencyType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgencyType, agencyType),
            getPersistedAgencyType(agencyType)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgencyTypeWithPatch() throws Exception {
        // Initialize the database
        insertedAgencyType = agencyTypeRepository.saveAndFlush(agencyType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyType using partial update
        AgencyType partialUpdatedAgencyType = new AgencyType();
        partialUpdatedAgencyType.setId(agencyType.getId());

        partialUpdatedAgencyType.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).isActive(UPDATED_IS_ACTIVE);

        restAgencyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgencyType))
            )
            .andExpect(status().isOk());

        // Validate the AgencyType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyTypeUpdatableFieldsEquals(partialUpdatedAgencyType, getPersistedAgencyType(partialUpdatedAgencyType));
    }

    @Test
    @Transactional
    void patchNonExistingAgencyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agencyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgencyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgencyType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agencyType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgencyType() throws Exception {
        // Initialize the database
        insertedAgencyType = agencyTypeRepository.saveAndFlush(agencyType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agencyType
        restAgencyTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, agencyType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agencyTypeRepository.count();
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

    protected AgencyType getPersistedAgencyType(AgencyType agencyType) {
        return agencyTypeRepository.findById(agencyType.getId()).orElseThrow();
    }

    protected void assertPersistedAgencyTypeToMatchAllProperties(AgencyType expectedAgencyType) {
        assertAgencyTypeAllPropertiesEquals(expectedAgencyType, getPersistedAgencyType(expectedAgencyType));
    }

    protected void assertPersistedAgencyTypeToMatchUpdatableProperties(AgencyType expectedAgencyType) {
        assertAgencyTypeAllUpdatablePropertiesEquals(expectedAgencyType, getPersistedAgencyType(expectedAgencyType));
    }
}
