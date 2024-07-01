package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Gate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Gate entity.
 */
@Repository
public interface GateRepository extends JpaRepository<Gate, Long>, JpaSpecificationExecutor<Gate> {
    default Optional<Gate> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Gate> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Gate> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select gate from Gate gate left join fetch gate.zone", countQuery = "select count(gate) from Gate gate")
    Page<Gate> findAllWithToOneRelationships(Pageable pageable);

    @Query("select gate from Gate gate left join fetch gate.zone")
    List<Gate> findAllWithToOneRelationships();

    @Query("select gate from Gate gate left join fetch gate.zone where gate.id =:id")
    Optional<Gate> findOneWithToOneRelationships(@Param("id") Long id);
}
