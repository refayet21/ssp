package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EntryLog;
import com.mycompany.myapp.repository.EntryLogRepository;
import com.mycompany.myapp.service.EntryLogQueryService;
import com.mycompany.myapp.service.EntryLogService;
import com.mycompany.myapp.service.criteria.EntryLogCriteria;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.EntryLog}.
 */
@RestController
@RequestMapping("/api/entry-logs")
public class EntryLogResource {

    private final Logger log = LoggerFactory.getLogger(EntryLogResource.class);

    private static final String ENTITY_NAME = "entryLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntryLogService entryLogService;

    private final EntryLogRepository entryLogRepository;

    private final EntryLogQueryService entryLogQueryService;

    public EntryLogResource(
        EntryLogService entryLogService,
        EntryLogRepository entryLogRepository,
        EntryLogQueryService entryLogQueryService
    ) {
        this.entryLogService = entryLogService;
        this.entryLogRepository = entryLogRepository;
        this.entryLogQueryService = entryLogQueryService;
    }

    /**
     * {@code POST  /entry-logs} : Create a new entryLog.
     *
     * @param entryLog the entryLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entryLog, or with status {@code 400 (Bad Request)} if the entryLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EntryLog> createEntryLog(@Valid @RequestBody EntryLog entryLog) throws URISyntaxException {
        log.debug("REST request to save EntryLog : {}", entryLog);
        if (entryLog.getId() != null) {
            throw new BadRequestAlertException("A new entryLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        entryLog = entryLogService.save(entryLog);
        return ResponseEntity.created(new URI("/api/entry-logs/" + entryLog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, entryLog.getId().toString()))
            .body(entryLog);
    }

    /**
     * {@code PUT  /entry-logs/:id} : Updates an existing entryLog.
     *
     * @param id the id of the entryLog to save.
     * @param entryLog the entryLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entryLog,
     * or with status {@code 400 (Bad Request)} if the entryLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entryLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntryLog> updateEntryLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EntryLog entryLog
    ) throws URISyntaxException {
        log.debug("REST request to update EntryLog : {}, {}", id, entryLog);
        if (entryLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entryLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entryLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        entryLog = entryLogService.update(entryLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entryLog.getId().toString()))
            .body(entryLog);
    }

    /**
     * {@code PATCH  /entry-logs/:id} : Partial updates given fields of an existing entryLog, field will ignore if it is null
     *
     * @param id the id of the entryLog to save.
     * @param entryLog the entryLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entryLog,
     * or with status {@code 400 (Bad Request)} if the entryLog is not valid,
     * or with status {@code 404 (Not Found)} if the entryLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the entryLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EntryLog> partialUpdateEntryLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EntryLog entryLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update EntryLog partially : {}, {}", id, entryLog);
        if (entryLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entryLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entryLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EntryLog> result = entryLogService.partialUpdate(entryLog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entryLog.getId().toString())
        );
    }

    /**
     * {@code GET  /entry-logs} : get all the entryLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entryLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EntryLog>> getAllEntryLogs(
        EntryLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EntryLogs by criteria: {}", criteria);

        Page<EntryLog> page = entryLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /entry-logs/count} : count all the entryLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEntryLogs(EntryLogCriteria criteria) {
        log.debug("REST request to count EntryLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(entryLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /entry-logs/:id} : get the "id" entryLog.
     *
     * @param id the id of the entryLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entryLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntryLog> getEntryLog(@PathVariable("id") Long id) {
        log.debug("REST request to get EntryLog : {}", id);
        Optional<EntryLog> entryLog = entryLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entryLog);
    }

    /**
     * {@code DELETE  /entry-logs/:id} : delete the "id" entryLog.
     *
     * @param id the id of the entryLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntryLog(@PathVariable("id") Long id) {
        log.debug("REST request to delete EntryLog : {}", id);
        entryLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
