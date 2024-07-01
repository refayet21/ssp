package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AgencyType;
import com.mycompany.myapp.repository.AgencyTypeRepository;
import com.mycompany.myapp.service.AgencyTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AgencyType}.
 */
@RestController
@RequestMapping("/api/agency-types")
public class AgencyTypeResource {

    private final Logger log = LoggerFactory.getLogger(AgencyTypeResource.class);

    private static final String ENTITY_NAME = "agencyType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgencyTypeService agencyTypeService;

    private final AgencyTypeRepository agencyTypeRepository;

    public AgencyTypeResource(AgencyTypeService agencyTypeService, AgencyTypeRepository agencyTypeRepository) {
        this.agencyTypeService = agencyTypeService;
        this.agencyTypeRepository = agencyTypeRepository;
    }

    /**
     * {@code POST  /agency-types} : Create a new agencyType.
     *
     * @param agencyType the agencyType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agencyType, or with status {@code 400 (Bad Request)} if the agencyType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgencyType> createAgencyType(@RequestBody AgencyType agencyType) throws URISyntaxException {
        log.debug("REST request to save AgencyType : {}", agencyType);
        if (agencyType.getId() != null) {
            throw new BadRequestAlertException("A new agencyType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agencyType = agencyTypeService.save(agencyType);
        return ResponseEntity.created(new URI("/api/agency-types/" + agencyType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agencyType.getId().toString()))
            .body(agencyType);
    }

    /**
     * {@code PUT  /agency-types/:id} : Updates an existing agencyType.
     *
     * @param id the id of the agencyType to save.
     * @param agencyType the agencyType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyType,
     * or with status {@code 400 (Bad Request)} if the agencyType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agencyType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgencyType> updateAgencyType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgencyType agencyType
    ) throws URISyntaxException {
        log.debug("REST request to update AgencyType : {}, {}", id, agencyType);
        if (agencyType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agencyType = agencyTypeService.update(agencyType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyType.getId().toString()))
            .body(agencyType);
    }

    /**
     * {@code PATCH  /agency-types/:id} : Partial updates given fields of an existing agencyType, field will ignore if it is null
     *
     * @param id the id of the agencyType to save.
     * @param agencyType the agencyType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyType,
     * or with status {@code 400 (Bad Request)} if the agencyType is not valid,
     * or with status {@code 404 (Not Found)} if the agencyType is not found,
     * or with status {@code 500 (Internal Server Error)} if the agencyType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgencyType> partialUpdateAgencyType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgencyType agencyType
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgencyType partially : {}, {}", id, agencyType);
        if (agencyType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgencyType> result = agencyTypeService.partialUpdate(agencyType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyType.getId().toString())
        );
    }

    /**
     * {@code GET  /agency-types} : get all the agencyTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencyTypes in body.
     */
    @GetMapping("")
    public List<AgencyType> getAllAgencyTypes() {
        log.debug("REST request to get all AgencyTypes");
        return agencyTypeService.findAll();
    }

    /**
     * {@code GET  /agency-types/:id} : get the "id" agencyType.
     *
     * @param id the id of the agencyType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agencyType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgencyType> getAgencyType(@PathVariable("id") Long id) {
        log.debug("REST request to get AgencyType : {}", id);
        Optional<AgencyType> agencyType = agencyTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agencyType);
    }

    /**
     * {@code DELETE  /agency-types/:id} : delete the "id" agencyType.
     *
     * @param id the id of the agencyType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgencyType(@PathVariable("id") Long id) {
        log.debug("REST request to delete AgencyType : {}", id);
        agencyTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
