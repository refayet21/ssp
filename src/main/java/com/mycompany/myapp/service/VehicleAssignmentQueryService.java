package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.VehicleAssignment;
import com.mycompany.myapp.repository.VehicleAssignmentRepository;
import com.mycompany.myapp.service.criteria.VehicleAssignmentCriteria;
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
 * Service for executing complex queries for {@link VehicleAssignment} entities in the database.
 * The main input is a {@link VehicleAssignmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link VehicleAssignment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehicleAssignmentQueryService extends QueryService<VehicleAssignment> {

    private final Logger log = LoggerFactory.getLogger(VehicleAssignmentQueryService.class);

    private final VehicleAssignmentRepository vehicleAssignmentRepository;

    public VehicleAssignmentQueryService(VehicleAssignmentRepository vehicleAssignmentRepository) {
        this.vehicleAssignmentRepository = vehicleAssignmentRepository;
    }

    /**
     * Return a {@link Page} of {@link VehicleAssignment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleAssignment> findByCriteria(VehicleAssignmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VehicleAssignment> specification = createSpecification(criteria);
        return vehicleAssignmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehicleAssignmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VehicleAssignment> specification = createSpecification(criteria);
        return vehicleAssignmentRepository.count(specification);
    }

    /**
     * Function to convert {@link VehicleAssignmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VehicleAssignment> createSpecification(VehicleAssignmentCriteria criteria) {
        Specification<VehicleAssignment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VehicleAssignment_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), VehicleAssignment_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), VehicleAssignment_.endDate));
            }
            if (criteria.getIsPrimary() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPrimary(), VehicleAssignment_.isPrimary));
            }
            if (criteria.getIsRental() != null) {
                specification = specification.and(buildSpecification(criteria.getIsRental(), VehicleAssignment_.isRental));
            }
            if (criteria.getPassId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPassId(), root -> root.join(VehicleAssignment_.passes, JoinType.LEFT).get(Pass_.id))
                );
            }
            if (criteria.getAgencyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyId(), root -> root.join(VehicleAssignment_.agency, JoinType.LEFT).get(Agency_.id))
                );
            }
            if (criteria.getVehicleId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getVehicleId(),
                        root -> root.join(VehicleAssignment_.vehicle, JoinType.LEFT).get(Vehicle_.id)
                    )
                );
            }
        }
        return specification;
    }
}
