package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Zone;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Zone}.
 */
public interface ZoneService {
    /**
     * Save a zone.
     *
     * @param zone the entity to save.
     * @return the persisted entity.
     */
    Zone save(Zone zone);

    /**
     * Updates a zone.
     *
     * @param zone the entity to update.
     * @return the persisted entity.
     */
    Zone update(Zone zone);

    /**
     * Partially updates a zone.
     *
     * @param zone the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Zone> partialUpdate(Zone zone);

    /**
     * Get all the zones.
     *
     * @return the list of entities.
     */
    List<Zone> findAll();

    /**
     * Get all the zones with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Zone> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" zone.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Zone> findOne(Long id);

    /**
     * Delete the "id" zone.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
