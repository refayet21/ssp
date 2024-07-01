package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Assignment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Assignment}.
 */
public interface AssignmentService {
    /**
     * Save a assignment.
     *
     * @param assignment the entity to save.
     * @return the persisted entity.
     */
    Assignment save(Assignment assignment);

    /**
     * Updates a assignment.
     *
     * @param assignment the entity to update.
     * @return the persisted entity.
     */
    Assignment update(Assignment assignment);

    /**
     * Partially updates a assignment.
     *
     * @param assignment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Assignment> partialUpdate(Assignment assignment);

    /**
     * Get all the assignments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Assignment> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" assignment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Assignment> findOne(Long id);

    /**
     * Delete the "id" assignment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
