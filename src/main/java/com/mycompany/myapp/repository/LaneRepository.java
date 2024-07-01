package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lane;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Lane entity.
 */
@Repository
public interface LaneRepository extends JpaRepository<Lane, Long>, JpaSpecificationExecutor<Lane> {
    default Optional<Lane> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Lane> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Lane> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select lane from Lane lane left join fetch lane.gate", countQuery = "select count(lane) from Lane lane")
    Page<Lane> findAllWithToOneRelationships(Pageable pageable);

    @Query("select lane from Lane lane left join fetch lane.gate")
    List<Lane> findAllWithToOneRelationships();

    @Query("select lane from Lane lane left join fetch lane.gate where lane.id =:id")
    Optional<Lane> findOneWithToOneRelationships(@Param("id") Long id);
}
