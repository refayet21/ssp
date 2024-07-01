package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AgencyLicense;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AgencyLicense}.
 */
public interface AgencyLicenseService {
    /**
     * Save a agencyLicense.
     *
     * @param agencyLicense the entity to save.
     * @return the persisted entity.
     */
    AgencyLicense save(AgencyLicense agencyLicense);

    /**
     * Updates a agencyLicense.
     *
     * @param agencyLicense the entity to update.
     * @return the persisted entity.
     */
    AgencyLicense update(AgencyLicense agencyLicense);

    /**
     * Partially updates a agencyLicense.
     *
     * @param agencyLicense the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgencyLicense> partialUpdate(AgencyLicense agencyLicense);

    /**
     * Get all the agencyLicenses with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgencyLicense> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" agencyLicense.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgencyLicense> findOne(Long id);

    /**
     * Delete the "id" agencyLicense.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
