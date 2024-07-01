package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UnionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Union;
import com.mycompany.myapp.domain.Upazila;
import com.mycompany.myapp.repository.UnionRepository;
import com.mycompany.myapp.service.UnionService;
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
 * Integration tests for the {@link UnionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UnionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/unions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UnionRepository unionRepository;

    @Mock
    private UnionRepository unionRepositoryMock;

    @Mock
    private UnionService unionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnionMockMvc;

    private Union union;

    private Union insertedUnion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Union createEntity(EntityManager em) {
        Union union = new Union().name(DEFAULT_NAME);
        // Add required entity
        Upazila upazila;
        if (TestUtil.findAll(em, Upazila.class).isEmpty()) {
            upazila = UpazilaResourceIT.createEntity(em);
            em.persist(upazila);
            em.flush();
        } else {
            upazila = TestUtil.findAll(em, Upazila.class).get(0);
        }
        union.setUpazila(upazila);
        return union;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Union createUpdatedEntity(EntityManager em) {
        Union union = new Union().name(UPDATED_NAME);
        // Add required entity
        Upazila upazila;
        if (TestUtil.findAll(em, Upazila.class).isEmpty()) {
            upazila = UpazilaResourceIT.createUpdatedEntity(em);
            em.persist(upazila);
            em.flush();
        } else {
            upazila = TestUtil.findAll(em, Upazila.class).get(0);
        }
        union.setUpazila(upazila);
        return union;
    }

    @BeforeEach
    public void initTest() {
        union = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUnion != null) {
            unionRepository.delete(insertedUnion);
            insertedUnion = null;
        }
    }

    @Test
    @Transactional
    void createUnion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Union
        var returnedUnion = om.readValue(
            restUnionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(union)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Union.class
        );

        // Validate the Union in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUnionUpdatableFieldsEquals(returnedUnion, getPersistedUnion(returnedUnion));

        insertedUnion = returnedUnion;
    }

    @Test
    @Transactional
    void createUnionWithExistingId() throws Exception {
        // Create the Union with an existing ID
        union.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(union)))
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        union.setName(null);

        // Create the Union, which fails.

        restUnionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(union)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnions() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        // Get all the unionList
        restUnionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(union.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUnionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(unionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUnionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(unionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUnionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(unionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUnionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(unionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUnion() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        // Get the union
        restUnionMockMvc
            .perform(get(ENTITY_API_URL_ID, union.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(union.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getUnionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        Long id = union.getId();

        defaultUnionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUnionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUnionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUnionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        // Get all the unionList where name equals to
        defaultUnionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUnionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        // Get all the unionList where name in
        defaultUnionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUnionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        // Get all the unionList where name is not null
        defaultUnionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllUnionsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        // Get all the unionList where name contains
        defaultUnionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUnionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        // Get all the unionList where name does not contain
        defaultUnionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllUnionsByUpazilaIsEqualToSomething() throws Exception {
        Upazila upazila;
        if (TestUtil.findAll(em, Upazila.class).isEmpty()) {
            unionRepository.saveAndFlush(union);
            upazila = UpazilaResourceIT.createEntity(em);
        } else {
            upazila = TestUtil.findAll(em, Upazila.class).get(0);
        }
        em.persist(upazila);
        em.flush();
        union.setUpazila(upazila);
        unionRepository.saveAndFlush(union);
        Long upazilaId = upazila.getId();
        // Get all the unionList where upazila equals to upazilaId
        defaultUnionShouldBeFound("upazilaId.equals=" + upazilaId);

        // Get all the unionList where upazila equals to (upazilaId + 1)
        defaultUnionShouldNotBeFound("upazilaId.equals=" + (upazilaId + 1));
    }

    private void defaultUnionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUnionShouldBeFound(shouldBeFound);
        defaultUnionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUnionShouldBeFound(String filter) throws Exception {
        restUnionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(union.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restUnionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUnionShouldNotBeFound(String filter) throws Exception {
        restUnionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUnionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUnion() throws Exception {
        // Get the union
        restUnionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnion() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the union
        Union updatedUnion = unionRepository.findById(union.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUnion are not directly saved in db
        em.detach(updatedUnion);
        updatedUnion.name(UPDATED_NAME);

        restUnionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUnion))
            )
            .andExpect(status().isOk());

        // Validate the Union in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUnionToMatchAllProperties(updatedUnion);
    }

    @Test
    @Transactional
    void putNonExistingUnion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        union.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(put(ENTITY_API_URL_ID, union.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(union)))
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        union.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(union))
            )
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        union.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(union)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Union in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnionWithPatch() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the union using partial update
        Union partialUpdatedUnion = new Union();
        partialUpdatedUnion.setId(union.getId());

        restUnionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnion))
            )
            .andExpect(status().isOk());

        // Validate the Union in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUnionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUnion, union), getPersistedUnion(union));
    }

    @Test
    @Transactional
    void fullUpdateUnionWithPatch() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the union using partial update
        Union partialUpdatedUnion = new Union();
        partialUpdatedUnion.setId(union.getId());

        partialUpdatedUnion.name(UPDATED_NAME);

        restUnionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUnion))
            )
            .andExpect(status().isOk());

        // Validate the Union in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUnionUpdatableFieldsEquals(partialUpdatedUnion, getPersistedUnion(partialUpdatedUnion));
    }

    @Test
    @Transactional
    void patchNonExistingUnion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        union.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, union.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(union))
            )
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        union.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(union))
            )
            .andExpect(status().isBadRequest());

        // Validate the Union in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        union.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(union)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Union in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnion() throws Exception {
        // Initialize the database
        insertedUnion = unionRepository.saveAndFlush(union);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the union
        restUnionMockMvc
            .perform(delete(ENTITY_API_URL_ID, union.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return unionRepository.count();
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

    protected Union getPersistedUnion(Union union) {
        return unionRepository.findById(union.getId()).orElseThrow();
    }

    protected void assertPersistedUnionToMatchAllProperties(Union expectedUnion) {
        assertUnionAllPropertiesEquals(expectedUnion, getPersistedUnion(expectedUnion));
    }

    protected void assertPersistedUnionToMatchUpdatableProperties(Union expectedUnion) {
        assertUnionAllUpdatablePropertiesEquals(expectedUnion, getPersistedUnion(expectedUnion));
    }
}
