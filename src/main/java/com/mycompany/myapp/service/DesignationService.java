package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Designation;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Designation}.
 */
public interface DesignationService {
    /**
     * Save a designation.
     *
     * @param designation the entity to save.
     * @return the persisted entity.
     */
    Designation save(Designation designation);

    /**
     * Updates a designation.
     *
     * @param designation the entity to update.
     * @return the persisted entity.
     */
    Designation update(Designation designation);

    /**
     * Partially updates a designation.
     *
     * @param designation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Designation> partialUpdate(Designation designation);

    /**
     * Get all the designations.
     *
     * @return the list of entities.
     */
    List<Designation> findAll();

    /**
     * Get the "id" designation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Designation> findOne(Long id);

    /**
     * Delete the "id" designation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
