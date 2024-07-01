package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AgencyLicenseAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Agency;
import com.mycompany.myapp.domain.AgencyLicense;
import com.mycompany.myapp.domain.AgencyLicenseType;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AgencyLicenseRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.AgencyLicenseService;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AgencyLicenseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AgencyLicenseResourceIT {

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NO = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ISSUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/agency-licenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgencyLicenseRepository agencyLicenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private AgencyLicenseRepository agencyLicenseRepositoryMock;

    @Mock
    private AgencyLicenseService agencyLicenseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgencyLicenseMockMvc;

    private AgencyLicense agencyLicense;

    private AgencyLicense insertedAgencyLicense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyLicense createEntity(EntityManager em) {
        AgencyLicense agencyLicense = new AgencyLicense()
            .filePath(DEFAULT_FILE_PATH)
            .serialNo(DEFAULT_SERIAL_NO)
            .issueDate(DEFAULT_ISSUE_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE);
        // Add required entity
        AgencyLicenseType agencyLicenseType;
        if (TestUtil.findAll(em, AgencyLicenseType.class).isEmpty()) {
            agencyLicenseType = AgencyLicenseTypeResourceIT.createEntity(em);
            em.persist(agencyLicenseType);
            em.flush();
        } else {
            agencyLicenseType = TestUtil.findAll(em, AgencyLicenseType.class).get(0);
        }
        agencyLicense.setAgencyLicenseType(agencyLicenseType);
        return agencyLicense;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyLicense createUpdatedEntity(EntityManager em) {
        AgencyLicense agencyLicense = new AgencyLicense()
            .filePath(UPDATED_FILE_PATH)
            .serialNo(UPDATED_SERIAL_NO)
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE);
        // Add required entity
        AgencyLicenseType agencyLicenseType;
        if (TestUtil.findAll(em, AgencyLicenseType.class).isEmpty()) {
            agencyLicenseType = AgencyLicenseTypeResourceIT.createUpdatedEntity(em);
            em.persist(agencyLicenseType);
            em.flush();
        } else {
            agencyLicenseType = TestUtil.findAll(em, AgencyLicenseType.class).get(0);
        }
        agencyLicense.setAgencyLicenseType(agencyLicenseType);
        return agencyLicense;
    }

    @BeforeEach
    public void initTest() {
        agencyLicense = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAgencyLicense != null) {
            agencyLicenseRepository.delete(insertedAgencyLicense);
            insertedAgencyLicense = null;
        }
    }

    @Test
    @Transactional
    void createAgencyLicense() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AgencyLicense
        var returnedAgencyLicense = om.readValue(
            restAgencyLicenseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyLicense)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgencyLicense.class
        );

        // Validate the AgencyLicense in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAgencyLicenseUpdatableFieldsEquals(returnedAgencyLicense, getPersistedAgencyLicense(returnedAgencyLicense));

        insertedAgencyLicense = returnedAgencyLicense;
    }

    @Test
    @Transactional
    void createAgencyLicenseWithExistingId() throws Exception {
        // Create the AgencyLicense with an existing ID
        agencyLicense.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyLicenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyLicense)))
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicense in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgencyLicenses() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList
        restAgencyLicenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencyLicense.getId().intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].serialNo").value(hasItem(DEFAULT_SERIAL_NO)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgencyLicensesWithEagerRelationshipsIsEnabled() throws Exception {
        when(agencyLicenseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgencyLicenseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(agencyLicenseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgencyLicensesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(agencyLicenseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgencyLicenseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(agencyLicenseRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAgencyLicense() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get the agencyLicense
        restAgencyLicenseMockMvc
            .perform(get(ENTITY_API_URL_ID, agencyLicense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agencyLicense.getId().intValue()))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.serialNo").value(DEFAULT_SERIAL_NO))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()));
    }

    @Test
    @Transactional
    void getAgencyLicensesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        Long id = agencyLicense.getId();

        defaultAgencyLicenseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAgencyLicenseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAgencyLicenseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where filePath equals to
        defaultAgencyLicenseFiltering("filePath.equals=" + DEFAULT_FILE_PATH, "filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where filePath in
        defaultAgencyLicenseFiltering("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH, "filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where filePath is not null
        defaultAgencyLicenseFiltering("filePath.specified=true", "filePath.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByFilePathContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where filePath contains
        defaultAgencyLicenseFiltering("filePath.contains=" + DEFAULT_FILE_PATH, "filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where filePath does not contain
        defaultAgencyLicenseFiltering("filePath.doesNotContain=" + UPDATED_FILE_PATH, "filePath.doesNotContain=" + DEFAULT_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesBySerialNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where serialNo equals to
        defaultAgencyLicenseFiltering("serialNo.equals=" + DEFAULT_SERIAL_NO, "serialNo.equals=" + UPDATED_SERIAL_NO);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesBySerialNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where serialNo in
        defaultAgencyLicenseFiltering("serialNo.in=" + DEFAULT_SERIAL_NO + "," + UPDATED_SERIAL_NO, "serialNo.in=" + UPDATED_SERIAL_NO);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesBySerialNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where serialNo is not null
        defaultAgencyLicenseFiltering("serialNo.specified=true", "serialNo.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyLicensesBySerialNoContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where serialNo contains
        defaultAgencyLicenseFiltering("serialNo.contains=" + DEFAULT_SERIAL_NO, "serialNo.contains=" + UPDATED_SERIAL_NO);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesBySerialNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where serialNo does not contain
        defaultAgencyLicenseFiltering("serialNo.doesNotContain=" + UPDATED_SERIAL_NO, "serialNo.doesNotContain=" + DEFAULT_SERIAL_NO);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where issueDate equals to
        defaultAgencyLicenseFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where issueDate in
        defaultAgencyLicenseFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where issueDate is not null
        defaultAgencyLicenseFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByIssueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where issueDate is greater than or equal to
        defaultAgencyLicenseFiltering(
            "issueDate.greaterThanOrEqual=" + DEFAULT_ISSUE_DATE,
            "issueDate.greaterThanOrEqual=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByIssueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where issueDate is less than or equal to
        defaultAgencyLicenseFiltering("issueDate.lessThanOrEqual=" + DEFAULT_ISSUE_DATE, "issueDate.lessThanOrEqual=" + SMALLER_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByIssueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where issueDate is less than
        defaultAgencyLicenseFiltering("issueDate.lessThan=" + UPDATED_ISSUE_DATE, "issueDate.lessThan=" + DEFAULT_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByIssueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where issueDate is greater than
        defaultAgencyLicenseFiltering("issueDate.greaterThan=" + SMALLER_ISSUE_DATE, "issueDate.greaterThan=" + DEFAULT_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where expiryDate equals to
        defaultAgencyLicenseFiltering("expiryDate.equals=" + DEFAULT_EXPIRY_DATE, "expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where expiryDate in
        defaultAgencyLicenseFiltering(
            "expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE,
            "expiryDate.in=" + UPDATED_EXPIRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where expiryDate is not null
        defaultAgencyLicenseFiltering("expiryDate.specified=true", "expiryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByExpiryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where expiryDate is greater than or equal to
        defaultAgencyLicenseFiltering(
            "expiryDate.greaterThanOrEqual=" + DEFAULT_EXPIRY_DATE,
            "expiryDate.greaterThanOrEqual=" + UPDATED_EXPIRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByExpiryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where expiryDate is less than or equal to
        defaultAgencyLicenseFiltering(
            "expiryDate.lessThanOrEqual=" + DEFAULT_EXPIRY_DATE,
            "expiryDate.lessThanOrEqual=" + SMALLER_EXPIRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByExpiryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where expiryDate is less than
        defaultAgencyLicenseFiltering("expiryDate.lessThan=" + UPDATED_EXPIRY_DATE, "expiryDate.lessThan=" + DEFAULT_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByExpiryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        // Get all the agencyLicenseList where expiryDate is greater than
        defaultAgencyLicenseFiltering("expiryDate.greaterThan=" + SMALLER_EXPIRY_DATE, "expiryDate.greaterThan=" + DEFAULT_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByVerifiedByIsEqualToSomething() throws Exception {
        User verifiedBy;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            agencyLicenseRepository.saveAndFlush(agencyLicense);
            verifiedBy = UserResourceIT.createEntity(em);
        } else {
            verifiedBy = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(verifiedBy);
        em.flush();
        agencyLicense.setVerifiedBy(verifiedBy);
        agencyLicenseRepository.saveAndFlush(agencyLicense);
        Long verifiedById = verifiedBy.getId();
        // Get all the agencyLicenseList where verifiedBy equals to verifiedById
        defaultAgencyLicenseShouldBeFound("verifiedById.equals=" + verifiedById);

        // Get all the agencyLicenseList where verifiedBy equals to (verifiedById + 1)
        defaultAgencyLicenseShouldNotBeFound("verifiedById.equals=" + (verifiedById + 1));
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByAgencyLicenseTypeIsEqualToSomething() throws Exception {
        AgencyLicenseType agencyLicenseType;
        if (TestUtil.findAll(em, AgencyLicenseType.class).isEmpty()) {
            agencyLicenseRepository.saveAndFlush(agencyLicense);
            agencyLicenseType = AgencyLicenseTypeResourceIT.createEntity(em);
        } else {
            agencyLicenseType = TestUtil.findAll(em, AgencyLicenseType.class).get(0);
        }
        em.persist(agencyLicenseType);
        em.flush();
        agencyLicense.setAgencyLicenseType(agencyLicenseType);
        agencyLicenseRepository.saveAndFlush(agencyLicense);
        Long agencyLicenseTypeId = agencyLicenseType.getId();
        // Get all the agencyLicenseList where agencyLicenseType equals to agencyLicenseTypeId
        defaultAgencyLicenseShouldBeFound("agencyLicenseTypeId.equals=" + agencyLicenseTypeId);

        // Get all the agencyLicenseList where agencyLicenseType equals to (agencyLicenseTypeId + 1)
        defaultAgencyLicenseShouldNotBeFound("agencyLicenseTypeId.equals=" + (agencyLicenseTypeId + 1));
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByBelongsToIsEqualToSomething() throws Exception {
        Agency belongsTo;
        if (TestUtil.findAll(em, Agency.class).isEmpty()) {
            agencyLicenseRepository.saveAndFlush(agencyLicense);
            belongsTo = AgencyResourceIT.createEntity(em);
        } else {
            belongsTo = TestUtil.findAll(em, Agency.class).get(0);
        }
        em.persist(belongsTo);
        em.flush();
        agencyLicense.setBelongsTo(belongsTo);
        agencyLicenseRepository.saveAndFlush(agencyLicense);
        Long belongsToId = belongsTo.getId();
        // Get all the agencyLicenseList where belongsTo equals to belongsToId
        defaultAgencyLicenseShouldBeFound("belongsToId.equals=" + belongsToId);

        // Get all the agencyLicenseList where belongsTo equals to (belongsToId + 1)
        defaultAgencyLicenseShouldNotBeFound("belongsToId.equals=" + (belongsToId + 1));
    }

    @Test
    @Transactional
    void getAllAgencyLicensesByIssuedByIsEqualToSomething() throws Exception {
        Agency issuedBy;
        if (TestUtil.findAll(em, Agency.class).isEmpty()) {
            agencyLicenseRepository.saveAndFlush(agencyLicense);
            issuedBy = AgencyResourceIT.createEntity(em);
        } else {
            issuedBy = TestUtil.findAll(em, Agency.class).get(0);
        }
        em.persist(issuedBy);
        em.flush();
        agencyLicense.setIssuedBy(issuedBy);
        agencyLicenseRepository.saveAndFlush(agencyLicense);
        Long issuedById = issuedBy.getId();
        // Get all the agencyLicenseList where issuedBy equals to issuedById
        defaultAgencyLicenseShouldBeFound("issuedById.equals=" + issuedById);

        // Get all the agencyLicenseList where issuedBy equals to (issuedById + 1)
        defaultAgencyLicenseShouldNotBeFound("issuedById.equals=" + (issuedById + 1));
    }

    private void defaultAgencyLicenseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAgencyLicenseShouldBeFound(shouldBeFound);
        defaultAgencyLicenseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgencyLicenseShouldBeFound(String filter) throws Exception {
        restAgencyLicenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencyLicense.getId().intValue())))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].serialNo").value(hasItem(DEFAULT_SERIAL_NO)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())));

        // Check, that the count call also returns 1
        restAgencyLicenseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgencyLicenseShouldNotBeFound(String filter) throws Exception {
        restAgencyLicenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgencyLicenseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgencyLicense() throws Exception {
        // Get the agencyLicense
        restAgencyLicenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgencyLicense() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyLicense
        AgencyLicense updatedAgencyLicense = agencyLicenseRepository.findById(agencyLicense.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgencyLicense are not directly saved in db
        em.detach(updatedAgencyLicense);
        updatedAgencyLicense
            .filePath(UPDATED_FILE_PATH)
            .serialNo(UPDATED_SERIAL_NO)
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE);

        restAgencyLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgencyLicense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAgencyLicense))
            )
            .andExpect(status().isOk());

        // Validate the AgencyLicense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgencyLicenseToMatchAllProperties(updatedAgencyLicense);
    }

    @Test
    @Transactional
    void putNonExistingAgencyLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicense.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyLicense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyLicense))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgencyLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyLicense))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgencyLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyLicenseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyLicense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyLicense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgencyLicenseWithPatch() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyLicense using partial update
        AgencyLicense partialUpdatedAgencyLicense = new AgencyLicense();
        partialUpdatedAgencyLicense.setId(agencyLicense.getId());

        partialUpdatedAgencyLicense.expiryDate(UPDATED_EXPIRY_DATE);

        restAgencyLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyLicense.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgencyLicense))
            )
            .andExpect(status().isOk());

        // Validate the AgencyLicense in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyLicenseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgencyLicense, agencyLicense),
            getPersistedAgencyLicense(agencyLicense)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgencyLicenseWithPatch() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agencyLicense using partial update
        AgencyLicense partialUpdatedAgencyLicense = new AgencyLicense();
        partialUpdatedAgencyLicense.setId(agencyLicense.getId());

        partialUpdatedAgencyLicense
            .filePath(UPDATED_FILE_PATH)
            .serialNo(UPDATED_SERIAL_NO)
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE);

        restAgencyLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyLicense.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgencyLicense))
            )
            .andExpect(status().isOk());

        // Validate the AgencyLicense in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyLicenseUpdatableFieldsEquals(partialUpdatedAgencyLicense, getPersistedAgencyLicense(partialUpdatedAgencyLicense));
    }

    @Test
    @Transactional
    void patchNonExistingAgencyLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicense.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agencyLicense.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyLicense))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgencyLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyLicense))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyLicense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgencyLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencyLicense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyLicenseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agencyLicense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyLicense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgencyLicense() throws Exception {
        // Initialize the database
        insertedAgencyLicense = agencyLicenseRepository.saveAndFlush(agencyLicense);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agencyLicense
        restAgencyLicenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, agencyLicense.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agencyLicenseRepository.count();
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

    protected AgencyLicense getPersistedAgencyLicense(AgencyLicense agencyLicense) {
        return agencyLicenseRepository.findById(agencyLicense.getId()).orElseThrow();
    }

    protected void assertPersistedAgencyLicenseToMatchAllProperties(AgencyLicense expectedAgencyLicense) {
        assertAgencyLicenseAllPropertiesEquals(expectedAgencyLicense, getPersistedAgencyLicense(expectedAgencyLicense));
    }

    protected void assertPersistedAgencyLicenseToMatchUpdatableProperties(AgencyLicense expectedAgencyLicense) {
        assertAgencyLicenseAllUpdatablePropertiesEquals(expectedAgencyLicense, getPersistedAgencyLicense(expectedAgencyLicense));
    }
}
