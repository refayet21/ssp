package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Pass;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Pass}.
 */
public interface PassService {
    /**
     * Save a pass.
     *
     * @param pass the entity to save.
     * @return the persisted entity.
     */
    Pass save(Pass pass);

    /**
     * Updates a pass.
     *
     * @param pass the entity to update.
     * @return the persisted entity.
     */
    Pass update(Pass pass);

    /**
     * Partially updates a pass.
     *
     * @param pass the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pass> partialUpdate(Pass pass);

    /**
     * Get all the passes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pass> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pass.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pass> findOne(Long id);

    /**
     * Delete the "id" pass.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
