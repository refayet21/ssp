package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AgencyType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgencyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgencyTypeRepository extends JpaRepository<AgencyType, Long> {}
