package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Agency;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Agency entity.
 */
@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long>, JpaSpecificationExecutor<Agency> {
    default Optional<Agency> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Agency> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Agency> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select agency from Agency agency left join fetch agency.agencyType",
        countQuery = "select count(agency) from Agency agency"
    )
    Page<Agency> findAllWithToOneRelationships(Pageable pageable);

    @Query("select agency from Agency agency left join fetch agency.agencyType")
    List<Agency> findAllWithToOneRelationships();

    @Query("select agency from Agency agency left join fetch agency.agencyType where agency.id =:id")
    Optional<Agency> findOneWithToOneRelationships(@Param("id") Long id);
}
