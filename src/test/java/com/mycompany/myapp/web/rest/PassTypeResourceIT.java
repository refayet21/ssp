package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PassTypeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PassType;
import com.mycompany.myapp.domain.enumeration.IssueChannelType;
import com.mycompany.myapp.domain.enumeration.PassMediaType;
import com.mycompany.myapp.domain.enumeration.PassUserType;
import com.mycompany.myapp.domain.enumeration.TaxCodeType;
import com.mycompany.myapp.repository.PassTypeRepository;
import com.mycompany.myapp.service.PassTypeService;
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
 * Integration tests for the {@link PassTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PassTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_PRINTED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRINTED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_ISSUE_FEE = 1D;
    private static final Double UPDATED_ISSUE_FEE = 2D;

    private static final Double DEFAULT_RENEW_FEE = 1D;
    private static final Double UPDATED_RENEW_FEE = 2D;

    private static final Double DEFAULT_CANCEL_FEE = 1D;
    private static final Double UPDATED_CANCEL_FEE = 2D;

    private static final Long DEFAULT_MINIMUM_DURATION = 1L;
    private static final Long UPDATED_MINIMUM_DURATION = 2L;

    private static final Long DEFAULT_MAXIMUM_DURATION = 1L;
    private static final Long UPDATED_MAXIMUM_DURATION = 2L;

    private static final IssueChannelType DEFAULT_ISSUE_CHANNEL_TYPE = IssueChannelType.ONPREM;
    private static final IssueChannelType UPDATED_ISSUE_CHANNEL_TYPE = IssueChannelType.ONLINE;

    private static final TaxCodeType DEFAULT_TAX_CODE = TaxCodeType.VAT;
    private static final TaxCodeType UPDATED_TAX_CODE = TaxCodeType.NOVAT;

    private static final PassMediaType DEFAULT_PASS_MEDIA_TYPE = PassMediaType.BARCODE;
    private static final PassMediaType UPDATED_PASS_MEDIA_TYPE = PassMediaType.QRCODE;

    private static final PassUserType DEFAULT_PASS_USER_TYPE = PassUserType.HUMAN;
    private static final PassUserType UPDATED_PASS_USER_TYPE = PassUserType.VEHICLE;

    private static final String ENTITY_API_URL = "/api/pass-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PassTypeRepository passTypeRepository;

    @Mock
    private PassTypeRepository passTypeRepositoryMock;

    @Mock
    private PassTypeService passTypeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassTypeMockMvc;

    private PassType passType;

    private PassType insertedPassType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PassType createEntity(EntityManager em) {
        PassType passType = new PassType()
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .isActive(DEFAULT_IS_ACTIVE)
            .printedName(DEFAULT_PRINTED_NAME)
            .issueFee(DEFAULT_ISSUE_FEE)
            .renewFee(DEFAULT_RENEW_FEE)
            .cancelFee(DEFAULT_CANCEL_FEE)
            .minimumDuration(DEFAULT_MINIMUM_DURATION)
            .maximumDuration(DEFAULT_MAXIMUM_DURATION)
            .issueChannelType(DEFAULT_ISSUE_CHANNEL_TYPE)
            .taxCode(DEFAULT_TAX_CODE)
            .passMediaType(DEFAULT_PASS_MEDIA_TYPE)
            .passUserType(DEFAULT_PASS_USER_TYPE);
        return passType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PassType createUpdatedEntity(EntityManager em) {
        PassType passType = new PassType()
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .printedName(UPDATED_PRINTED_NAME)
            .issueFee(UPDATED_ISSUE_FEE)
            .renewFee(UPDATED_RENEW_FEE)
            .cancelFee(UPDATED_CANCEL_FEE)
            .minimumDuration(UPDATED_MINIMUM_DURATION)
            .maximumDuration(UPDATED_MAXIMUM_DURATION)
            .issueChannelType(UPDATED_ISSUE_CHANNEL_TYPE)
            .taxCode(UPDATED_TAX_CODE)
            .passMediaType(UPDATED_PASS_MEDIA_TYPE)
            .passUserType(UPDATED_PASS_USER_TYPE);
        return passType;
    }

    @BeforeEach
    public void initTest() {
        passType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPassType != null) {
            passTypeRepository.delete(insertedPassType);
            insertedPassType = null;
        }
    }

    @Test
    @Transactional
    void createPassType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PassType
        var returnedPassType = om.readValue(
            restPassTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PassType.class
        );

        // Validate the PassType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPassTypeUpdatableFieldsEquals(returnedPassType, getPersistedPassType(returnedPassType));

        insertedPassType = returnedPassType;
    }

    @Test
    @Transactional
    void createPassTypeWithExistingId() throws Exception {
        // Create the PassType with an existing ID
        passType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passType)))
            .andExpect(status().isBadRequest());

        // Validate the PassType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPassTypes() throws Exception {
        // Initialize the database
        insertedPassType = passTypeRepository.saveAndFlush(passType);

        // Get all the passTypeList
        restPassTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].printedName").value(hasItem(DEFAULT_PRINTED_NAME)))
            .andExpect(jsonPath("$.[*].issueFee").value(hasItem(DEFAULT_ISSUE_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].renewFee").value(hasItem(DEFAULT_RENEW_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].cancelFee").value(hasItem(DEFAULT_CANCEL_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumDuration").value(hasItem(DEFAULT_MINIMUM_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].maximumDuration").value(hasItem(DEFAULT_MAXIMUM_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].issueChannelType").value(hasItem(DEFAULT_ISSUE_CHANNEL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE.toString())))
            .andExpect(jsonPath("$.[*].passMediaType").value(hasItem(DEFAULT_PASS_MEDIA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].passUserType").value(hasItem(DEFAULT_PASS_USER_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPassTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(passTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPassTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(passTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPassTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(passTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPassTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(passTypeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPassType() throws Exception {
        // Initialize the database
        insertedPassType = passTypeRepository.saveAndFlush(passType);

        // Get the passType
        restPassTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, passType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.printedName").value(DEFAULT_PRINTED_NAME))
            .andExpect(jsonPath("$.issueFee").value(DEFAULT_ISSUE_FEE.doubleValue()))
            .andExpect(jsonPath("$.renewFee").value(DEFAULT_RENEW_FEE.doubleValue()))
            .andExpect(jsonPath("$.cancelFee").value(DEFAULT_CANCEL_FEE.doubleValue()))
            .andExpect(jsonPath("$.minimumDuration").value(DEFAULT_MINIMUM_DURATION.intValue()))
            .andExpect(jsonPath("$.maximumDuration").value(DEFAULT_MAXIMUM_DURATION.intValue()))
            .andExpect(jsonPath("$.issueChannelType").value(DEFAULT_ISSUE_CHANNEL_TYPE.toString()))
            .andExpect(jsonPath("$.taxCode").value(DEFAULT_TAX_CODE.toString()))
            .andExpect(jsonPath("$.passMediaType").value(DEFAULT_PASS_MEDIA_TYPE.toString()))
            .andExpect(jsonPath("$.passUserType").value(DEFAULT_PASS_USER_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPassType() throws Exception {
        // Get the passType
        restPassTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPassType() throws Exception {
        // Initialize the database
        insertedPassType = passTypeRepository.saveAndFlush(passType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passType
        PassType updatedPassType = passTypeRepository.findById(passType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPassType are not directly saved in db
        em.detach(updatedPassType);
        updatedPassType
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .printedName(UPDATED_PRINTED_NAME)
            .issueFee(UPDATED_ISSUE_FEE)
            .renewFee(UPDATED_RENEW_FEE)
            .cancelFee(UPDATED_CANCEL_FEE)
            .minimumDuration(UPDATED_MINIMUM_DURATION)
            .maximumDuration(UPDATED_MAXIMUM_DURATION)
            .issueChannelType(UPDATED_ISSUE_CHANNEL_TYPE)
            .taxCode(UPDATED_TAX_CODE)
            .passMediaType(UPDATED_PASS_MEDIA_TYPE)
            .passUserType(UPDATED_PASS_USER_TYPE);

        restPassTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPassType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPassType))
            )
            .andExpect(status().isOk());

        // Validate the PassType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPassTypeToMatchAllProperties(updatedPassType);
    }

    @Test
    @Transactional
    void putNonExistingPassType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passType.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PassType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPassType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PassType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPassType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PassType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePassTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPassType = passTypeRepository.saveAndFlush(passType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passType using partial update
        PassType partialUpdatedPassType = new PassType();
        partialUpdatedPassType.setId(passType.getId());

        partialUpdatedPassType
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .printedName(UPDATED_PRINTED_NAME)
            .renewFee(UPDATED_RENEW_FEE)
            .minimumDuration(UPDATED_MINIMUM_DURATION)
            .passMediaType(UPDATED_PASS_MEDIA_TYPE);

        restPassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPassType))
            )
            .andExpect(status().isOk());

        // Validate the PassType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPassType, passType), getPersistedPassType(passType));
    }

    @Test
    @Transactional
    void fullUpdatePassTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPassType = passTypeRepository.saveAndFlush(passType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passType using partial update
        PassType partialUpdatedPassType = new PassType();
        partialUpdatedPassType.setId(passType.getId());

        partialUpdatedPassType
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .isActive(UPDATED_IS_ACTIVE)
            .printedName(UPDATED_PRINTED_NAME)
            .issueFee(UPDATED_ISSUE_FEE)
            .renewFee(UPDATED_RENEW_FEE)
            .cancelFee(UPDATED_CANCEL_FEE)
            .minimumDuration(UPDATED_MINIMUM_DURATION)
            .maximumDuration(UPDATED_MAXIMUM_DURATION)
            .issueChannelType(UPDATED_ISSUE_CHANNEL_TYPE)
            .taxCode(UPDATED_TAX_CODE)
            .passMediaType(UPDATED_PASS_MEDIA_TYPE)
            .passUserType(UPDATED_PASS_USER_TYPE);

        restPassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPassType))
            )
            .andExpect(status().isOk());

        // Validate the PassType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassTypeUpdatableFieldsEquals(partialUpdatedPassType, getPersistedPassType(partialUpdatedPassType));
    }

    @Test
    @Transactional
    void patchNonExistingPassType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, passType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(passType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PassType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPassType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(passType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PassType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPassType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(passType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PassType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePassType() throws Exception {
        // Initialize the database
        insertedPassType = passTypeRepository.saveAndFlush(passType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the passType
        restPassTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, passType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return passTypeRepository.count();
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

    protected PassType getPersistedPassType(PassType passType) {
        return passTypeRepository.findById(passType.getId()).orElseThrow();
    }

    protected void assertPersistedPassTypeToMatchAllProperties(PassType expectedPassType) {
        assertPassTypeAllPropertiesEquals(expectedPassType, getPersistedPassType(expectedPassType));
    }

    protected void assertPersistedPassTypeToMatchUpdatableProperties(PassType expectedPassType) {
        assertPassTypeAllUpdatablePropertiesEquals(expectedPassType, getPersistedPassType(expectedPassType));
    }
}
