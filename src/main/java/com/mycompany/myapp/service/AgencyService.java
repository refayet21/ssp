package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Agency;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Agency}.
 */
public interface AgencyService {
    /**
     * Save a agency.
     *
     * @param agency the entity to save.
     * @return the persisted entity.
     */
    Agency save(Agency agency);

    /**
     * Updates a agency.
     *
     * @param agency the entity to update.
     * @return the persisted entity.
     */
    Agency update(Agency agency);

    /**
     * Partially updates a agency.
     *
     * @param agency the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Agency> partialUpdate(Agency agency);

    /**
     * Get all the agencies with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Agency> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" agency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Agency> findOne(Long id);

    /**
     * Delete the "id" agency.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
