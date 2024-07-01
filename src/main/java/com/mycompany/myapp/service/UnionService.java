package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Union;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Union}.
 */
public interface UnionService {
    /**
     * Save a union.
     *
     * @param union the entity to save.
     * @return the persisted entity.
     */
    Union save(Union union);

    /**
     * Updates a union.
     *
     * @param union the entity to update.
     * @return the persisted entity.
     */
    Union update(Union union);

    /**
     * Partially updates a union.
     *
     * @param union the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Union> partialUpdate(Union union);

    /**
     * Get all the unions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Union> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" union.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Union> findOne(Long id);

    /**
     * Delete the "id" union.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
