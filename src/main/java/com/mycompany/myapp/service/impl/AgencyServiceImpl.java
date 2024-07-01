package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Agency;
import com.mycompany.myapp.repository.AgencyRepository;
import com.mycompany.myapp.service.AgencyService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Agency}.
 */
@Service
@Transactional
public class AgencyServiceImpl implements AgencyService {

    private final Logger log = LoggerFactory.getLogger(AgencyServiceImpl.class);

    private final AgencyRepository agencyRepository;

    public AgencyServiceImpl(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Override
    public Agency save(Agency agency) {
        log.debug("Request to save Agency : {}", agency);
        return agencyRepository.save(agency);
    }

    @Override
    public Agency update(Agency agency) {
        log.debug("Request to update Agency : {}", agency);
        return agencyRepository.save(agency);
    }

    @Override
    public Optional<Agency> partialUpdate(Agency agency) {
        log.debug("Request to partially update Agency : {}", agency);

        return agencyRepository
            .findById(agency.getId())
            .map(existingAgency -> {
                if (agency.getName() != null) {
                    existingAgency.setName(agency.getName());
                }
                if (agency.getShortName() != null) {
                    existingAgency.setShortName(agency.getShortName());
                }
                if (agency.getIsInternal() != null) {
                    existingAgency.setIsInternal(agency.getIsInternal());
                }
                if (agency.getIsDummy() != null) {
                    existingAgency.setIsDummy(agency.getIsDummy());
                }

                return existingAgency;
            })
            .map(agencyRepository::save);
    }

    public Page<Agency> findAllWithEagerRelationships(Pageable pageable) {
        return agencyRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Agency> findOne(Long id) {
        log.debug("Request to get Agency : {}", id);
        return agencyRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Agency : {}", id);
        agencyRepository.deleteById(id);
    }
}
