package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PassAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Assignment;
import com.mycompany.myapp.domain.Pass;
import com.mycompany.myapp.domain.PassType;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.VehicleAssignment;
import com.mycompany.myapp.domain.enumeration.PassStatusType;
import com.mycompany.myapp.repository.PassRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.PassService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PassResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PassResourceIT {

    private static final Double DEFAULT_COLLECTED_FEE = 1D;
    private static final Double UPDATED_COLLECTED_FEE = 2D;
    private static final Double SMALLER_COLLECTED_FEE = 1D - 1D;

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PassStatusType DEFAULT_STATUS = PassStatusType.REQUESTED;
    private static final PassStatusType UPDATED_STATUS = PassStatusType.PRINTED;

    private static final Long DEFAULT_PASS_NUMBER = 1L;
    private static final Long UPDATED_PASS_NUMBER = 2L;
    private static final Long SMALLER_PASS_NUMBER = 1L - 1L;

    private static final String DEFAULT_MEDIA_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_SERIAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/passes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PassRepository passRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PassRepository passRepositoryMock;

    @Mock
    private PassService passServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassMockMvc;

    private Pass pass;

    private Pass insertedPass;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pass createEntity(EntityManager em) {
        Pass pass = new Pass()
            .collectedFee(DEFAULT_COLLECTED_FEE)
            .fromDate(DEFAULT_FROM_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .passNumber(DEFAULT_PASS_NUMBER)
            .mediaSerial(DEFAULT_MEDIA_SERIAL);
        // Add required entity
        PassType passType;
        if (TestUtil.findAll(em, PassType.class).isEmpty()) {
            passType = PassTypeResourceIT.createEntity(em);
            em.persist(passType);
            em.flush();
        } else {
            passType = TestUtil.findAll(em, PassType.class).get(0);
        }
        pass.setPassType(passType);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        pass.setRequestedBy(user);
        // Add required entity
        Assignment assignment;
        if (TestUtil.findAll(em, Assignment.class).isEmpty()) {
            assignment = AssignmentResourceIT.createEntity(em);
            em.persist(assignment);
            em.flush();
        } else {
            assignment = TestUtil.findAll(em, Assignment.class).get(0);
        }
        pass.setAssignment(assignment);
        return pass;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pass createUpdatedEntity(EntityManager em) {
        Pass pass = new Pass()
            .collectedFee(UPDATED_COLLECTED_FEE)
            .fromDate(UPDATED_FROM_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .passNumber(UPDATED_PASS_NUMBER)
            .mediaSerial(UPDATED_MEDIA_SERIAL);
        // Add required entity
        PassType passType;
        if (TestUtil.findAll(em, PassType.class).isEmpty()) {
            passType = PassTypeResourceIT.createUpdatedEntity(em);
            em.persist(passType);
            em.flush();
        } else {
            passType = TestUtil.findAll(em, PassType.class).get(0);
        }
        pass.setPassType(passType);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        pass.setRequestedBy(user);
        // Add required entity
        Assignment assignment;
        if (TestUtil.findAll(em, Assignment.class).isEmpty()) {
            assignment = AssignmentResourceIT.createUpdatedEntity(em);
            em.persist(assignment);
            em.flush();
        } else {
            assignment = TestUtil.findAll(em, Assignment.class).get(0);
        }
        pass.setAssignment(assignment);
        return pass;
    }

    @BeforeEach
    public void initTest() {
        pass = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPass != null) {
            passRepository.delete(insertedPass);
            insertedPass = null;
        }
    }

    @Test
    @Transactional
    void createPass() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pass
        var returnedPass = om.readValue(
            restPassMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pass)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Pass.class
        );

        // Validate the Pass in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPassUpdatableFieldsEquals(returnedPass, getPersistedPass(returnedPass));

        insertedPass = returnedPass;
    }

    @Test
    @Transactional
    void createPassWithExistingId() throws Exception {
        // Create the Pass with an existing ID
        pass.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pass)))
            .andExpect(status().isBadRequest());

        // Validate the Pass in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPasses() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList
        restPassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pass.getId().intValue())))
            .andExpect(jsonPath("$.[*].collectedFee").value(hasItem(DEFAULT_COLLECTED_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].passNumber").value(hasItem(DEFAULT_PASS_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].mediaSerial").value(hasItem(DEFAULT_MEDIA_SERIAL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPassesWithEagerRelationshipsIsEnabled() throws Exception {
        when(passServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPassMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(passServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPassesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(passServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPassMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(passRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPass() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get the pass
        restPassMockMvc
            .perform(get(ENTITY_API_URL_ID, pass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pass.getId().intValue()))
            .andExpect(jsonPath("$.collectedFee").value(DEFAULT_COLLECTED_FEE.doubleValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.passNumber").value(DEFAULT_PASS_NUMBER.intValue()))
            .andExpect(jsonPath("$.mediaSerial").value(DEFAULT_MEDIA_SERIAL));
    }

    @Test
    @Transactional
    void getPassesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        Long id = pass.getId();

        defaultPassFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPassFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPassFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPassesByCollectedFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where collectedFee equals to
        defaultPassFiltering("collectedFee.equals=" + DEFAULT_COLLECTED_FEE, "collectedFee.equals=" + UPDATED_COLLECTED_FEE);
    }

    @Test
    @Transactional
    void getAllPassesByCollectedFeeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where collectedFee in
        defaultPassFiltering(
            "collectedFee.in=" + DEFAULT_COLLECTED_FEE + "," + UPDATED_COLLECTED_FEE,
            "collectedFee.in=" + UPDATED_COLLECTED_FEE
        );
    }

    @Test
    @Transactional
    void getAllPassesByCollectedFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where collectedFee is not null
        defaultPassFiltering("collectedFee.specified=true", "collectedFee.specified=false");
    }

    @Test
    @Transactional
    void getAllPassesByCollectedFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where collectedFee is greater than or equal to
        defaultPassFiltering(
            "collectedFee.greaterThanOrEqual=" + DEFAULT_COLLECTED_FEE,
            "collectedFee.greaterThanOrEqual=" + UPDATED_COLLECTED_FEE
        );
    }

    @Test
    @Transactional
    void getAllPassesByCollectedFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where collectedFee is less than or equal to
        defaultPassFiltering(
            "collectedFee.lessThanOrEqual=" + DEFAULT_COLLECTED_FEE,
            "collectedFee.lessThanOrEqual=" + SMALLER_COLLECTED_FEE
        );
    }

    @Test
    @Transactional
    void getAllPassesByCollectedFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where collectedFee is less than
        defaultPassFiltering("collectedFee.lessThan=" + UPDATED_COLLECTED_FEE, "collectedFee.lessThan=" + DEFAULT_COLLECTED_FEE);
    }

    @Test
    @Transactional
    void getAllPassesByCollectedFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where collectedFee is greater than
        defaultPassFiltering("collectedFee.greaterThan=" + SMALLER_COLLECTED_FEE, "collectedFee.greaterThan=" + DEFAULT_COLLECTED_FEE);
    }

    @Test
    @Transactional
    void getAllPassesByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where fromDate equals to
        defaultPassFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllPassesByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where fromDate in
        defaultPassFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllPassesByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where fromDate is not null
        defaultPassFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPassesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where endDate equals to
        defaultPassFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPassesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where endDate in
        defaultPassFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllPassesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where endDate is not null
        defaultPassFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPassesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where status equals to
        defaultPassFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPassesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where status in
        defaultPassFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPassesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where status is not null
        defaultPassFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllPassesByPassNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where passNumber equals to
        defaultPassFiltering("passNumber.equals=" + DEFAULT_PASS_NUMBER, "passNumber.equals=" + UPDATED_PASS_NUMBER);
    }

    @Test
    @Transactional
    void getAllPassesByPassNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where passNumber in
        defaultPassFiltering("passNumber.in=" + DEFAULT_PASS_NUMBER + "," + UPDATED_PASS_NUMBER, "passNumber.in=" + UPDATED_PASS_NUMBER);
    }

    @Test
    @Transactional
    void getAllPassesByPassNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where passNumber is not null
        defaultPassFiltering("passNumber.specified=true", "passNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPassesByPassNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where passNumber is greater than or equal to
        defaultPassFiltering(
            "passNumber.greaterThanOrEqual=" + DEFAULT_PASS_NUMBER,
            "passNumber.greaterThanOrEqual=" + UPDATED_PASS_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPassesByPassNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where passNumber is less than or equal to
        defaultPassFiltering("passNumber.lessThanOrEqual=" + DEFAULT_PASS_NUMBER, "passNumber.lessThanOrEqual=" + SMALLER_PASS_NUMBER);
    }

    @Test
    @Transactional
    void getAllPassesByPassNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where passNumber is less than
        defaultPassFiltering("passNumber.lessThan=" + UPDATED_PASS_NUMBER, "passNumber.lessThan=" + DEFAULT_PASS_NUMBER);
    }

    @Test
    @Transactional
    void getAllPassesByPassNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where passNumber is greater than
        defaultPassFiltering("passNumber.greaterThan=" + SMALLER_PASS_NUMBER, "passNumber.greaterThan=" + DEFAULT_PASS_NUMBER);
    }

    @Test
    @Transactional
    void getAllPassesByMediaSerialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where mediaSerial equals to
        defaultPassFiltering("mediaSerial.equals=" + DEFAULT_MEDIA_SERIAL, "mediaSerial.equals=" + UPDATED_MEDIA_SERIAL);
    }

    @Test
    @Transactional
    void getAllPassesByMediaSerialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where mediaSerial in
        defaultPassFiltering(
            "mediaSerial.in=" + DEFAULT_MEDIA_SERIAL + "," + UPDATED_MEDIA_SERIAL,
            "mediaSerial.in=" + UPDATED_MEDIA_SERIAL
        );
    }

    @Test
    @Transactional
    void getAllPassesByMediaSerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where mediaSerial is not null
        defaultPassFiltering("mediaSerial.specified=true", "mediaSerial.specified=false");
    }

    @Test
    @Transactional
    void getAllPassesByMediaSerialContainsSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where mediaSerial contains
        defaultPassFiltering("mediaSerial.contains=" + DEFAULT_MEDIA_SERIAL, "mediaSerial.contains=" + UPDATED_MEDIA_SERIAL);
    }

    @Test
    @Transactional
    void getAllPassesByMediaSerialNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        // Get all the passList where mediaSerial does not contain
        defaultPassFiltering("mediaSerial.doesNotContain=" + UPDATED_MEDIA_SERIAL, "mediaSerial.doesNotContain=" + DEFAULT_MEDIA_SERIAL);
    }

    @Test
    @Transactional
    void getAllPassesByPassTypeIsEqualToSomething() throws Exception {
        PassType passType;
        if (TestUtil.findAll(em, PassType.class).isEmpty()) {
            passRepository.saveAndFlush(pass);
            passType = PassTypeResourceIT.createEntity(em);
        } else {
            passType = TestUtil.findAll(em, PassType.class).get(0);
        }
        em.persist(passType);
        em.flush();
        pass.setPassType(passType);
        passRepository.saveAndFlush(pass);
        Long passTypeId = passType.getId();
        // Get all the passList where passType equals to passTypeId
        defaultPassShouldBeFound("passTypeId.equals=" + passTypeId);

        // Get all the passList where passType equals to (passTypeId + 1)
        defaultPassShouldNotBeFound("passTypeId.equals=" + (passTypeId + 1));
    }

    @Test
    @Transactional
    void getAllPassesByRequestedByIsEqualToSomething() throws Exception {
        User requestedBy;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            passRepository.saveAndFlush(pass);
            requestedBy = UserResourceIT.createEntity(em);
        } else {
            requestedBy = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(requestedBy);
        em.flush();
        pass.setRequestedBy(requestedBy);
        passRepository.saveAndFlush(pass);
        Long requestedById = requestedBy.getId();
        // Get all the passList where requestedBy equals to requestedById
        defaultPassShouldBeFound("requestedById.equals=" + requestedById);

        // Get all the passList where requestedBy equals to (requestedById + 1)
        defaultPassShouldNotBeFound("requestedById.equals=" + (requestedById + 1));
    }

    @Test
    @Transactional
    void getAllPassesByAssignmentIsEqualToSomething() throws Exception {
        Assignment assignment;
        if (TestUtil.findAll(em, Assignment.class).isEmpty()) {
            passRepository.saveAndFlush(pass);
            assignment = AssignmentResourceIT.createEntity(em);
        } else {
            assignment = TestUtil.findAll(em, Assignment.class).get(0);
        }
        em.persist(assignment);
        em.flush();
        pass.setAssignment(assignment);
        passRepository.saveAndFlush(pass);
        Long assignmentId = assignment.getId();
        // Get all the passList where assignment equals to assignmentId
        defaultPassShouldBeFound("assignmentId.equals=" + assignmentId);

        // Get all the passList where assignment equals to (assignmentId + 1)
        defaultPassShouldNotBeFound("assignmentId.equals=" + (assignmentId + 1));
    }

    @Test
    @Transactional
    void getAllPassesByVehicleAssignmentIsEqualToSomething() throws Exception {
        VehicleAssignment vehicleAssignment;
        if (TestUtil.findAll(em, VehicleAssignment.class).isEmpty()) {
            passRepository.saveAndFlush(pass);
            vehicleAssignment = VehicleAssignmentResourceIT.createEntity(em);
        } else {
            vehicleAssignment = TestUtil.findAll(em, VehicleAssignment.class).get(0);
        }
        em.persist(vehicleAssignment);
        em.flush();
        pass.setVehicleAssignment(vehicleAssignment);
        passRepository.saveAndFlush(pass);
        Long vehicleAssignmentId = vehicleAssignment.getId();
        // Get all the passList where vehicleAssignment equals to vehicleAssignmentId
        defaultPassShouldBeFound("vehicleAssignmentId.equals=" + vehicleAssignmentId);

        // Get all the passList where vehicleAssignment equals to (vehicleAssignmentId + 1)
        defaultPassShouldNotBeFound("vehicleAssignmentId.equals=" + (vehicleAssignmentId + 1));
    }

    private void defaultPassFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPassShouldBeFound(shouldBeFound);
        defaultPassShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPassShouldBeFound(String filter) throws Exception {
        restPassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pass.getId().intValue())))
            .andExpect(jsonPath("$.[*].collectedFee").value(hasItem(DEFAULT_COLLECTED_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].passNumber").value(hasItem(DEFAULT_PASS_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].mediaSerial").value(hasItem(DEFAULT_MEDIA_SERIAL)));

        // Check, that the count call also returns 1
        restPassMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPassShouldNotBeFound(String filter) throws Exception {
        restPassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPassMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPass() throws Exception {
        // Get the pass
        restPassMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPass() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pass
        Pass updatedPass = passRepository.findById(pass.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPass are not directly saved in db
        em.detach(updatedPass);
        updatedPass
            .collectedFee(UPDATED_COLLECTED_FEE)
            .fromDate(UPDATED_FROM_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .passNumber(UPDATED_PASS_NUMBER)
            .mediaSerial(UPDATED_MEDIA_SERIAL);

        restPassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPass.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPass))
            )
            .andExpect(status().isOk());

        // Validate the Pass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPassToMatchAllProperties(updatedPass);
    }

    @Test
    @Transactional
    void putNonExistingPass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pass.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassMockMvc
            .perform(put(ENTITY_API_URL_ID, pass.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pass)))
            .andExpect(status().isBadRequest());

        // Validate the Pass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pass.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pass))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pass.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pass)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePassWithPatch() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pass using partial update
        Pass partialUpdatedPass = new Pass();
        partialUpdatedPass.setId(pass.getId());

        partialUpdatedPass.collectedFee(UPDATED_COLLECTED_FEE).fromDate(UPDATED_FROM_DATE).mediaSerial(UPDATED_MEDIA_SERIAL);

        restPassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPass.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPass))
            )
            .andExpect(status().isOk());

        // Validate the Pass in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPass, pass), getPersistedPass(pass));
    }

    @Test
    @Transactional
    void fullUpdatePassWithPatch() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pass using partial update
        Pass partialUpdatedPass = new Pass();
        partialUpdatedPass.setId(pass.getId());

        partialUpdatedPass
            .collectedFee(UPDATED_COLLECTED_FEE)
            .fromDate(UPDATED_FROM_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .passNumber(UPDATED_PASS_NUMBER)
            .mediaSerial(UPDATED_MEDIA_SERIAL);

        restPassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPass.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPass))
            )
            .andExpect(status().isOk());

        // Validate the Pass in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassUpdatableFieldsEquals(partialUpdatedPass, getPersistedPass(partialUpdatedPass));
    }

    @Test
    @Transactional
    void patchNonExistingPass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pass.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassMockMvc
            .perform(patch(ENTITY_API_URL_ID, pass.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pass)))
            .andExpect(status().isBadRequest());

        // Validate the Pass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pass.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pass))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pass.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pass)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePass() throws Exception {
        // Initialize the database
        insertedPass = passRepository.saveAndFlush(pass);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pass
        restPassMockMvc
            .perform(delete(ENTITY_API_URL_ID, pass.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return passRepository.count();
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

    protected Pass getPersistedPass(Pass pass) {
        return passRepository.findById(pass.getId()).orElseThrow();
    }

    protected void assertPersistedPassToMatchAllProperties(Pass expectedPass) {
        assertPassAllPropertiesEquals(expectedPass, getPersistedPass(expectedPass));
    }

    protected void assertPersistedPassToMatchUpdatableProperties(Pass expectedPass) {
        assertPassAllUpdatablePropertiesEquals(expectedPass, getPersistedPass(expectedPass));
    }
}
