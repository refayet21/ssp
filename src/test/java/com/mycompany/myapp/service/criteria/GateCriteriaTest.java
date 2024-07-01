package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GateCriteriaTest {

    @Test
    void newGateCriteriaHasAllFiltersNullTest() {
        var gateCriteria = new GateCriteria();
        assertThat(gateCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void gateCriteriaFluentMethodsCreatesFiltersTest() {
        var gateCriteria = new GateCriteria();

        setAllFilters(gateCriteria);

        assertThat(gateCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void gateCriteriaCopyCreatesNullFilterTest() {
        var gateCriteria = new GateCriteria();
        var copy = gateCriteria.copy();

        assertThat(gateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(gateCriteria)
        );
    }

    @Test
    void gateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var gateCriteria = new GateCriteria();
        setAllFilters(gateCriteria);

        var copy = gateCriteria.copy();

        assertThat(gateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(gateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var gateCriteria = new GateCriteria();

        assertThat(gateCriteria).hasToString("GateCriteria{}");
    }

    private static void setAllFilters(GateCriteria gateCriteria) {
        gateCriteria.id();
        gateCriteria.name();
        gateCriteria.shortName();
        gateCriteria.lat();
        gateCriteria.lon();
        gateCriteria.gateType();
        gateCriteria.isActive();
        gateCriteria.laneId();
        gateCriteria.zoneId();
        gateCriteria.distinct();
    }

    private static Condition<GateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getShortName()) &&
                condition.apply(criteria.getLat()) &&
                condition.apply(criteria.getLon()) &&
                condition.apply(criteria.getGateType()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getLaneId()) &&
                condition.apply(criteria.getZoneId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GateCriteria> copyFiltersAre(GateCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getShortName(), copy.getShortName()) &&
                condition.apply(criteria.getLat(), copy.getLat()) &&
                condition.apply(criteria.getLon(), copy.getLon()) &&
                condition.apply(criteria.getGateType(), copy.getGateType()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getLaneId(), copy.getLaneId()) &&
                condition.apply(criteria.getZoneId(), copy.getZoneId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
