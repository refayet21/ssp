package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.LaneAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AccessProfile;
import com.mycompany.myapp.domain.Gate;
import com.mycompany.myapp.domain.Lane;
import com.mycompany.myapp.domain.enumeration.DirectionType;
import com.mycompany.myapp.repository.LaneRepository;
import com.mycompany.myapp.service.LaneService;
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
 * Integration tests for the {@link LaneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LaneResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final DirectionType DEFAULT_DIRECTION = DirectionType.IN;
    private static final DirectionType UPDATED_DIRECTION = DirectionType.OUT;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/lanes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LaneRepository laneRepository;

    @Mock
    private LaneRepository laneRepositoryMock;

    @Mock
    private LaneService laneServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLaneMockMvc;

    private Lane lane;

    private Lane insertedLane;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lane createEntity(EntityManager em) {
        Lane lane = new Lane().name(DEFAULT_NAME).shortName(DEFAULT_SHORT_NAME).direction(DEFAULT_DIRECTION).isActive(DEFAULT_IS_ACTIVE);
        return lane;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lane createUpdatedEntity(EntityManager em) {
        Lane lane = new Lane().name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).direction(UPDATED_DIRECTION).isActive(UPDATED_IS_ACTIVE);
        return lane;
    }

    @BeforeEach
    public void initTest() {
        lane = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLane != null) {
            laneRepository.delete(insertedLane);
            insertedLane = null;
        }
    }

    @Test
    @Transactional
    void createLane() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Lane
        var returnedLane = om.readValue(
            restLaneMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lane)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Lane.class
        );

        // Validate the Lane in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLaneUpdatableFieldsEquals(returnedLane, getPersistedLane(returnedLane));

        insertedLane = returnedLane;
    }

    @Test
    @Transactional
    void createLaneWithExistingId() throws Exception {
        // Create the Lane with an existing ID
        lane.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLaneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lane)))
            .andExpect(status().isBadRequest());

        // Validate the Lane in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLanes() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList
        restLaneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lane.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLanesWithEagerRelationshipsIsEnabled() throws Exception {
        when(laneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLaneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(laneServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLanesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(laneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLaneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(laneRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLane() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get the lane
        restLaneMockMvc
            .perform(get(ENTITY_API_URL_ID, lane.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lane.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getLanesByIdFiltering() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        Long id = lane.getId();

        defaultLaneFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLaneFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLaneFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLanesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where name equals to
        defaultLaneFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLanesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where name in
        defaultLaneFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLanesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where name is not null
        defaultLaneFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllLanesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where name contains
        defaultLaneFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLanesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where name does not contain
        defaultLaneFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllLanesByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where shortName equals to
        defaultLaneFiltering("shortName.equals=" + DEFAULT_SHORT_NAME, "shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllLanesByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where shortName in
        defaultLaneFiltering("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME, "shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllLanesByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where shortName is not null
        defaultLaneFiltering("shortName.specified=true", "shortName.specified=false");
    }

    @Test
    @Transactional
    void getAllLanesByShortNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where shortName contains
        defaultLaneFiltering("shortName.contains=" + DEFAULT_SHORT_NAME, "shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllLanesByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where shortName does not contain
        defaultLaneFiltering("shortName.doesNotContain=" + UPDATED_SHORT_NAME, "shortName.doesNotContain=" + DEFAULT_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllLanesByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where direction equals to
        defaultLaneFiltering("direction.equals=" + DEFAULT_DIRECTION, "direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllLanesByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where direction in
        defaultLaneFiltering("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION, "direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllLanesByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where direction is not null
        defaultLaneFiltering("direction.specified=true", "direction.specified=false");
    }

    @Test
    @Transactional
    void getAllLanesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where isActive equals to
        defaultLaneFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLanesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where isActive in
        defaultLaneFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLanesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        // Get all the laneList where isActive is not null
        defaultLaneFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllLanesByGateIsEqualToSomething() throws Exception {
        Gate gate;
        if (TestUtil.findAll(em, Gate.class).isEmpty()) {
            laneRepository.saveAndFlush(lane);
            gate = GateResourceIT.createEntity(em);
        } else {
            gate = TestUtil.findAll(em, Gate.class).get(0);
        }
        em.persist(gate);
        em.flush();
        lane.setGate(gate);
        laneRepository.saveAndFlush(lane);
        Long gateId = gate.getId();
        // Get all the laneList where gate equals to gateId
        defaultLaneShouldBeFound("gateId.equals=" + gateId);

        // Get all the laneList where gate equals to (gateId + 1)
        defaultLaneShouldNotBeFound("gateId.equals=" + (gateId + 1));
    }

    @Test
    @Transactional
    void getAllLanesByAccessProfileIsEqualToSomething() throws Exception {
        AccessProfile accessProfile;
        if (TestUtil.findAll(em, AccessProfile.class).isEmpty()) {
            laneRepository.saveAndFlush(lane);
            accessProfile = AccessProfileResourceIT.createEntity(em);
        } else {
            accessProfile = TestUtil.findAll(em, AccessProfile.class).get(0);
        }
        em.persist(accessProfile);
        em.flush();
        lane.addAccessProfile(accessProfile);
        laneRepository.saveAndFlush(lane);
        Long accessProfileId = accessProfile.getId();
        // Get all the laneList where accessProfile equals to accessProfileId
        defaultLaneShouldBeFound("accessProfileId.equals=" + accessProfileId);

        // Get all the laneList where accessProfile equals to (accessProfileId + 1)
        defaultLaneShouldNotBeFound("accessProfileId.equals=" + (accessProfileId + 1));
    }

    private void defaultLaneFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLaneShouldBeFound(shouldBeFound);
        defaultLaneShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLaneShouldBeFound(String filter) throws Exception {
        restLaneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lane.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restLaneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLaneShouldNotBeFound(String filter) throws Exception {
        restLaneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLaneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLane() throws Exception {
        // Get the lane
        restLaneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLane() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lane
        Lane updatedLane = laneRepository.findById(lane.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLane are not directly saved in db
        em.detach(updatedLane);
        updatedLane.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).direction(UPDATED_DIRECTION).isActive(UPDATED_IS_ACTIVE);

        restLaneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLane.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLane))
            )
            .andExpect(status().isOk());

        // Validate the Lane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLaneToMatchAllProperties(updatedLane);
    }

    @Test
    @Transactional
    void putNonExistingLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lane.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaneMockMvc
            .perform(put(ENTITY_API_URL_ID, lane.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lane)))
            .andExpect(status().isBadRequest());

        // Validate the Lane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lane.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lane))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lane.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lane)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLaneWithPatch() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lane using partial update
        Lane partialUpdatedLane = new Lane();
        partialUpdatedLane.setId(lane.getId());

        restLaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLane.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLane))
            )
            .andExpect(status().isOk());

        // Validate the Lane in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLaneUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLane, lane), getPersistedLane(lane));
    }

    @Test
    @Transactional
    void fullUpdateLaneWithPatch() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lane using partial update
        Lane partialUpdatedLane = new Lane();
        partialUpdatedLane.setId(lane.getId());

        partialUpdatedLane.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).direction(UPDATED_DIRECTION).isActive(UPDATED_IS_ACTIVE);

        restLaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLane.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLane))
            )
            .andExpect(status().isOk());

        // Validate the Lane in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLaneUpdatableFieldsEquals(partialUpdatedLane, getPersistedLane(partialUpdatedLane));
    }

    @Test
    @Transactional
    void patchNonExistingLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lane.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaneMockMvc
            .perform(patch(ENTITY_API_URL_ID, lane.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lane)))
            .andExpect(status().isBadRequest());

        // Validate the Lane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lane.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lane))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLane() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lane.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lane)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lane in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLane() throws Exception {
        // Initialize the database
        insertedLane = laneRepository.saveAndFlush(lane);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lane
        restLaneMockMvc
            .perform(delete(ENTITY_API_URL_ID, lane.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return laneRepository.count();
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

    protected Lane getPersistedLane(Lane lane) {
        return laneRepository.findById(lane.getId()).orElseThrow();
    }

    protected void assertPersistedLaneToMatchAllProperties(Lane expectedLane) {
        assertLaneAllPropertiesEquals(expectedLane, getPersistedLane(expectedLane));
    }

    protected void assertPersistedLaneToMatchUpdatableProperties(Lane expectedLane) {
        assertLaneAllUpdatablePropertiesEquals(expectedLane, getPersistedLane(expectedLane));
    }
}
