package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.PostOffice;
import com.mycompany.myapp.repository.PostOfficeRepository;
import com.mycompany.myapp.service.PostOfficeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.PostOffice}.
 */
@Service
@Transactional
public class PostOfficeServiceImpl implements PostOfficeService {

    private final Logger log = LoggerFactory.getLogger(PostOfficeServiceImpl.class);

    private final PostOfficeRepository postOfficeRepository;

    public PostOfficeServiceImpl(PostOfficeRepository postOfficeRepository) {
        this.postOfficeRepository = postOfficeRepository;
    }

    @Override
    public PostOffice save(PostOffice postOffice) {
        log.debug("Request to save PostOffice : {}", postOffice);
        return postOfficeRepository.save(postOffice);
    }

    @Override
    public PostOffice update(PostOffice postOffice) {
        log.debug("Request to update PostOffice : {}", postOffice);
        return postOfficeRepository.save(postOffice);
    }

    @Override
    public Optional<PostOffice> partialUpdate(PostOffice postOffice) {
        log.debug("Request to partially update PostOffice : {}", postOffice);

        return postOfficeRepository
            .findById(postOffice.getId())
            .map(existingPostOffice -> {
                if (postOffice.getName() != null) {
                    existingPostOffice.setName(postOffice.getName());
                }
                if (postOffice.getCode() != null) {
                    existingPostOffice.setCode(postOffice.getCode());
                }

                return existingPostOffice;
            })
            .map(postOfficeRepository::save);
    }

    public Page<PostOffice> findAllWithEagerRelationships(Pageable pageable) {
        return postOfficeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostOffice> findOne(Long id) {
        log.debug("Request to get PostOffice : {}", id);
        return postOfficeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostOffice : {}", id);
        postOfficeRepository.deleteById(id);
    }
}
