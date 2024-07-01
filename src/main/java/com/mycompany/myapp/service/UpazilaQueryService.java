package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Upazila;
import com.mycompany.myapp.repository.UpazilaRepository;
import com.mycompany.myapp.service.criteria.UpazilaCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Upazila} entities in the database.
 * The main input is a {@link UpazilaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Upazila} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UpazilaQueryService extends QueryService<Upazila> {

    private final Logger log = LoggerFactory.getLogger(UpazilaQueryService.class);

    private final UpazilaRepository upazilaRepository;

    public UpazilaQueryService(UpazilaRepository upazilaRepository) {
        this.upazilaRepository = upazilaRepository;
    }

    /**
     * Return a {@link List} of {@link Upazila} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Upazila> findByCriteria(UpazilaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Upazila> specification = createSpecification(criteria);
        return upazilaRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UpazilaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Upazila> specification = createSpecification(criteria);
        return upazilaRepository.count(specification);
    }

    /**
     * Function to convert {@link UpazilaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Upazila> createSpecification(UpazilaCriteria criteria) {
        Specification<Upazila> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Upazila_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Upazila_.name));
            }
            if (criteria.getUnionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUnionId(), root -> root.join(Upazila_.unions, JoinType.LEFT).get(Union_.id))
                );
            }
            if (criteria.getCityCorpPouraId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getCityCorpPouraId(),
                        root -> root.join(Upazila_.cityCorpPouras, JoinType.LEFT).get(CityCorpPoura_.id)
                    )
                );
            }
            if (criteria.getDistrictId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDistrictId(), root -> root.join(Upazila_.district, JoinType.LEFT).get(District_.id))
                );
            }
        }
        return specification;
    }
}
