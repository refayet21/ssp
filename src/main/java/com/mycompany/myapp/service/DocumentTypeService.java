package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.DocumentType;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.DocumentType}.
 */
public interface DocumentTypeService {
    /**
     * Save a documentType.
     *
     * @param documentType the entity to save.
     * @return the persisted entity.
     */
    DocumentType save(DocumentType documentType);

    /**
     * Updates a documentType.
     *
     * @param documentType the entity to update.
     * @return the persisted entity.
     */
    DocumentType update(DocumentType documentType);

    /**
     * Partially updates a documentType.
     *
     * @param documentType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentType> partialUpdate(DocumentType documentType);

    /**
     * Get the "id" documentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentType> findOne(Long id);

    /**
     * Delete the "id" documentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
