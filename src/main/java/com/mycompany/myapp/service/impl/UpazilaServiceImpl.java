package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Upazila;
import com.mycompany.myapp.repository.UpazilaRepository;
import com.mycompany.myapp.service.UpazilaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Upazila}.
 */
@Service
@Transactional
public class UpazilaServiceImpl implements UpazilaService {

    private final Logger log = LoggerFactory.getLogger(UpazilaServiceImpl.class);

    private final UpazilaRepository upazilaRepository;

    public UpazilaServiceImpl(UpazilaRepository upazilaRepository) {
        this.upazilaRepository = upazilaRepository;
    }

    @Override
    public Upazila save(Upazila upazila) {
        log.debug("Request to save Upazila : {}", upazila);
        return upazilaRepository.save(upazila);
    }

    @Override
    public Upazila update(Upazila upazila) {
        log.debug("Request to update Upazila : {}", upazila);
        return upazilaRepository.save(upazila);
    }

    @Override
    public Optional<Upazila> partialUpdate(Upazila upazila) {
        log.debug("Request to partially update Upazila : {}", upazila);

        return upazilaRepository
            .findById(upazila.getId())
            .map(existingUpazila -> {
                if (upazila.getName() != null) {
                    existingUpazila.setName(upazila.getName());
                }

                return existingUpazila;
            })
            .map(upazilaRepository::save);
    }

    public Page<Upazila> findAllWithEagerRelationships(Pageable pageable) {
        return upazilaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Upazila> findOne(Long id) {
        log.debug("Request to get Upazila : {}", id);
        return upazilaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Upazila : {}", id);
        upazilaRepository.deleteById(id);
    }
}
