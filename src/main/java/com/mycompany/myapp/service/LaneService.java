package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Lane;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Lane}.
 */
public interface LaneService {
    /**
     * Save a lane.
     *
     * @param lane the entity to save.
     * @return the persisted entity.
     */
    Lane save(Lane lane);

    /**
     * Updates a lane.
     *
     * @param lane the entity to update.
     * @return the persisted entity.
     */
    Lane update(Lane lane);

    /**
     * Partially updates a lane.
     *
     * @param lane the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Lane> partialUpdate(Lane lane);

    /**
     * Get all the lanes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Lane> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" lane.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Lane> findOne(Long id);

    /**
     * Delete the "id" lane.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
