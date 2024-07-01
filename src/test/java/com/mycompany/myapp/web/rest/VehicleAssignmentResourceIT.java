package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.VehicleAssignmentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Agency;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.domain.VehicleAssignment;
import com.mycompany.myapp.repository.VehicleAssignmentRepository;
import com.mycompany.myapp.service.VehicleAssignmentService;
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
 * Integration tests for the {@link VehicleAssignmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VehicleAssignmentResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_PRIMARY = false;
    private static final Boolean UPDATED_IS_PRIMARY = true;

    private static final Boolean DEFAULT_IS_RENTAL = false;
    private static final Boolean UPDATED_IS_RENTAL = true;

    private static final String ENTITY_API_URL = "/api/vehicle-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleAssignmentRepository vehicleAssignmentRepository;

    @Mock
    private VehicleAssignmentRepository vehicleAssignmentRepositoryMock;

    @Mock
    private VehicleAssignmentService vehicleAssignmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleAssignmentMockMvc;

    private VehicleAssignment vehicleAssignment;

    private VehicleAssignment insertedVehicleAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleAssignment createEntity(EntityManager em) {
        VehicleAssignment vehicleAssignment = new VehicleAssignment()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .isPrimary(DEFAULT_IS_PRIMARY)
            .isRental(DEFAULT_IS_RENTAL);
        return vehicleAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleAssignment createUpdatedEntity(EntityManager em) {
        VehicleAssignment vehicleAssignment = new VehicleAssignment()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .isRental(UPDATED_IS_RENTAL);
        return vehicleAssignment;
    }

    @BeforeEach
    public void initTest() {
        vehicleAssignment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleAssignment != null) {
            vehicleAssignmentRepository.delete(insertedVehicleAssignment);
            insertedVehicleAssignment = null;
        }
    }

    @Test
    @Transactional
    void createVehicleAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleAssignment
        var returnedVehicleAssignment = om.readValue(
            restVehicleAssignmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleAssignment)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleAssignment.class
        );

        // Validate the VehicleAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVehicleAssignmentUpdatableFieldsEquals(returnedVehicleAssignment, getPersistedVehicleAssignment(returnedVehicleAssignment));

        insertedVehicleAssignment = returnedVehicleAssignment;
    }

    @Test
    @Transactional
    void createVehicleAssignmentWithExistingId() throws Exception {
        // Create the VehicleAssignment with an existing ID
        vehicleAssignment.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleAssignment)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vehicleAssignment.setStartDate(null);

        // Create the VehicleAssignment, which fails.

        restVehicleAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleAssignment)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicleAssignments() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList
        restVehicleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY.booleanValue())))
            .andExpect(jsonPath("$.[*].isRental").value(hasItem(DEFAULT_IS_RENTAL.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehicleAssignmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vehicleAssignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vehicleAssignmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehicleAssignmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vehicleAssignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vehicleAssignmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVehicleAssignment() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get the vehicleAssignment
        restVehicleAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleAssignment.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.isPrimary").value(DEFAULT_IS_PRIMARY.booleanValue()))
            .andExpect(jsonPath("$.isRental").value(DEFAULT_IS_RENTAL.booleanValue()));
    }

    @Test
    @Transactional
    void getVehicleAssignmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        Long id = vehicleAssignment.getId();

        defaultVehicleAssignmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVehicleAssignmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVehicleAssignmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where startDate equals to
        defaultVehicleAssignmentFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where startDate in
        defaultVehicleAssignmentFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where startDate is not null
        defaultVehicleAssignmentFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where startDate is greater than or equal to
        defaultVehicleAssignmentFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where startDate is less than or equal to
        defaultVehicleAssignmentFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where startDate is less than
        defaultVehicleAssignmentFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where startDate is greater than
        defaultVehicleAssignmentFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where endDate equals to
        defaultVehicleAssignmentFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where endDate in
        defaultVehicleAssignmentFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where endDate is not null
        defaultVehicleAssignmentFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where endDate is greater than or equal to
        defaultVehicleAssignmentFiltering(
            "endDate.greaterThanOrEqual=" + DEFAULT_END_DATE,
            "endDate.greaterThanOrEqual=" + UPDATED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where endDate is less than or equal to
        defaultVehicleAssignmentFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where endDate is less than
        defaultVehicleAssignmentFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where endDate is greater than
        defaultVehicleAssignmentFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByIsPrimaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where isPrimary equals to
        defaultVehicleAssignmentFiltering("isPrimary.equals=" + DEFAULT_IS_PRIMARY, "isPrimary.equals=" + UPDATED_IS_PRIMARY);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByIsPrimaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where isPrimary in
        defaultVehicleAssignmentFiltering(
            "isPrimary.in=" + DEFAULT_IS_PRIMARY + "," + UPDATED_IS_PRIMARY,
            "isPrimary.in=" + UPDATED_IS_PRIMARY
        );
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByIsPrimaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where isPrimary is not null
        defaultVehicleAssignmentFiltering("isPrimary.specified=true", "isPrimary.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByIsRentalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where isRental equals to
        defaultVehicleAssignmentFiltering("isRental.equals=" + DEFAULT_IS_RENTAL, "isRental.equals=" + UPDATED_IS_RENTAL);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByIsRentalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where isRental in
        defaultVehicleAssignmentFiltering("isRental.in=" + DEFAULT_IS_RENTAL + "," + UPDATED_IS_RENTAL, "isRental.in=" + UPDATED_IS_RENTAL);
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByIsRentalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        // Get all the vehicleAssignmentList where isRental is not null
        defaultVehicleAssignmentFiltering("isRental.specified=true", "isRental.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByAgencyIsEqualToSomething() throws Exception {
        Agency agency;
        if (TestUtil.findAll(em, Agency.class).isEmpty()) {
            vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);
            agency = AgencyResourceIT.createEntity(em);
        } else {
            agency = TestUtil.findAll(em, Agency.class).get(0);
        }
        em.persist(agency);
        em.flush();
        vehicleAssignment.setAgency(agency);
        vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);
        Long agencyId = agency.getId();
        // Get all the vehicleAssignmentList where agency equals to agencyId
        defaultVehicleAssignmentShouldBeFound("agencyId.equals=" + agencyId);

        // Get all the vehicleAssignmentList where agency equals to (agencyId + 1)
        defaultVehicleAssignmentShouldNotBeFound("agencyId.equals=" + (agencyId + 1));
    }

    @Test
    @Transactional
    void getAllVehicleAssignmentsByVehicleIsEqualToSomething() throws Exception {
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);
            vehicle = VehicleResourceIT.createEntity(em);
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        em.persist(vehicle);
        em.flush();
        vehicleAssignment.setVehicle(vehicle);
        vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);
        Long vehicleId = vehicle.getId();
        // Get all the vehicleAssignmentList where vehicle equals to vehicleId
        defaultVehicleAssignmentShouldBeFound("vehicleId.equals=" + vehicleId);

        // Get all the vehicleAssignmentList where vehicle equals to (vehicleId + 1)
        defaultVehicleAssignmentShouldNotBeFound("vehicleId.equals=" + (vehicleId + 1));
    }

    private void defaultVehicleAssignmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVehicleAssignmentShouldBeFound(shouldBeFound);
        defaultVehicleAssignmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehicleAssignmentShouldBeFound(String filter) throws Exception {
        restVehicleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY.booleanValue())))
            .andExpect(jsonPath("$.[*].isRental").value(hasItem(DEFAULT_IS_RENTAL.booleanValue())));

        // Check, that the count call also returns 1
        restVehicleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehicleAssignmentShouldNotBeFound(String filter) throws Exception {
        restVehicleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehicleAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVehicleAssignment() throws Exception {
        // Get the vehicleAssignment
        restVehicleAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleAssignment() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleAssignment
        VehicleAssignment updatedVehicleAssignment = vehicleAssignmentRepository.findById(vehicleAssignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleAssignment are not directly saved in db
        em.detach(updatedVehicleAssignment);
        updatedVehicleAssignment
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .isRental(UPDATED_IS_RENTAL);

        restVehicleAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicleAssignment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVehicleAssignment))
            )
            .andExpect(status().isOk());

        // Validate the VehicleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleAssignmentToMatchAllProperties(updatedVehicleAssignment);
    }

    @Test
    @Transactional
    void putNonExistingVehicleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAssignment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleAssignment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleAssignment))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAssignment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleAssignment))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAssignment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleAssignmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleAssignment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleAssignment using partial update
        VehicleAssignment partialUpdatedVehicleAssignment = new VehicleAssignment();
        partialUpdatedVehicleAssignment.setId(vehicleAssignment.getId());

        partialUpdatedVehicleAssignment.endDate(UPDATED_END_DATE).isPrimary(UPDATED_IS_PRIMARY).isRental(UPDATED_IS_RENTAL);

        restVehicleAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleAssignment))
            )
            .andExpect(status().isOk());

        // Validate the VehicleAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleAssignment, vehicleAssignment),
            getPersistedVehicleAssignment(vehicleAssignment)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleAssignment using partial update
        VehicleAssignment partialUpdatedVehicleAssignment = new VehicleAssignment();
        partialUpdatedVehicleAssignment.setId(vehicleAssignment.getId());

        partialUpdatedVehicleAssignment
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .isPrimary(UPDATED_IS_PRIMARY)
            .isRental(UPDATED_IS_RENTAL);

        restVehicleAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleAssignment))
            )
            .andExpect(status().isOk());

        // Validate the VehicleAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleAssignmentUpdatableFieldsEquals(
            partialUpdatedVehicleAssignment,
            getPersistedVehicleAssignment(partialUpdatedVehicleAssignment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingVehicleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAssignment.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleAssignment))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAssignment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleAssignment))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleAssignment.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleAssignmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleAssignment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleAssignment() throws Exception {
        // Initialize the database
        insertedVehicleAssignment = vehicleAssignmentRepository.saveAndFlush(vehicleAssignment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleAssignment
        restVehicleAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleAssignment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleAssignmentRepository.count();
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

    protected VehicleAssignment getPersistedVehicleAssignment(VehicleAssignment vehicleAssignment) {
        return vehicleAssignmentRepository.findById(vehicleAssignment.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleAssignmentToMatchAllProperties(VehicleAssignment expectedVehicleAssignment) {
        assertVehicleAssignmentAllPropertiesEquals(expectedVehicleAssignment, getPersistedVehicleAssignment(expectedVehicleAssignment));
    }

    protected void assertPersistedVehicleAssignmentToMatchUpdatableProperties(VehicleAssignment expectedVehicleAssignment) {
        assertVehicleAssignmentAllUpdatablePropertiesEquals(
            expectedVehicleAssignment,
            getPersistedVehicleAssignment(expectedVehicleAssignment)
        );
    }
}
