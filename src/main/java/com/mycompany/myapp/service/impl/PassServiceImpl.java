package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Pass;
import com.mycompany.myapp.repository.PassRepository;
import com.mycompany.myapp.service.PassService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Pass}.
 */
@Service
@Transactional
public class PassServiceImpl implements PassService {

    private final Logger log = LoggerFactory.getLogger(PassServiceImpl.class);

    private final PassRepository passRepository;

    public PassServiceImpl(PassRepository passRepository) {
        this.passRepository = passRepository;
    }

    @Override
    public Pass save(Pass pass) {
        log.debug("Request to save Pass : {}", pass);
        return passRepository.save(pass);
    }

    @Override
    public Pass update(Pass pass) {
        log.debug("Request to update Pass : {}", pass);
        return passRepository.save(pass);
    }

    @Override
    public Optional<Pass> partialUpdate(Pass pass) {
        log.debug("Request to partially update Pass : {}", pass);

        return passRepository
            .findById(pass.getId())
            .map(existingPass -> {
                if (pass.getCollectedFee() != null) {
                    existingPass.setCollectedFee(pass.getCollectedFee());
                }
                if (pass.getFromDate() != null) {
                    existingPass.setFromDate(pass.getFromDate());
                }
                if (pass.getEndDate() != null) {
                    existingPass.setEndDate(pass.getEndDate());
                }
                if (pass.getStatus() != null) {
                    existingPass.setStatus(pass.getStatus());
                }
                if (pass.getPassNumber() != null) {
                    existingPass.setPassNumber(pass.getPassNumber());
                }
                if (pass.getMediaSerial() != null) {
                    existingPass.setMediaSerial(pass.getMediaSerial());
                }

                return existingPass;
            })
            .map(passRepository::save);
    }

    public Page<Pass> findAllWithEagerRelationships(Pageable pageable) {
        return passRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pass> findOne(Long id) {
        log.debug("Request to get Pass : {}", id);
        return passRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pass : {}", id);
        passRepository.deleteById(id);
    }
}
