package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AccessProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.AccessProfile}.
 */
public interface AccessProfileService {
    /**
     * Save a accessProfile.
     *
     * @param accessProfile the entity to save.
     * @return the persisted entity.
     */
    AccessProfile save(AccessProfile accessProfile);

    /**
     * Updates a accessProfile.
     *
     * @param accessProfile the entity to update.
     * @return the persisted entity.
     */
    AccessProfile update(AccessProfile accessProfile);

    /**
     * Partially updates a accessProfile.
     *
     * @param accessProfile the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccessProfile> partialUpdate(AccessProfile accessProfile);

    /**
     * Get all the accessProfiles.
     *
     * @return the list of entities.
     */
    List<AccessProfile> findAll();

    /**
     * Get all the accessProfiles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccessProfile> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" accessProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccessProfile> findOne(Long id);

    /**
     * Delete the "id" accessProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
