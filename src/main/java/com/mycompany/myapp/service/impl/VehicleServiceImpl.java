package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.service.VehicleService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Vehicle}.
 */
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final Logger log = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        log.debug("Request to save Vehicle : {}", vehicle);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        log.debug("Request to update Vehicle : {}", vehicle);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> partialUpdate(Vehicle vehicle) {
        log.debug("Request to partially update Vehicle : {}", vehicle);

        return vehicleRepository
            .findById(vehicle.getId())
            .map(existingVehicle -> {
                if (vehicle.getName() != null) {
                    existingVehicle.setName(vehicle.getName());
                }
                if (vehicle.getRegNo() != null) {
                    existingVehicle.setRegNo(vehicle.getRegNo());
                }
                if (vehicle.getZone() != null) {
                    existingVehicle.setZone(vehicle.getZone());
                }
                if (vehicle.getCategory() != null) {
                    existingVehicle.setCategory(vehicle.getCategory());
                }
                if (vehicle.getSerialNo() != null) {
                    existingVehicle.setSerialNo(vehicle.getSerialNo());
                }
                if (vehicle.getVehicleNo() != null) {
                    existingVehicle.setVehicleNo(vehicle.getVehicleNo());
                }
                if (vehicle.getChasisNo() != null) {
                    existingVehicle.setChasisNo(vehicle.getChasisNo());
                }
                if (vehicle.getIsPersonal() != null) {
                    existingVehicle.setIsPersonal(vehicle.getIsPersonal());
                }
                if (vehicle.getIsBlackListed() != null) {
                    existingVehicle.setIsBlackListed(vehicle.getIsBlackListed());
                }
                if (vehicle.getLogStatus() != null) {
                    existingVehicle.setLogStatus(vehicle.getLogStatus());
                }

                return existingVehicle;
            })
            .map(vehicleRepository::save);
    }

    public Page<Vehicle> findAllWithEagerRelationships(Pageable pageable) {
        return vehicleRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicle> findOne(Long id) {
        log.debug("Request to get Vehicle : {}", id);
        return vehicleRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vehicle : {}", id);
        vehicleRepository.deleteById(id);
    }
}
