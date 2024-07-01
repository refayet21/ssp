package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Upazila;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Upazila}.
 */
public interface UpazilaService {
    /**
     * Save a upazila.
     *
     * @param upazila the entity to save.
     * @return the persisted entity.
     */
    Upazila save(Upazila upazila);

    /**
     * Updates a upazila.
     *
     * @param upazila the entity to update.
     * @return the persisted entity.
     */
    Upazila update(Upazila upazila);

    /**
     * Partially updates a upazila.
     *
     * @param upazila the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Upazila> partialUpdate(Upazila upazila);

    /**
     * Get all the upazilas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Upazila> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" upazila.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Upazila> findOne(Long id);

    /**
     * Delete the "id" upazila.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
