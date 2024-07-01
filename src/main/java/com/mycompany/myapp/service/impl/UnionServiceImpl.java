package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Union;
import com.mycompany.myapp.repository.UnionRepository;
import com.mycompany.myapp.service.UnionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Union}.
 */
@Service
@Transactional
public class UnionServiceImpl implements UnionService {

    private final Logger log = LoggerFactory.getLogger(UnionServiceImpl.class);

    private final UnionRepository unionRepository;

    public UnionServiceImpl(UnionRepository unionRepository) {
        this.unionRepository = unionRepository;
    }

    @Override
    public Union save(Union union) {
        log.debug("Request to save Union : {}", union);
        return unionRepository.save(union);
    }

    @Override
    public Union update(Union union) {
        log.debug("Request to update Union : {}", union);
        return unionRepository.save(union);
    }

    @Override
    public Optional<Union> partialUpdate(Union union) {
        log.debug("Request to partially update Union : {}", union);

        return unionRepository
            .findById(union.getId())
            .map(existingUnion -> {
                if (union.getName() != null) {
                    existingUnion.setName(union.getName());
                }

                return existingUnion;
            })
            .map(unionRepository::save);
    }

    public Page<Union> findAllWithEagerRelationships(Pageable pageable) {
        return unionRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Union> findOne(Long id) {
        log.debug("Request to get Union : {}", id);
        return unionRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Union : {}", id);
        unionRepository.deleteById(id);
    }
}
