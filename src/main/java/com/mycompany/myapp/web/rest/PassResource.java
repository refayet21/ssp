package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Pass;
import com.mycompany.myapp.repository.PassRepository;
import com.mycompany.myapp.service.PassQueryService;
import com.mycompany.myapp.service.PassService;
import com.mycompany.myapp.service.criteria.PassCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Pass}.
 */
@RestController
@RequestMapping("/api/passes")
public class PassResource {

    private final Logger log = LoggerFactory.getLogger(PassResource.class);

    private static final String ENTITY_NAME = "pass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassService passService;

    private final PassRepository passRepository;

    private final PassQueryService passQueryService;

    public PassResource(PassService passService, PassRepository passRepository, PassQueryService passQueryService) {
        this.passService = passService;
        this.passRepository = passRepository;
        this.passQueryService = passQueryService;
    }

    /**
     * {@code POST  /passes} : Create a new pass.
     *
     * @param pass the pass to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pass, or with status {@code 400 (Bad Request)} if the pass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Pass> createPass(@Valid @RequestBody Pass pass) throws URISyntaxException {
        log.debug("REST request to save Pass : {}", pass);
        if (pass.getId() != null) {
            throw new BadRequestAlertException("A new pass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pass = passService.save(pass);
        return ResponseEntity.created(new URI("/api/passes/" + pass.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pass.getId().toString()))
            .body(pass);
    }

    /**
     * {@code PUT  /passes/:id} : Updates an existing pass.
     *
     * @param id the id of the pass to save.
     * @param pass the pass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pass,
     * or with status {@code 400 (Bad Request)} if the pass is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pass> updatePass(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Pass pass)
        throws URISyntaxException {
        log.debug("REST request to update Pass : {}, {}", id, pass);
        if (pass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pass.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pass = passService.update(pass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pass.getId().toString()))
            .body(pass);
    }

    /**
     * {@code PATCH  /passes/:id} : Partial updates given fields of an existing pass, field will ignore if it is null
     *
     * @param id the id of the pass to save.
     * @param pass the pass to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pass,
     * or with status {@code 400 (Bad Request)} if the pass is not valid,
     * or with status {@code 404 (Not Found)} if the pass is not found,
     * or with status {@code 500 (Internal Server Error)} if the pass couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pass> partialUpdatePass(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Pass pass
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pass partially : {}, {}", id, pass);
        if (pass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pass.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pass> result = passService.partialUpdate(pass);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pass.getId().toString())
        );
    }

    /**
     * {@code GET  /passes} : get all the passes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Pass>> getAllPasses(
        PassCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Passes by criteria: {}", criteria);

        Page<Pass> page = passQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /passes/count} : count all the passes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPasses(PassCriteria criteria) {
        log.debug("REST request to count Passes by criteria: {}", criteria);
        return ResponseEntity.ok().body(passQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /passes/:id} : get the "id" pass.
     *
     * @param id the id of the pass to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pass, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pass> getPass(@PathVariable("id") Long id) {
        log.debug("REST request to get Pass : {}", id);
        Optional<Pass> pass = passService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pass);
    }

    /**
     * {@code DELETE  /passes/:id} : delete the "id" pass.
     *
     * @param id the id of the pass to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePass(@PathVariable("id") Long id) {
        log.debug("REST request to delete Pass : {}", id);
        passService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
