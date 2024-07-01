package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PostOffice;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.PostOffice}.
 */
public interface PostOfficeService {
    /**
     * Save a postOffice.
     *
     * @param postOffice the entity to save.
     * @return the persisted entity.
     */
    PostOffice save(PostOffice postOffice);

    /**
     * Updates a postOffice.
     *
     * @param postOffice the entity to update.
     * @return the persisted entity.
     */
    PostOffice update(PostOffice postOffice);

    /**
     * Partially updates a postOffice.
     *
     * @param postOffice the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostOffice> partialUpdate(PostOffice postOffice);

    /**
     * Get all the postOffices with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostOffice> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" postOffice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostOffice> findOne(Long id);

    /**
     * Delete the "id" postOffice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
