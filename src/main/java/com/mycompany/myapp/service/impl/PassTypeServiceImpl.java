package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.PassType;
import com.mycompany.myapp.repository.PassTypeRepository;
import com.mycompany.myapp.service.PassTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.PassType}.
 */
@Service
@Transactional
public class PassTypeServiceImpl implements PassTypeService {

    private final Logger log = LoggerFactory.getLogger(PassTypeServiceImpl.class);

    private final PassTypeRepository passTypeRepository;

    public PassTypeServiceImpl(PassTypeRepository passTypeRepository) {
        this.passTypeRepository = passTypeRepository;
    }

    @Override
    public PassType save(PassType passType) {
        log.debug("Request to save PassType : {}", passType);
        return passTypeRepository.save(passType);
    }

    @Override
    public PassType update(PassType passType) {
        log.debug("Request to update PassType : {}", passType);
        return passTypeRepository.save(passType);
    }

    @Override
    public Optional<PassType> partialUpdate(PassType passType) {
        log.debug("Request to partially update PassType : {}", passType);

        return passTypeRepository
            .findById(passType.getId())
            .map(existingPassType -> {
                if (passType.getName() != null) {
                    existingPassType.setName(passType.getName());
                }
                if (passType.getShortName() != null) {
                    existingPassType.setShortName(passType.getShortName());
                }
                if (passType.getIsActive() != null) {
                    existingPassType.setIsActive(passType.getIsActive());
                }
                if (passType.getPrintedName() != null) {
                    existingPassType.setPrintedName(passType.getPrintedName());
                }
                if (passType.getIssueFee() != null) {
                    existingPassType.setIssueFee(passType.getIssueFee());
                }
                if (passType.getRenewFee() != null) {
                    existingPassType.setRenewFee(passType.getRenewFee());
                }
                if (passType.getCancelFee() != null) {
                    existingPassType.setCancelFee(passType.getCancelFee());
                }
                if (passType.getMinimumDuration() != null) {
                    existingPassType.setMinimumDuration(passType.getMinimumDuration());
                }
                if (passType.getMaximumDuration() != null) {
                    existingPassType.setMaximumDuration(passType.getMaximumDuration());
                }
                if (passType.getIssueChannelType() != null) {
                    existingPassType.setIssueChannelType(passType.getIssueChannelType());
                }
                if (passType.getTaxCode() != null) {
                    existingPassType.setTaxCode(passType.getTaxCode());
                }
                if (passType.getPassMediaType() != null) {
                    existingPassType.setPassMediaType(passType.getPassMediaType());
                }
                if (passType.getPassUserType() != null) {
                    existingPassType.setPassUserType(passType.getPassUserType());
                }

                return existingPassType;
            })
            .map(passTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PassType> findAll() {
        log.debug("Request to get all PassTypes");
        return passTypeRepository.findAll();
    }

    public Page<PassType> findAllWithEagerRelationships(Pageable pageable) {
        return passTypeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PassType> findOne(Long id) {
        log.debug("Request to get PassType : {}", id);
        return passTypeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PassType : {}", id);
        passTypeRepository.deleteById(id);
    }
}
