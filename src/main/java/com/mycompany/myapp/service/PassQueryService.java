package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Pass;
import com.mycompany.myapp.repository.PassRepository;
import com.mycompany.myapp.service.criteria.PassCriteria;
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
 * Service for executing complex queries for {@link Pass} entities in the database.
 * The main input is a {@link PassCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Pass} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PassQueryService extends QueryService<Pass> {

    private final Logger log = LoggerFactory.getLogger(PassQueryService.class);

    private final PassRepository passRepository;

    public PassQueryService(PassRepository passRepository) {
        this.passRepository = passRepository;
    }

    /**
     * Return a {@link Page} of {@link Pass} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Pass> findByCriteria(PassCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pass> specification = createSpecification(criteria);
        return passRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PassCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pass> specification = createSpecification(criteria);
        return passRepository.count(specification);
    }

    /**
     * Function to convert {@link PassCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pass> createSpecification(PassCriteria criteria) {
        Specification<Pass> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pass_.id));
            }
            if (criteria.getCollectedFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCollectedFee(), Pass_.collectedFee));
            }
            if (criteria.getFromDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFromDate(), Pass_.fromDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Pass_.endDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Pass_.status));
            }
            if (criteria.getPassNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPassNumber(), Pass_.passNumber));
            }
            if (criteria.getMediaSerial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMediaSerial(), Pass_.mediaSerial));
            }
            if (criteria.getEntryLogId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEntryLogId(), root -> root.join(Pass_.entryLogs, JoinType.LEFT).get(EntryLog_.id))
                );
            }
            if (criteria.getPassTypeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPassTypeId(), root -> root.join(Pass_.passType, JoinType.LEFT).get(PassType_.id))
                );
            }
            if (criteria.getRequestedById() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRequestedById(), root -> root.join(Pass_.requestedBy, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getAssignmentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAssignmentId(), root -> root.join(Pass_.assignment, JoinType.LEFT).get(Assignment_.id))
                );
            }
            if (criteria.getVehicleAssignmentId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getVehicleAssignmentId(),
                        root -> root.join(Pass_.vehicleAssignment, JoinType.LEFT).get(VehicleAssignment_.id)
                    )
                );
            }
        }
        return specification;
    }
}
