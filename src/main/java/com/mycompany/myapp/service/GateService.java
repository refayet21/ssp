package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Gate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Gate}.
 */
public interface GateService {
    /**
     * Save a gate.
     *
     * @param gate the entity to save.
     * @return the persisted entity.
     */
    Gate save(Gate gate);

    /**
     * Updates a gate.
     *
     * @param gate the entity to update.
     * @return the persisted entity.
     */
    Gate update(Gate gate);

    /**
     * Partially updates a gate.
     *
     * @param gate the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Gate> partialUpdate(Gate gate);

    /**
     * Get all the gates with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Gate> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" gate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Gate> findOne(Long id);

    /**
     * Delete the "id" gate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
