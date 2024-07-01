package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Gate;
import com.mycompany.myapp.repository.GateRepository;
import com.mycompany.myapp.service.criteria.GateCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Gate} entities in the database.
 * The main input is a {@link GateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Gate} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GateQueryService extends QueryService<Gate> {

    private final Logger log = LoggerFactory.getLogger(GateQueryService.class);

    private final GateRepository gateRepository;

    public GateQueryService(GateRepository gateRepository) {
        this.gateRepository = gateRepository;
    }

    /**
     * Return a {@link List} of {@link Gate} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Gate> findByCriteria(GateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Gate> specification = createSpecification(criteria);
        return gateRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Gate> specification = createSpecification(criteria);
        return gateRepository.count(specification);
    }

    /**
     * Function to convert {@link GateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Gate> createSpecification(GateCriteria criteria) {
        Specification<Gate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Gate_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Gate_.name));
            }
            if (criteria.getShortName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortName(), Gate_.shortName));
            }
            if (criteria.getLat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLat(), Gate_.lat));
            }
            if (criteria.getLon() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLon(), Gate_.lon));
            }
            if (criteria.getGateType() != null) {
                specification = specification.and(buildSpecification(criteria.getGateType(), Gate_.gateType));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Gate_.isActive));
            }
            if (criteria.getLaneId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLaneId(), root -> root.join(Gate_.lanes, JoinType.LEFT).get(Lane_.id))
                );
            }
            if (criteria.getZoneId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getZoneId(), root -> root.join(Gate_.zone, JoinType.LEFT).get(Zone_.id))
                );
            }
        }
        return specification;
    }
}
