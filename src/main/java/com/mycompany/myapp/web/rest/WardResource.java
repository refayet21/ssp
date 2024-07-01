package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Ward;
import com.mycompany.myapp.repository.WardRepository;
import com.mycompany.myapp.service.WardService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Ward}.
 */
@RestController
@RequestMapping("/api/wards")
public class WardResource {

    private final Logger log = LoggerFactory.getLogger(WardResource.class);

    private static final String ENTITY_NAME = "ward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WardService wardService;

    private final WardRepository wardRepository;

    public WardResource(WardService wardService, WardRepository wardRepository) {
        this.wardService = wardService;
        this.wardRepository = wardRepository;
    }

    /**
     * {@code POST  /wards} : Create a new ward.
     *
     * @param ward the ward to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ward, or with status {@code 400 (Bad Request)} if the ward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ward> createWard(@Valid @RequestBody Ward ward) throws URISyntaxException {
        log.debug("REST request to save Ward : {}", ward);
        if (ward.getId() != null) {
            throw new BadRequestAlertException("A new ward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ward = wardService.save(ward);
        return ResponseEntity.created(new URI("/api/wards/" + ward.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ward.getId().toString()))
            .body(ward);
    }

    /**
     * {@code PUT  /wards/:id} : Updates an existing ward.
     *
     * @param id the id of the ward to save.
     * @param ward the ward to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ward,
     * or with status {@code 400 (Bad Request)} if the ward is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ward couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ward> updateWard(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Ward ward)
        throws URISyntaxException {
        log.debug("REST request to update Ward : {}, {}", id, ward);
        if (ward.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ward.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ward = wardService.update(ward);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ward.getId().toString()))
            .body(ward);
    }

    /**
     * {@code PATCH  /wards/:id} : Partial updates given fields of an existing ward, field will ignore if it is null
     *
     * @param id the id of the ward to save.
     * @param ward the ward to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ward,
     * or with status {@code 400 (Bad Request)} if the ward is not valid,
     * or with status {@code 404 (Not Found)} if the ward is not found,
     * or with status {@code 500 (Internal Server Error)} if the ward couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ward> partialUpdateWard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ward ward
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ward partially : {}, {}", id, ward);
        if (ward.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ward.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ward> result = wardService.partialUpdate(ward);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ward.getId().toString())
        );
    }

    /**
     * {@code GET  /wards} : get all the wards.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wards in body.
     */
    @GetMapping("")
    public List<Ward> getAllWards(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Wards");
        return wardService.findAll();
    }

    /**
     * {@code GET  /wards/:id} : get the "id" ward.
     *
     * @param id the id of the ward to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ward, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ward> getWard(@PathVariable("id") Long id) {
        log.debug("REST request to get Ward : {}", id);
        Optional<Ward> ward = wardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ward);
    }

    /**
     * {@code DELETE  /wards/:id} : delete the "id" ward.
     *
     * @param id the id of the ward to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWard(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ward : {}", id);
        wardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
