package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.VehicleType;
import com.mycompany.myapp.repository.VehicleTypeRepository;
import com.mycompany.myapp.service.VehicleTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.VehicleType}.
 */
@Service
@Transactional
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final Logger log = LoggerFactory.getLogger(VehicleTypeServiceImpl.class);

    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeServiceImpl(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @Override
    public VehicleType save(VehicleType vehicleType) {
        log.debug("Request to save VehicleType : {}", vehicleType);
        return vehicleTypeRepository.save(vehicleType);
    }

    @Override
    public VehicleType update(VehicleType vehicleType) {
        log.debug("Request to update VehicleType : {}", vehicleType);
        return vehicleTypeRepository.save(vehicleType);
    }

    @Override
    public Optional<VehicleType> partialUpdate(VehicleType vehicleType) {
        log.debug("Request to partially update VehicleType : {}", vehicleType);

        return vehicleTypeRepository
            .findById(vehicleType.getId())
            .map(existingVehicleType -> {
                if (vehicleType.getName() != null) {
                    existingVehicleType.setName(vehicleType.getName());
                }
                if (vehicleType.getNumberOfOperators() != null) {
                    existingVehicleType.setNumberOfOperators(vehicleType.getNumberOfOperators());
                }

                return existingVehicleType;
            })
            .map(vehicleTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleType> findAll() {
        log.debug("Request to get all VehicleTypes");
        return vehicleTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleType> findOne(Long id) {
        log.debug("Request to get VehicleType : {}", id);
        return vehicleTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleType : {}", id);
        vehicleTypeRepository.deleteById(id);
    }
}
