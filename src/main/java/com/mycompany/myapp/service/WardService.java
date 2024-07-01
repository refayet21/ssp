package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Ward;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Ward}.
 */
public interface WardService {
    /**
     * Save a ward.
     *
     * @param ward the entity to save.
     * @return the persisted entity.
     */
    Ward save(Ward ward);

    /**
     * Updates a ward.
     *
     * @param ward the entity to update.
     * @return the persisted entity.
     */
    Ward update(Ward ward);

    /**
     * Partially updates a ward.
     *
     * @param ward the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ward> partialUpdate(Ward ward);

    /**
     * Get all the wards.
     *
     * @return the list of entities.
     */
    List<Ward> findAll();

    /**
     * Get all the wards with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ward> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" ward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ward> findOne(Long id);

    /**
     * Delete the "id" ward.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
