package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.GateAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Gate;
import com.mycompany.myapp.domain.Zone;
import com.mycompany.myapp.domain.enumeration.GateType;
import com.mycompany.myapp.repository.GateRepository;
import com.mycompany.myapp.service.GateService;
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
 * Integration tests for the {@link GateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_LAT = 1F;
    private static final Float UPDATED_LAT = 2F;
    private static final Float SMALLER_LAT = 1F - 1F;

    private static final Float DEFAULT_LON = 1F;
    private static final Float UPDATED_LON = 2F;
    private static final Float SMALLER_LON = 1F - 1F;

    private static final GateType DEFAULT_GATE_TYPE = GateType.HUMAN;
    private static final GateType UPDATED_GATE_TYPE = GateType.VEHICLE;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/gates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GateRepository gateRepository;

    @Mock
    private GateRepository gateRepositoryMock;

    @Mock
    private GateService gateServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGateMockMvc;

    private Gate gate;

    private Gate insertedGate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gate createEntity(EntityManager em) {
        Gate gate = new Gate()
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .gateType(DEFAULT_GATE_TYPE)
            .isActive(DEFAULT_IS_ACTIVE);
        return gate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gate createUpdatedEntity(EntityManager em) {
        Gate gate = new Gate()
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .gateType(UPDATED_GATE_TYPE)
            .isActive(UPDATED_IS_ACTIVE);
        return gate;
    }

    @BeforeEach
    public void initTest() {
        gate = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedGate != null) {
            gateRepository.delete(insertedGate);
            insertedGate = null;
        }
    }

    @Test
    @Transactional
    void createGate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Gate
        var returnedGate = om.readValue(
            restGateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Gate.class
        );

        // Validate the Gate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGateUpdatableFieldsEquals(returnedGate, getPersistedGate(returnedGate));

        insertedGate = returnedGate;
    }

    @Test
    @Transactional
    void createGateWithExistingId() throws Exception {
        // Create the Gate with an existing ID
        gate.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gate)))
            .andExpect(status().isBadRequest());

        // Validate the Gate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGates() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList
        restGateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].gateType").value(hasItem(DEFAULT_GATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(gateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(gateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGate() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get the gate
        restGateMockMvc
            .perform(get(ENTITY_API_URL_ID, gate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()))
            .andExpect(jsonPath("$.gateType").value(DEFAULT_GATE_TYPE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getGatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        Long id = gate.getId();

        defaultGateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where name equals to
        defaultGateFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where name in
        defaultGateFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where name is not null
        defaultGateFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllGatesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where name contains
        defaultGateFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where name does not contain
        defaultGateFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllGatesByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where shortName equals to
        defaultGateFiltering("shortName.equals=" + DEFAULT_SHORT_NAME, "shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllGatesByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where shortName in
        defaultGateFiltering("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME, "shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllGatesByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where shortName is not null
        defaultGateFiltering("shortName.specified=true", "shortName.specified=false");
    }

    @Test
    @Transactional
    void getAllGatesByShortNameContainsSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where shortName contains
        defaultGateFiltering("shortName.contains=" + DEFAULT_SHORT_NAME, "shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllGatesByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where shortName does not contain
        defaultGateFiltering("shortName.doesNotContain=" + UPDATED_SHORT_NAME, "shortName.doesNotContain=" + DEFAULT_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllGatesByLatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lat equals to
        defaultGateFiltering("lat.equals=" + DEFAULT_LAT, "lat.equals=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllGatesByLatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lat in
        defaultGateFiltering("lat.in=" + DEFAULT_LAT + "," + UPDATED_LAT, "lat.in=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllGatesByLatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lat is not null
        defaultGateFiltering("lat.specified=true", "lat.specified=false");
    }

    @Test
    @Transactional
    void getAllGatesByLatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lat is greater than or equal to
        defaultGateFiltering("lat.greaterThanOrEqual=" + DEFAULT_LAT, "lat.greaterThanOrEqual=" + UPDATED_LAT);
    }

    @Test
    @Transactional
    void getAllGatesByLatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lat is less than or equal to
        defaultGateFiltering("lat.lessThanOrEqual=" + DEFAULT_LAT, "lat.lessThanOrEqual=" + SMALLER_LAT);
    }

    @Test
    @Transactional
    void getAllGatesByLatIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lat is less than
        defaultGateFiltering("lat.lessThan=" + UPDATED_LAT, "lat.lessThan=" + DEFAULT_LAT);
    }

    @Test
    @Transactional
    void getAllGatesByLatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lat is greater than
        defaultGateFiltering("lat.greaterThan=" + SMALLER_LAT, "lat.greaterThan=" + DEFAULT_LAT);
    }

    @Test
    @Transactional
    void getAllGatesByLonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lon equals to
        defaultGateFiltering("lon.equals=" + DEFAULT_LON, "lon.equals=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllGatesByLonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lon in
        defaultGateFiltering("lon.in=" + DEFAULT_LON + "," + UPDATED_LON, "lon.in=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllGatesByLonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lon is not null
        defaultGateFiltering("lon.specified=true", "lon.specified=false");
    }

    @Test
    @Transactional
    void getAllGatesByLonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lon is greater than or equal to
        defaultGateFiltering("lon.greaterThanOrEqual=" + DEFAULT_LON, "lon.greaterThanOrEqual=" + UPDATED_LON);
    }

    @Test
    @Transactional
    void getAllGatesByLonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lon is less than or equal to
        defaultGateFiltering("lon.lessThanOrEqual=" + DEFAULT_LON, "lon.lessThanOrEqual=" + SMALLER_LON);
    }

    @Test
    @Transactional
    void getAllGatesByLonIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lon is less than
        defaultGateFiltering("lon.lessThan=" + UPDATED_LON, "lon.lessThan=" + DEFAULT_LON);
    }

    @Test
    @Transactional
    void getAllGatesByLonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where lon is greater than
        defaultGateFiltering("lon.greaterThan=" + SMALLER_LON, "lon.greaterThan=" + DEFAULT_LON);
    }

    @Test
    @Transactional
    void getAllGatesByGateTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where gateType equals to
        defaultGateFiltering("gateType.equals=" + DEFAULT_GATE_TYPE, "gateType.equals=" + UPDATED_GATE_TYPE);
    }

    @Test
    @Transactional
    void getAllGatesByGateTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where gateType in
        defaultGateFiltering("gateType.in=" + DEFAULT_GATE_TYPE + "," + UPDATED_GATE_TYPE, "gateType.in=" + UPDATED_GATE_TYPE);
    }

    @Test
    @Transactional
    void getAllGatesByGateTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where gateType is not null
        defaultGateFiltering("gateType.specified=true", "gateType.specified=false");
    }

    @Test
    @Transactional
    void getAllGatesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where isActive equals to
        defaultGateFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllGatesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where isActive in
        defaultGateFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllGatesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        // Get all the gateList where isActive is not null
        defaultGateFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllGatesByZoneIsEqualToSomething() throws Exception {
        Zone zone;
        if (TestUtil.findAll(em, Zone.class).isEmpty()) {
            gateRepository.saveAndFlush(gate);
            zone = ZoneResourceIT.createEntity(em);
        } else {
            zone = TestUtil.findAll(em, Zone.class).get(0);
        }
        em.persist(zone);
        em.flush();
        gate.setZone(zone);
        gateRepository.saveAndFlush(gate);
        Long zoneId = zone.getId();
        // Get all the gateList where zone equals to zoneId
        defaultGateShouldBeFound("zoneId.equals=" + zoneId);

        // Get all the gateList where zone equals to (zoneId + 1)
        defaultGateShouldNotBeFound("zoneId.equals=" + (zoneId + 1));
    }

    private void defaultGateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGateShouldBeFound(shouldBeFound);
        defaultGateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGateShouldBeFound(String filter) throws Exception {
        restGateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].gateType").value(hasItem(DEFAULT_GATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restGateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGateShouldNotBeFound(String filter) throws Exception {
        restGateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGate() throws Exception {
        // Get the gate
        restGateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGate() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gate
        Gate updatedGate = gateRepository.findById(gate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGate are not directly saved in db
        em.detach(updatedGate);
        updatedGate
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .gateType(UPDATED_GATE_TYPE)
            .isActive(UPDATED_IS_ACTIVE);

        restGateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedGate))
            )
            .andExpect(status().isOk());

        // Validate the Gate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGateToMatchAllProperties(updatedGate);
    }

    @Test
    @Transactional
    void putNonExistingGate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGateMockMvc
            .perform(put(ENTITY_API_URL_ID, gate.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gate)))
            .andExpect(status().isBadRequest());

        // Validate the Gate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGateWithPatch() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gate using partial update
        Gate partialUpdatedGate = new Gate();
        partialUpdatedGate.setId(gate.getId());

        partialUpdatedGate.name(UPDATED_NAME).gateType(UPDATED_GATE_TYPE).isActive(UPDATED_IS_ACTIVE);

        restGateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGate))
            )
            .andExpect(status().isOk());

        // Validate the Gate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGateUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGate, gate), getPersistedGate(gate));
    }

    @Test
    @Transactional
    void fullUpdateGateWithPatch() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the gate using partial update
        Gate partialUpdatedGate = new Gate();
        partialUpdatedGate.setId(gate.getId());

        partialUpdatedGate
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .gateType(UPDATED_GATE_TYPE)
            .isActive(UPDATED_IS_ACTIVE);

        restGateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGate))
            )
            .andExpect(status().isOk());

        // Validate the Gate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGateUpdatableFieldsEquals(partialUpdatedGate, getPersistedGate(partialUpdatedGate));
    }

    @Test
    @Transactional
    void patchNonExistingGate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGateMockMvc
            .perform(patch(ENTITY_API_URL_ID, gate.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gate)))
            .andExpect(status().isBadRequest());

        // Validate the Gate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        gate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGate() throws Exception {
        // Initialize the database
        insertedGate = gateRepository.saveAndFlush(gate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the gate
        restGateMockMvc
            .perform(delete(ENTITY_API_URL_ID, gate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return gateRepository.count();
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

    protected Gate getPersistedGate(Gate gate) {
        return gateRepository.findById(gate.getId()).orElseThrow();
    }

    protected void assertPersistedGateToMatchAllProperties(Gate expectedGate) {
        assertGateAllPropertiesEquals(expectedGate, getPersistedGate(expectedGate));
    }

    protected void assertPersistedGateToMatchUpdatableProperties(Gate expectedGate) {
        assertGateAllUpdatablePropertiesEquals(expectedGate, getPersistedGate(expectedGate));
    }
}
