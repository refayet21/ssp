package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UpazilaCriteriaTest {

    @Test
    void newUpazilaCriteriaHasAllFiltersNullTest() {
        var upazilaCriteria = new UpazilaCriteria();
        assertThat(upazilaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void upazilaCriteriaFluentMethodsCreatesFiltersTest() {
        var upazilaCriteria = new UpazilaCriteria();

        setAllFilters(upazilaCriteria);

        assertThat(upazilaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void upazilaCriteriaCopyCreatesNullFilterTest() {
        var upazilaCriteria = new UpazilaCriteria();
        var copy = upazilaCriteria.copy();

        assertThat(upazilaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(upazilaCriteria)
        );
    }

    @Test
    void upazilaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var upazilaCriteria = new UpazilaCriteria();
        setAllFilters(upazilaCriteria);

        var copy = upazilaCriteria.copy();

        assertThat(upazilaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(upazilaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var upazilaCriteria = new UpazilaCriteria();

        assertThat(upazilaCriteria).hasToString("UpazilaCriteria{}");
    }

    private static void setAllFilters(UpazilaCriteria upazilaCriteria) {
        upazilaCriteria.id();
        upazilaCriteria.name();
        upazilaCriteria.unionId();
        upazilaCriteria.cityCorpPouraId();
        upazilaCriteria.districtId();
        upazilaCriteria.distinct();
    }

    private static Condition<UpazilaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getUnionId()) &&
                condition.apply(criteria.getCityCorpPouraId()) &&
                condition.apply(criteria.getDistrictId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UpazilaCriteria> copyFiltersAre(UpazilaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getUnionId(), copy.getUnionId()) &&
                condition.apply(criteria.getCityCorpPouraId(), copy.getCityCorpPouraId()) &&
                condition.apply(criteria.getDistrictId(), copy.getDistrictId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
