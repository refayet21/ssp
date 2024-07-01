package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AgencyLicenseType;
import com.mycompany.myapp.repository.AgencyLicenseTypeRepository;
import com.mycompany.myapp.service.AgencyLicenseTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AgencyLicenseType}.
 */
@Service
@Transactional
public class AgencyLicenseTypeServiceImpl implements AgencyLicenseTypeService {

    private final Logger log = LoggerFactory.getLogger(AgencyLicenseTypeServiceImpl.class);

    private final AgencyLicenseTypeRepository agencyLicenseTypeRepository;

    public AgencyLicenseTypeServiceImpl(AgencyLicenseTypeRepository agencyLicenseTypeRepository) {
        this.agencyLicenseTypeRepository = agencyLicenseTypeRepository;
    }

    @Override
    public AgencyLicenseType save(AgencyLicenseType agencyLicenseType) {
        log.debug("Request to save AgencyLicenseType : {}", agencyLicenseType);
        return agencyLicenseTypeRepository.save(agencyLicenseType);
    }

    @Override
    public AgencyLicenseType update(AgencyLicenseType agencyLicenseType) {
        log.debug("Request to update AgencyLicenseType : {}", agencyLicenseType);
        return agencyLicenseTypeRepository.save(agencyLicenseType);
    }

    @Override
    public Optional<AgencyLicenseType> partialUpdate(AgencyLicenseType agencyLicenseType) {
        log.debug("Request to partially update AgencyLicenseType : {}", agencyLicenseType);

        return agencyLicenseTypeRepository
            .findById(agencyLicenseType.getId())
            .map(existingAgencyLicenseType -> {
                if (agencyLicenseType.getName() != null) {
                    existingAgencyLicenseType.setName(agencyLicenseType.getName());
                }
                if (agencyLicenseType.getIsActive() != null) {
                    existingAgencyLicenseType.setIsActive(agencyLicenseType.getIsActive());
                }

                return existingAgencyLicenseType;
            })
            .map(agencyLicenseTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgencyLicenseType> findAll() {
        log.debug("Request to get all AgencyLicenseTypes");
        return agencyLicenseTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgencyLicenseType> findOne(Long id) {
        log.debug("Request to get AgencyLicenseType : {}", id);
        return agencyLicenseTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgencyLicenseType : {}", id);
        agencyLicenseTypeRepository.deleteById(id);
    }
}
