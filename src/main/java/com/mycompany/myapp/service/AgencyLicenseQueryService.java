package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.AgencyLicense;
import com.mycompany.myapp.repository.AgencyLicenseRepository;
import com.mycompany.myapp.service.criteria.AgencyLicenseCriteria;
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
 * Service for executing complex queries for {@link AgencyLicense} entities in the database.
 * The main input is a {@link AgencyLicenseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AgencyLicense} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgencyLicenseQueryService extends QueryService<AgencyLicense> {

    private final Logger log = LoggerFactory.getLogger(AgencyLicenseQueryService.class);

    private final AgencyLicenseRepository agencyLicenseRepository;

    public AgencyLicenseQueryService(AgencyLicenseRepository agencyLicenseRepository) {
        this.agencyLicenseRepository = agencyLicenseRepository;
    }

    /**
     * Return a {@link Page} of {@link AgencyLicense} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AgencyLicense> findByCriteria(AgencyLicenseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AgencyLicense> specification = createSpecification(criteria);
        return agencyLicenseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgencyLicenseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AgencyLicense> specification = createSpecification(criteria);
        return agencyLicenseRepository.count(specification);
    }

    /**
     * Function to convert {@link AgencyLicenseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AgencyLicense> createSpecification(AgencyLicenseCriteria criteria) {
        Specification<AgencyLicense> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AgencyLicense_.id));
            }
            if (criteria.getFilePath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilePath(), AgencyLicense_.filePath));
            }
            if (criteria.getSerialNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialNo(), AgencyLicense_.serialNo));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), AgencyLicense_.issueDate));
            }
            if (criteria.getExpiryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiryDate(), AgencyLicense_.expiryDate));
            }
            if (criteria.getVerifiedById() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getVerifiedById(),
                        root -> root.join(AgencyLicense_.verifiedBy, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getAgencyLicenseTypeId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getAgencyLicenseTypeId(),
                        root -> root.join(AgencyLicense_.agencyLicenseType, JoinType.LEFT).get(AgencyLicenseType_.id)
                    )
                );
            }
            if (criteria.getBelongsToId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getBelongsToId(),
                        root -> root.join(AgencyLicense_.belongsTo, JoinType.LEFT).get(Agency_.id)
                    )
                );
            }
            if (criteria.getIssuedById() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getIssuedById(), root -> root.join(AgencyLicense_.issuedBy, JoinType.LEFT).get(Agency_.id))
                );
            }
        }
        return specification;
    }
}
