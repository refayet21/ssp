package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PassType;
import com.mycompany.myapp.repository.PassTypeRepository;
import com.mycompany.myapp.service.PassTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PassType}.
 */
@RestController
@RequestMapping("/api/pass-types")
public class PassTypeResource {

    private final Logger log = LoggerFactory.getLogger(PassTypeResource.class);

    private static final String ENTITY_NAME = "passType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassTypeService passTypeService;

    private final PassTypeRepository passTypeRepository;

    public PassTypeResource(PassTypeService passTypeService, PassTypeRepository passTypeRepository) {
        this.passTypeService = passTypeService;
        this.passTypeRepository = passTypeRepository;
    }

    /**
     * {@code POST  /pass-types} : Create a new passType.
     *
     * @param passType the passType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passType, or with status {@code 400 (Bad Request)} if the passType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PassType> createPassType(@RequestBody PassType passType) throws URISyntaxException {
        log.debug("REST request to save PassType : {}", passType);
        if (passType.getId() != null) {
            throw new BadRequestAlertException("A new passType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        passType = passTypeService.save(passType);
        return ResponseEntity.created(new URI("/api/pass-types/" + passType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, passType.getId().toString()))
            .body(passType);
    }

    /**
     * {@code PUT  /pass-types/:id} : Updates an existing passType.
     *
     * @param id the id of the passType to save.
     * @param passType the passType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passType,
     * or with status {@code 400 (Bad Request)} if the passType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PassType> updatePassType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PassType passType
    ) throws URISyntaxException {
        log.debug("REST request to update PassType : {}, {}", id, passType);
        if (passType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        passType = passTypeService.update(passType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passType.getId().toString()))
            .body(passType);
    }

    /**
     * {@code PATCH  /pass-types/:id} : Partial updates given fields of an existing passType, field will ignore if it is null
     *
     * @param id the id of the passType to save.
     * @param passType the passType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passType,
     * or with status {@code 400 (Bad Request)} if the passType is not valid,
     * or with status {@code 404 (Not Found)} if the passType is not found,
     * or with status {@code 500 (Internal Server Error)} if the passType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PassType> partialUpdatePassType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PassType passType
    ) throws URISyntaxException {
        log.debug("REST request to partial update PassType partially : {}, {}", id, passType);
        if (passType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PassType> result = passTypeService.partialUpdate(passType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passType.getId().toString())
        );
    }

    /**
     * {@code GET  /pass-types} : get all the passTypes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passTypes in body.
     */
    @GetMapping("")
    public List<PassType> getAllPassTypes(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all PassTypes");
        return passTypeService.findAll();
    }

    /**
     * {@code GET  /pass-types/:id} : get the "id" passType.
     *
     * @param id the id of the passType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PassType> getPassType(@PathVariable("id") Long id) {
        log.debug("REST request to get PassType : {}", id);
        Optional<PassType> passType = passTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passType);
    }

    /**
     * {@code DELETE  /pass-types/:id} : delete the "id" passType.
     *
     * @param id the id of the passType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassType(@PathVariable("id") Long id) {
        log.debug("REST request to delete PassType : {}", id);
        passTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
