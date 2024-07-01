package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PassType;
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
public class PassTypeRepositoryWithBagRelationshipsImpl implements PassTypeRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PASSTYPES_PARAMETER = "passTypes";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PassType> fetchBagRelationships(Optional<PassType> passType) {
        return passType.map(this::fetchAgencies);
    }

    @Override
    public Page<PassType> fetchBagRelationships(Page<PassType> passTypes) {
        return new PageImpl<>(fetchBagRelationships(passTypes.getContent()), passTypes.getPageable(), passTypes.getTotalElements());
    }

    @Override
    public List<PassType> fetchBagRelationships(List<PassType> passTypes) {
        return Optional.of(passTypes).map(this::fetchAgencies).orElse(Collections.emptyList());
    }

    PassType fetchAgencies(PassType result) {
        return entityManager
            .createQuery("select passType from PassType passType left join fetch passType.agencies where passType.id = :id", PassType.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<PassType> fetchAgencies(List<PassType> passTypes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, passTypes.size()).forEach(index -> order.put(passTypes.get(index).getId(), index));
        List<PassType> result = entityManager
            .createQuery(
                "select passType from PassType passType left join fetch passType.agencies where passType in :passTypes",
                PassType.class
            )
            .setParameter(PASSTYPES_PARAMETER, passTypes)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
