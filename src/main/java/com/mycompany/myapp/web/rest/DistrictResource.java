package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.repository.DistrictRepository;
import com.mycompany.myapp.service.DistrictService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.District}.
 */
@RestController
@RequestMapping("/api/districts")
public class DistrictResource {

    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);

    private static final String ENTITY_NAME = "district";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DistrictService districtService;

    private final DistrictRepository districtRepository;

    public DistrictResource(DistrictService districtService, DistrictRepository districtRepository) {
        this.districtService = districtService;
        this.districtRepository = districtRepository;
    }

    /**
     * {@code POST  /districts} : Create a new district.
     *
     * @param district the district to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new district, or with status {@code 400 (Bad Request)} if the district has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<District> createDistrict(@Valid @RequestBody District district) throws URISyntaxException {
        log.debug("REST request to save District : {}", district);
        if (district.getId() != null) {
            throw new BadRequestAlertException("A new district cannot already have an ID", ENTITY_NAME, "idexists");
        }
        district = districtService.save(district);
        return ResponseEntity.created(new URI("/api/districts/" + district.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, district.getId().toString()))
            .body(district);
    }

    /**
     * {@code PUT  /districts/:id} : Updates an existing district.
     *
     * @param id the id of the district to save.
     * @param district the district to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated district,
     * or with status {@code 400 (Bad Request)} if the district is not valid,
     * or with status {@code 500 (Internal Server Error)} if the district couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<District> updateDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody District district
    ) throws URISyntaxException {
        log.debug("REST request to update District : {}, {}", id, district);
        if (district.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, district.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!districtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        district = districtService.update(district);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, district.getId().toString()))
            .body(district);
    }

    /**
     * {@code PATCH  /districts/:id} : Partial updates given fields of an existing district, field will ignore if it is null
     *
     * @param id the id of the district to save.
     * @param district the district to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated district,
     * or with status {@code 400 (Bad Request)} if the district is not valid,
     * or with status {@code 404 (Not Found)} if the district is not found,
     * or with status {@code 500 (Internal Server Error)} if the district couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<District> partialUpdateDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody District district
    ) throws URISyntaxException {
        log.debug("REST request to partial update District partially : {}, {}", id, district);
        if (district.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, district.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!districtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<District> result = districtService.partialUpdate(district);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, district.getId().toString())
        );
    }

    /**
     * {@code GET  /districts} : get all the districts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of districts in body.
     */
    @GetMapping("")
    public List<District> getAllDistricts(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Districts");
        return districtService.findAll();
    }

    /**
     * {@code GET  /districts/:id} : get the "id" district.
     *
     * @param id the id of the district to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the district, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<District> getDistrict(@PathVariable("id") Long id) {
        log.debug("REST request to get District : {}", id);
        Optional<District> district = districtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(district);
    }

    /**
     * {@code DELETE  /districts/:id} : delete the "id" district.
     *
     * @param id the id of the district to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable("id") Long id) {
        log.debug("REST request to delete District : {}", id);
        districtService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
