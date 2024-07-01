package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Vehicle;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Vehicle}.
 */
public interface VehicleService {
    /**
     * Save a vehicle.
     *
     * @param vehicle the entity to save.
     * @return the persisted entity.
     */
    Vehicle save(Vehicle vehicle);

    /**
     * Updates a vehicle.
     *
     * @param vehicle the entity to update.
     * @return the persisted entity.
     */
    Vehicle update(Vehicle vehicle);

    /**
     * Partially updates a vehicle.
     *
     * @param vehicle the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Vehicle> partialUpdate(Vehicle vehicle);

    /**
     * Get all the vehicles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Vehicle> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" vehicle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vehicle> findOne(Long id);

    /**
     * Delete the "id" vehicle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
