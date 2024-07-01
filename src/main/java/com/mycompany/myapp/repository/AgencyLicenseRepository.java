package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AgencyLicense;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgencyLicense entity.
 */
@Repository
public interface AgencyLicenseRepository extends JpaRepository<AgencyLicense, Long>, JpaSpecificationExecutor<AgencyLicense> {
    @Query("select agencyLicense from AgencyLicense agencyLicense where agencyLicense.verifiedBy.login = ?#{authentication.name}")
    List<AgencyLicense> findByVerifiedByIsCurrentUser();

    default Optional<AgencyLicense> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AgencyLicense> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AgencyLicense> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select agencyLicense from AgencyLicense agencyLicense left join fetch agencyLicense.verifiedBy",
        countQuery = "select count(agencyLicense) from AgencyLicense agencyLicense"
    )
    Page<AgencyLicense> findAllWithToOneRelationships(Pageable pageable);

    @Query("select agencyLicense from AgencyLicense agencyLicense left join fetch agencyLicense.verifiedBy")
    List<AgencyLicense> findAllWithToOneRelationships();

    @Query("select agencyLicense from AgencyLicense agencyLicense left join fetch agencyLicense.verifiedBy where agencyLicense.id =:id")
    Optional<AgencyLicense> findOneWithToOneRelationships(@Param("id") Long id);
}
