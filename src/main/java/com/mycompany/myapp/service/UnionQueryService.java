package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Union;
import com.mycompany.myapp.repository.UnionRepository;
import com.mycompany.myapp.service.criteria.UnionCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Union} entities in the database.
 * The main input is a {@link UnionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Union} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UnionQueryService extends QueryService<Union> {

    private final Logger log = LoggerFactory.getLogger(UnionQueryService.class);

    private final UnionRepository unionRepository;

    public UnionQueryService(UnionRepository unionRepository) {
        this.unionRepository = unionRepository;
    }

    /**
     * Return a {@link List} of {@link Union} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Union> findByCriteria(UnionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Union> specification = createSpecification(criteria);
        return unionRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UnionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Union> specification = createSpecification(criteria);
        return unionRepository.count(specification);
    }

    /**
     * Function to convert {@link UnionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Union> createSpecification(UnionCriteria criteria) {
        Specification<Union> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Union_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Union_.name));
            }
            if (criteria.getWardId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getWardId(), root -> root.join(Union_.wards, JoinType.LEFT).get(Ward_.id))
                );
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root -> root.join(Union_.addresses, JoinType.LEFT).get(Address_.id))
                );
            }
            if (criteria.getUpazilaId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUpazilaId(), root -> root.join(Union_.upazila, JoinType.LEFT).get(Upazila_.id))
                );
            }
        }
        return specification;
    }
}
