package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Lane;
import com.mycompany.myapp.repository.LaneRepository;
import com.mycompany.myapp.service.LaneQueryService;
import com.mycompany.myapp.service.LaneService;
import com.mycompany.myapp.service.criteria.LaneCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Lane}.
 */
@RestController
@RequestMapping("/api/lanes")
public class LaneResource {

    private final Logger log = LoggerFactory.getLogger(LaneResource.class);

    private static final String ENTITY_NAME = "lane";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LaneService laneService;

    private final LaneRepository laneRepository;

    private final LaneQueryService laneQueryService;

    public LaneResource(LaneService laneService, LaneRepository laneRepository, LaneQueryService laneQueryService) {
        this.laneService = laneService;
        this.laneRepository = laneRepository;
        this.laneQueryService = laneQueryService;
    }

    /**
     * {@code POST  /lanes} : Create a new lane.
     *
     * @param lane the lane to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lane, or with status {@code 400 (Bad Request)} if the lane has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Lane> createLane(@RequestBody Lane lane) throws URISyntaxException {
        log.debug("REST request to save Lane : {}", lane);
        if (lane.getId() != null) {
            throw new BadRequestAlertException("A new lane cannot already have an ID", ENTITY_NAME, "idexists");
        }
        lane = laneService.save(lane);
        return ResponseEntity.created(new URI("/api/lanes/" + lane.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, lane.getId().toString()))
            .body(lane);
    }

    /**
     * {@code PUT  /lanes/:id} : Updates an existing lane.
     *
     * @param id the id of the lane to save.
     * @param lane the lane to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lane,
     * or with status {@code 400 (Bad Request)} if the lane is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lane couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Lane> updateLane(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lane lane)
        throws URISyntaxException {
        log.debug("REST request to update Lane : {}, {}", id, lane);
        if (lane.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lane.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lane = laneService.update(lane);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lane.getId().toString()))
            .body(lane);
    }

    /**
     * {@code PATCH  /lanes/:id} : Partial updates given fields of an existing lane, field will ignore if it is null
     *
     * @param id the id of the lane to save.
     * @param lane the lane to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lane,
     * or with status {@code 400 (Bad Request)} if the lane is not valid,
     * or with status {@code 404 (Not Found)} if the lane is not found,
     * or with status {@code 500 (Internal Server Error)} if the lane couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Lane> partialUpdateLane(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lane lane)
        throws URISyntaxException {
        log.debug("REST request to partial update Lane partially : {}, {}", id, lane);
        if (lane.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lane.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lane> result = laneService.partialUpdate(lane);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lane.getId().toString())
        );
    }

    /**
     * {@code GET  /lanes} : get all the lanes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lanes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Lane>> getAllLanes(LaneCriteria criteria) {
        log.debug("REST request to get Lanes by criteria: {}", criteria);

        List<Lane> entityList = laneQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /lanes/count} : count all the lanes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLanes(LaneCriteria criteria) {
        log.debug("REST request to count Lanes by criteria: {}", criteria);
        return ResponseEntity.ok().body(laneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lanes/:id} : get the "id" lane.
     *
     * @param id the id of the lane to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lane, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lane> getLane(@PathVariable("id") Long id) {
        log.debug("REST request to get Lane : {}", id);
        Optional<Lane> lane = laneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lane);
    }

    /**
     * {@code DELETE  /lanes/:id} : delete the "id" lane.
     *
     * @param id the id of the lane to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLane(@PathVariable("id") Long id) {
        log.debug("REST request to delete Lane : {}", id);
        laneService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
