package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RMO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RMO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RMORepository extends JpaRepository<RMO, Long> {}
