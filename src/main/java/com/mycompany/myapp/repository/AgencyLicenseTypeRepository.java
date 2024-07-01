package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AgencyLicenseType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgencyLicenseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgencyLicenseTypeRepository extends JpaRepository<AgencyLicenseType, Long> {}
