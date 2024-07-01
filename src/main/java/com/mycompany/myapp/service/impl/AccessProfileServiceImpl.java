package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AccessProfile;
import com.mycompany.myapp.repository.AccessProfileRepository;
import com.mycompany.myapp.service.AccessProfileService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AccessProfile}.
 */
@Service
@Transactional
public class AccessProfileServiceImpl implements AccessProfileService {

    private final Logger log = LoggerFactory.getLogger(AccessProfileServiceImpl.class);

    private final AccessProfileRepository accessProfileRepository;

    public AccessProfileServiceImpl(AccessProfileRepository accessProfileRepository) {
        this.accessProfileRepository = accessProfileRepository;
    }

    @Override
    public AccessProfile save(AccessProfile accessProfile) {
        log.debug("Request to save AccessProfile : {}", accessProfile);
        return accessProfileRepository.save(accessProfile);
    }

    @Override
    public AccessProfile update(AccessProfile accessProfile) {
        log.debug("Request to update AccessProfile : {}", accessProfile);
        return accessProfileRepository.save(accessProfile);
    }

    @Override
    public Optional<AccessProfile> partialUpdate(AccessProfile accessProfile) {
        log.debug("Request to partially update AccessProfile : {}", accessProfile);

        return accessProfileRepository
            .findById(accessProfile.getId())
            .map(existingAccessProfile -> {
                if (accessProfile.getName() != null) {
                    existingAccessProfile.setName(accessProfile.getName());
                }
                if (accessProfile.getDescription() != null) {
                    existingAccessProfile.setDescription(accessProfile.getDescription());
                }
                if (accessProfile.getStartTimeOfDay() != null) {
                    existingAccessProfile.setStartTimeOfDay(accessProfile.getStartTimeOfDay());
                }
                if (accessProfile.getEndTimeOfDay() != null) {
                    existingAccessProfile.setEndTimeOfDay(accessProfile.getEndTimeOfDay());
                }
                if (accessProfile.getDayOfWeek() != null) {
                    existingAccessProfile.setDayOfWeek(accessProfile.getDayOfWeek());
                }
                if (accessProfile.getAction() != null) {
                    existingAccessProfile.setAction(accessProfile.getAction());
                }

                return existingAccessProfile;
            })
            .map(accessProfileRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessProfile> findAll() {
        log.debug("Request to get all AccessProfiles");
        return accessProfileRepository.findAll();
    }

    public Page<AccessProfile> findAllWithEagerRelationships(Pageable pageable) {
        return accessProfileRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccessProfile> findOne(Long id) {
        log.debug("Request to get AccessProfile : {}", id);
        return accessProfileRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccessProfile : {}", id);
        accessProfileRepository.deleteById(id);
    }
}
