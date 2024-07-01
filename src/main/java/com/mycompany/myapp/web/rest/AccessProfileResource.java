package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AccessProfile;
import com.mycompany.myapp.repository.AccessProfileRepository;
import com.mycompany.myapp.service.AccessProfileService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AccessProfile}.
 */
@RestController
@RequestMapping("/api/access-profiles")
public class AccessProfileResource {

    private final Logger log = LoggerFactory.getLogger(AccessProfileResource.class);

    private static final String ENTITY_NAME = "accessProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccessProfileService accessProfileService;

    private final AccessProfileRepository accessProfileRepository;

    public AccessProfileResource(AccessProfileService accessProfileService, AccessProfileRepository accessProfileRepository) {
        this.accessProfileService = accessProfileService;
        this.accessProfileRepository = accessProfileRepository;
    }

    /**
     * {@code POST  /access-profiles} : Create a new accessProfile.
     *
     * @param accessProfile the accessProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accessProfile, or with status {@code 400 (Bad Request)} if the accessProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccessProfile> createAccessProfile(@RequestBody AccessProfile accessProfile) throws URISyntaxException {
        log.debug("REST request to save AccessProfile : {}", accessProfile);
        if (accessProfile.getId() != null) {
            throw new BadRequestAlertException("A new accessProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accessProfile = accessProfileService.save(accessProfile);
        return ResponseEntity.created(new URI("/api/access-profiles/" + accessProfile.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, accessProfile.getId().toString()))
            .body(accessProfile);
    }

    /**
     * {@code PUT  /access-profiles/:id} : Updates an existing accessProfile.
     *
     * @param id the id of the accessProfile to save.
     * @param accessProfile the accessProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessProfile,
     * or with status {@code 400 (Bad Request)} if the accessProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accessProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccessProfile> updateAccessProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccessProfile accessProfile
    ) throws URISyntaxException {
        log.debug("REST request to update AccessProfile : {}, {}", id, accessProfile);
        if (accessProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accessProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accessProfile = accessProfileService.update(accessProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accessProfile.getId().toString()))
            .body(accessProfile);
    }

    /**
     * {@code PATCH  /access-profiles/:id} : Partial updates given fields of an existing accessProfile, field will ignore if it is null
     *
     * @param id the id of the accessProfile to save.
     * @param accessProfile the accessProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accessProfile,
     * or with status {@code 400 (Bad Request)} if the accessProfile is not valid,
     * or with status {@code 404 (Not Found)} if the accessProfile is not found,
     * or with status {@code 500 (Internal Server Error)} if the accessProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccessProfile> partialUpdateAccessProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccessProfile accessProfile
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccessProfile partially : {}, {}", id, accessProfile);
        if (accessProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accessProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accessProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccessProfile> result = accessProfileService.partialUpdate(accessProfile);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accessProfile.getId().toString())
        );
    }

    /**
     * {@code GET  /access-profiles} : get all the accessProfiles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accessProfiles in body.
     */
    @GetMapping("")
    public List<AccessProfile> getAllAccessProfiles(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all AccessProfiles");
        return accessProfileService.findAll();
    }

    /**
     * {@code GET  /access-profiles/:id} : get the "id" accessProfile.
     *
     * @param id the id of the accessProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accessProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccessProfile> getAccessProfile(@PathVariable("id") Long id) {
        log.debug("REST request to get AccessProfile : {}", id);
        Optional<AccessProfile> accessProfile = accessProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accessProfile);
    }

    /**
     * {@code DELETE  /access-profiles/:id} : delete the "id" accessProfile.
     *
     * @param id the id of the accessProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessProfile(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccessProfile : {}", id);
        accessProfileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
