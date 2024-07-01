package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Union;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Union entity.
 */
@Repository
public interface UnionRepository extends JpaRepository<Union, Long>, JpaSpecificationExecutor<Union> {
    default Optional<Union> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Union> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Union> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select jhiUnion from Union jhiUnion left join fetch jhiUnion.upazila",
        countQuery = "select count(jhiUnion) from Union jhiUnion"
    )
    Page<Union> findAllWithToOneRelationships(Pageable pageable);

    @Query("select jhiUnion from Union jhiUnion left join fetch jhiUnion.upazila")
    List<Union> findAllWithToOneRelationships();

    @Query("select jhiUnion from Union jhiUnion left join fetch jhiUnion.upazila where jhiUnion.id =:id")
    Optional<Union> findOneWithToOneRelationships(@Param("id") Long id);
}
