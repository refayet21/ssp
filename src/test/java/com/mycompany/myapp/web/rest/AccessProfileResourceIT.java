package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AccessProfileAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AccessProfile;
import com.mycompany.myapp.domain.enumeration.ActionType;
import com.mycompany.myapp.repository.AccessProfileRepository;
import com.mycompany.myapp.service.AccessProfileService;
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
 * Integration tests for the {@link AccessProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AccessProfileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_START_TIME_OF_DAY = 1;
    private static final Integer UPDATED_START_TIME_OF_DAY = 2;

    private static final Integer DEFAULT_END_TIME_OF_DAY = 1;
    private static final Integer UPDATED_END_TIME_OF_DAY = 2;

    private static final Integer DEFAULT_DAY_OF_WEEK = 1;
    private static final Integer UPDATED_DAY_OF_WEEK = 2;

    private static final ActionType DEFAULT_ACTION = ActionType.ALLOW;
    private static final ActionType UPDATED_ACTION = ActionType.DENY;

    private static final String ENTITY_API_URL = "/api/access-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccessProfileRepository accessProfileRepository;

    @Mock
    private AccessProfileRepository accessProfileRepositoryMock;

    @Mock
    private AccessProfileService accessProfileServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccessProfileMockMvc;

    private AccessProfile accessProfile;

    private AccessProfile insertedAccessProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessProfile createEntity(EntityManager em) {
        AccessProfile accessProfile = new AccessProfile()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .startTimeOfDay(DEFAULT_START_TIME_OF_DAY)
            .endTimeOfDay(DEFAULT_END_TIME_OF_DAY)
            .dayOfWeek(DEFAULT_DAY_OF_WEEK)
            .action(DEFAULT_ACTION);
        return accessProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessProfile createUpdatedEntity(EntityManager em) {
        AccessProfile accessProfile = new AccessProfile()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startTimeOfDay(UPDATED_START_TIME_OF_DAY)
            .endTimeOfDay(UPDATED_END_TIME_OF_DAY)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .action(UPDATED_ACTION);
        return accessProfile;
    }

    @BeforeEach
    public void initTest() {
        accessProfile = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccessProfile != null) {
            accessProfileRepository.delete(insertedAccessProfile);
            insertedAccessProfile = null;
        }
    }

    @Test
    @Transactional
    void createAccessProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AccessProfile
        var returnedAccessProfile = om.readValue(
            restAccessProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accessProfile)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccessProfile.class
        );

        // Validate the AccessProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAccessProfileUpdatableFieldsEquals(returnedAccessProfile, getPersistedAccessProfile(returnedAccessProfile));

        insertedAccessProfile = returnedAccessProfile;
    }

    @Test
    @Transactional
    void createAccessProfileWithExistingId() throws Exception {
        // Create the AccessProfile with an existing ID
        accessProfile.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccessProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accessProfile)))
            .andExpect(status().isBadRequest());

        // Validate the AccessProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccessProfiles() throws Exception {
        // Initialize the database
        insertedAccessProfile = accessProfileRepository.saveAndFlush(accessProfile);

        // Get all the accessProfileList
        restAccessProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accessProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startTimeOfDay").value(hasItem(DEFAULT_START_TIME_OF_DAY)))
            .andExpect(jsonPath("$.[*].endTimeOfDay").value(hasItem(DEFAULT_END_TIME_OF_DAY)))
            .andExpect(jsonPath("$.[*].dayOfWeek").value(hasItem(DEFAULT_DAY_OF_WEEK)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAccessProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(accessProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAccessProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(accessProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAccessProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(accessProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAccessProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(accessProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAccessProfile() throws Exception {
        // Initialize the database
        insertedAccessProfile = accessProfileRepository.saveAndFlush(accessProfile);

        // Get the accessProfile
        restAccessProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, accessProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accessProfile.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startTimeOfDay").value(DEFAULT_START_TIME_OF_DAY))
            .andExpect(jsonPath("$.endTimeOfDay").value(DEFAULT_END_TIME_OF_DAY))
            .andExpect(jsonPath("$.dayOfWeek").value(DEFAULT_DAY_OF_WEEK))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccessProfile() throws Exception {
        // Get the accessProfile
        restAccessProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccessProfile() throws Exception {
        // Initialize the database
        insertedAccessProfile = accessProfileRepository.saveAndFlush(accessProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accessProfile
        AccessProfile updatedAccessProfile = accessProfileRepository.findById(accessProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccessProfile are not directly saved in db
        em.detach(updatedAccessProfile);
        updatedAccessProfile
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startTimeOfDay(UPDATED_START_TIME_OF_DAY)
            .endTimeOfDay(UPDATED_END_TIME_OF_DAY)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .action(UPDATED_ACTION);

        restAccessProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccessProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAccessProfile))
            )
            .andExpect(status().isOk());

        // Validate the AccessProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccessProfileToMatchAllProperties(updatedAccessProfile);
    }

    @Test
    @Transactional
    void putNonExistingAccessProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessProfile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accessProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accessProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccessProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accessProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccessProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accessProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccessProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccessProfileWithPatch() throws Exception {
        // Initialize the database
        insertedAccessProfile = accessProfileRepository.saveAndFlush(accessProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accessProfile using partial update
        AccessProfile partialUpdatedAccessProfile = new AccessProfile();
        partialUpdatedAccessProfile.setId(accessProfile.getId());

        partialUpdatedAccessProfile
            .startTimeOfDay(UPDATED_START_TIME_OF_DAY)
            .endTimeOfDay(UPDATED_END_TIME_OF_DAY)
            .dayOfWeek(UPDATED_DAY_OF_WEEK);

        restAccessProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccessProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccessProfile))
            )
            .andExpect(status().isOk());

        // Validate the AccessProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccessProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccessProfile, accessProfile),
            getPersistedAccessProfile(accessProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccessProfileWithPatch() throws Exception {
        // Initialize the database
        insertedAccessProfile = accessProfileRepository.saveAndFlush(accessProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accessProfile using partial update
        AccessProfile partialUpdatedAccessProfile = new AccessProfile();
        partialUpdatedAccessProfile.setId(accessProfile.getId());

        partialUpdatedAccessProfile
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .startTimeOfDay(UPDATED_START_TIME_OF_DAY)
            .endTimeOfDay(UPDATED_END_TIME_OF_DAY)
            .dayOfWeek(UPDATED_DAY_OF_WEEK)
            .action(UPDATED_ACTION);

        restAccessProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccessProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccessProfile))
            )
            .andExpect(status().isOk());

        // Validate the AccessProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccessProfileUpdatableFieldsEquals(partialUpdatedAccessProfile, getPersistedAccessProfile(partialUpdatedAccessProfile));
    }

    @Test
    @Transactional
    void patchNonExistingAccessProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessProfile.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accessProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accessProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccessProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accessProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccessProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccessProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accessProfile.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccessProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accessProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccessProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccessProfile() throws Exception {
        // Initialize the database
        insertedAccessProfile = accessProfileRepository.saveAndFlush(accessProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the accessProfile
        restAccessProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, accessProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return accessProfileRepository.count();
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

    protected AccessProfile getPersistedAccessProfile(AccessProfile accessProfile) {
        return accessProfileRepository.findById(accessProfile.getId()).orElseThrow();
    }

    protected void assertPersistedAccessProfileToMatchAllProperties(AccessProfile expectedAccessProfile) {
        assertAccessProfileAllPropertiesEquals(expectedAccessProfile, getPersistedAccessProfile(expectedAccessProfile));
    }

    protected void assertPersistedAccessProfileToMatchUpdatableProperties(AccessProfile expectedAccessProfile) {
        assertAccessProfileAllUpdatablePropertiesEquals(expectedAccessProfile, getPersistedAccessProfile(expectedAccessProfile));
    }
}
