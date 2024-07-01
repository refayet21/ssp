package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AccessProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AccessProfileRepositoryWithBagRelationshipsImpl implements AccessProfileRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ACCESSPROFILES_PARAMETER = "accessProfiles";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AccessProfile> fetchBagRelationships(Optional<AccessProfile> accessProfile) {
        return accessProfile.map(this::fetchLanes);
    }

    @Override
    public Page<AccessProfile> fetchBagRelationships(Page<AccessProfile> accessProfiles) {
        return new PageImpl<>(
            fetchBagRelationships(accessProfiles.getContent()),
            accessProfiles.getPageable(),
            accessProfiles.getTotalElements()
        );
    }

    @Override
    public List<AccessProfile> fetchBagRelationships(List<AccessProfile> accessProfiles) {
        return Optional.of(accessProfiles).map(this::fetchLanes).orElse(Collections.emptyList());
    }

    AccessProfile fetchLanes(AccessProfile result) {
        return entityManager
            .createQuery(
                "select accessProfile from AccessProfile accessProfile left join fetch accessProfile.lanes where accessProfile.id = :id",
                AccessProfile.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AccessProfile> fetchLanes(List<AccessProfile> accessProfiles) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, accessProfiles.size()).forEach(index -> order.put(accessProfiles.get(index).getId(), index));
        List<AccessProfile> result = entityManager
            .createQuery(
                "select accessProfile from AccessProfile accessProfile left join fetch accessProfile.lanes where accessProfile in :accessProfiles",
                AccessProfile.class
            )
            .setParameter(ACCESSPROFILES_PARAMETER, accessProfiles)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
