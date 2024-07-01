package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.VehicleAssignment;
import com.mycompany.myapp.repository.VehicleAssignmentRepository;
import com.mycompany.myapp.service.VehicleAssignmentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.VehicleAssignment}.
 */
@Service
@Transactional
public class VehicleAssignmentServiceImpl implements VehicleAssignmentService {

    private final Logger log = LoggerFactory.getLogger(VehicleAssignmentServiceImpl.class);

    private final VehicleAssignmentRepository vehicleAssignmentRepository;

    public VehicleAssignmentServiceImpl(VehicleAssignmentRepository vehicleAssignmentRepository) {
        this.vehicleAssignmentRepository = vehicleAssignmentRepository;
    }

    @Override
    public VehicleAssignment save(VehicleAssignment vehicleAssignment) {
        log.debug("Request to save VehicleAssignment : {}", vehicleAssignment);
        return vehicleAssignmentRepository.save(vehicleAssignment);
    }

    @Override
    public VehicleAssignment update(VehicleAssignment vehicleAssignment) {
        log.debug("Request to update VehicleAssignment : {}", vehicleAssignment);
        return vehicleAssignmentRepository.save(vehicleAssignment);
    }

    @Override
    public Optional<VehicleAssignment> partialUpdate(VehicleAssignment vehicleAssignment) {
        log.debug("Request to partially update VehicleAssignment : {}", vehicleAssignment);

        return vehicleAssignmentRepository
            .findById(vehicleAssignment.getId())
            .map(existingVehicleAssignment -> {
                if (vehicleAssignment.getStartDate() != null) {
                    existingVehicleAssignment.setStartDate(vehicleAssignment.getStartDate());
                }
                if (vehicleAssignment.getEndDate() != null) {
                    existingVehicleAssignment.setEndDate(vehicleAssignment.getEndDate());
                }
                if (vehicleAssignment.getIsPrimary() != null) {
                    existingVehicleAssignment.setIsPrimary(vehicleAssignment.getIsPrimary());
                }
                if (vehicleAssignment.getIsRental() != null) {
                    existingVehicleAssignment.setIsRental(vehicleAssignment.getIsRental());
                }

                return existingVehicleAssignment;
            })
            .map(vehicleAssignmentRepository::save);
    }

    public Page<VehicleAssignment> findAllWithEagerRelationships(Pageable pageable) {
        return vehicleAssignmentRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleAssignment> findOne(Long id) {
        log.debug("Request to get VehicleAssignment : {}", id);
        return vehicleAssignmentRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleAssignment : {}", id);
        vehicleAssignmentRepository.deleteById(id);
    }
}
