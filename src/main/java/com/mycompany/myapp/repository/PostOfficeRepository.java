package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PostOffice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostOffice entity.
 */
@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Long>, JpaSpecificationExecutor<PostOffice> {
    default Optional<PostOffice> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PostOffice> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PostOffice> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select postOffice from PostOffice postOffice left join fetch postOffice.district",
        countQuery = "select count(postOffice) from PostOffice postOffice"
    )
    Page<PostOffice> findAllWithToOneRelationships(Pageable pageable);

    @Query("select postOffice from PostOffice postOffice left join fetch postOffice.district")
    List<PostOffice> findAllWithToOneRelationships();

    @Query("select postOffice from PostOffice postOffice left join fetch postOffice.district where postOffice.id =:id")
    Optional<PostOffice> findOneWithToOneRelationships(@Param("id") Long id);
}
