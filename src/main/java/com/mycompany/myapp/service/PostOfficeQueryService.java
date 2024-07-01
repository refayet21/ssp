package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.PostOffice;
import com.mycompany.myapp.repository.PostOfficeRepository;
import com.mycompany.myapp.service.criteria.PostOfficeCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PostOffice} entities in the database.
 * The main input is a {@link PostOfficeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PostOffice} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PostOfficeQueryService extends QueryService<PostOffice> {

    private final Logger log = LoggerFactory.getLogger(PostOfficeQueryService.class);

    private final PostOfficeRepository postOfficeRepository;

    public PostOfficeQueryService(PostOfficeRepository postOfficeRepository) {
        this.postOfficeRepository = postOfficeRepository;
    }

    /**
     * Return a {@link List} of {@link PostOffice} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PostOffice> findByCriteria(PostOfficeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PostOffice> specification = createSpecification(criteria);
        return postOfficeRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PostOfficeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PostOffice> specification = createSpecification(criteria);
        return postOfficeRepository.count(specification);
    }

    /**
     * Function to convert {@link PostOfficeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PostOffice> createSpecification(PostOfficeCriteria criteria) {
        Specification<PostOffice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PostOffice_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PostOffice_.name));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), PostOffice_.code));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root -> root.join(PostOffice_.addresses, JoinType.LEFT).get(Address_.id))
                );
            }
            if (criteria.getDistrictId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDistrictId(), root -> root.join(PostOffice_.district, JoinType.LEFT).get(District_.id))
                );
            }
        }
        return specification;
    }
}
