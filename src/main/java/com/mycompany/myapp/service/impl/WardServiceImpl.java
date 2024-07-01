package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Ward;
import com.mycompany.myapp.repository.WardRepository;
import com.mycompany.myapp.service.WardService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Ward}.
 */
@Service
@Transactional
public class WardServiceImpl implements WardService {

    private final Logger log = LoggerFactory.getLogger(WardServiceImpl.class);

    private final WardRepository wardRepository;

    public WardServiceImpl(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    @Override
    public Ward save(Ward ward) {
        log.debug("Request to save Ward : {}", ward);
        return wardRepository.save(ward);
    }

    @Override
    public Ward update(Ward ward) {
        log.debug("Request to update Ward : {}", ward);
        return wardRepository.save(ward);
    }

    @Override
    public Optional<Ward> partialUpdate(Ward ward) {
        log.debug("Request to partially update Ward : {}", ward);

        return wardRepository
            .findById(ward.getId())
            .map(existingWard -> {
                if (ward.getName() != null) {
                    existingWard.setName(ward.getName());
                }

                return existingWard;
            })
            .map(wardRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ward> findAll() {
        log.debug("Request to get all Wards");
        return wardRepository.findAll();
    }

    public Page<Ward> findAllWithEagerRelationships(Pageable pageable) {
        return wardRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ward> findOne(Long id) {
        log.debug("Request to get Ward : {}", id);
        return wardRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ward : {}", id);
        wardRepository.deleteById(id);
    }
}
