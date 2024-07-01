package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Vehicle;
import com.mycompany.myapp.repository.VehicleRepository;
import com.mycompany.myapp.service.criteria.VehicleCriteria;
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
 * Service for executing complex queries for {@link Vehicle} entities in the database.
 * The main input is a {@link VehicleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Vehicle} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehicleQueryService extends QueryService<Vehicle> {

    private final Logger log = LoggerFactory.getLogger(VehicleQueryService.class);

    private final VehicleRepository vehicleRepository;

    public VehicleQueryService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Return a {@link Page} of {@link Vehicle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Vehicle> findByCriteria(VehicleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehicleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.count(specification);
    }

    /**
     * Function to convert {@link VehicleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vehicle> createSpecification(VehicleCriteria criteria) {
        Specification<Vehicle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vehicle_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Vehicle_.name));
            }
            if (criteria.getRegNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegNo(), Vehicle_.regNo));
            }
            if (criteria.getZone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZone(), Vehicle_.zone));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), Vehicle_.category));
            }
            if (criteria.getSerialNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialNo(), Vehicle_.serialNo));
            }
            if (criteria.getVehicleNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVehicleNo(), Vehicle_.vehicleNo));
            }
            if (criteria.getChasisNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChasisNo(), Vehicle_.chasisNo));
            }
            if (criteria.getIsPersonal() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPersonal(), Vehicle_.isPersonal));
            }
            if (criteria.getIsBlackListed() != null) {
                specification = specification.and(buildSpecification(criteria.getIsBlackListed(), Vehicle_.isBlackListed));
            }
            if (criteria.getLogStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getLogStatus(), Vehicle_.logStatus));
            }
            if (criteria.getDocumentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDocumentId(), root -> root.join(Vehicle_.documents, JoinType.LEFT).get(Document_.id))
                );
            }
            if (criteria.getVehicleAssigmentId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getVehicleAssigmentId(),
                        root -> root.join(Vehicle_.vehicleAssigments, JoinType.LEFT).get(VehicleAssignment_.id)
                    )
                );
            }
            if (criteria.getVehicleTypeId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getVehicleTypeId(),
                        root -> root.join(Vehicle_.vehicleType, JoinType.LEFT).get(VehicleType_.id)
                    )
                );
            }
        }
        return specification;
    }
}
