package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EntryLogAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.EntryLog;
import com.mycompany.myapp.domain.Lane;
import com.mycompany.myapp.domain.Pass;
import com.mycompany.myapp.domain.enumeration.ActionType;
import com.mycompany.myapp.domain.enumeration.DirectionType;
import com.mycompany.myapp.domain.enumeration.PassStatusType;
import com.mycompany.myapp.repository.EntryLogRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link EntryLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntryLogResourceIT {

    private static final Instant DEFAULT_EVENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DirectionType DEFAULT_DIRECTION = DirectionType.IN;
    private static final DirectionType UPDATED_DIRECTION = DirectionType.OUT;

    private static final PassStatusType DEFAULT_PASS_STATUS = PassStatusType.REQUESTED;
    private static final PassStatusType UPDATED_PASS_STATUS = PassStatusType.PRINTED;

    private static final ActionType DEFAULT_ACTION_TYPE = ActionType.ALLOW;
    private static final ActionType UPDATED_ACTION_TYPE = ActionType.DENY;

    private static final String ENTITY_API_URL = "/api/entry-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EntryLogRepository entryLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntryLogMockMvc;

    private EntryLog entryLog;

    private EntryLog insertedEntryLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntryLog createEntity(EntityManager em) {
        EntryLog entryLog = new EntryLog()
            .eventTime(DEFAULT_EVENT_TIME)
            .direction(DEFAULT_DIRECTION)
            .passStatus(DEFAULT_PASS_STATUS)
            .actionType(DEFAULT_ACTION_TYPE);
        // Add required entity
        Lane lane;
        if (TestUtil.findAll(em, Lane.class).isEmpty()) {
            lane = LaneResourceIT.createEntity(em);
            em.persist(lane);
            em.flush();
        } else {
            lane = TestUtil.findAll(em, Lane.class).get(0);
        }
        entryLog.setLane(lane);
        return entryLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntryLog createUpdatedEntity(EntityManager em) {
        EntryLog entryLog = new EntryLog()
            .eventTime(UPDATED_EVENT_TIME)
            .direction(UPDATED_DIRECTION)
            .passStatus(UPDATED_PASS_STATUS)
            .actionType(UPDATED_ACTION_TYPE);
        // Add required entity
        Lane lane;
        if (TestUtil.findAll(em, Lane.class).isEmpty()) {
            lane = LaneResourceIT.createUpdatedEntity(em);
            em.persist(lane);
            em.flush();
        } else {
            lane = TestUtil.findAll(em, Lane.class).get(0);
        }
        entryLog.setLane(lane);
        return entryLog;
    }

    @BeforeEach
    public void initTest() {
        entryLog = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEntryLog != null) {
            entryLogRepository.delete(insertedEntryLog);
            insertedEntryLog = null;
        }
    }

    @Test
    @Transactional
    void createEntryLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EntryLog
        var returnedEntryLog = om.readValue(
            restEntryLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entryLog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EntryLog.class
        );

        // Validate the EntryLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEntryLogUpdatableFieldsEquals(returnedEntryLog, getPersistedEntryLog(returnedEntryLog));

        insertedEntryLog = returnedEntryLog;
    }

    @Test
    @Transactional
    void createEntryLogWithExistingId() throws Exception {
        // Create the EntryLog with an existing ID
        entryLog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntryLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entryLog)))
            .andExpect(status().isBadRequest());

        // Validate the EntryLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEntryLogs() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList
        restEntryLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entryLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventTime").value(hasItem(DEFAULT_EVENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].passStatus").value(hasItem(DEFAULT_PASS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getEntryLog() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get the entryLog
        restEntryLogMockMvc
            .perform(get(ENTITY_API_URL_ID, entryLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entryLog.getId().intValue()))
            .andExpect(jsonPath("$.eventTime").value(DEFAULT_EVENT_TIME.toString()))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION.toString()))
            .andExpect(jsonPath("$.passStatus").value(DEFAULT_PASS_STATUS.toString()))
            .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getEntryLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        Long id = entryLog.getId();

        defaultEntryLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEntryLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEntryLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEntryLogsByEventTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where eventTime equals to
        defaultEntryLogFiltering("eventTime.equals=" + DEFAULT_EVENT_TIME, "eventTime.equals=" + UPDATED_EVENT_TIME);
    }

    @Test
    @Transactional
    void getAllEntryLogsByEventTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where eventTime in
        defaultEntryLogFiltering("eventTime.in=" + DEFAULT_EVENT_TIME + "," + UPDATED_EVENT_TIME, "eventTime.in=" + UPDATED_EVENT_TIME);
    }

    @Test
    @Transactional
    void getAllEntryLogsByEventTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where eventTime is not null
        defaultEntryLogFiltering("eventTime.specified=true", "eventTime.specified=false");
    }

    @Test
    @Transactional
    void getAllEntryLogsByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where direction equals to
        defaultEntryLogFiltering("direction.equals=" + DEFAULT_DIRECTION, "direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllEntryLogsByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where direction in
        defaultEntryLogFiltering("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION, "direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllEntryLogsByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where direction is not null
        defaultEntryLogFiltering("direction.specified=true", "direction.specified=false");
    }

    @Test
    @Transactional
    void getAllEntryLogsByPassStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where passStatus equals to
        defaultEntryLogFiltering("passStatus.equals=" + DEFAULT_PASS_STATUS, "passStatus.equals=" + UPDATED_PASS_STATUS);
    }

    @Test
    @Transactional
    void getAllEntryLogsByPassStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where passStatus in
        defaultEntryLogFiltering(
            "passStatus.in=" + DEFAULT_PASS_STATUS + "," + UPDATED_PASS_STATUS,
            "passStatus.in=" + UPDATED_PASS_STATUS
        );
    }

    @Test
    @Transactional
    void getAllEntryLogsByPassStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where passStatus is not null
        defaultEntryLogFiltering("passStatus.specified=true", "passStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEntryLogsByActionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where actionType equals to
        defaultEntryLogFiltering("actionType.equals=" + DEFAULT_ACTION_TYPE, "actionType.equals=" + UPDATED_ACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllEntryLogsByActionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where actionType in
        defaultEntryLogFiltering(
            "actionType.in=" + DEFAULT_ACTION_TYPE + "," + UPDATED_ACTION_TYPE,
            "actionType.in=" + UPDATED_ACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllEntryLogsByActionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        // Get all the entryLogList where actionType is not null
        defaultEntryLogFiltering("actionType.specified=true", "actionType.specified=false");
    }

    @Test
    @Transactional
    void getAllEntryLogsByPassIsEqualToSomething() throws Exception {
        Pass pass;
        if (TestUtil.findAll(em, Pass.class).isEmpty()) {
            entryLogRepository.saveAndFlush(entryLog);
            pass = PassResourceIT.createEntity(em);
        } else {
            pass = TestUtil.findAll(em, Pass.class).get(0);
        }
        em.persist(pass);
        em.flush();
        entryLog.setPass(pass);
        entryLogRepository.saveAndFlush(entryLog);
        Long passId = pass.getId();
        // Get all the entryLogList where pass equals to passId
        defaultEntryLogShouldBeFound("passId.equals=" + passId);

        // Get all the entryLogList where pass equals to (passId + 1)
        defaultEntryLogShouldNotBeFound("passId.equals=" + (passId + 1));
    }

    @Test
    @Transactional
    void getAllEntryLogsByLaneIsEqualToSomething() throws Exception {
        Lane lane;
        if (TestUtil.findAll(em, Lane.class).isEmpty()) {
            entryLogRepository.saveAndFlush(entryLog);
            lane = LaneResourceIT.createEntity(em);
        } else {
            lane = TestUtil.findAll(em, Lane.class).get(0);
        }
        em.persist(lane);
        em.flush();
        entryLog.setLane(lane);
        entryLogRepository.saveAndFlush(entryLog);
        Long laneId = lane.getId();
        // Get all the entryLogList where lane equals to laneId
        defaultEntryLogShouldBeFound("laneId.equals=" + laneId);

        // Get all the entryLogList where lane equals to (laneId + 1)
        defaultEntryLogShouldNotBeFound("laneId.equals=" + (laneId + 1));
    }

    private void defaultEntryLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEntryLogShouldBeFound(shouldBeFound);
        defaultEntryLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEntryLogShouldBeFound(String filter) throws Exception {
        restEntryLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entryLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventTime").value(hasItem(DEFAULT_EVENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION.toString())))
            .andExpect(jsonPath("$.[*].passStatus").value(hasItem(DEFAULT_PASS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())));

        // Check, that the count call also returns 1
        restEntryLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEntryLogShouldNotBeFound(String filter) throws Exception {
        restEntryLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEntryLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEntryLog() throws Exception {
        // Get the entryLog
        restEntryLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEntryLog() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entryLog
        EntryLog updatedEntryLog = entryLogRepository.findById(entryLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEntryLog are not directly saved in db
        em.detach(updatedEntryLog);
        updatedEntryLog
            .eventTime(UPDATED_EVENT_TIME)
            .direction(UPDATED_DIRECTION)
            .passStatus(UPDATED_PASS_STATUS)
            .actionType(UPDATED_ACTION_TYPE);

        restEntryLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEntryLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEntryLog))
            )
            .andExpect(status().isOk());

        // Validate the EntryLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEntryLogToMatchAllProperties(updatedEntryLog);
    }

    @Test
    @Transactional
    void putNonExistingEntryLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entryLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntryLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entryLog.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entryLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntryLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntryLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entryLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntryLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(entryLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntryLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntryLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entryLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntryLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(entryLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntryLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntryLogWithPatch() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entryLog using partial update
        EntryLog partialUpdatedEntryLog = new EntryLog();
        partialUpdatedEntryLog.setId(entryLog.getId());

        restEntryLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntryLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntryLog))
            )
            .andExpect(status().isOk());

        // Validate the EntryLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntryLogUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEntryLog, entryLog), getPersistedEntryLog(entryLog));
    }

    @Test
    @Transactional
    void fullUpdateEntryLogWithPatch() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the entryLog using partial update
        EntryLog partialUpdatedEntryLog = new EntryLog();
        partialUpdatedEntryLog.setId(entryLog.getId());

        partialUpdatedEntryLog
            .eventTime(UPDATED_EVENT_TIME)
            .direction(UPDATED_DIRECTION)
            .passStatus(UPDATED_PASS_STATUS)
            .actionType(UPDATED_ACTION_TYPE);

        restEntryLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntryLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEntryLog))
            )
            .andExpect(status().isOk());

        // Validate the EntryLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEntryLogUpdatableFieldsEquals(partialUpdatedEntryLog, getPersistedEntryLog(partialUpdatedEntryLog));
    }

    @Test
    @Transactional
    void patchNonExistingEntryLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entryLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntryLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entryLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entryLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntryLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntryLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entryLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntryLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(entryLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntryLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntryLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        entryLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntryLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(entryLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntryLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntryLog() throws Exception {
        // Initialize the database
        insertedEntryLog = entryLogRepository.saveAndFlush(entryLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the entryLog
        restEntryLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, entryLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return entryLogRepository.count();
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

    protected EntryLog getPersistedEntryLog(EntryLog entryLog) {
        return entryLogRepository.findById(entryLog.getId()).orElseThrow();
    }

    protected void assertPersistedEntryLogToMatchAllProperties(EntryLog expectedEntryLog) {
        assertEntryLogAllPropertiesEquals(expectedEntryLog, getPersistedEntryLog(expectedEntryLog));
    }

    protected void assertPersistedEntryLogToMatchUpdatableProperties(EntryLog expectedEntryLog) {
        assertEntryLogAllUpdatablePropertiesEquals(expectedEntryLog, getPersistedEntryLog(expectedEntryLog));
    }
}
