package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.VehicleAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.domain.VehicleType;
import com.mycompany.myapp.domain.enumeration.LogStatusType;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.service.VehicleService;
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
 * Integration tests for the {@link VehicleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VehicleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REG_NO = "AAAAAAAAAA";
    private static final String UPDATED_REG_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ZONE = "AAAAAAAAAA";
    private static final String UPDATED_ZONE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NO = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NO = "BBBBBBBBBB";

    private static final String DEFAULT_VEHICLE_NO = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_NO = "BBBBBBBBBB";

    private static final String DEFAULT_CHASIS_NO = "AAAAAAAAAA";
    private static final String UPDATED_CHASIS_NO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PERSONAL = false;
    private static final Boolean UPDATED_IS_PERSONAL = true;

    private static final Boolean DEFAULT_IS_BLACK_LISTED = false;
    private static final Boolean UPDATED_IS_BLACK_LISTED = true;

    private static final LogStatusType DEFAULT_LOG_STATUS = LogStatusType.DRAFT;
    private static final LogStatusType UPDATED_LOG_STATUS = LogStatusType.PENDING;

    private static final String ENTITY_API_URL = "/api/vehicles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleRepository vehicleRepositoryMock;

    @Mock
    private VehicleService vehicleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleMockMvc;

    private Vehicle vehicle;

    private Vehicle insertedVehicle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createEntity(EntityManager em) {
        Vehicle vehicle = new Vehicle()
            .name(DEFAULT_NAME)
            .regNo(DEFAULT_REG_NO)
            .zone(DEFAULT_ZONE)
            .category(DEFAULT_CATEGORY)
            .serialNo(DEFAULT_SERIAL_NO)
            .vehicleNo(DEFAULT_VEHICLE_NO)
            .chasisNo(DEFAULT_CHASIS_NO)
            .isPersonal(DEFAULT_IS_PERSONAL)
            .isBlackListed(DEFAULT_IS_BLACK_LISTED)
            .logStatus(DEFAULT_LOG_STATUS);
        return vehicle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createUpdatedEntity(EntityManager em) {
        Vehicle vehicle = new Vehicle()
            .name(UPDATED_NAME)
            .regNo(UPDATED_REG_NO)
            .zone(UPDATED_ZONE)
            .category(UPDATED_CATEGORY)
            .serialNo(UPDATED_SERIAL_NO)
            .vehicleNo(UPDATED_VEHICLE_NO)
            .chasisNo(UPDATED_CHASIS_NO)
            .isPersonal(UPDATED_IS_PERSONAL)
            .isBlackListed(UPDATED_IS_BLACK_LISTED)
            .logStatus(UPDATED_LOG_STATUS);
        return vehicle;
    }

    @BeforeEach
    public void initTest() {
        vehicle = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicle != null) {
            vehicleRepository.delete(insertedVehicle);
            insertedVehicle = null;
        }
    }

    @Test
    @Transactional
    void createVehicle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vehicle
        var returnedVehicle = om.readValue(
            restVehicleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicle)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Vehicle.class
        );

        // Validate the Vehicle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVehicleUpdatableFieldsEquals(returnedVehicle, getPersistedVehicle(returnedVehicle));

        insertedVehicle = returnedVehicle;
    }

    @Test
    @Transactional
    void createVehicleWithExistingId() throws Exception {
        // Create the Vehicle with an existing ID
        vehicle.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicle)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicles() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].regNo").value(hasItem(DEFAULT_REG_NO)))
            .andExpect(jsonPath("$.[*].zone").value(hasItem(DEFAULT_ZONE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].serialNo").value(hasItem(DEFAULT_SERIAL_NO)))
            .andExpect(jsonPath("$.[*].vehicleNo").value(hasItem(DEFAULT_VEHICLE_NO)))
            .andExpect(jsonPath("$.[*].chasisNo").value(hasItem(DEFAULT_CHASIS_NO)))
            .andExpect(jsonPath("$.[*].isPersonal").value(hasItem(DEFAULT_IS_PERSONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].isBlackListed").value(hasItem(DEFAULT_IS_BLACK_LISTED.booleanValue())))
            .andExpect(jsonPath("$.[*].logStatus").value(hasItem(DEFAULT_LOG_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehiclesWithEagerRelationshipsIsEnabled() throws Exception {
        when(vehicleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vehicleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehiclesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vehicleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehicleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vehicleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get the vehicle
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicle.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.regNo").value(DEFAULT_REG_NO))
            .andExpect(jsonPath("$.zone").value(DEFAULT_ZONE))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.serialNo").value(DEFAULT_SERIAL_NO))
            .andExpect(jsonPath("$.vehicleNo").value(DEFAULT_VEHICLE_NO))
            .andExpect(jsonPath("$.chasisNo").value(DEFAULT_CHASIS_NO))
            .andExpect(jsonPath("$.isPersonal").value(DEFAULT_IS_PERSONAL.booleanValue()))
            .andExpect(jsonPath("$.isBlackListed").value(DEFAULT_IS_BLACK_LISTED.booleanValue()))
            .andExpect(jsonPath("$.logStatus").value(DEFAULT_LOG_STATUS.toString()));
    }

    @Test
    @Transactional
    void getVehiclesByIdFiltering() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        Long id = vehicle.getId();

        defaultVehicleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVehicleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVehicleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVehiclesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name equals to
        defaultVehicleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehiclesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name in
        defaultVehicleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehiclesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name is not null
        defaultVehicleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name contains
        defaultVehicleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehiclesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where name does not contain
        defaultVehicleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllVehiclesByRegNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where regNo equals to
        defaultVehicleFiltering("regNo.equals=" + DEFAULT_REG_NO, "regNo.equals=" + UPDATED_REG_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByRegNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where regNo in
        defaultVehicleFiltering("regNo.in=" + DEFAULT_REG_NO + "," + UPDATED_REG_NO, "regNo.in=" + UPDATED_REG_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByRegNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where regNo is not null
        defaultVehicleFiltering("regNo.specified=true", "regNo.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByRegNoContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where regNo contains
        defaultVehicleFiltering("regNo.contains=" + DEFAULT_REG_NO, "regNo.contains=" + UPDATED_REG_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByRegNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where regNo does not contain
        defaultVehicleFiltering("regNo.doesNotContain=" + UPDATED_REG_NO, "regNo.doesNotContain=" + DEFAULT_REG_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where zone equals to
        defaultVehicleFiltering("zone.equals=" + DEFAULT_ZONE, "zone.equals=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllVehiclesByZoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where zone in
        defaultVehicleFiltering("zone.in=" + DEFAULT_ZONE + "," + UPDATED_ZONE, "zone.in=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllVehiclesByZoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where zone is not null
        defaultVehicleFiltering("zone.specified=true", "zone.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByZoneContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where zone contains
        defaultVehicleFiltering("zone.contains=" + DEFAULT_ZONE, "zone.contains=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllVehiclesByZoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where zone does not contain
        defaultVehicleFiltering("zone.doesNotContain=" + UPDATED_ZONE, "zone.doesNotContain=" + DEFAULT_ZONE);
    }

    @Test
    @Transactional
    void getAllVehiclesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where category equals to
        defaultVehicleFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllVehiclesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where category in
        defaultVehicleFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllVehiclesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where category is not null
        defaultVehicleFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where category contains
        defaultVehicleFiltering("category.contains=" + DEFAULT_CATEGORY, "category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllVehiclesByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where category does not contain
        defaultVehicleFiltering("category.doesNotContain=" + UPDATED_CATEGORY, "category.doesNotContain=" + DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllVehiclesBySerialNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where serialNo equals to
        defaultVehicleFiltering("serialNo.equals=" + DEFAULT_SERIAL_NO, "serialNo.equals=" + UPDATED_SERIAL_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesBySerialNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where serialNo in
        defaultVehicleFiltering("serialNo.in=" + DEFAULT_SERIAL_NO + "," + UPDATED_SERIAL_NO, "serialNo.in=" + UPDATED_SERIAL_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesBySerialNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where serialNo is not null
        defaultVehicleFiltering("serialNo.specified=true", "serialNo.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesBySerialNoContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where serialNo contains
        defaultVehicleFiltering("serialNo.contains=" + DEFAULT_SERIAL_NO, "serialNo.contains=" + UPDATED_SERIAL_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesBySerialNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where serialNo does not contain
        defaultVehicleFiltering("serialNo.doesNotContain=" + UPDATED_SERIAL_NO, "serialNo.doesNotContain=" + DEFAULT_SERIAL_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleNo equals to
        defaultVehicleFiltering("vehicleNo.equals=" + DEFAULT_VEHICLE_NO, "vehicleNo.equals=" + UPDATED_VEHICLE_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleNo in
        defaultVehicleFiltering("vehicleNo.in=" + DEFAULT_VEHICLE_NO + "," + UPDATED_VEHICLE_NO, "vehicleNo.in=" + UPDATED_VEHICLE_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleNo is not null
        defaultVehicleFiltering("vehicleNo.specified=true", "vehicleNo.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleNoContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleNo contains
        defaultVehicleFiltering("vehicleNo.contains=" + DEFAULT_VEHICLE_NO, "vehicleNo.contains=" + UPDATED_VEHICLE_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleNo does not contain
        defaultVehicleFiltering("vehicleNo.doesNotContain=" + UPDATED_VEHICLE_NO, "vehicleNo.doesNotContain=" + DEFAULT_VEHICLE_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByChasisNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chasisNo equals to
        defaultVehicleFiltering("chasisNo.equals=" + DEFAULT_CHASIS_NO, "chasisNo.equals=" + UPDATED_CHASIS_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByChasisNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chasisNo in
        defaultVehicleFiltering("chasisNo.in=" + DEFAULT_CHASIS_NO + "," + UPDATED_CHASIS_NO, "chasisNo.in=" + UPDATED_CHASIS_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByChasisNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chasisNo is not null
        defaultVehicleFiltering("chasisNo.specified=true", "chasisNo.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByChasisNoContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chasisNo contains
        defaultVehicleFiltering("chasisNo.contains=" + DEFAULT_CHASIS_NO, "chasisNo.contains=" + UPDATED_CHASIS_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByChasisNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chasisNo does not contain
        defaultVehicleFiltering("chasisNo.doesNotContain=" + UPDATED_CHASIS_NO, "chasisNo.doesNotContain=" + DEFAULT_CHASIS_NO);
    }

    @Test
    @Transactional
    void getAllVehiclesByIsPersonalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isPersonal equals to
        defaultVehicleFiltering("isPersonal.equals=" + DEFAULT_IS_PERSONAL, "isPersonal.equals=" + UPDATED_IS_PERSONAL);
    }

    @Test
    @Transactional
    void getAllVehiclesByIsPersonalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isPersonal in
        defaultVehicleFiltering("isPersonal.in=" + DEFAULT_IS_PERSONAL + "," + UPDATED_IS_PERSONAL, "isPersonal.in=" + UPDATED_IS_PERSONAL);
    }

    @Test
    @Transactional
    void getAllVehiclesByIsPersonalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isPersonal is not null
        defaultVehicleFiltering("isPersonal.specified=true", "isPersonal.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByIsBlackListedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isBlackListed equals to
        defaultVehicleFiltering("isBlackListed.equals=" + DEFAULT_IS_BLACK_LISTED, "isBlackListed.equals=" + UPDATED_IS_BLACK_LISTED);
    }

    @Test
    @Transactional
    void getAllVehiclesByIsBlackListedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isBlackListed in
        defaultVehicleFiltering(
            "isBlackListed.in=" + DEFAULT_IS_BLACK_LISTED + "," + UPDATED_IS_BLACK_LISTED,
            "isBlackListed.in=" + UPDATED_IS_BLACK_LISTED
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByIsBlackListedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isBlackListed is not null
        defaultVehicleFiltering("isBlackListed.specified=true", "isBlackListed.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByLogStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where logStatus equals to
        defaultVehicleFiltering("logStatus.equals=" + DEFAULT_LOG_STATUS, "logStatus.equals=" + UPDATED_LOG_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByLogStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where logStatus in
        defaultVehicleFiltering("logStatus.in=" + DEFAULT_LOG_STATUS + "," + UPDATED_LOG_STATUS, "logStatus.in=" + UPDATED_LOG_STATUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByLogStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where logStatus is not null
        defaultVehicleFiltering("logStatus.specified=true", "logStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleTypeIsEqualToSomething() throws Exception {
        VehicleType vehicleType;
        if (TestUtil.findAll(em, VehicleType.class).isEmpty()) {
            vehicleRepository.saveAndFlush(vehicle);
            vehicleType = VehicleTypeResourceIT.createEntity(em);
        } else {
            vehicleType = TestUtil.findAll(em, VehicleType.class).get(0);
        }
        em.persist(vehicleType);
        em.flush();
        vehicle.setVehicleType(vehicleType);
        vehicleRepository.saveAndFlush(vehicle);
        Long vehicleTypeId = vehicleType.getId();
        // Get all the vehicleList where vehicleType equals to vehicleTypeId
        defaultVehicleShouldBeFound("vehicleTypeId.equals=" + vehicleTypeId);

        // Get all the vehicleList where vehicleType equals to (vehicleTypeId + 1)
        defaultVehicleShouldNotBeFound("vehicleTypeId.equals=" + (vehicleTypeId + 1));
    }

    private void defaultVehicleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVehicleShouldBeFound(shouldBeFound);
        defaultVehicleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehicleShouldBeFound(String filter) throws Exception {
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].regNo").value(hasItem(DEFAULT_REG_NO)))
            .andExpect(jsonPath("$.[*].zone").value(hasItem(DEFAULT_ZONE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].serialNo").value(hasItem(DEFAULT_SERIAL_NO)))
            .andExpect(jsonPath("$.[*].vehicleNo").value(hasItem(DEFAULT_VEHICLE_NO)))
            .andExpect(jsonPath("$.[*].chasisNo").value(hasItem(DEFAULT_CHASIS_NO)))
            .andExpect(jsonPath("$.[*].isPersonal").value(hasItem(DEFAULT_IS_PERSONAL.booleanValue())))
            .andExpect(jsonPath("$.[*].isBlackListed").value(hasItem(DEFAULT_IS_BLACK_LISTED.booleanValue())))
            .andExpect(jsonPath("$.[*].logStatus").value(hasItem(DEFAULT_LOG_STATUS.toString())));

        // Check, that the count call also returns 1
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehicleShouldNotBeFound(String filter) throws Exception {
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVehicle() throws Exception {
        // Get the vehicle
        restVehicleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle
        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicle are not directly saved in db
        em.detach(updatedVehicle);
        updatedVehicle
            .name(UPDATED_NAME)
            .regNo(UPDATED_REG_NO)
            .zone(UPDATED_ZONE)
            .category(UPDATED_CATEGORY)
            .serialNo(UPDATED_SERIAL_NO)
            .vehicleNo(UPDATED_VEHICLE_NO)
            .chasisNo(UPDATED_CHASIS_NO)
            .isPersonal(UPDATED_IS_PERSONAL)
            .isBlackListed(UPDATED_IS_BLACK_LISTED)
            .logStatus(UPDATED_LOG_STATUS);

        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleToMatchAllProperties(updatedVehicle);
    }

    @Test
    @Transactional
    void putNonExistingVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(put(ENTITY_API_URL_ID, vehicle.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicle)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle.regNo(UPDATED_REG_NO).serialNo(UPDATED_SERIAL_NO).vehicleNo(UPDATED_VEHICLE_NO).chasisNo(UPDATED_CHASIS_NO);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVehicle, vehicle), getPersistedVehicle(vehicle));
    }

    @Test
    @Transactional
    void fullUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .name(UPDATED_NAME)
            .regNo(UPDATED_REG_NO)
            .zone(UPDATED_ZONE)
            .category(UPDATED_CATEGORY)
            .serialNo(UPDATED_SERIAL_NO)
            .vehicleNo(UPDATED_VEHICLE_NO)
            .chasisNo(UPDATED_CHASIS_NO)
            .isPersonal(UPDATED_IS_PERSONAL)
            .isBlackListed(UPDATED_IS_BLACK_LISTED)
            .logStatus(UPDATED_LOG_STATUS);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUpdatableFieldsEquals(partialUpdatedVehicle, getPersistedVehicle(partialUpdatedVehicle));
    }

    @Test
    @Transactional
    void patchNonExistingVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicle.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicle))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicle
        restVehicleMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleRepository.count();
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

    protected Vehicle getPersistedVehicle(Vehicle vehicle) {
        return vehicleRepository.findById(vehicle.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleToMatchAllProperties(Vehicle expectedVehicle) {
        assertVehicleAllPropertiesEquals(expectedVehicle, getPersistedVehicle(expectedVehicle));
    }

    protected void assertPersistedVehicleToMatchUpdatableProperties(Vehicle expectedVehicle) {
        assertVehicleAllUpdatablePropertiesEquals(expectedVehicle, getPersistedVehicle(expectedVehicle));
    }
}
