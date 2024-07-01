package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.RMO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.RMO}.
 */
public interface RMOService {
    /**
     * Save a rMO.
     *
     * @param rMO the entity to save.
     * @return the persisted entity.
     */
    RMO save(RMO rMO);

    /**
     * Updates a rMO.
     *
     * @param rMO the entity to update.
     * @return the persisted entity.
     */
    RMO update(RMO rMO);

    /**
     * Partially updates a rMO.
     *
     * @param rMO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RMO> partialUpdate(RMO rMO);

    /**
     * Get all the rMOS.
     *
     * @return the list of entities.
     */
    List<RMO> findAll();

    /**
     * Get the "id" rMO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RMO> findOne(Long id);

    /**
     * Delete the "id" rMO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
