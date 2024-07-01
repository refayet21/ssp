package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AccessProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AccessProfileRepositoryWithBagRelationships {
    Optional<AccessProfile> fetchBagRelationships(Optional<AccessProfile> accessProfile);

    List<AccessProfile> fetchBagRelationships(List<AccessProfile> accessProfiles);

    Page<AccessProfile> fetchBagRelationships(Page<AccessProfile> accessProfiles);
}
