package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PostOffice;
import com.mycompany.myapp.repository.PostOfficeRepository;
import com.mycompany.myapp.service.PostOfficeQueryService;
import com.mycompany.myapp.service.PostOfficeService;
import com.mycompany.myapp.service.criteria.PostOfficeCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PostOffice}.
 */
@RestController
@RequestMapping("/api/post-offices")
public class PostOfficeResource {

    private final Logger log = LoggerFactory.getLogger(PostOfficeResource.class);

    private static final String ENTITY_NAME = "postOffice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostOfficeService postOfficeService;

    private final PostOfficeRepository postOfficeRepository;

    private final PostOfficeQueryService postOfficeQueryService;

    public PostOfficeResource(
        PostOfficeService postOfficeService,
        PostOfficeRepository postOfficeRepository,
        PostOfficeQueryService postOfficeQueryService
    ) {
        this.postOfficeService = postOfficeService;
        this.postOfficeRepository = postOfficeRepository;
        this.postOfficeQueryService = postOfficeQueryService;
    }

    /**
     * {@code POST  /post-offices} : Create a new postOffice.
     *
     * @param postOffice the postOffice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postOffice, or with status {@code 400 (Bad Request)} if the postOffice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostOffice> createPostOffice(@Valid @RequestBody PostOffice postOffice) throws URISyntaxException {
        log.debug("REST request to save PostOffice : {}", postOffice);
        if (postOffice.getId() != null) {
            throw new BadRequestAlertException("A new postOffice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        postOffice = postOfficeService.save(postOffice);
        return ResponseEntity.created(new URI("/api/post-offices/" + postOffice.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, postOffice.getId().toString()))
            .body(postOffice);
    }

    /**
     * {@code PUT  /post-offices/:id} : Updates an existing postOffice.
     *
     * @param id the id of the postOffice to save.
     * @param postOffice the postOffice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postOffice,
     * or with status {@code 400 (Bad Request)} if the postOffice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postOffice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostOffice> updatePostOffice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostOffice postOffice
    ) throws URISyntaxException {
        log.debug("REST request to update PostOffice : {}, {}", id, postOffice);
        if (postOffice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postOffice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postOfficeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        postOffice = postOfficeService.update(postOffice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postOffice.getId().toString()))
            .body(postOffice);
    }

    /**
     * {@code PATCH  /post-offices/:id} : Partial updates given fields of an existing postOffice, field will ignore if it is null
     *
     * @param id the id of the postOffice to save.
     * @param postOffice the postOffice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postOffice,
     * or with status {@code 400 (Bad Request)} if the postOffice is not valid,
     * or with status {@code 404 (Not Found)} if the postOffice is not found,
     * or with status {@code 500 (Internal Server Error)} if the postOffice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostOffice> partialUpdatePostOffice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostOffice postOffice
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostOffice partially : {}, {}", id, postOffice);
        if (postOffice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postOffice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postOfficeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostOffice> result = postOfficeService.partialUpdate(postOffice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postOffice.getId().toString())
        );
    }

    /**
     * {@code GET  /post-offices} : get all the postOffices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postOffices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PostOffice>> getAllPostOffices(PostOfficeCriteria criteria) {
        log.debug("REST request to get PostOffices by criteria: {}", criteria);

        List<PostOffice> entityList = postOfficeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /post-offices/count} : count all the postOffices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPostOffices(PostOfficeCriteria criteria) {
        log.debug("REST request to count PostOffices by criteria: {}", criteria);
        return ResponseEntity.ok().body(postOfficeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /post-offices/:id} : get the "id" postOffice.
     *
     * @param id the id of the postOffice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postOffice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostOffice> getPostOffice(@PathVariable("id") Long id) {
        log.debug("REST request to get PostOffice : {}", id);
        Optional<PostOffice> postOffice = postOfficeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postOffice);
    }

    /**
     * {@code DELETE  /post-offices/:id} : delete the "id" postOffice.
     *
     * @param id the id of the postOffice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostOffice(@PathVariable("id") Long id) {
        log.debug("REST request to delete PostOffice : {}", id);
        postOfficeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
