package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Union;
import com.mycompany.myapp.repository.UnionRepository;
import com.mycompany.myapp.service.UnionQueryService;
import com.mycompany.myapp.service.UnionService;
import com.mycompany.myapp.service.criteria.UnionCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Union}.
 */
@RestController
@RequestMapping("/api/unions")
public class UnionResource {

    private final Logger log = LoggerFactory.getLogger(UnionResource.class);

    private static final String ENTITY_NAME = "union";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnionService unionService;

    private final UnionRepository unionRepository;

    private final UnionQueryService unionQueryService;

    public UnionResource(UnionService unionService, UnionRepository unionRepository, UnionQueryService unionQueryService) {
        this.unionService = unionService;
        this.unionRepository = unionRepository;
        this.unionQueryService = unionQueryService;
    }

    /**
     * {@code POST  /unions} : Create a new union.
     *
     * @param union the union to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new union, or with status {@code 400 (Bad Request)} if the union has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Union> createUnion(@Valid @RequestBody Union union) throws URISyntaxException {
        log.debug("REST request to save Union : {}", union);
        if (union.getId() != null) {
            throw new BadRequestAlertException("A new union cannot already have an ID", ENTITY_NAME, "idexists");
        }
        union = unionService.save(union);
        return ResponseEntity.created(new URI("/api/unions/" + union.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, union.getId().toString()))
            .body(union);
    }

    /**
     * {@code PUT  /unions/:id} : Updates an existing union.
     *
     * @param id the id of the union to save.
     * @param union the union to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated union,
     * or with status {@code 400 (Bad Request)} if the union is not valid,
     * or with status {@code 500 (Internal Server Error)} if the union couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Union> updateUnion(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Union union)
        throws URISyntaxException {
        log.debug("REST request to update Union : {}, {}", id, union);
        if (union.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, union.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        union = unionService.update(union);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, union.getId().toString()))
            .body(union);
    }

    /**
     * {@code PATCH  /unions/:id} : Partial updates given fields of an existing union, field will ignore if it is null
     *
     * @param id the id of the union to save.
     * @param union the union to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated union,
     * or with status {@code 400 (Bad Request)} if the union is not valid,
     * or with status {@code 404 (Not Found)} if the union is not found,
     * or with status {@code 500 (Internal Server Error)} if the union couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Union> partialUpdateUnion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Union union
    ) throws URISyntaxException {
        log.debug("REST request to partial update Union partially : {}, {}", id, union);
        if (union.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, union.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Union> result = unionService.partialUpdate(union);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, union.getId().toString())
        );
    }

    /**
     * {@code GET  /unions} : get all the unions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Union>> getAllUnions(UnionCriteria criteria) {
        log.debug("REST request to get Unions by criteria: {}", criteria);

        List<Union> entityList = unionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /unions/count} : count all the unions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUnions(UnionCriteria criteria) {
        log.debug("REST request to count Unions by criteria: {}", criteria);
        return ResponseEntity.ok().body(unionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /unions/:id} : get the "id" union.
     *
     * @param id the id of the union to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the union, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Union> getUnion(@PathVariable("id") Long id) {
        log.debug("REST request to get Union : {}", id);
        Optional<Union> union = unionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(union);
    }

    /**
     * {@code DELETE  /unions/:id} : delete the "id" union.
     *
     * @param id the id of the union to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnion(@PathVariable("id") Long id) {
        log.debug("REST request to delete Union : {}", id);
        unionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
