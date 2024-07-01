package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CityCorpPoura;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CityCorpPoura}.
 */
public interface CityCorpPouraService {
    /**
     * Save a cityCorpPoura.
     *
     * @param cityCorpPoura the entity to save.
     * @return the persisted entity.
     */
    CityCorpPoura save(CityCorpPoura cityCorpPoura);

    /**
     * Updates a cityCorpPoura.
     *
     * @param cityCorpPoura the entity to update.
     * @return the persisted entity.
     */
    CityCorpPoura update(CityCorpPoura cityCorpPoura);

    /**
     * Partially updates a cityCorpPoura.
     *
     * @param cityCorpPoura the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CityCorpPoura> partialUpdate(CityCorpPoura cityCorpPoura);

    /**
     * Get all the cityCorpPouras.
     *
     * @return the list of entities.
     */
    List<CityCorpPoura> findAll();

    /**
     * Get all the cityCorpPouras with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CityCorpPoura> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cityCorpPoura.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CityCorpPoura> findOne(Long id);

    /**
     * Delete the "id" cityCorpPoura.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
