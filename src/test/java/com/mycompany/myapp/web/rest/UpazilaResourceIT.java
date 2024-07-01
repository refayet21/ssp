package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UpazilaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.domain.Upazila;
import com.mycompany.myapp.repository.UpazilaRepository;
import com.mycompany.myapp.service.UpazilaService;
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
 * Integration tests for the {@link UpazilaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UpazilaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/upazilas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UpazilaRepository upazilaRepository;

    @Mock
    private UpazilaRepository upazilaRepositoryMock;

    @Mock
    private UpazilaService upazilaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUpazilaMockMvc;

    private Upazila upazila;

    private Upazila insertedUpazila;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upazila createEntity(EntityManager em) {
        Upazila upazila = new Upazila().name(DEFAULT_NAME);
        // Add required entity
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        upazila.setDistrict(district);
        return upazila;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upazila createUpdatedEntity(EntityManager em) {
        Upazila upazila = new Upazila().name(UPDATED_NAME);
        // Add required entity
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createUpdatedEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        upazila.setDistrict(district);
        return upazila;
    }

    @BeforeEach
    public void initTest() {
        upazila = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUpazila != null) {
            upazilaRepository.delete(insertedUpazila);
            insertedUpazila = null;
        }
    }

    @Test
    @Transactional
    void createUpazila() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Upazila
        var returnedUpazila = om.readValue(
            restUpazilaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(upazila)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Upazila.class
        );

        // Validate the Upazila in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUpazilaUpdatableFieldsEquals(returnedUpazila, getPersistedUpazila(returnedUpazila));

        insertedUpazila = returnedUpazila;
    }

    @Test
    @Transactional
    void createUpazilaWithExistingId() throws Exception {
        // Create the Upazila with an existing ID
        upazila.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(upazila)))
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        upazila.setName(null);

        // Create the Upazila, which fails.

        restUpazilaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(upazila)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUpazilas() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        // Get all the upazilaList
        restUpazilaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(upazila.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUpazilasWithEagerRelationshipsIsEnabled() throws Exception {
        when(upazilaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUpazilaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(upazilaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUpazilasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(upazilaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUpazilaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(upazilaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUpazila() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        // Get the upazila
        restUpazilaMockMvc
            .perform(get(ENTITY_API_URL_ID, upazila.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(upazila.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getUpazilasByIdFiltering() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        Long id = upazila.getId();

        defaultUpazilaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUpazilaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUpazilaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUpazilasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        // Get all the upazilaList where name equals to
        defaultUpazilaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUpazilasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        // Get all the upazilaList where name in
        defaultUpazilaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUpazilasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        // Get all the upazilaList where name is not null
        defaultUpazilaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUpazilasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        // Get all the upazilaList where name contains
        defaultUpazilaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUpazilasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        // Get all the upazilaList where name does not contain
        defaultUpazilaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUpazilasByDistrictIsEqualToSomething() throws Exception {
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            upazilaRepository.saveAndFlush(upazila);
            district = DistrictResourceIT.createEntity(em);
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        em.persist(district);
        em.flush();
        upazila.setDistrict(district);
        upazilaRepository.saveAndFlush(upazila);
        Long districtId = district.getId();
        // Get all the upazilaList where district equals to districtId
        defaultUpazilaShouldBeFound("districtId.equals=" + districtId);

        // Get all the upazilaList where district equals to (districtId + 1)
        defaultUpazilaShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    private void defaultUpazilaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUpazilaShouldBeFound(shouldBeFound);
        defaultUpazilaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUpazilaShouldBeFound(String filter) throws Exception {
        restUpazilaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(upazila.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restUpazilaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUpazilaShouldNotBeFound(String filter) throws Exception {
        restUpazilaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUpazilaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUpazila() throws Exception {
        // Get the upazila
        restUpazilaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUpazila() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the upazila
        Upazila updatedUpazila = upazilaRepository.findById(upazila.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUpazila are not directly saved in db
        em.detach(updatedUpazila);
        updatedUpazila.name(UPDATED_NAME);

        restUpazilaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUpazila.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUpazila))
            )
            .andExpect(status().isOk());

        // Validate the Upazila in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUpazilaToMatchAllProperties(updatedUpazila);
    }

    @Test
    @Transactional
    void putNonExistingUpazila() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upazila.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(put(ENTITY_API_URL_ID, upazila.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(upazila)))
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUpazila() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upazila.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(upazila))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUpazila() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upazila.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(upazila)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Upazila in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUpazilaWithPatch() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the upazila using partial update
        Upazila partialUpdatedUpazila = new Upazila();
        partialUpdatedUpazila.setId(upazila.getId());

        partialUpdatedUpazila.name(UPDATED_NAME);

        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUpazila.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUpazila))
            )
            .andExpect(status().isOk());

        // Validate the Upazila in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUpazilaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUpazila, upazila), getPersistedUpazila(upazila));
    }

    @Test
    @Transactional
    void fullUpdateUpazilaWithPatch() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the upazila using partial update
        Upazila partialUpdatedUpazila = new Upazila();
        partialUpdatedUpazila.setId(upazila.getId());

        partialUpdatedUpazila.name(UPDATED_NAME);

        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUpazila.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUpazila))
            )
            .andExpect(status().isOk());

        // Validate the Upazila in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUpazilaUpdatableFieldsEquals(partialUpdatedUpazila, getPersistedUpazila(partialUpdatedUpazila));
    }

    @Test
    @Transactional
    void patchNonExistingUpazila() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upazila.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, upazila.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(upazila))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUpazila() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upazila.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(upazila))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upazila in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUpazila() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        upazila.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpazilaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(upazila)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Upazila in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUpazila() throws Exception {
        // Initialize the database
        insertedUpazila = upazilaRepository.saveAndFlush(upazila);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the upazila
        restUpazilaMockMvc
            .perform(delete(ENTITY_API_URL_ID, upazila.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return upazilaRepository.count();
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

    protected Upazila getPersistedUpazila(Upazila upazila) {
        return upazilaRepository.findById(upazila.getId()).orElseThrow();
    }

    protected void assertPersistedUpazilaToMatchAllProperties(Upazila expectedUpazila) {
        assertUpazilaAllPropertiesEquals(expectedUpazila, getPersistedUpazila(expectedUpazila));
    }

    protected void assertPersistedUpazilaToMatchUpdatableProperties(Upazila expectedUpazila) {
        assertUpazilaAllUpdatablePropertiesEquals(expectedUpazila, getPersistedUpazila(expectedUpazila));
    }
}
