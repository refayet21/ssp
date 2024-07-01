package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Agency;
import com.mycompany.myapp.repository.AgencyRepository;
import com.mycompany.myapp.service.criteria.AgencyCriteria;
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
 * Service for executing complex queries for {@link Agency} entities in the database.
 * The main input is a {@link AgencyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Agency} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgencyQueryService extends QueryService<Agency> {

    private final Logger log = LoggerFactory.getLogger(AgencyQueryService.class);

    private final AgencyRepository agencyRepository;

    public AgencyQueryService(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    /**
     * Return a {@link Page} of {@link Agency} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Agency> findByCriteria(AgencyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Agency> specification = createSpecification(criteria);
        return agencyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgencyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Agency> specification = createSpecification(criteria);
        return agencyRepository.count(specification);
    }

    /**
     * Function to convert {@link AgencyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Agency> createSpecification(AgencyCriteria criteria) {
        Specification<Agency> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Agency_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Agency_.name));
            }
            if (criteria.getShortName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortName(), Agency_.shortName));
            }
            if (criteria.getIsInternal() != null) {
                specification = specification.and(buildSpecification(criteria.getIsInternal(), Agency_.isInternal));
            }
            if (criteria.getIsDummy() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDummy(), Agency_.isDummy));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root -> root.join(Agency_.addresses, JoinType.LEFT).get(Address_.id))
                );
            }
            if (criteria.getDocumentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDocumentId(), root -> root.join(Agency_.documents, JoinType.LEFT).get(Document_.id))
                );
            }
            if (criteria.getAssignmentId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getAssignmentId(),
                        root -> root.join(Agency_.assignments, JoinType.LEFT).get(Assignment_.id)
                    )
                );
            }
            if (criteria.getLicenseId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLicenseId(), root -> root.join(Agency_.licenses, JoinType.LEFT).get(AgencyLicense_.id))
                );
            }
            if (criteria.getIssuerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getIssuerId(), root -> root.join(Agency_.issuers, JoinType.LEFT).get(AgencyLicense_.id))
                );
            }
            if (criteria.getDepartmentId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getDepartmentId(),
                        root -> root.join(Agency_.departments, JoinType.LEFT).get(Department_.id)
                    )
                );
            }
            if (criteria.getAgencyTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyTypeId(), root -> root.join(Agency_.agencyType, JoinType.LEFT).get(AgencyType_.id))
                );
            }
            if (criteria.getZoneId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getZoneId(), root -> root.join(Agency_.zones, JoinType.LEFT).get(Zone_.id))
                );
            }
            if (criteria.getPassTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPassTypeId(), root -> root.join(Agency_.passTypes, JoinType.LEFT).get(PassType_.id))
                );
            }
        }
        return specification;
    }
}
