package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PassType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.PassType}.
 */
public interface PassTypeService {
    /**
     * Save a passType.
     *
     * @param passType the entity to save.
     * @return the persisted entity.
     */
    PassType save(PassType passType);

    /**
     * Updates a passType.
     *
     * @param passType the entity to update.
     * @return the persisted entity.
     */
    PassType update(PassType passType);

    /**
     * Partially updates a passType.
     *
     * @param passType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PassType> partialUpdate(PassType passType);

    /**
     * Get all the passTypes.
     *
     * @return the list of entities.
     */
    List<PassType> findAll();

    /**
     * Get all the passTypes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PassType> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" passType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PassType> findOne(Long id);

    /**
     * Delete the "id" passType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
