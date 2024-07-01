package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.VehicleAssignment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.VehicleAssignment}.
 */
public interface VehicleAssignmentService {
    /**
     * Save a vehicleAssignment.
     *
     * @param vehicleAssignment the entity to save.
     * @return the persisted entity.
     */
    VehicleAssignment save(VehicleAssignment vehicleAssignment);

    /**
     * Updates a vehicleAssignment.
     *
     * @param vehicleAssignment the entity to update.
     * @return the persisted entity.
     */
    VehicleAssignment update(VehicleAssignment vehicleAssignment);

    /**
     * Partially updates a vehicleAssignment.
     *
     * @param vehicleAssignment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleAssignment> partialUpdate(VehicleAssignment vehicleAssignment);

    /**
     * Get all the vehicleAssignments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleAssignment> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" vehicleAssignment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleAssignment> findOne(Long id);

    /**
     * Delete the "id" vehicleAssignment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
