package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.VehicleType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.VehicleType}.
 */
public interface VehicleTypeService {
    /**
     * Save a vehicleType.
     *
     * @param vehicleType the entity to save.
     * @return the persisted entity.
     */
    VehicleType save(VehicleType vehicleType);

    /**
     * Updates a vehicleType.
     *
     * @param vehicleType the entity to update.
     * @return the persisted entity.
     */
    VehicleType update(VehicleType vehicleType);

    /**
     * Partially updates a vehicleType.
     *
     * @param vehicleType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleType> partialUpdate(VehicleType vehicleType);

    /**
     * Get all the vehicleTypes.
     *
     * @return the list of entities.
     */
    List<VehicleType> findAll();

    /**
     * Get the "id" vehicleType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleType> findOne(Long id);

    /**
     * Delete the "id" vehicleType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
