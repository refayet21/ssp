package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Division;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Division}.
 */
public interface DivisionService {
    /**
     * Save a division.
     *
     * @param division the entity to save.
     * @return the persisted entity.
     */
    Division save(Division division);

    /**
     * Updates a division.
     *
     * @param division the entity to update.
     * @return the persisted entity.
     */
    Division update(Division division);

    /**
     * Partially updates a division.
     *
     * @param division the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Division> partialUpdate(Division division);

    /**
     * Get all the divisions.
     *
     * @return the list of entities.
     */
    List<Division> findAll();

    /**
     * Get the "id" division.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Division> findOne(Long id);

    /**
     * Delete the "id" division.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
