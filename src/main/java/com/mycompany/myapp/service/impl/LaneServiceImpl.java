package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Lane;
import com.mycompany.myapp.repository.LaneRepository;
import com.mycompany.myapp.service.LaneService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Lane}.
 */
@Service
@Transactional
public class LaneServiceImpl implements LaneService {

    private final Logger log = LoggerFactory.getLogger(LaneServiceImpl.class);

    private final LaneRepository laneRepository;

    public LaneServiceImpl(LaneRepository laneRepository) {
        this.laneRepository = laneRepository;
    }

    @Override
    public Lane save(Lane lane) {
        log.debug("Request to save Lane : {}", lane);
        return laneRepository.save(lane);
    }

    @Override
    public Lane update(Lane lane) {
        log.debug("Request to update Lane : {}", lane);
        return laneRepository.save(lane);
    }

    @Override
    public Optional<Lane> partialUpdate(Lane lane) {
        log.debug("Request to partially update Lane : {}", lane);

        return laneRepository
            .findById(lane.getId())
            .map(existingLane -> {
                if (lane.getName() != null) {
                    existingLane.setName(lane.getName());
                }
                if (lane.getShortName() != null) {
                    existingLane.setShortName(lane.getShortName());
                }
                if (lane.getDirection() != null) {
                    existingLane.setDirection(lane.getDirection());
                }
                if (lane.getIsActive() != null) {
                    existingLane.setIsActive(lane.getIsActive());
                }

                return existingLane;
            })
            .map(laneRepository::save);
    }

    public Page<Lane> findAllWithEagerRelationships(Pageable pageable) {
        return laneRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lane> findOne(Long id) {
        log.debug("Request to get Lane : {}", id);
        return laneRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lane : {}", id);
        laneRepository.deleteById(id);
    }
}
