package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Lane;
import com.mycompany.myapp.repository.LaneRepository;
import com.mycompany.myapp.service.criteria.LaneCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Lane} entities in the database.
 * The main input is a {@link LaneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Lane} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LaneQueryService extends QueryService<Lane> {

    private final Logger log = LoggerFactory.getLogger(LaneQueryService.class);

    private final LaneRepository laneRepository;

    public LaneQueryService(LaneRepository laneRepository) {
        this.laneRepository = laneRepository;
    }

    /**
     * Return a {@link List} of {@link Lane} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Lane> findByCriteria(LaneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lane> specification = createSpecification(criteria);
        return laneRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LaneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Lane> specification = createSpecification(criteria);
        return laneRepository.count(specification);
    }

    /**
     * Function to convert {@link LaneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lane> createSpecification(LaneCriteria criteria) {
        Specification<Lane> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Lane_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Lane_.name));
            }
            if (criteria.getShortName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortName(), Lane_.shortName));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildSpecification(criteria.getDirection(), Lane_.direction));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Lane_.isActive));
            }
            if (criteria.getEntryLogId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEntryLogId(), root -> root.join(Lane_.entryLogs, JoinType.LEFT).get(EntryLog_.id))
                );
            }
            if (criteria.getGateId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGateId(), root -> root.join(Lane_.gate, JoinType.LEFT).get(Gate_.id))
                );
            }
            if (criteria.getAccessProfileId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getAccessProfileId(),
                        root -> root.join(Lane_.accessProfiles, JoinType.LEFT).get(AccessProfile_.id)
                    )
                );
            }
        }
        return specification;
    }
}
