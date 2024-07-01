package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CityCorpPoura;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CityCorpPoura entity.
 */
@Repository
public interface CityCorpPouraRepository extends JpaRepository<CityCorpPoura, Long> {
    default Optional<CityCorpPoura> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CityCorpPoura> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CityCorpPoura> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cityCorpPoura from CityCorpPoura cityCorpPoura left join fetch cityCorpPoura.district left join fetch cityCorpPoura.upazila left join fetch cityCorpPoura.rmo",
        countQuery = "select count(cityCorpPoura) from CityCorpPoura cityCorpPoura"
    )
    Page<CityCorpPoura> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cityCorpPoura from CityCorpPoura cityCorpPoura left join fetch cityCorpPoura.district left join fetch cityCorpPoura.upazila left join fetch cityCorpPoura.rmo"
    )
    List<CityCorpPoura> findAllWithToOneRelationships();

    @Query(
        "select cityCorpPoura from CityCorpPoura cityCorpPoura left join fetch cityCorpPoura.district left join fetch cityCorpPoura.upazila left join fetch cityCorpPoura.rmo where cityCorpPoura.id =:id"
    )
    Optional<CityCorpPoura> findOneWithToOneRelationships(@Param("id") Long id);
}
