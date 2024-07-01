package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PostOfficeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.domain.PostOffice;
import com.mycompany.myapp.repository.PostOfficeRepository;
import com.mycompany.myapp.service.PostOfficeService;
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
 * Integration tests for the {@link PostOfficeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PostOfficeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/post-offices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostOfficeRepository postOfficeRepository;

    @Mock
    private PostOfficeRepository postOfficeRepositoryMock;

    @Mock
    private PostOfficeService postOfficeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostOfficeMockMvc;

    private PostOffice postOffice;

    private PostOffice insertedPostOffice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostOffice createEntity(EntityManager em) {
        PostOffice postOffice = new PostOffice().name(DEFAULT_NAME).code(DEFAULT_CODE);
        // Add required entity
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        postOffice.setDistrict(district);
        return postOffice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostOffice createUpdatedEntity(EntityManager em) {
        PostOffice postOffice = new PostOffice().name(UPDATED_NAME).code(UPDATED_CODE);
        // Add required entity
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            district = DistrictResourceIT.createUpdatedEntity(em);
            em.persist(district);
            em.flush();
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        postOffice.setDistrict(district);
        return postOffice;
    }

    @BeforeEach
    public void initTest() {
        postOffice = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPostOffice != null) {
            postOfficeRepository.delete(insertedPostOffice);
            insertedPostOffice = null;
        }
    }

    @Test
    @Transactional
    void createPostOffice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PostOffice
        var returnedPostOffice = om.readValue(
            restPostOfficeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postOffice)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostOffice.class
        );

        // Validate the PostOffice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPostOfficeUpdatableFieldsEquals(returnedPostOffice, getPersistedPostOffice(returnedPostOffice));

        insertedPostOffice = returnedPostOffice;
    }

    @Test
    @Transactional
    void createPostOfficeWithExistingId() throws Exception {
        // Create the PostOffice with an existing ID
        postOffice.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostOfficeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postOffice)))
            .andExpect(status().isBadRequest());

        // Validate the PostOffice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postOffice.setName(null);

        // Create the PostOffice, which fails.

        restPostOfficeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postOffice)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postOffice.setCode(null);

        // Create the PostOffice, which fails.

        restPostOfficeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postOffice)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostOffices() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList
        restPostOfficeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postOffice.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostOfficesWithEagerRelationshipsIsEnabled() throws Exception {
        when(postOfficeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPostOfficeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(postOfficeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostOfficesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(postOfficeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPostOfficeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(postOfficeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPostOffice() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get the postOffice
        restPostOfficeMockMvc
            .perform(get(ENTITY_API_URL_ID, postOffice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postOffice.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getPostOfficesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        Long id = postOffice.getId();

        defaultPostOfficeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPostOfficeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPostOfficeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPostOfficesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where name equals to
        defaultPostOfficeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPostOfficesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where name in
        defaultPostOfficeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPostOfficesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where name is not null
        defaultPostOfficeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPostOfficesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where name contains
        defaultPostOfficeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPostOfficesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where name does not contain
        defaultPostOfficeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPostOfficesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where code equals to
        defaultPostOfficeFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPostOfficesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where code in
        defaultPostOfficeFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPostOfficesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where code is not null
        defaultPostOfficeFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllPostOfficesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where code contains
        defaultPostOfficeFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllPostOfficesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        // Get all the postOfficeList where code does not contain
        defaultPostOfficeFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllPostOfficesByDistrictIsEqualToSomething() throws Exception {
        District district;
        if (TestUtil.findAll(em, District.class).isEmpty()) {
            postOfficeRepository.saveAndFlush(postOffice);
            district = DistrictResourceIT.createEntity(em);
        } else {
            district = TestUtil.findAll(em, District.class).get(0);
        }
        em.persist(district);
        em.flush();
        postOffice.setDistrict(district);
        postOfficeRepository.saveAndFlush(postOffice);
        Long districtId = district.getId();
        // Get all the postOfficeList where district equals to districtId
        defaultPostOfficeShouldBeFound("districtId.equals=" + districtId);

        // Get all the postOfficeList where district equals to (districtId + 1)
        defaultPostOfficeShouldNotBeFound("districtId.equals=" + (districtId + 1));
    }

    private void defaultPostOfficeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPostOfficeShouldBeFound(shouldBeFound);
        defaultPostOfficeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostOfficeShouldBeFound(String filter) throws Exception {
        restPostOfficeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postOffice.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restPostOfficeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostOfficeShouldNotBeFound(String filter) throws Exception {
        restPostOfficeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostOfficeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPostOffice() throws Exception {
        // Get the postOffice
        restPostOfficeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostOffice() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postOffice
        PostOffice updatedPostOffice = postOfficeRepository.findById(postOffice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostOffice are not directly saved in db
        em.detach(updatedPostOffice);
        updatedPostOffice.name(UPDATED_NAME).code(UPDATED_CODE);

        restPostOfficeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPostOffice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPostOffice))
            )
            .andExpect(status().isOk());

        // Validate the PostOffice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostOfficeToMatchAllProperties(updatedPostOffice);
    }

    @Test
    @Transactional
    void putNonExistingPostOffice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postOffice.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostOfficeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postOffice.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postOffice))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostOffice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostOffice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postOffice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostOfficeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postOffice))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostOffice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostOffice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postOffice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostOfficeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postOffice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostOffice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostOfficeWithPatch() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postOffice using partial update
        PostOffice partialUpdatedPostOffice = new PostOffice();
        partialUpdatedPostOffice.setId(postOffice.getId());

        partialUpdatedPostOffice.name(UPDATED_NAME).code(UPDATED_CODE);

        restPostOfficeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostOffice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostOffice))
            )
            .andExpect(status().isOk());

        // Validate the PostOffice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostOfficeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPostOffice, postOffice),
            getPersistedPostOffice(postOffice)
        );
    }

    @Test
    @Transactional
    void fullUpdatePostOfficeWithPatch() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postOffice using partial update
        PostOffice partialUpdatedPostOffice = new PostOffice();
        partialUpdatedPostOffice.setId(postOffice.getId());

        partialUpdatedPostOffice.name(UPDATED_NAME).code(UPDATED_CODE);

        restPostOfficeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostOffice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostOffice))
            )
            .andExpect(status().isOk());

        // Validate the PostOffice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostOfficeUpdatableFieldsEquals(partialUpdatedPostOffice, getPersistedPostOffice(partialUpdatedPostOffice));
    }

    @Test
    @Transactional
    void patchNonExistingPostOffice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postOffice.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostOfficeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postOffice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postOffice))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostOffice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostOffice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postOffice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostOfficeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postOffice))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostOffice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostOffice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postOffice.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostOfficeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postOffice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostOffice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostOffice() throws Exception {
        // Initialize the database
        insertedPostOffice = postOfficeRepository.saveAndFlush(postOffice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the postOffice
        restPostOfficeMockMvc
            .perform(delete(ENTITY_API_URL_ID, postOffice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postOfficeRepository.count();
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

    protected PostOffice getPersistedPostOffice(PostOffice postOffice) {
        return postOfficeRepository.findById(postOffice.getId()).orElseThrow();
    }

    protected void assertPersistedPostOfficeToMatchAllProperties(PostOffice expectedPostOffice) {
        assertPostOfficeAllPropertiesEquals(expectedPostOffice, getPersistedPostOffice(expectedPostOffice));
    }

    protected void assertPersistedPostOfficeToMatchUpdatableProperties(PostOffice expectedPostOffice) {
        assertPostOfficeAllUpdatablePropertiesEquals(expectedPostOffice, getPersistedPostOffice(expectedPostOffice));
    }
}
