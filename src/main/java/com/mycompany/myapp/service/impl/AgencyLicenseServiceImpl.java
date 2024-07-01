package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AgencyLicense;
import com.mycompany.myapp.repository.AgencyLicenseRepository;
import com.mycompany.myapp.service.AgencyLicenseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AgencyLicense}.
 */
@Service
@Transactional
public class AgencyLicenseServiceImpl implements AgencyLicenseService {

    private final Logger log = LoggerFactory.getLogger(AgencyLicenseServiceImpl.class);

    private final AgencyLicenseRepository agencyLicenseRepository;

    public AgencyLicenseServiceImpl(AgencyLicenseRepository agencyLicenseRepository) {
        this.agencyLicenseRepository = agencyLicenseRepository;
    }

    @Override
    public AgencyLicense save(AgencyLicense agencyLicense) {
        log.debug("Request to save AgencyLicense : {}", agencyLicense);
        return agencyLicenseRepository.save(agencyLicense);
    }

    @Override
    public AgencyLicense update(AgencyLicense agencyLicense) {
        log.debug("Request to update AgencyLicense : {}", agencyLicense);
        return agencyLicenseRepository.save(agencyLicense);
    }

    @Override
    public Optional<AgencyLicense> partialUpdate(AgencyLicense agencyLicense) {
        log.debug("Request to partially update AgencyLicense : {}", agencyLicense);

        return agencyLicenseRepository
            .findById(agencyLicense.getId())
            .map(existingAgencyLicense -> {
                if (agencyLicense.getFilePath() != null) {
                    existingAgencyLicense.setFilePath(agencyLicense.getFilePath());
                }
                if (agencyLicense.getSerialNo() != null) {
                    existingAgencyLicense.setSerialNo(agencyLicense.getSerialNo());
                }
                if (agencyLicense.getIssueDate() != null) {
                    existingAgencyLicense.setIssueDate(agencyLicense.getIssueDate());
                }
                if (agencyLicense.getExpiryDate() != null) {
                    existingAgencyLicense.setExpiryDate(agencyLicense.getExpiryDate());
                }

                return existingAgencyLicense;
            })
            .map(agencyLicenseRepository::save);
    }

    public Page<AgencyLicense> findAllWithEagerRelationships(Pageable pageable) {
        return agencyLicenseRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgencyLicense> findOne(Long id) {
        log.debug("Request to get AgencyLicense : {}", id);
        return agencyLicenseRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgencyLicense : {}", id);
        agencyLicenseRepository.deleteById(id);
    }
}
