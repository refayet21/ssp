package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AgencyType;
import com.mycompany.myapp.repository.AgencyTypeRepository;
import com.mycompany.myapp.service.AgencyTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AgencyType}.
 */
@Service
@Transactional
public class AgencyTypeServiceImpl implements AgencyTypeService {

    private final Logger log = LoggerFactory.getLogger(AgencyTypeServiceImpl.class);

    private final AgencyTypeRepository agencyTypeRepository;

    public AgencyTypeServiceImpl(AgencyTypeRepository agencyTypeRepository) {
        this.agencyTypeRepository = agencyTypeRepository;
    }

    @Override
    public AgencyType save(AgencyType agencyType) {
        log.debug("Request to save AgencyType : {}", agencyType);
        return agencyTypeRepository.save(agencyType);
    }

    @Override
    public AgencyType update(AgencyType agencyType) {
        log.debug("Request to update AgencyType : {}", agencyType);
        return agencyTypeRepository.save(agencyType);
    }

    @Override
    public Optional<AgencyType> partialUpdate(AgencyType agencyType) {
        log.debug("Request to partially update AgencyType : {}", agencyType);

        return agencyTypeRepository
            .findById(agencyType.getId())
            .map(existingAgencyType -> {
                if (agencyType.getName() != null) {
                    existingAgencyType.setName(agencyType.getName());
                }
                if (agencyType.getShortName() != null) {
                    existingAgencyType.setShortName(agencyType.getShortName());
                }
                if (agencyType.getIsActive() != null) {
                    existingAgencyType.setIsActive(agencyType.getIsActive());
                }

                return existingAgencyType;
            })
            .map(agencyTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgencyType> findAll() {
        log.debug("Request to get all AgencyTypes");
        return agencyTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AgencyType> findOne(Long id) {
        log.debug("Request to get AgencyType : {}", id);
        return agencyTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AgencyType : {}", id);
        agencyTypeRepository.deleteById(id);
    }
}
