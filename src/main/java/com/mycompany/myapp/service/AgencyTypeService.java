package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AgencyType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AgencyType}.
 */
public interface AgencyTypeService {
    /**
     * Save a agencyType.
     *
     * @param agencyType the entity to save.
     * @return the persisted entity.
     */
    AgencyType save(AgencyType agencyType);

    /**
     * Updates a agencyType.
     *
     * @param agencyType the entity to update.
     * @return the persisted entity.
     */
    AgencyType update(AgencyType agencyType);

    /**
     * Partially updates a agencyType.
     *
     * @param agencyType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgencyType> partialUpdate(AgencyType agencyType);

    /**
     * Get all the agencyTypes.
     *
     * @return the list of entities.
     */
    List<AgencyType> findAll();

    /**
     * Get the "id" agencyType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgencyType> findOne(Long id);

    /**
     * Delete the "id" agencyType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
