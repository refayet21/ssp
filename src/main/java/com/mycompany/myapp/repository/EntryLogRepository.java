package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EntryLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EntryLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntryLogRepository extends JpaRepository<EntryLog, Long>, JpaSpecificationExecutor<EntryLog> {}
