package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AgencyLicense;
import com.mycompany.myapp.repository.AgencyLicenseRepository;
import com.mycompany.myapp.service.AgencyLicenseQueryService;
import com.mycompany.myapp.service.AgencyLicenseService;
import com.mycompany.myapp.service.criteria.AgencyLicenseCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AgencyLicense}.
 */
@RestController
@RequestMapping("/api/agency-licenses")
public class AgencyLicenseResource {

    private final Logger log = LoggerFactory.getLogger(AgencyLicenseResource.class);

    private static final String ENTITY_NAME = "agencyLicense";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgencyLicenseService agencyLicenseService;

    private final AgencyLicenseRepository agencyLicenseRepository;

    private final AgencyLicenseQueryService agencyLicenseQueryService;

    public AgencyLicenseResource(
        AgencyLicenseService agencyLicenseService,
        AgencyLicenseRepository agencyLicenseRepository,
        AgencyLicenseQueryService agencyLicenseQueryService
    ) {
        this.agencyLicenseService = agencyLicenseService;
        this.agencyLicenseRepository = agencyLicenseRepository;
        this.agencyLicenseQueryService = agencyLicenseQueryService;
    }

    /**
     * {@code POST  /agency-licenses} : Create a new agencyLicense.
     *
     * @param agencyLicense the agencyLicense to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agencyLicense, or with status {@code 400 (Bad Request)} if the agencyLicense has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgencyLicense> createAgencyLicense(@Valid @RequestBody AgencyLicense agencyLicense) throws URISyntaxException {
        log.debug("REST request to save AgencyLicense : {}", agencyLicense);
        if (agencyLicense.getId() != null) {
            throw new BadRequestAlertException("A new agencyLicense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agencyLicense = agencyLicenseService.save(agencyLicense);
        return ResponseEntity.created(new URI("/api/agency-licenses/" + agencyLicense.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agencyLicense.getId().toString()))
            .body(agencyLicense);
    }

    /**
     * {@code PUT  /agency-licenses/:id} : Updates an existing agencyLicense.
     *
     * @param id the id of the agencyLicense to save.
     * @param agencyLicense the agencyLicense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyLicense,
     * or with status {@code 400 (Bad Request)} if the agencyLicense is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agencyLicense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgencyLicense> updateAgencyLicense(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgencyLicense agencyLicense
    ) throws URISyntaxException {
        log.debug("REST request to update AgencyLicense : {}, {}", id, agencyLicense);
        if (agencyLicense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyLicense.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyLicenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agencyLicense = agencyLicenseService.update(agencyLicense);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyLicense.getId().toString()))
            .body(agencyLicense);
    }

    /**
     * {@code PATCH  /agency-licenses/:id} : Partial updates given fields of an existing agencyLicense, field will ignore if it is null
     *
     * @param id the id of the agencyLicense to save.
     * @param agencyLicense the agencyLicense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyLicense,
     * or with status {@code 400 (Bad Request)} if the agencyLicense is not valid,
     * or with status {@code 404 (Not Found)} if the agencyLicense is not found,
     * or with status {@code 500 (Internal Server Error)} if the agencyLicense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgencyLicense> partialUpdateAgencyLicense(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgencyLicense agencyLicense
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgencyLicense partially : {}, {}", id, agencyLicense);
        if (agencyLicense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyLicense.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyLicenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgencyLicense> result = agencyLicenseService.partialUpdate(agencyLicense);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agencyLicense.getId().toString())
        );
    }

    /**
     * {@code GET  /agency-licenses} : get all the agencyLicenses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencyLicenses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AgencyLicense>> getAllAgencyLicenses(
        AgencyLicenseCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AgencyLicenses by criteria: {}", criteria);

        Page<AgencyLicense> page = agencyLicenseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agency-licenses/count} : count all the agencyLicenses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAgencyLicenses(AgencyLicenseCriteria criteria) {
        log.debug("REST request to count AgencyLicenses by criteria: {}", criteria);
        return ResponseEntity.ok().body(agencyLicenseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agency-licenses/:id} : get the "id" agencyLicense.
     *
     * @param id the id of the agencyLicense to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agencyLicense, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgencyLicense> getAgencyLicense(@PathVariable("id") Long id) {
        log.debug("REST request to get AgencyLicense : {}", id);
        Optional<AgencyLicense> agencyLicense = agencyLicenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agencyLicense);
    }

    /**
     * {@code DELETE  /agency-licenses/:id} : delete the "id" agencyLicense.
     *
     * @param id the id of the agencyLicense to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgencyLicense(@PathVariable("id") Long id) {
        log.debug("REST request to delete AgencyLicense : {}", id);
        agencyLicenseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
