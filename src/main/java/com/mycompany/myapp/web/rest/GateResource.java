package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Gate;
import com.mycompany.myapp.repository.GateRepository;
import com.mycompany.myapp.service.GateQueryService;
import com.mycompany.myapp.service.GateService;
import com.mycompany.myapp.service.criteria.GateCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Gate}.
 */
@RestController
@RequestMapping("/api/gates")
public class GateResource {

    private final Logger log = LoggerFactory.getLogger(GateResource.class);

    private static final String ENTITY_NAME = "gate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GateService gateService;

    private final GateRepository gateRepository;

    private final GateQueryService gateQueryService;

    public GateResource(GateService gateService, GateRepository gateRepository, GateQueryService gateQueryService) {
        this.gateService = gateService;
        this.gateRepository = gateRepository;
        this.gateQueryService = gateQueryService;
    }

    /**
     * {@code POST  /gates} : Create a new gate.
     *
     * @param gate the gate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gate, or with status {@code 400 (Bad Request)} if the gate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Gate> createGate(@RequestBody Gate gate) throws URISyntaxException {
        log.debug("REST request to save Gate : {}", gate);
        if (gate.getId() != null) {
            throw new BadRequestAlertException("A new gate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        gate = gateService.save(gate);
        return ResponseEntity.created(new URI("/api/gates/" + gate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, gate.getId().toString()))
            .body(gate);
    }

    /**
     * {@code PUT  /gates/:id} : Updates an existing gate.
     *
     * @param id the id of the gate to save.
     * @param gate the gate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gate,
     * or with status {@code 400 (Bad Request)} if the gate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Gate> updateGate(@PathVariable(value = "id", required = false) final Long id, @RequestBody Gate gate)
        throws URISyntaxException {
        log.debug("REST request to update Gate : {}, {}", id, gate);
        if (gate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        gate = gateService.update(gate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gate.getId().toString()))
            .body(gate);
    }

    /**
     * {@code PATCH  /gates/:id} : Partial updates given fields of an existing gate, field will ignore if it is null
     *
     * @param id the id of the gate to save.
     * @param gate the gate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gate,
     * or with status {@code 400 (Bad Request)} if the gate is not valid,
     * or with status {@code 404 (Not Found)} if the gate is not found,
     * or with status {@code 500 (Internal Server Error)} if the gate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Gate> partialUpdateGate(@PathVariable(value = "id", required = false) final Long id, @RequestBody Gate gate)
        throws URISyntaxException {
        log.debug("REST request to partial update Gate partially : {}, {}", id, gate);
        if (gate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Gate> result = gateService.partialUpdate(gate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gate.getId().toString())
        );
    }

    /**
     * {@code GET  /gates} : get all the gates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Gate>> getAllGates(GateCriteria criteria) {
        log.debug("REST request to get Gates by criteria: {}", criteria);

        List<Gate> entityList = gateQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /gates/count} : count all the gates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGates(GateCriteria criteria) {
        log.debug("REST request to count Gates by criteria: {}", criteria);
        return ResponseEntity.ok().body(gateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gates/:id} : get the "id" gate.
     *
     * @param id the id of the gate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Gate> getGate(@PathVariable("id") Long id) {
        log.debug("REST request to get Gate : {}", id);
        Optional<Gate> gate = gateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gate);
    }

    /**
     * {@code DELETE  /gates/:id} : delete the "id" gate.
     *
     * @param id the id of the gate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGate(@PathVariable("id") Long id) {
        log.debug("REST request to delete Gate : {}", id);
        gateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
