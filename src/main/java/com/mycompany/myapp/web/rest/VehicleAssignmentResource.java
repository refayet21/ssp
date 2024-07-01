package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.VehicleAssignment;
import com.mycompany.myapp.repository.VehicleAssignmentRepository;
import com.mycompany.myapp.service.VehicleAssignmentQueryService;
import com.mycompany.myapp.service.VehicleAssignmentService;
import com.mycompany.myapp.service.criteria.VehicleAssignmentCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.VehicleAssignment}.
 */
@RestController
@RequestMapping("/api/vehicle-assignments")
public class VehicleAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(VehicleAssignmentResource.class);

    private static final String ENTITY_NAME = "vehicleAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleAssignmentService vehicleAssignmentService;

    private final VehicleAssignmentRepository vehicleAssignmentRepository;

    private final VehicleAssignmentQueryService vehicleAssignmentQueryService;

    public VehicleAssignmentResource(
        VehicleAssignmentService vehicleAssignmentService,
        VehicleAssignmentRepository vehicleAssignmentRepository,
        VehicleAssignmentQueryService vehicleAssignmentQueryService
    ) {
        this.vehicleAssignmentService = vehicleAssignmentService;
        this.vehicleAssignmentRepository = vehicleAssignmentRepository;
        this.vehicleAssignmentQueryService = vehicleAssignmentQueryService;
    }

    /**
     * {@code POST  /vehicle-assignments} : Create a new vehicleAssignment.
     *
     * @param vehicleAssignment the vehicleAssignment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleAssignment, or with status {@code 400 (Bad Request)} if the vehicleAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleAssignment> createVehicleAssignment(@Valid @RequestBody VehicleAssignment vehicleAssignment)
        throws URISyntaxException {
        log.debug("REST request to save VehicleAssignment : {}", vehicleAssignment);
        if (vehicleAssignment.getId() != null) {
            throw new BadRequestAlertException("A new vehicleAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleAssignment = vehicleAssignmentService.save(vehicleAssignment);
        return ResponseEntity.created(new URI("/api/vehicle-assignments/" + vehicleAssignment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleAssignment.getId().toString()))
            .body(vehicleAssignment);
    }

    /**
     * {@code PUT  /vehicle-assignments/:id} : Updates an existing vehicleAssignment.
     *
     * @param id the id of the vehicleAssignment to save.
     * @param vehicleAssignment the vehicleAssignment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleAssignment,
     * or with status {@code 400 (Bad Request)} if the vehicleAssignment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleAssignment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleAssignment> updateVehicleAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleAssignment vehicleAssignment
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleAssignment : {}, {}", id, vehicleAssignment);
        if (vehicleAssignment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleAssignment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleAssignment = vehicleAssignmentService.update(vehicleAssignment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleAssignment.getId().toString()))
            .body(vehicleAssignment);
    }

    /**
     * {@code PATCH  /vehicle-assignments/:id} : Partial updates given fields of an existing vehicleAssignment, field will ignore if it is null
     *
     * @param id the id of the vehicleAssignment to save.
     * @param vehicleAssignment the vehicleAssignment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleAssignment,
     * or with status {@code 400 (Bad Request)} if the vehicleAssignment is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleAssignment is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleAssignment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleAssignment> partialUpdateVehicleAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleAssignment vehicleAssignment
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleAssignment partially : {}, {}", id, vehicleAssignment);
        if (vehicleAssignment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleAssignment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleAssignment> result = vehicleAssignmentService.partialUpdate(vehicleAssignment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleAssignment.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-assignments} : get all the vehicleAssignments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleAssignments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleAssignment>> getAllVehicleAssignments(
        VehicleAssignmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get VehicleAssignments by criteria: {}", criteria);

        Page<VehicleAssignment> page = vehicleAssignmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-assignments/count} : count all the vehicleAssignments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVehicleAssignments(VehicleAssignmentCriteria criteria) {
        log.debug("REST request to count VehicleAssignments by criteria: {}", criteria);
        return ResponseEntity.ok().body(vehicleAssignmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vehicle-assignments/:id} : get the "id" vehicleAssignment.
     *
     * @param id the id of the vehicleAssignment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleAssignment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleAssignment> getVehicleAssignment(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleAssignment : {}", id);
        Optional<VehicleAssignment> vehicleAssignment = vehicleAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleAssignment);
    }

    /**
     * {@code DELETE  /vehicle-assignments/:id} : delete the "id" vehicleAssignment.
     *
     * @param id the id of the vehicleAssignment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleAssignment(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleAssignment : {}", id);
        vehicleAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
