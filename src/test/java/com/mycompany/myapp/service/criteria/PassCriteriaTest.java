package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PassCriteriaTest {

    @Test
    void newPassCriteriaHasAllFiltersNullTest() {
        var passCriteria = new PassCriteria();
        assertThat(passCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void passCriteriaFluentMethodsCreatesFiltersTest() {
        var passCriteria = new PassCriteria();

        setAllFilters(passCriteria);

        assertThat(passCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void passCriteriaCopyCreatesNullFilterTest() {
        var passCriteria = new PassCriteria();
        var copy = passCriteria.copy();

        assertThat(passCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(passCriteria)
        );
    }

    @Test
    void passCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var passCriteria = new PassCriteria();
        setAllFilters(passCriteria);

        var copy = passCriteria.copy();

        assertThat(passCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(passCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var passCriteria = new PassCriteria();

        assertThat(passCriteria).hasToString("PassCriteria{}");
    }

    private static void setAllFilters(PassCriteria passCriteria) {
        passCriteria.id();
        passCriteria.collectedFee();
        passCriteria.fromDate();
        passCriteria.endDate();
        passCriteria.status();
        passCriteria.passNumber();
        passCriteria.mediaSerial();
        passCriteria.entryLogId();
        passCriteria.passTypeId();
        passCriteria.requestedById();
        passCriteria.assignmentId();
        passCriteria.vehicleAssignmentId();
        passCriteria.distinct();
    }

    private static Condition<PassCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCollectedFee()) &&
                condition.apply(criteria.getFromDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPassNumber()) &&
                condition.apply(criteria.getMediaSerial()) &&
                condition.apply(criteria.getEntryLogId()) &&
                condition.apply(criteria.getPassTypeId()) &&
                condition.apply(criteria.getRequestedById()) &&
                condition.apply(criteria.getAssignmentId()) &&
                condition.apply(criteria.getVehicleAssignmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PassCriteria> copyFiltersAre(PassCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCollectedFee(), copy.getCollectedFee()) &&
                condition.apply(criteria.getFromDate(), copy.getFromDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPassNumber(), copy.getPassNumber()) &&
                condition.apply(criteria.getMediaSerial(), copy.getMediaSerial()) &&
                condition.apply(criteria.getEntryLogId(), copy.getEntryLogId()) &&
                condition.apply(criteria.getPassTypeId(), copy.getPassTypeId()) &&
                condition.apply(criteria.getRequestedById(), copy.getRequestedById()) &&
                condition.apply(criteria.getAssignmentId(), copy.getAssignmentId()) &&
                condition.apply(criteria.getVehicleAssignmentId(), copy.getVehicleAssignmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
