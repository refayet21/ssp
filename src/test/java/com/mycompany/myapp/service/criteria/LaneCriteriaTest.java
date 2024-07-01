package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LaneCriteriaTest {

    @Test
    void newLaneCriteriaHasAllFiltersNullTest() {
        var laneCriteria = new LaneCriteria();
        assertThat(laneCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void laneCriteriaFluentMethodsCreatesFiltersTest() {
        var laneCriteria = new LaneCriteria();

        setAllFilters(laneCriteria);

        assertThat(laneCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void laneCriteriaCopyCreatesNullFilterTest() {
        var laneCriteria = new LaneCriteria();
        var copy = laneCriteria.copy();

        assertThat(laneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(laneCriteria)
        );
    }

    @Test
    void laneCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var laneCriteria = new LaneCriteria();
        setAllFilters(laneCriteria);

        var copy = laneCriteria.copy();

        assertThat(laneCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(laneCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var laneCriteria = new LaneCriteria();

        assertThat(laneCriteria).hasToString("LaneCriteria{}");
    }

    private static void setAllFilters(LaneCriteria laneCriteria) {
        laneCriteria.id();
        laneCriteria.name();
        laneCriteria.shortName();
        laneCriteria.direction();
        laneCriteria.isActive();
        laneCriteria.entryLogId();
        laneCriteria.gateId();
        laneCriteria.accessProfileId();
        laneCriteria.distinct();
    }

    private static Condition<LaneCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getShortName()) &&
                condition.apply(criteria.getDirection()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getEntryLogId()) &&
                condition.apply(criteria.getGateId()) &&
                condition.apply(criteria.getAccessProfileId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LaneCriteria> copyFiltersAre(LaneCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getShortName(), copy.getShortName()) &&
                condition.apply(criteria.getDirection(), copy.getDirection()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getEntryLogId(), copy.getEntryLogId()) &&
                condition.apply(criteria.getGateId(), copy.getGateId()) &&
                condition.apply(criteria.getAccessProfileId(), copy.getAccessProfileId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
