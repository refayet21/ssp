package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.EntryLog;
import com.mycompany.myapp.repository.EntryLogRepository;
import com.mycompany.myapp.service.criteria.EntryLogCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EntryLog} entities in the database.
 * The main input is a {@link EntryLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EntryLog} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EntryLogQueryService extends QueryService<EntryLog> {

    private final Logger log = LoggerFactory.getLogger(EntryLogQueryService.class);

    private final EntryLogRepository entryLogRepository;

    public EntryLogQueryService(EntryLogRepository entryLogRepository) {
        this.entryLogRepository = entryLogRepository;
    }

    /**
     * Return a {@link Page} of {@link EntryLog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EntryLog> findByCriteria(EntryLogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EntryLog> specification = createSpecification(criteria);
        return entryLogRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EntryLogCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EntryLog> specification = createSpecification(criteria);
        return entryLogRepository.count(specification);
    }

    /**
     * Function to convert {@link EntryLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EntryLog> createSpecification(EntryLogCriteria criteria) {
        Specification<EntryLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EntryLog_.id));
            }
            if (criteria.getEventTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventTime(), EntryLog_.eventTime));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildSpecification(criteria.getDirection(), EntryLog_.direction));
            }
            if (criteria.getPassStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPassStatus(), EntryLog_.passStatus));
            }
            if (criteria.getActionType() != null) {
                specification = specification.and(buildSpecification(criteria.getActionType(), EntryLog_.actionType));
            }
            if (criteria.getPassId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPassId(), root -> root.join(EntryLog_.pass, JoinType.LEFT).get(Pass_.id))
                );
            }
            if (criteria.getLaneId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLaneId(), root -> root.join(EntryLog_.lane, JoinType.LEFT).get(Lane_.id))
                );
            }
        }
        return specification;
    }
}
