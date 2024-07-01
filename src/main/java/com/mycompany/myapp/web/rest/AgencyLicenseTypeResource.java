package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AgencyLicenseType;
import com.mycompany.myapp.repository.AgencyLicenseTypeRepository;
import com.mycompany.myapp.service.AgencyLicenseTypeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AgencyLicenseType}.
 */
@RestController
@RequestMapping("/api/agency-license-types")
public class AgencyLicenseTypeResource {

    private final Logger log = LoggerFactory.getLogger(AgencyLicenseTypeResource.class);

    private static final String ENTITY_NAME = "agencyLicenseType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgencyLicenseTypeService agencyLicenseTypeService;

    private final AgencyLicenseTypeRepository agencyLicenseTypeRepository;

    public AgencyLicenseTypeResource(
        AgencyLicenseTypeService agencyLicenseTypeService,
        AgencyLicenseTypeRepository agencyLicenseTypeRepository
    ) {
        this.agencyLicenseTypeService = agencyLicenseTypeService;
        this.agencyLicenseTypeRepository = agencyLicenseTypeRepository;
    }

    /**
     * {@code POST  /agency-license-types} : Create a new agencyLicenseType.
     *
     * @param agencyLicenseType the agencyLicenseType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agencyLicenseType, or with status {@code 400 (Bad Request)} if the agencyLicenseType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgencyLicenseType> createAgencyLicenseType(@Valid @RequestBody AgencyLicenseType agencyLicenseType)
        throws URISyntaxException {
        log.debug("REST request to save AgencyLicenseType : {}", agencyLicenseType);
        if (agencyLicenseType.getId() != null) {
            throw new BadRequestAlertException("A new agencyLicenseType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agencyLicenseType = agencyLicenseTypeService.save(agencyLicenseType);
        return ResponseEntity.created(new URI("/api/agency-license-types/" + agencyLicenseType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agencyLicenseType.getId().toString()))
            .body(agencyLicenseType);
    }

    /**
     * {@code PUT  /agency-license-types/:id} : Updates an existing agencyLicenseType.
     *
     * @param id the id of the agencyLicenseType to save.
     * @param agencyLicenseType the agencyLicenseType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyLicenseType,
     * or with status {@code 400 (Bad Request)} if the agencyLicenseType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agencyLicenseType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgencyLicenseType> updateAgencyLicenseType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgencyLicenseType agencyLicenseType
    ) throws URISyntaxException {
        log.debug("REST request to update AgencyLicenseType : {}, {}", id, agencyLicenseType);
        if (agencyLicenseType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyLicenseType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyLicenseTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agencyLicenseType = agencyLicenseTypeService.update(agencyLicenseType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyLicenseType.getId().toString()))
            .body(agencyLicenseType);
    }

    /**
     * {@code PATCH  /agency-license-types/:id} : Partial updates given fields of an existing agencyLicenseType, field will ignore if it is null
     *
     * @param id the id of the agencyLicenseType to save.
     * @param agencyLicenseType the agencyLicenseType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyLicenseType,
     * or with status {@code 400 (Bad Request)} if the agencyLicenseType is not valid,
     * or with status {@code 404 (Not Found)} if the agencyLicenseType is not found,
     * or with status {@code 500 (Internal Server Error)} if the agencyLicenseType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgencyLicenseType> partialUpdateAgencyLicenseType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgencyLicenseType agencyLicenseType
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgencyLicenseType partially : {}, {}", id, agencyLicenseType);
        if (agencyLicenseType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyLicenseType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyLicenseTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgencyLicenseType> result = agencyLicenseTypeService.partialUpdate(agencyLicenseType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyLicenseType.getId().toString())
        );
    }

    /**
     * {@code GET  /agency-license-types} : get all the agencyLicenseTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencyLicenseTypes in body.
     */
    @GetMapping("")
    public List<AgencyLicenseType> getAllAgencyLicenseTypes() {
        log.debug("REST request to get all AgencyLicenseTypes");
        return agencyLicenseTypeService.findAll();
    }

    /**
     * {@code GET  /agency-license-types/:id} : get the "id" agencyLicenseType.
     *
     * @param id the id of the agencyLicenseType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agencyLicenseType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgencyLicenseType> getAgencyLicenseType(@PathVariable("id") Long id) {
        log.debug("REST request to get AgencyLicenseType : {}", id);
        Optional<AgencyLicenseType> agencyLicenseType = agencyLicenseTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agencyLicenseType);
    }

    /**
     * {@code DELETE  /agency-license-types/:id} : delete the "id" agencyLicenseType.
     *
     * @param id the id of the agencyLicenseType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgencyLicenseType(@PathVariable("id") Long id) {
        log.debug("REST request to delete AgencyLicenseType : {}", id);
        agencyLicenseTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
