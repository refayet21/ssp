package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Zone;
import com.mycompany.myapp.repository.ZoneRepository;
import com.mycompany.myapp.service.ZoneService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Zone}.
 */
@Service
@Transactional
public class ZoneServiceImpl implements ZoneService {

    private final Logger log = LoggerFactory.getLogger(ZoneServiceImpl.class);

    private final ZoneRepository zoneRepository;

    public ZoneServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Zone save(Zone zone) {
        log.debug("Request to save Zone : {}", zone);
        return zoneRepository.save(zone);
    }

    @Override
    public Zone update(Zone zone) {
        log.debug("Request to update Zone : {}", zone);
        return zoneRepository.save(zone);
    }

    @Override
    public Optional<Zone> partialUpdate(Zone zone) {
        log.debug("Request to partially update Zone : {}", zone);

        return zoneRepository
            .findById(zone.getId())
            .map(existingZone -> {
                if (zone.getName() != null) {
                    existingZone.setName(zone.getName());
                }
                if (zone.getShortName() != null) {
                    existingZone.setShortName(zone.getShortName());
                }
                if (zone.getLocation() != null) {
                    existingZone.setLocation(zone.getLocation());
                }
                if (zone.getIsActive() != null) {
                    existingZone.setIsActive(zone.getIsActive());
                }

                return existingZone;
            })
            .map(zoneRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Zone> findAll() {
        log.debug("Request to get all Zones");
        return zoneRepository.findAll();
    }

    public Page<Zone> findAllWithEagerRelationships(Pageable pageable) {
        return zoneRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Zone> findOne(Long id) {
        log.debug("Request to get Zone : {}", id);
        return zoneRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Zone : {}", id);
        zoneRepository.deleteById(id);
    }
}
