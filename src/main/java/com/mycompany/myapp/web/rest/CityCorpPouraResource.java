package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CityCorpPoura;
import com.mycompany.myapp.repository.CityCorpPouraRepository;
import com.mycompany.myapp.service.CityCorpPouraService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CityCorpPoura}.
 */
@RestController
@RequestMapping("/api/city-corp-pouras")
public class CityCorpPouraResource {

    private final Logger log = LoggerFactory.getLogger(CityCorpPouraResource.class);

    private static final String ENTITY_NAME = "cityCorpPoura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CityCorpPouraService cityCorpPouraService;

    private final CityCorpPouraRepository cityCorpPouraRepository;

    public CityCorpPouraResource(CityCorpPouraService cityCorpPouraService, CityCorpPouraRepository cityCorpPouraRepository) {
        this.cityCorpPouraService = cityCorpPouraService;
        this.cityCorpPouraRepository = cityCorpPouraRepository;
    }

    /**
     * {@code POST  /city-corp-pouras} : Create a new cityCorpPoura.
     *
     * @param cityCorpPoura the cityCorpPoura to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cityCorpPoura, or with status {@code 400 (Bad Request)} if the cityCorpPoura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CityCorpPoura> createCityCorpPoura(@Valid @RequestBody CityCorpPoura cityCorpPoura) throws URISyntaxException {
        log.debug("REST request to save CityCorpPoura : {}", cityCorpPoura);
        if (cityCorpPoura.getId() != null) {
            throw new BadRequestAlertException("A new cityCorpPoura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cityCorpPoura = cityCorpPouraService.save(cityCorpPoura);
        return ResponseEntity.created(new URI("/api/city-corp-pouras/" + cityCorpPoura.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cityCorpPoura.getId().toString()))
            .body(cityCorpPoura);
    }

    /**
     * {@code PUT  /city-corp-pouras/:id} : Updates an existing cityCorpPoura.
     *
     * @param id the id of the cityCorpPoura to save.
     * @param cityCorpPoura the cityCorpPoura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityCorpPoura,
     * or with status {@code 400 (Bad Request)} if the cityCorpPoura is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cityCorpPoura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CityCorpPoura> updateCityCorpPoura(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CityCorpPoura cityCorpPoura
    ) throws URISyntaxException {
        log.debug("REST request to update CityCorpPoura : {}, {}", id, cityCorpPoura);
        if (cityCorpPoura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cityCorpPoura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityCorpPouraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cityCorpPoura = cityCorpPouraService.update(cityCorpPoura);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cityCorpPoura.getId().toString()))
            .body(cityCorpPoura);
    }

    /**
     * {@code PATCH  /city-corp-pouras/:id} : Partial updates given fields of an existing cityCorpPoura, field will ignore if it is null
     *
     * @param id the id of the cityCorpPoura to save.
     * @param cityCorpPoura the cityCorpPoura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityCorpPoura,
     * or with status {@code 400 (Bad Request)} if the cityCorpPoura is not valid,
     * or with status {@code 404 (Not Found)} if the cityCorpPoura is not found,
     * or with status {@code 500 (Internal Server Error)} if the cityCorpPoura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CityCorpPoura> partialUpdateCityCorpPoura(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CityCorpPoura cityCorpPoura
    ) throws URISyntaxException {
        log.debug("REST request to partial update CityCorpPoura partially : {}, {}", id, cityCorpPoura);
        if (cityCorpPoura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cityCorpPoura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityCorpPouraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CityCorpPoura> result = cityCorpPouraService.partialUpdate(cityCorpPoura);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cityCorpPoura.getId().toString())
        );
    }

    /**
     * {@code GET  /city-corp-pouras} : get all the cityCorpPouras.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cityCorpPouras in body.
     */
    @GetMapping("")
    public List<CityCorpPoura> getAllCityCorpPouras(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all CityCorpPouras");
        return cityCorpPouraService.findAll();
    }

    /**
     * {@code GET  /city-corp-pouras/:id} : get the "id" cityCorpPoura.
     *
     * @param id the id of the cityCorpPoura to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cityCorpPoura, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CityCorpPoura> getCityCorpPoura(@PathVariable("id") Long id) {
        log.debug("REST request to get CityCorpPoura : {}", id);
        Optional<CityCorpPoura> cityCorpPoura = cityCorpPouraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cityCorpPoura);
    }

    /**
     * {@code DELETE  /city-corp-pouras/:id} : delete the "id" cityCorpPoura.
     *
     * @param id the id of the cityCorpPoura to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCityCorpPoura(@PathVariable("id") Long id) {
        log.debug("REST request to delete CityCorpPoura : {}", id);
        cityCorpPouraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
