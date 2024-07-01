package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EntryLogCriteriaTest {

    @Test
    void newEntryLogCriteriaHasAllFiltersNullTest() {
        var entryLogCriteria = new EntryLogCriteria();
        assertThat(entryLogCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void entryLogCriteriaFluentMethodsCreatesFiltersTest() {
        var entryLogCriteria = new EntryLogCriteria();

        setAllFilters(entryLogCriteria);

        assertThat(entryLogCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void entryLogCriteriaCopyCreatesNullFilterTest() {
        var entryLogCriteria = new EntryLogCriteria();
        var copy = entryLogCriteria.copy();

        assertThat(entryLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(entryLogCriteria)
        );
    }

    @Test
    void entryLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var entryLogCriteria = new EntryLogCriteria();
        setAllFilters(entryLogCriteria);

        var copy = entryLogCriteria.copy();

        assertThat(entryLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(entryLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var entryLogCriteria = new EntryLogCriteria();

        assertThat(entryLogCriteria).hasToString("EntryLogCriteria{}");
    }

    private static void setAllFilters(EntryLogCriteria entryLogCriteria) {
        entryLogCriteria.id();
        entryLogCriteria.eventTime();
        entryLogCriteria.direction();
        entryLogCriteria.passStatus();
        entryLogCriteria.actionType();
        entryLogCriteria.passId();
        entryLogCriteria.laneId();
        entryLogCriteria.distinct();
    }

    private static Condition<EntryLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEventTime()) &&
                condition.apply(criteria.getDirection()) &&
                condition.apply(criteria.getPassStatus()) &&
                condition.apply(criteria.getActionType()) &&
                condition.apply(criteria.getPassId()) &&
                condition.apply(criteria.getLaneId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EntryLogCriteria> copyFiltersAre(EntryLogCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEventTime(), copy.getEventTime()) &&
                condition.apply(criteria.getDirection(), copy.getDirection()) &&
                condition.apply(criteria.getPassStatus(), copy.getPassStatus()) &&
                condition.apply(criteria.getActionType(), copy.getActionType()) &&
                condition.apply(criteria.getPassId(), copy.getPassId()) &&
                condition.apply(criteria.getLaneId(), copy.getLaneId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
