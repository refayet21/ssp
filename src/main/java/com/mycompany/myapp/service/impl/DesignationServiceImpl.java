package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Designation;
import com.mycompany.myapp.repository.DesignationRepository;
import com.mycompany.myapp.service.DesignationService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Designation}.
 */
@Service
@Transactional
public class DesignationServiceImpl implements DesignationService {

    private final Logger log = LoggerFactory.getLogger(DesignationServiceImpl.class);

    private final DesignationRepository designationRepository;

    public DesignationServiceImpl(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    @Override
    public Designation save(Designation designation) {
        log.debug("Request to save Designation : {}", designation);
        return designationRepository.save(designation);
    }

    @Override
    public Designation update(Designation designation) {
        log.debug("Request to update Designation : {}", designation);
        return designationRepository.save(designation);
    }

    @Override
    public Optional<Designation> partialUpdate(Designation designation) {
        log.debug("Request to partially update Designation : {}", designation);

        return designationRepository
            .findById(designation.getId())
            .map(existingDesignation -> {
                if (designation.getName() != null) {
                    existingDesignation.setName(designation.getName());
                }
                if (designation.getShortName() != null) {
                    existingDesignation.setShortName(designation.getShortName());
                }
                if (designation.getIsActive() != null) {
                    existingDesignation.setIsActive(designation.getIsActive());
                }

                return existingDesignation;
            })
            .map(designationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Designation> findAll() {
        log.debug("Request to get all Designations");
        return designationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Designation> findOne(Long id) {
        log.debug("Request to get Designation : {}", id);
        return designationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Designation : {}", id);
        designationRepository.deleteById(id);
    }
}
