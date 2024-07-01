package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.VehicleType;
import com.mycompany.myapp.repository.VehicleTypeRepository;
import com.mycompany.myapp.service.VehicleTypeService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.VehicleType}.
 */
@RestController
@RequestMapping("/api/vehicle-types")
public class VehicleTypeResource {

    private final Logger log = LoggerFactory.getLogger(VehicleTypeResource.class);

    private static final String ENTITY_NAME = "vehicleType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleTypeService vehicleTypeService;

    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeResource(VehicleTypeService vehicleTypeService, VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeService = vehicleTypeService;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    /**
     * {@code POST  /vehicle-types} : Create a new vehicleType.
     *
     * @param vehicleType the vehicleType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleType, or with status {@code 400 (Bad Request)} if the vehicleType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleType> createVehicleType(@RequestBody VehicleType vehicleType) throws URISyntaxException {
        log.debug("REST request to save VehicleType : {}", vehicleType);
        if (vehicleType.getId() != null) {
            throw new BadRequestAlertException("A new vehicleType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleType = vehicleTypeService.save(vehicleType);
        return ResponseEntity.created(new URI("/api/vehicle-types/" + vehicleType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleType.getId().toString()))
            .body(vehicleType);
    }

    /**
     * {@code PUT  /vehicle-types/:id} : Updates an existing vehicleType.
     *
     * @param id the id of the vehicleType to save.
     * @param vehicleType the vehicleType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleType,
     * or with status {@code 400 (Bad Request)} if the vehicleType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleType> updateVehicleType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleType vehicleType
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleType : {}, {}", id, vehicleType);
        if (vehicleType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleType = vehicleTypeService.update(vehicleType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleType.getId().toString()))
            .body(vehicleType);
    }

    /**
     * {@code PATCH  /vehicle-types/:id} : Partial updates given fields of an existing vehicleType, field will ignore if it is null
     *
     * @param id the id of the vehicleType to save.
     * @param vehicleType the vehicleType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleType,
     * or with status {@code 400 (Bad Request)} if the vehicleType is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleType is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleType> partialUpdateVehicleType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleType vehicleType
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleType partially : {}, {}", id, vehicleType);
        if (vehicleType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleType> result = vehicleTypeService.partialUpdate(vehicleType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleType.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-types} : get all the vehicleTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleTypes in body.
     */
    @GetMapping("")
    public List<VehicleType> getAllVehicleTypes() {
        log.debug("REST request to get all VehicleTypes");
        return vehicleTypeService.findAll();
    }

    /**
     * {@code GET  /vehicle-types/:id} : get the "id" vehicleType.
     *
     * @param id the id of the vehicleType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleType> getVehicleType(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleType : {}", id);
        Optional<VehicleType> vehicleType = vehicleTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleType);
    }

    /**
     * {@code DELETE  /vehicle-types/:id} : delete the "id" vehicleType.
     *
     * @param id the id of the vehicleType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleType(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleType : {}", id);
        vehicleTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
