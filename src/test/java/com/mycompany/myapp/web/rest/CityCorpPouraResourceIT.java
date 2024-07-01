package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CityCorpPouraAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CityCorpPoura;
import com.mycompany.myapp.domain.RMO;
import com.mycompany.myapp.repository.CityCorpPouraRepository;
import com.mycompany.myapp.service.CityCorpPouraService;
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
 * Integration tests for the {@link CityCorpPouraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CityCorpPouraResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/city-corp-pouras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CityCorpPouraRepository cityCorpPouraRepository;

    @Mock
    private CityCorpPouraRepository cityCorpPouraRepositoryMock;

    @Mock
    private CityCorpPouraService cityCorpPouraServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCityCorpPouraMockMvc;

    private CityCorpPoura cityCorpPoura;

    private CityCorpPoura insertedCityCorpPoura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CityCorpPoura createEntity(EntityManager em) {
        CityCorpPoura cityCorpPoura = new CityCorpPoura().name(DEFAULT_NAME);
        // Add required entity
        RMO rMO;
        if (TestUtil.findAll(em, RMO.class).isEmpty()) {
            rMO = RMOResourceIT.createEntity(em);
            em.persist(rMO);
            em.flush();
        } else {
            rMO = TestUtil.findAll(em, RMO.class).get(0);
        }
        cityCorpPoura.setRmo(rMO);
        return cityCorpPoura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CityCorpPoura createUpdatedEntity(EntityManager em) {
        CityCorpPoura cityCorpPoura = new CityCorpPoura().name(UPDATED_NAME);
        // Add required entity
        RMO rMO;
        if (TestUtil.findAll(em, RMO.class).isEmpty()) {
            rMO = RMOResourceIT.createUpdatedEntity(em);
            em.persist(rMO);
            em.flush();
        } else {
            rMO = TestUtil.findAll(em, RMO.class).get(0);
        }
        cityCorpPoura.setRmo(rMO);
        return cityCorpPoura;
    }

    @BeforeEach
    public void initTest() {
        cityCorpPoura = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCityCorpPoura != null) {
            cityCorpPouraRepository.delete(insertedCityCorpPoura);
            insertedCityCorpPoura = null;
        }
    }

    @Test
    @Transactional
    void createCityCorpPoura() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CityCorpPoura
        var returnedCityCorpPoura = om.readValue(
            restCityCorpPouraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityCorpPoura)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CityCorpPoura.class
        );

        // Validate the CityCorpPoura in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCityCorpPouraUpdatableFieldsEquals(returnedCityCorpPoura, getPersistedCityCorpPoura(returnedCityCorpPoura));

        insertedCityCorpPoura = returnedCityCorpPoura;
    }

    @Test
    @Transactional
    void createCityCorpPouraWithExistingId() throws Exception {
        // Create the CityCorpPoura with an existing ID
        cityCorpPoura.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityCorpPouraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityCorpPoura)))
            .andExpect(status().isBadRequest());

        // Validate the CityCorpPoura in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cityCorpPoura.setName(null);

        // Create the CityCorpPoura, which fails.

        restCityCorpPouraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityCorpPoura)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCityCorpPouras() throws Exception {
        // Initialize the database
        insertedCityCorpPoura = cityCorpPouraRepository.saveAndFlush(cityCorpPoura);

        // Get all the cityCorpPouraList
        restCityCorpPouraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cityCorpPoura.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCityCorpPourasWithEagerRelationshipsIsEnabled() throws Exception {
        when(cityCorpPouraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCityCorpPouraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cityCorpPouraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCityCorpPourasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cityCorpPouraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCityCorpPouraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cityCorpPouraRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCityCorpPoura() throws Exception {
        // Initialize the database
        insertedCityCorpPoura = cityCorpPouraRepository.saveAndFlush(cityCorpPoura);

        // Get the cityCorpPoura
        restCityCorpPouraMockMvc
            .perform(get(ENTITY_API_URL_ID, cityCorpPoura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cityCorpPoura.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCityCorpPoura() throws Exception {
        // Get the cityCorpPoura
        restCityCorpPouraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCityCorpPoura() throws Exception {
        // Initialize the database
        insertedCityCorpPoura = cityCorpPouraRepository.saveAndFlush(cityCorpPoura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cityCorpPoura
        CityCorpPoura updatedCityCorpPoura = cityCorpPouraRepository.findById(cityCorpPoura.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCityCorpPoura are not directly saved in db
        em.detach(updatedCityCorpPoura);
        updatedCityCorpPoura.name(UPDATED_NAME);

        restCityCorpPouraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCityCorpPoura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCityCorpPoura))
            )
            .andExpect(status().isOk());

        // Validate the CityCorpPoura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCityCorpPouraToMatchAllProperties(updatedCityCorpPoura);
    }

    @Test
    @Transactional
    void putNonExistingCityCorpPoura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityCorpPoura.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityCorpPouraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityCorpPoura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityCorpPoura))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityCorpPoura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCityCorpPoura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityCorpPoura.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityCorpPouraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityCorpPoura))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityCorpPoura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCityCorpPoura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityCorpPoura.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityCorpPouraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityCorpPoura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CityCorpPoura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCityCorpPouraWithPatch() throws Exception {
        // Initialize the database
        insertedCityCorpPoura = cityCorpPouraRepository.saveAndFlush(cityCorpPoura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cityCorpPoura using partial update
        CityCorpPoura partialUpdatedCityCorpPoura = new CityCorpPoura();
        partialUpdatedCityCorpPoura.setId(cityCorpPoura.getId());

        partialUpdatedCityCorpPoura.name(UPDATED_NAME);

        restCityCorpPouraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCityCorpPoura.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCityCorpPoura))
            )
            .andExpect(status().isOk());

        // Validate the CityCorpPoura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityCorpPouraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCityCorpPoura, cityCorpPoura),
            getPersistedCityCorpPoura(cityCorpPoura)
        );
    }

    @Test
    @Transactional
    void fullUpdateCityCorpPouraWithPatch() throws Exception {
        // Initialize the database
        insertedCityCorpPoura = cityCorpPouraRepository.saveAndFlush(cityCorpPoura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cityCorpPoura using partial update
        CityCorpPoura partialUpdatedCityCorpPoura = new CityCorpPoura();
        partialUpdatedCityCorpPoura.setId(cityCorpPoura.getId());

        partialUpdatedCityCorpPoura.name(UPDATED_NAME);

        restCityCorpPouraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCityCorpPoura.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCityCorpPoura))
            )
            .andExpect(status().isOk());

        // Validate the CityCorpPoura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityCorpPouraUpdatableFieldsEquals(partialUpdatedCityCorpPoura, getPersistedCityCorpPoura(partialUpdatedCityCorpPoura));
    }

    @Test
    @Transactional
    void patchNonExistingCityCorpPoura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityCorpPoura.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityCorpPouraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cityCorpPoura.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cityCorpPoura))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityCorpPoura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCityCorpPoura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityCorpPoura.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityCorpPouraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cityCorpPoura))
            )
            .andExpect(status().isBadRequest());

        // Validate the CityCorpPoura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCityCorpPoura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cityCorpPoura.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityCorpPouraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cityCorpPoura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CityCorpPoura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCityCorpPoura() throws Exception {
        // Initialize the database
        insertedCityCorpPoura = cityCorpPouraRepository.saveAndFlush(cityCorpPoura);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cityCorpPoura
        restCityCorpPouraMockMvc
            .perform(delete(ENTITY_API_URL_ID, cityCorpPoura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cityCorpPouraRepository.count();
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

    protected CityCorpPoura getPersistedCityCorpPoura(CityCorpPoura cityCorpPoura) {
        return cityCorpPouraRepository.findById(cityCorpPoura.getId()).orElseThrow();
    }

    protected void assertPersistedCityCorpPouraToMatchAllProperties(CityCorpPoura expectedCityCorpPoura) {
        assertCityCorpPouraAllPropertiesEquals(expectedCityCorpPoura, getPersistedCityCorpPoura(expectedCityCorpPoura));
    }

    protected void assertPersistedCityCorpPouraToMatchUpdatableProperties(CityCorpPoura expectedCityCorpPoura) {
        assertCityCorpPouraAllUpdatablePropertiesEquals(expectedCityCorpPoura, getPersistedCityCorpPoura(expectedCityCorpPoura));
    }
}
