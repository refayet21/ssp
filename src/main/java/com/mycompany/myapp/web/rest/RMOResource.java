package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.RMO;
import com.mycompany.myapp.repository.RMORepository;
import com.mycompany.myapp.service.RMOService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RMO}.
 */
@RestController
@RequestMapping("/api/rmos")
public class RMOResource {

    private final Logger log = LoggerFactory.getLogger(RMOResource.class);

    private static final String ENTITY_NAME = "rMO";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RMOService rMOService;

    private final RMORepository rMORepository;

    public RMOResource(RMOService rMOService, RMORepository rMORepository) {
        this.rMOService = rMOService;
        this.rMORepository = rMORepository;
    }

    /**
     * {@code POST  /rmos} : Create a new rMO.
     *
     * @param rMO the rMO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rMO, or with status {@code 400 (Bad Request)} if the rMO has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RMO> createRMO(@Valid @RequestBody RMO rMO) throws URISyntaxException {
        log.debug("REST request to save RMO : {}", rMO);
        if (rMO.getId() != null) {
            throw new BadRequestAlertException("A new rMO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rMO = rMOService.save(rMO);
        return ResponseEntity.created(new URI("/api/rmos/" + rMO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rMO.getId().toString()))
            .body(rMO);
    }

    /**
     * {@code PUT  /rmos/:id} : Updates an existing rMO.
     *
     * @param id the id of the rMO to save.
     * @param rMO the rMO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rMO,
     * or with status {@code 400 (Bad Request)} if the rMO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rMO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RMO> updateRMO(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody RMO rMO)
        throws URISyntaxException {
        log.debug("REST request to update RMO : {}, {}", id, rMO);
        if (rMO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rMO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rMORepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rMO = rMOService.update(rMO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rMO.getId().toString()))
            .body(rMO);
    }

    /**
     * {@code PATCH  /rmos/:id} : Partial updates given fields of an existing rMO, field will ignore if it is null
     *
     * @param id the id of the rMO to save.
     * @param rMO the rMO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rMO,
     * or with status {@code 400 (Bad Request)} if the rMO is not valid,
     * or with status {@code 404 (Not Found)} if the rMO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rMO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RMO> partialUpdateRMO(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody RMO rMO)
        throws URISyntaxException {
        log.debug("REST request to partial update RMO partially : {}, {}", id, rMO);
        if (rMO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rMO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rMORepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RMO> result = rMOService.partialUpdate(rMO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rMO.getId().toString())
        );
    }

    /**
     * {@code GET  /rmos} : get all the rMOS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rMOS in body.
     */
    @GetMapping("")
    public List<RMO> getAllRMOS() {
        log.debug("REST request to get all RMOS");
        return rMOService.findAll();
    }

    /**
     * {@code GET  /rmos/:id} : get the "id" rMO.
     *
     * @param id the id of the rMO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rMO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RMO> getRMO(@PathVariable("id") Long id) {
        log.debug("REST request to get RMO : {}", id);
        Optional<RMO> rMO = rMOService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rMO);
    }

    /**
     * {@code DELETE  /rmos/:id} : delete the "id" rMO.
     *
     * @param id the id of the rMO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRMO(@PathVariable("id") Long id) {
        log.debug("REST request to delete RMO : {}", id);
        rMOService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
