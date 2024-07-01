package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DivisionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Division;
import com.mycompany.myapp.repository.DivisionRepository;
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
 * Integration tests for the {@link DivisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DivisionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/divisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDivisionMockMvc;

    private Division division;

    private Division insertedDivision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Division createEntity(EntityManager em) {
        Division division = new Division().name(DEFAULT_NAME);
        return division;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Division createUpdatedEntity(EntityManager em) {
        Division division = new Division().name(UPDATED_NAME);
        return division;
    }

    @BeforeEach
    public void initTest() {
        division = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDivision != null) {
            divisionRepository.delete(insertedDivision);
            insertedDivision = null;
        }
    }

    @Test
    @Transactional
    void createDivision() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Division
        var returnedDivision = om.readValue(
            restDivisionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(division)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Division.class
        );

        // Validate the Division in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDivisionUpdatableFieldsEquals(returnedDivision, getPersistedDivision(returnedDivision));

        insertedDivision = returnedDivision;
    }

    @Test
    @Transactional
    void createDivisionWithExistingId() throws Exception {
        // Create the Division with an existing ID
        division.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(division)))
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        division.setName(null);

        // Create the Division, which fails.

        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(division)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDivisions() throws Exception {
        // Initialize the database
        insertedDivision = divisionRepository.saveAndFlush(division);

        // Get all the divisionList
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(division.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDivision() throws Exception {
        // Initialize the database
        insertedDivision = divisionRepository.saveAndFlush(division);

        // Get the division
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL_ID, division.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(division.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDivision() throws Exception {
        // Get the division
        restDivisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDivision() throws Exception {
        // Initialize the database
        insertedDivision = divisionRepository.saveAndFlush(division);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the division
        Division updatedDivision = divisionRepository.findById(division.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDivision are not directly saved in db
        em.detach(updatedDivision);
        updatedDivision.name(UPDATED_NAME);

        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDivision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDivisionToMatchAllProperties(updatedDivision);
    }

    @Test
    @Transactional
    void putNonExistingDivision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        division.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, division.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDivision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        division.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDivision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        division.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(division)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Division in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDivisionWithPatch() throws Exception {
        // Initialize the database
        insertedDivision = divisionRepository.saveAndFlush(division);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the division using partial update
        Division partialUpdatedDivision = new Division();
        partialUpdatedDivision.setId(division.getId());

        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDivisionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDivision, division), getPersistedDivision(division));
    }

    @Test
    @Transactional
    void fullUpdateDivisionWithPatch() throws Exception {
        // Initialize the database
        insertedDivision = divisionRepository.saveAndFlush(division);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the division using partial update
        Division partialUpdatedDivision = new Division();
        partialUpdatedDivision.setId(division.getId());

        partialUpdatedDivision.name(UPDATED_NAME);

        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDivisionUpdatableFieldsEquals(partialUpdatedDivision, getPersistedDivision(partialUpdatedDivision));
    }

    @Test
    @Transactional
    void patchNonExistingDivision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        division.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, division.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDivision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        division.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDivision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        division.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(division)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Division in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDivision() throws Exception {
        // Initialize the database
        insertedDivision = divisionRepository.saveAndFlush(division);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the division
        restDivisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, division.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return divisionRepository.count();
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

    protected Division getPersistedDivision(Division division) {
        return divisionRepository.findById(division.getId()).orElseThrow();
    }

    protected void assertPersistedDivisionToMatchAllProperties(Division expectedDivision) {
        assertDivisionAllPropertiesEquals(expectedDivision, getPersistedDivision(expectedDivision));
    }

    protected void assertPersistedDivisionToMatchUpdatableProperties(Division expectedDivision) {
        assertDivisionAllUpdatablePropertiesEquals(expectedDivision, getPersistedDivision(expectedDivision));
    }
}
