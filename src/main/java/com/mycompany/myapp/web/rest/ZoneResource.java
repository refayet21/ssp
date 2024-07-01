package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Zone;
import com.mycompany.myapp.repository.ZoneRepository;
import com.mycompany.myapp.service.ZoneService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Zone}.
 */
@RestController
@RequestMapping("/api/zones")
public class ZoneResource {

    private final Logger log = LoggerFactory.getLogger(ZoneResource.class);

    private static final String ENTITY_NAME = "zone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ZoneService zoneService;

    private final ZoneRepository zoneRepository;

    public ZoneResource(ZoneService zoneService, ZoneRepository zoneRepository) {
        this.zoneService = zoneService;
        this.zoneRepository = zoneRepository;
    }

    /**
     * {@code POST  /zones} : Create a new zone.
     *
     * @param zone the zone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new zone, or with status {@code 400 (Bad Request)} if the zone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Zone> createZone(@RequestBody Zone zone) throws URISyntaxException {
        log.debug("REST request to save Zone : {}", zone);
        if (zone.getId() != null) {
            throw new BadRequestAlertException("A new zone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        zone = zoneService.save(zone);
        return ResponseEntity.created(new URI("/api/zones/" + zone.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, zone.getId().toString()))
            .body(zone);
    }

    /**
     * {@code PUT  /zones/:id} : Updates an existing zone.
     *
     * @param id the id of the zone to save.
     * @param zone the zone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zone,
     * or with status {@code 400 (Bad Request)} if the zone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Zone> updateZone(@PathVariable(value = "id", required = false) final Long id, @RequestBody Zone zone)
        throws URISyntaxException {
        log.debug("REST request to update Zone : {}, {}", id, zone);
        if (zone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        zone = zoneService.update(zone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zone.getId().toString()))
            .body(zone);
    }

    /**
     * {@code PATCH  /zones/:id} : Partial updates given fields of an existing zone, field will ignore if it is null
     *
     * @param id the id of the zone to save.
     * @param zone the zone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zone,
     * or with status {@code 400 (Bad Request)} if the zone is not valid,
     * or with status {@code 404 (Not Found)} if the zone is not found,
     * or with status {@code 500 (Internal Server Error)} if the zone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Zone> partialUpdateZone(@PathVariable(value = "id", required = false) final Long id, @RequestBody Zone zone)
        throws URISyntaxException {
        log.debug("REST request to partial update Zone partially : {}, {}", id, zone);
        if (zone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, zone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!zoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Zone> result = zoneService.partialUpdate(zone);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zone.getId().toString())
        );
    }

    /**
     * {@code GET  /zones} : get all the zones.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of zones in body.
     */
    @GetMapping("")
    public List<Zone> getAllZones(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Zones");
        return zoneService.findAll();
    }

    /**
     * {@code GET  /zones/:id} : get the "id" zone.
     *
     * @param id the id of the zone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the zone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZone(@PathVariable("id") Long id) {
        log.debug("REST request to get Zone : {}", id);
        Optional<Zone> zone = zoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(zone);
    }

    /**
     * {@code DELETE  /zones/:id} : delete the "id" zone.
     *
     * @param id the id of the zone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable("id") Long id) {
        log.debug("REST request to delete Zone : {}", id);
        zoneService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
