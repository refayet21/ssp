package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ZoneAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Zone;
import com.mycompany.myapp.repository.ZoneRepository;
import com.mycompany.myapp.service.ZoneService;
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
 * Integration tests for the {@link ZoneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ZoneResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/zones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ZoneRepository zoneRepository;

    @Mock
    private ZoneRepository zoneRepositoryMock;

    @Mock
    private ZoneService zoneServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZoneMockMvc;

    private Zone zone;

    private Zone insertedZone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zone createEntity(EntityManager em) {
        Zone zone = new Zone().name(DEFAULT_NAME).shortName(DEFAULT_SHORT_NAME).location(DEFAULT_LOCATION).isActive(DEFAULT_IS_ACTIVE);
        return zone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zone createUpdatedEntity(EntityManager em) {
        Zone zone = new Zone().name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).location(UPDATED_LOCATION).isActive(UPDATED_IS_ACTIVE);
        return zone;
    }

    @BeforeEach
    public void initTest() {
        zone = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedZone != null) {
            zoneRepository.delete(insertedZone);
            insertedZone = null;
        }
    }

    @Test
    @Transactional
    void createZone() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Zone
        var returnedZone = om.readValue(
            restZoneMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zone)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Zone.class
        );

        // Validate the Zone in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertZoneUpdatableFieldsEquals(returnedZone, getPersistedZone(returnedZone));

        insertedZone = returnedZone;
    }

    @Test
    @Transactional
    void createZoneWithExistingId() throws Exception {
        // Create the Zone with an existing ID
        zone.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zone)))
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllZones() throws Exception {
        // Initialize the database
        insertedZone = zoneRepository.saveAndFlush(zone);

        // Get all the zoneList
        restZoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zone.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllZonesWithEagerRelationshipsIsEnabled() throws Exception {
        when(zoneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(zoneServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllZonesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(zoneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(zoneRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getZone() throws Exception {
        // Initialize the database
        insertedZone = zoneRepository.saveAndFlush(zone);

        // Get the zone
        restZoneMockMvc
            .perform(get(ENTITY_API_URL_ID, zone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zone.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingZone() throws Exception {
        // Get the zone
        restZoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingZone() throws Exception {
        // Initialize the database
        insertedZone = zoneRepository.saveAndFlush(zone);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the zone
        Zone updatedZone = zoneRepository.findById(zone.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedZone are not directly saved in db
        em.detach(updatedZone);
        updatedZone.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).location(UPDATED_LOCATION).isActive(UPDATED_IS_ACTIVE);

        restZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedZone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedZone))
            )
            .andExpect(status().isOk());

        // Validate the Zone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedZoneToMatchAllProperties(updatedZone);
    }

    @Test
    @Transactional
    void putNonExistingZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zone.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(put(ENTITY_API_URL_ID, zone.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zone)))
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zone.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(zone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zone.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(zone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZoneWithPatch() throws Exception {
        // Initialize the database
        insertedZone = zoneRepository.saveAndFlush(zone);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the zone using partial update
        Zone partialUpdatedZone = new Zone();
        partialUpdatedZone.setId(zone.getId());

        partialUpdatedZone.shortName(UPDATED_SHORT_NAME).isActive(UPDATED_IS_ACTIVE);

        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedZone))
            )
            .andExpect(status().isOk());

        // Validate the Zone in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertZoneUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedZone, zone), getPersistedZone(zone));
    }

    @Test
    @Transactional
    void fullUpdateZoneWithPatch() throws Exception {
        // Initialize the database
        insertedZone = zoneRepository.saveAndFlush(zone);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the zone using partial update
        Zone partialUpdatedZone = new Zone();
        partialUpdatedZone.setId(zone.getId());

        partialUpdatedZone.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).location(UPDATED_LOCATION).isActive(UPDATED_IS_ACTIVE);

        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedZone))
            )
            .andExpect(status().isOk());

        // Validate the Zone in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertZoneUpdatableFieldsEquals(partialUpdatedZone, getPersistedZone(partialUpdatedZone));
    }

    @Test
    @Transactional
    void patchNonExistingZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zone.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(patch(ENTITY_API_URL_ID, zone.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(zone)))
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zone.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(zone))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        zone.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(zone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZone() throws Exception {
        // Initialize the database
        insertedZone = zoneRepository.saveAndFlush(zone);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the zone
        restZoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, zone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return zoneRepository.count();
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

    protected Zone getPersistedZone(Zone zone) {
        return zoneRepository.findById(zone.getId()).orElseThrow();
    }

    protected void assertPersistedZoneToMatchAllProperties(Zone expectedZone) {
        assertZoneAllPropertiesEquals(expectedZone, getPersistedZone(expectedZone));
    }

    protected void assertPersistedZoneToMatchUpdatableProperties(Zone expectedZone) {
        assertZoneAllUpdatablePropertiesEquals(expectedZone, getPersistedZone(expectedZone));
    }
}
