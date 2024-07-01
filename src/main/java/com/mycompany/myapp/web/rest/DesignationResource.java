package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Designation;
import com.mycompany.myapp.repository.DesignationRepository;
import com.mycompany.myapp.service.DesignationService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Designation}.
 */
@RestController
@RequestMapping("/api/designations")
public class DesignationResource {

    private final Logger log = LoggerFactory.getLogger(DesignationResource.class);

    private static final String ENTITY_NAME = "designation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DesignationService designationService;

    private final DesignationRepository designationRepository;

    public DesignationResource(DesignationService designationService, DesignationRepository designationRepository) {
        this.designationService = designationService;
        this.designationRepository = designationRepository;
    }

    /**
     * {@code POST  /designations} : Create a new designation.
     *
     * @param designation the designation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new designation, or with status {@code 400 (Bad Request)} if the designation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Designation> createDesignation(@Valid @RequestBody Designation designation) throws URISyntaxException {
        log.debug("REST request to save Designation : {}", designation);
        if (designation.getId() != null) {
            throw new BadRequestAlertException("A new designation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        designation = designationService.save(designation);
        return ResponseEntity.created(new URI("/api/designations/" + designation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, designation.getId().toString()))
            .body(designation);
    }

    /**
     * {@code PUT  /designations/:id} : Updates an existing designation.
     *
     * @param id the id of the designation to save.
     * @param designation the designation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated designation,
     * or with status {@code 400 (Bad Request)} if the designation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the designation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Designation> updateDesignation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Designation designation
    ) throws URISyntaxException {
        log.debug("REST request to update Designation : {}, {}", id, designation);
        if (designation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, designation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!designationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        designation = designationService.update(designation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, designation.getId().toString()))
            .body(designation);
    }

    /**
     * {@code PATCH  /designations/:id} : Partial updates given fields of an existing designation, field will ignore if it is null
     *
     * @param id the id of the designation to save.
     * @param designation the designation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated designation,
     * or with status {@code 400 (Bad Request)} if the designation is not valid,
     * or with status {@code 404 (Not Found)} if the designation is not found,
     * or with status {@code 500 (Internal Server Error)} if the designation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Designation> partialUpdateDesignation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Designation designation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Designation partially : {}, {}", id, designation);
        if (designation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, designation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!designationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Designation> result = designationService.partialUpdate(designation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, designation.getId().toString())
        );
    }

    /**
     * {@code GET  /designations} : get all the designations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of designations in body.
     */
    @GetMapping("")
    public List<Designation> getAllDesignations() {
        log.debug("REST request to get all Designations");
        return designationService.findAll();
    }

    /**
     * {@code GET  /designations/:id} : get the "id" designation.
     *
     * @param id the id of the designation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the designation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Designation> getDesignation(@PathVariable("id") Long id) {
        log.debug("REST request to get Designation : {}", id);
        Optional<Designation> designation = designationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(designation);
    }

    /**
     * {@code DELETE  /designations/:id} : delete the "id" designation.
     *
     * @param id the id of the designation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable("id") Long id) {
        log.debug("REST request to delete Designation : {}", id);
        designationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
