package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EntryLog;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.EntryLog}.
 */
public interface EntryLogService {
    /**
     * Save a entryLog.
     *
     * @param entryLog the entity to save.
     * @return the persisted entity.
     */
    EntryLog save(EntryLog entryLog);

    /**
     * Updates a entryLog.
     *
     * @param entryLog the entity to update.
     * @return the persisted entity.
     */
    EntryLog update(EntryLog entryLog);

    /**
     * Partially updates a entryLog.
     *
     * @param entryLog the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EntryLog> partialUpdate(EntryLog entryLog);

    /**
     * Get the "id" entryLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EntryLog> findOne(Long id);

    /**
     * Delete the "id" entryLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
