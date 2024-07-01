package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UnionCriteriaTest {

    @Test
    void newUnionCriteriaHasAllFiltersNullTest() {
        var unionCriteria = new UnionCriteria();
        assertThat(unionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void unionCriteriaFluentMethodsCreatesFiltersTest() {
        var unionCriteria = new UnionCriteria();

        setAllFilters(unionCriteria);

        assertThat(unionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void unionCriteriaCopyCreatesNullFilterTest() {
        var unionCriteria = new UnionCriteria();
        var copy = unionCriteria.copy();

        assertThat(unionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(unionCriteria)
        );
    }

    @Test
    void unionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var unionCriteria = new UnionCriteria();
        setAllFilters(unionCriteria);

        var copy = unionCriteria.copy();

        assertThat(unionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(unionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var unionCriteria = new UnionCriteria();

        assertThat(unionCriteria).hasToString("UnionCriteria{}");
    }

    private static void setAllFilters(UnionCriteria unionCriteria) {
        unionCriteria.id();
        unionCriteria.name();
        unionCriteria.wardId();
        unionCriteria.addressId();
        unionCriteria.upazilaId();
        unionCriteria.distinct();
    }

    private static Condition<UnionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getWardId()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getUpazilaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UnionCriteria> copyFiltersAre(UnionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getWardId(), copy.getWardId()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getUpazilaId(), copy.getUpazilaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
