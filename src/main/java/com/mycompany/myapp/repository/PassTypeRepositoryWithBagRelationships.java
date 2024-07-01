package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PassType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PassTypeRepositoryWithBagRelationships {
    Optional<PassType> fetchBagRelationships(Optional<PassType> passType);

    List<PassType> fetchBagRelationships(List<PassType> passTypes);

    Page<PassType> fetchBagRelationships(Page<PassType> passTypes);
}
