package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ward;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ward entity.
 */
@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    default Optional<Ward> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ward> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ward> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select ward from Ward ward left join fetch ward.cityCorpPoura left join fetch ward.union",
        countQuery = "select count(ward) from Ward ward"
    )
    Page<Ward> findAllWithToOneRelationships(Pageable pageable);

    @Query("select ward from Ward ward left join fetch ward.cityCorpPoura left join fetch ward.union")
    List<Ward> findAllWithToOneRelationships();

    @Query("select ward from Ward ward left join fetch ward.cityCorpPoura left join fetch ward.union where ward.id =:id")
    Optional<Ward> findOneWithToOneRelationships(@Param("id") Long id);
}
