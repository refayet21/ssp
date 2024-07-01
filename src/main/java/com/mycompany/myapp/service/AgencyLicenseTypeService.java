package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AgencyLicenseType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AgencyLicenseType}.
 */
public interface AgencyLicenseTypeService {
    /**
     * Save a agencyLicenseType.
     *
     * @param agencyLicenseType the entity to save.
     * @return the persisted entity.
     */
    AgencyLicenseType save(AgencyLicenseType agencyLicenseType);

    /**
     * Updates a agencyLicenseType.
     *
     * @param agencyLicenseType the entity to update.
     * @return the persisted entity.
     */
    AgencyLicenseType update(AgencyLicenseType agencyLicenseType);

    /**
     * Partially updates a agencyLicenseType.
     *
     * @param agencyLicenseType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgencyLicenseType> partialUpdate(AgencyLicenseType agencyLicenseType);

    /**
     * Get all the agencyLicenseTypes.
     *
     * @return the list of entities.
     */
    List<AgencyLicenseType> findAll();

    /**
     * Get the "id" agencyLicenseType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgencyLicenseType> findOne(Long id);

    /**
     * Delete the "id" agencyLicenseType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
