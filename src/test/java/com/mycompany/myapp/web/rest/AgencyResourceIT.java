package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AgencyAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Agency;
import com.mycompany.myapp.domain.AgencyType;
import com.mycompany.myapp.domain.PassType;
import com.mycompany.myapp.repository.AgencyRepository;
import com.mycompany.myapp.service.AgencyService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AgencyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AgencyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_INTERNAL = false;
    private static final Boolean UPDATED_IS_INTERNAL = true;

    private static final Boolean DEFAULT_IS_DUMMY = false;
    private static final Boolean UPDATED_IS_DUMMY = true;

    private static final String ENTITY_API_URL = "/api/agencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgencyRepository agencyRepository;

    @Mock
    private AgencyRepository agencyRepositoryMock;

    @Mock
    private AgencyService agencyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgencyMockMvc;

    private Agency agency;

    private Agency insertedAgency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agency createEntity(EntityManager em) {
        Agency agency = new Agency()
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .isInternal(DEFAULT_IS_INTERNAL)
            .isDummy(DEFAULT_IS_DUMMY);
        // Add required entity
        AgencyType agencyType;
        if (TestUtil.findAll(em, AgencyType.class).isEmpty()) {
            agencyType = AgencyTypeResourceIT.createEntity(em);
            em.persist(agencyType);
            em.flush();
        } else {
            agencyType = TestUtil.findAll(em, AgencyType.class).get(0);
        }
        agency.setAgencyType(agencyType);
        return agency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agency createUpdatedEntity(EntityManager em) {
        Agency agency = new Agency()
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .isInternal(UPDATED_IS_INTERNAL)
            .isDummy(UPDATED_IS_DUMMY);
        // Add required entity
        AgencyType agencyType;
        if (TestUtil.findAll(em, AgencyType.class).isEmpty()) {
            agencyType = AgencyTypeResourceIT.createUpdatedEntity(em);
            em.persist(agencyType);
            em.flush();
        } else {
            agencyType = TestUtil.findAll(em, AgencyType.class).get(0);
        }
        agency.setAgencyType(agencyType);
        return agency;
    }

    @BeforeEach
    public void initTest() {
        agency = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAgency != null) {
            agencyRepository.delete(insertedAgency);
            insertedAgency = null;
        }
    }

    @Test
    @Transactional
    void createAgency() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Agency
        var returnedAgency = om.readValue(
            restAgencyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agency)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Agency.class
        );

        // Validate the Agency in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAgencyUpdatableFieldsEquals(returnedAgency, getPersistedAgency(returnedAgency));

        insertedAgency = returnedAgency;
    }

    @Test
    @Transactional
    void createAgencyWithExistingId() throws Exception {
        // Create the Agency with an existing ID
        agency.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agency)))
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agency.setName(null);

        // Create the Agency, which fails.

        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agency)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgencies() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].isInternal").value(hasItem(DEFAULT_IS_INTERNAL.booleanValue())))
            .andExpect(jsonPath("$.[*].isDummy").value(hasItem(DEFAULT_IS_DUMMY.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgenciesWithEagerRelationshipsIsEnabled() throws Exception {
        when(agencyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgencyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(agencyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgenciesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(agencyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgencyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(agencyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get the agency
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL_ID, agency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agency.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.isInternal").value(DEFAULT_IS_INTERNAL.booleanValue()))
            .andExpect(jsonPath("$.isDummy").value(DEFAULT_IS_DUMMY.booleanValue()));
    }

    @Test
    @Transactional
    void getAgenciesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        Long id = agency.getId();

        defaultAgencyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAgencyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAgencyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name equals to
        defaultAgencyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name in
        defaultAgencyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name is not null
        defaultAgencyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name contains
        defaultAgencyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name does not contain
        defaultAgencyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where shortName equals to
        defaultAgencyFiltering("shortName.equals=" + DEFAULT_SHORT_NAME, "shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where shortName in
        defaultAgencyFiltering("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME, "shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where shortName is not null
        defaultAgencyFiltering("shortName.specified=true", "shortName.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByShortNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where shortName contains
        defaultAgencyFiltering("shortName.contains=" + DEFAULT_SHORT_NAME, "shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where shortName does not contain
        defaultAgencyFiltering("shortName.doesNotContain=" + UPDATED_SHORT_NAME, "shortName.doesNotContain=" + DEFAULT_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByIsInternalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where isInternal equals to
        defaultAgencyFiltering("isInternal.equals=" + DEFAULT_IS_INTERNAL, "isInternal.equals=" + UPDATED_IS_INTERNAL);
    }

    @Test
    @Transactional
    void getAllAgenciesByIsInternalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where isInternal in
        defaultAgencyFiltering("isInternal.in=" + DEFAULT_IS_INTERNAL + "," + UPDATED_IS_INTERNAL, "isInternal.in=" + UPDATED_IS_INTERNAL);
    }

    @Test
    @Transactional
    void getAllAgenciesByIsInternalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where isInternal is not null
        defaultAgencyFiltering("isInternal.specified=true", "isInternal.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByIsDummyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where isDummy equals to
        defaultAgencyFiltering("isDummy.equals=" + DEFAULT_IS_DUMMY, "isDummy.equals=" + UPDATED_IS_DUMMY);
    }

    @Test
    @Transactional
    void getAllAgenciesByIsDummyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where isDummy in
        defaultAgencyFiltering("isDummy.in=" + DEFAULT_IS_DUMMY + "," + UPDATED_IS_DUMMY, "isDummy.in=" + UPDATED_IS_DUMMY);
    }

    @Test
    @Transactional
    void getAllAgenciesByIsDummyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where isDummy is not null
        defaultAgencyFiltering("isDummy.specified=true", "isDummy.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByAgencyTypeIsEqualToSomething() throws Exception {
        AgencyType agencyType;
        if (TestUtil.findAll(em, AgencyType.class).isEmpty()) {
            agencyRepository.saveAndFlush(agency);
            agencyType = AgencyTypeResourceIT.createEntity(em);
        } else {
            agencyType = TestUtil.findAll(em, AgencyType.class).get(0);
        }
        em.persist(agencyType);
        em.flush();
        agency.setAgencyType(agencyType);
        agencyRepository.saveAndFlush(agency);
        Long agencyTypeId = agencyType.getId();
        // Get all the agencyList where agencyType equals to agencyTypeId
        defaultAgencyShouldBeFound("agencyTypeId.equals=" + agencyTypeId);

        // Get all the agencyList where agencyType equals to (agencyTypeId + 1)
        defaultAgencyShouldNotBeFound("agencyTypeId.equals=" + (agencyTypeId + 1));
    }

    @Test
    @Transactional
    void getAllAgenciesByPassTypeIsEqualToSomething() throws Exception {
        PassType passType;
        if (TestUtil.findAll(em, PassType.class).isEmpty()) {
            agencyRepository.saveAndFlush(agency);
            passType = PassTypeResourceIT.createEntity(em);
        } else {
            passType = TestUtil.findAll(em, PassType.class).get(0);
        }
        em.persist(passType);
        em.flush();
        agency.addPassType(passType);
        agencyRepository.saveAndFlush(agency);
        Long passTypeId = passType.getId();
        // Get all the agencyList where passType equals to passTypeId
        defaultAgencyShouldBeFound("passTypeId.equals=" + passTypeId);

        // Get all the agencyList where passType equals to (passTypeId + 1)
        defaultAgencyShouldNotBeFound("passTypeId.equals=" + (passTypeId + 1));
    }

    private void defaultAgencyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAgencyShouldBeFound(shouldBeFound);
        defaultAgencyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgencyShouldBeFound(String filter) throws Exception {
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].isInternal").value(hasItem(DEFAULT_IS_INTERNAL.booleanValue())))
            .andExpect(jsonPath("$.[*].isDummy").value(hasItem(DEFAULT_IS_DUMMY.booleanValue())));

        // Check, that the count call also returns 1
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgencyShouldNotBeFound(String filter) throws Exception {
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgency() throws Exception {
        // Get the agency
        restAgencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agency
        Agency updatedAgency = agencyRepository.findById(agency.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgency are not directly saved in db
        em.detach(updatedAgency);
        updatedAgency.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).isInternal(UPDATED_IS_INTERNAL).isDummy(UPDATED_IS_DUMMY);

        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgency.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgencyToMatchAllProperties(updatedAgency);
    }

    @Test
    @Transactional
    void putNonExistingAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(put(ENTITY_API_URL_ID, agency.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agency)))
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agency)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgencyWithPatch() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agency using partial update
        Agency partialUpdatedAgency = new Agency();
        partialUpdatedAgency.setId(agency.getId());

        partialUpdatedAgency.name(UPDATED_NAME).isInternal(UPDATED_IS_INTERNAL);

        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAgency, agency), getPersistedAgency(agency));
    }

    @Test
    @Transactional
    void fullUpdateAgencyWithPatch() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agency using partial update
        Agency partialUpdatedAgency = new Agency();
        partialUpdatedAgency.setId(agency.getId());

        partialUpdatedAgency.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).isInternal(UPDATED_IS_INTERNAL).isDummy(UPDATED_IS_DUMMY);

        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyUpdatableFieldsEquals(partialUpdatedAgency, getPersistedAgency(partialUpdatedAgency));
    }

    @Test
    @Transactional
    void patchNonExistingAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agency.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agency))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agency.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agency)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agency
        restAgencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, agency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agencyRepository.count();
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

    protected Agency getPersistedAgency(Agency agency) {
        return agencyRepository.findById(agency.getId()).orElseThrow();
    }

    protected void assertPersistedAgencyToMatchAllProperties(Agency expectedAgency) {
        assertAgencyAllPropertiesEquals(expectedAgency, getPersistedAgency(expectedAgency));
    }

    protected void assertPersistedAgencyToMatchUpdatableProperties(Agency expectedAgency) {
        assertAgencyAllUpdatablePropertiesEquals(expectedAgency, getPersistedAgency(expectedAgency));
    }
}
