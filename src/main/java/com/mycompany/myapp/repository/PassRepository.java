package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pass;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pass entity.
 */
@Repository
public interface PassRepository extends JpaRepository<Pass, Long>, JpaSpecificationExecutor<Pass> {
    @Query("select pass from Pass pass where pass.requestedBy.login = ?#{authentication.name}")
    List<Pass> findByRequestedByIsCurrentUser();

    default Optional<Pass> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Pass> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Pass> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pass from Pass pass left join fetch pass.passType left join fetch pass.requestedBy",
        countQuery = "select count(pass) from Pass pass"
    )
    Page<Pass> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pass from Pass pass left join fetch pass.passType left join fetch pass.requestedBy")
    List<Pass> findAllWithToOneRelationships();

    @Query("select pass from Pass pass left join fetch pass.passType left join fetch pass.requestedBy where pass.id =:id")
    Optional<Pass> findOneWithToOneRelationships(@Param("id") Long id);
}
