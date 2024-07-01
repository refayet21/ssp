package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.EntryLog;
import com.mycompany.myapp.repository.EntryLogRepository;
import com.mycompany.myapp.service.EntryLogService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.EntryLog}.
 */
@Service
@Transactional
public class EntryLogServiceImpl implements EntryLogService {

    private final Logger log = LoggerFactory.getLogger(EntryLogServiceImpl.class);

    private final EntryLogRepository entryLogRepository;

    public EntryLogServiceImpl(EntryLogRepository entryLogRepository) {
        this.entryLogRepository = entryLogRepository;
    }

    @Override
    public EntryLog save(EntryLog entryLog) {
        log.debug("Request to save EntryLog : {}", entryLog);
        return entryLogRepository.save(entryLog);
    }

    @Override
    public EntryLog update(EntryLog entryLog) {
        log.debug("Request to update EntryLog : {}", entryLog);
        return entryLogRepository.save(entryLog);
    }

    @Override
    public Optional<EntryLog> partialUpdate(EntryLog entryLog) {
        log.debug("Request to partially update EntryLog : {}", entryLog);

        return entryLogRepository
            .findById(entryLog.getId())
            .map(existingEntryLog -> {
                if (entryLog.getEventTime() != null) {
                    existingEntryLog.setEventTime(entryLog.getEventTime());
                }
                if (entryLog.getDirection() != null) {
                    existingEntryLog.setDirection(entryLog.getDirection());
                }
                if (entryLog.getPassStatus() != null) {
                    existingEntryLog.setPassStatus(entryLog.getPassStatus());
                }
                if (entryLog.getActionType() != null) {
                    existingEntryLog.setActionType(entryLog.getActionType());
                }

                return existingEntryLog;
            })
            .map(entryLogRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EntryLog> findOne(Long id) {
        log.debug("Request to get EntryLog : {}", id);
        return entryLogRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EntryLog : {}", id);
        entryLogRepository.deleteById(id);
    }
}
