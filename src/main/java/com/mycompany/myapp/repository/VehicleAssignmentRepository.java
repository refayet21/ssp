package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.VehicleAssignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleAssignment entity.
 */
@Repository
public interface VehicleAssignmentRepository extends JpaRepository<VehicleAssignment, Long>, JpaSpecificationExecutor<VehicleAssignment> {
    default Optional<VehicleAssignment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<VehicleAssignment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<VehicleAssignment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select vehicleAssignment from VehicleAssignment vehicleAssignment left join fetch vehicleAssignment.agency",
        countQuery = "select count(vehicleAssignment) from VehicleAssignment vehicleAssignment"
    )
    Page<VehicleAssignment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select vehicleAssignment from VehicleAssignment vehicleAssignment left join fetch vehicleAssignment.agency")
    List<VehicleAssignment> findAllWithToOneRelationships();

    @Query(
        "select vehicleAssignment from VehicleAssignment vehicleAssignment left join fetch vehicleAssignment.agency where vehicleAssignment.id =:id"
    )
    Optional<VehicleAssignment> findOneWithToOneRelationships(@Param("id") Long id);
}
