package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.VehicleTypeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VehicleType;
import com.mycompany.myapp.repository.VehicleTypeRepository;
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
 * Integration tests for the {@link VehicleTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_OPERATORS = 1;
    private static final Integer UPDATED_NUMBER_OF_OPERATORS = 2;

    private static final String ENTITY_API_URL = "/api/vehicle-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleTypeMockMvc;

    private VehicleType vehicleType;

    private VehicleType insertedVehicleType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleType createEntity(EntityManager em) {
        VehicleType vehicleType = new VehicleType().name(DEFAULT_NAME).numberOfOperators(DEFAULT_NUMBER_OF_OPERATORS);
        return vehicleType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleType createUpdatedEntity(EntityManager em) {
        VehicleType vehicleType = new VehicleType().name(UPDATED_NAME).numberOfOperators(UPDATED_NUMBER_OF_OPERATORS);
        return vehicleType;
    }

    @BeforeEach
    public void initTest() {
        vehicleType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleType != null) {
            vehicleTypeRepository.delete(insertedVehicleType);
            insertedVehicleType = null;
        }
    }

    @Test
    @Transactional
    void createVehicleType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleType
        var returnedVehicleType = om.readValue(
            restVehicleTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleType.class
        );

        // Validate the VehicleType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVehicleTypeUpdatableFieldsEquals(returnedVehicleType, getPersistedVehicleType(returnedVehicleType));

        insertedVehicleType = returnedVehicleType;
    }

    @Test
    @Transactional
    void createVehicleTypeWithExistingId() throws Exception {
        // Create the VehicleType with an existing ID
        vehicleType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleType)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicleTypes() throws Exception {
        // Initialize the database
        insertedVehicleType = vehicleTypeRepository.saveAndFlush(vehicleType);

        // Get all the vehicleTypeList
        restVehicleTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].numberOfOperators").value(hasItem(DEFAULT_NUMBER_OF_OPERATORS)));
    }

    @Test
    @Transactional
    void getVehicleType() throws Exception {
        // Initialize the database
        insertedVehicleType = vehicleTypeRepository.saveAndFlush(vehicleType);

        // Get the vehicleType
        restVehicleTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.numberOfOperators").value(DEFAULT_NUMBER_OF_OPERATORS));
    }

    @Test
    @Transactional
    void getNonExistingVehicleType() throws Exception {
        // Get the vehicleType
        restVehicleTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleType() throws Exception {
        // Initialize the database
        insertedVehicleType = vehicleTypeRepository.saveAndFlush(vehicleType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleType
        VehicleType updatedVehicleType = vehicleTypeRepository.findById(vehicleType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleType are not directly saved in db
        em.detach(updatedVehicleType);
        updatedVehicleType.name(UPDATED_NAME).numberOfOperators(UPDATED_NUMBER_OF_OPERATORS);

        restVehicleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicleType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVehicleType))
            )
            .andExpect(status().isOk());

        // Validate the VehicleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleTypeToMatchAllProperties(updatedVehicleType);
    }

    @Test
    @Transactional
    void putNonExistingVehicleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleType))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleType))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleTypeWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleType = vehicleTypeRepository.saveAndFlush(vehicleType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleType using partial update
        VehicleType partialUpdatedVehicleType = new VehicleType();
        partialUpdatedVehicleType.setId(vehicleType.getId());

        partialUpdatedVehicleType.numberOfOperators(UPDATED_NUMBER_OF_OPERATORS);

        restVehicleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleType))
            )
            .andExpect(status().isOk());

        // Validate the VehicleType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleType, vehicleType),
            getPersistedVehicleType(vehicleType)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleTypeWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleType = vehicleTypeRepository.saveAndFlush(vehicleType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleType using partial update
        VehicleType partialUpdatedVehicleType = new VehicleType();
        partialUpdatedVehicleType.setId(vehicleType.getId());

        partialUpdatedVehicleType.name(UPDATED_NAME).numberOfOperators(UPDATED_NUMBER_OF_OPERATORS);

        restVehicleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleType))
            )
            .andExpect(status().isOk());

        // Validate the VehicleType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleTypeUpdatableFieldsEquals(partialUpdatedVehicleType, getPersistedVehicleType(partialUpdatedVehicleType));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleType))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleType))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleType() throws Exception {
        // Initialize the database
        insertedVehicleType = vehicleTypeRepository.saveAndFlush(vehicleType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleType
        restVehicleTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleTypeRepository.count();
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

    protected VehicleType getPersistedVehicleType(VehicleType vehicleType) {
        return vehicleTypeRepository.findById(vehicleType.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleTypeToMatchAllProperties(VehicleType expectedVehicleType) {
        assertVehicleTypeAllPropertiesEquals(expectedVehicleType, getPersistedVehicleType(expectedVehicleType));
    }

    protected void assertPersistedVehicleTypeToMatchUpdatableProperties(VehicleType expectedVehicleType) {
        assertVehicleTypeAllUpdatablePropertiesEquals(expectedVehicleType, getPersistedVehicleType(expectedVehicleType));
    }
}
