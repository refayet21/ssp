package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.RMOAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.RMO;
import com.mycompany.myapp.repository.RMORepository;
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
 * Integration tests for the {@link RMOResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RMOResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rmos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RMORepository rMORepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRMOMockMvc;

    private RMO rMO;

    private RMO insertedRMO;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RMO createEntity(EntityManager em) {
        RMO rMO = new RMO().name(DEFAULT_NAME).code(DEFAULT_CODE);
        return rMO;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RMO createUpdatedEntity(EntityManager em) {
        RMO rMO = new RMO().name(UPDATED_NAME).code(UPDATED_CODE);
        return rMO;
    }

    @BeforeEach
    public void initTest() {
        rMO = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRMO != null) {
            rMORepository.delete(insertedRMO);
            insertedRMO = null;
        }
    }

    @Test
    @Transactional
    void createRMO() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RMO
        var returnedRMO = om.readValue(
            restRMOMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rMO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RMO.class
        );

        // Validate the RMO in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRMOUpdatableFieldsEquals(returnedRMO, getPersistedRMO(returnedRMO));

        insertedRMO = returnedRMO;
    }

    @Test
    @Transactional
    void createRMOWithExistingId() throws Exception {
        // Create the RMO with an existing ID
        rMO.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRMOMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rMO)))
            .andExpect(status().isBadRequest());

        // Validate the RMO in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rMO.setName(null);

        // Create the RMO, which fails.

        restRMOMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rMO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rMO.setCode(null);

        // Create the RMO, which fails.

        restRMOMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rMO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRMOS() throws Exception {
        // Initialize the database
        insertedRMO = rMORepository.saveAndFlush(rMO);

        // Get all the rMOList
        restRMOMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rMO.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getRMO() throws Exception {
        // Initialize the database
        insertedRMO = rMORepository.saveAndFlush(rMO);

        // Get the rMO
        restRMOMockMvc
            .perform(get(ENTITY_API_URL_ID, rMO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rMO.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingRMO() throws Exception {
        // Get the rMO
        restRMOMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRMO() throws Exception {
        // Initialize the database
        insertedRMO = rMORepository.saveAndFlush(rMO);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rMO
        RMO updatedRMO = rMORepository.findById(rMO.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRMO are not directly saved in db
        em.detach(updatedRMO);
        updatedRMO.name(UPDATED_NAME).code(UPDATED_CODE);

        restRMOMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRMO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedRMO))
            )
            .andExpect(status().isOk());

        // Validate the RMO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRMOToMatchAllProperties(updatedRMO);
    }

    @Test
    @Transactional
    void putNonExistingRMO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rMO.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRMOMockMvc
            .perform(put(ENTITY_API_URL_ID, rMO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rMO)))
            .andExpect(status().isBadRequest());

        // Validate the RMO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRMO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rMO.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRMOMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rMO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RMO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRMO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rMO.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRMOMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rMO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RMO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRMOWithPatch() throws Exception {
        // Initialize the database
        insertedRMO = rMORepository.saveAndFlush(rMO);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rMO using partial update
        RMO partialUpdatedRMO = new RMO();
        partialUpdatedRMO.setId(rMO.getId());

        partialUpdatedRMO.code(UPDATED_CODE);

        restRMOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRMO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRMO))
            )
            .andExpect(status().isOk());

        // Validate the RMO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRMOUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRMO, rMO), getPersistedRMO(rMO));
    }

    @Test
    @Transactional
    void fullUpdateRMOWithPatch() throws Exception {
        // Initialize the database
        insertedRMO = rMORepository.saveAndFlush(rMO);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rMO using partial update
        RMO partialUpdatedRMO = new RMO();
        partialUpdatedRMO.setId(rMO.getId());

        partialUpdatedRMO.name(UPDATED_NAME).code(UPDATED_CODE);

        restRMOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRMO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRMO))
            )
            .andExpect(status().isOk());

        // Validate the RMO in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRMOUpdatableFieldsEquals(partialUpdatedRMO, getPersistedRMO(partialUpdatedRMO));
    }

    @Test
    @Transactional
    void patchNonExistingRMO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rMO.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRMOMockMvc
            .perform(patch(ENTITY_API_URL_ID, rMO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rMO)))
            .andExpect(status().isBadRequest());

        // Validate the RMO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRMO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rMO.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRMOMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rMO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RMO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRMO() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rMO.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRMOMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rMO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RMO in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRMO() throws Exception {
        // Initialize the database
        insertedRMO = rMORepository.saveAndFlush(rMO);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rMO
        restRMOMockMvc.perform(delete(ENTITY_API_URL_ID, rMO.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rMORepository.count();
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

    protected RMO getPersistedRMO(RMO rMO) {
        return rMORepository.findById(rMO.getId()).orElseThrow();
    }

    protected void assertPersistedRMOToMatchAllProperties(RMO expectedRMO) {
        assertRMOAllPropertiesEquals(expectedRMO, getPersistedRMO(expectedRMO));
    }

    protected void assertPersistedRMOToMatchUpdatableProperties(RMO expectedRMO) {
        assertRMOAllUpdatablePropertiesEquals(expectedRMO, getPersistedRMO(expectedRMO));
    }
}
