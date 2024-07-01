package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.RMO;
import com.mycompany.myapp.repository.RMORepository;
import com.mycompany.myapp.service.RMOService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.RMO}.
 */
@Service
@Transactional
public class RMOServiceImpl implements RMOService {

    private final Logger log = LoggerFactory.getLogger(RMOServiceImpl.class);

    private final RMORepository rMORepository;

    public RMOServiceImpl(RMORepository rMORepository) {
        this.rMORepository = rMORepository;
    }

    @Override
    public RMO save(RMO rMO) {
        log.debug("Request to save RMO : {}", rMO);
        return rMORepository.save(rMO);
    }

    @Override
    public RMO update(RMO rMO) {
        log.debug("Request to update RMO : {}", rMO);
        return rMORepository.save(rMO);
    }

    @Override
    public Optional<RMO> partialUpdate(RMO rMO) {
        log.debug("Request to partially update RMO : {}", rMO);

        return rMORepository
            .findById(rMO.getId())
            .map(existingRMO -> {
                if (rMO.getName() != null) {
                    existingRMO.setName(rMO.getName());
                }
                if (rMO.getCode() != null) {
                    existingRMO.setCode(rMO.getCode());
                }

                return existingRMO;
            })
            .map(rMORepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RMO> findAll() {
        log.debug("Request to get all RMOS");
        return rMORepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RMO> findOne(Long id) {
        log.debug("Request to get RMO : {}", id);
        return rMORepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RMO : {}", id);
        rMORepository.deleteById(id);
    }
}
