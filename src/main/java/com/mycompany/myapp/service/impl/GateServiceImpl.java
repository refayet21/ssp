package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Gate;
import com.mycompany.myapp.repository.GateRepository;
import com.mycompany.myapp.service.GateService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Gate}.
 */
@Service
@Transactional
public class GateServiceImpl implements GateService {

    private final Logger log = LoggerFactory.getLogger(GateServiceImpl.class);

    private final GateRepository gateRepository;

    public GateServiceImpl(GateRepository gateRepository) {
        this.gateRepository = gateRepository;
    }

    @Override
    public Gate save(Gate gate) {
        log.debug("Request to save Gate : {}", gate);
        return gateRepository.save(gate);
    }

    @Override
    public Gate update(Gate gate) {
        log.debug("Request to update Gate : {}", gate);
        return gateRepository.save(gate);
    }

    @Override
    public Optional<Gate> partialUpdate(Gate gate) {
        log.debug("Request to partially update Gate : {}", gate);

        return gateRepository
            .findById(gate.getId())
            .map(existingGate -> {
                if (gate.getName() != null) {
                    existingGate.setName(gate.getName());
                }
                if (gate.getShortName() != null) {
                    existingGate.setShortName(gate.getShortName());
                }
                if (gate.getLat() != null) {
                    existingGate.setLat(gate.getLat());
                }
                if (gate.getLon() != null) {
                    existingGate.setLon(gate.getLon());
                }
                if (gate.getGateType() != null) {
                    existingGate.setGateType(gate.getGateType());
                }
                if (gate.getIsActive() != null) {
                    existingGate.setIsActive(gate.getIsActive());
                }

                return existingGate;
            })
            .map(gateRepository::save);
    }

    public Page<Gate> findAllWithEagerRelationships(Pageable pageable) {
        return gateRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Gate> findOne(Long id) {
        log.debug("Request to get Gate : {}", id);
        return gateRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gate : {}", id);
        gateRepository.deleteById(id);
    }
}
