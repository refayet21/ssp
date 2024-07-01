package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VehicleAssignmentCriteriaTest {

    @Test
    void newVehicleAssignmentCriteriaHasAllFiltersNullTest() {
        var vehicleAssignmentCriteria = new VehicleAssignmentCriteria();
        assertThat(vehicleAssignmentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void vehicleAssignmentCriteriaFluentMethodsCreatesFiltersTest() {
        var vehicleAssignmentCriteria = new VehicleAssignmentCriteria();

        setAllFilters(vehicleAssignmentCriteria);

        assertThat(vehicleAssignmentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void vehicleAssignmentCriteriaCopyCreatesNullFilterTest() {
        var vehicleAssignmentCriteria = new VehicleAssignmentCriteria();
        var copy = vehicleAssignmentCriteria.copy();

        assertThat(vehicleAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(vehicleAssignmentCriteria)
        );
    }

    @Test
    void vehicleAssignmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var vehicleAssignmentCriteria = new VehicleAssignmentCriteria();
        setAllFilters(vehicleAssignmentCriteria);

        var copy = vehicleAssignmentCriteria.copy();

        assertThat(vehicleAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(vehicleAssignmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var vehicleAssignmentCriteria = new VehicleAssignmentCriteria();

        assertThat(vehicleAssignmentCriteria).hasToString("VehicleAssignmentCriteria{}");
    }

    private static void setAllFilters(VehicleAssignmentCriteria vehicleAssignmentCriteria) {
        vehicleAssignmentCriteria.id();
        vehicleAssignmentCriteria.startDate();
        vehicleAssignmentCriteria.endDate();
        vehicleAssignmentCriteria.isPrimary();
        vehicleAssignmentCriteria.isRental();
        vehicleAssignmentCriteria.passId();
        vehicleAssignmentCriteria.agencyId();
        vehicleAssignmentCriteria.vehicleId();
        vehicleAssignmentCriteria.distinct();
    }

    private static Condition<VehicleAssignmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getIsPrimary()) &&
                condition.apply(criteria.getIsRental()) &&
                condition.apply(criteria.getPassId()) &&
                condition.apply(criteria.getAgencyId()) &&
                condition.apply(criteria.getVehicleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VehicleAssignmentCriteria> copyFiltersAre(
        VehicleAssignmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getIsPrimary(), copy.getIsPrimary()) &&
                condition.apply(criteria.getIsRental(), copy.getIsRental()) &&
                condition.apply(criteria.getPassId(), copy.getPassId()) &&
                condition.apply(criteria.getAgencyId(), copy.getAgencyId()) &&
                condition.apply(criteria.getVehicleId(), copy.getVehicleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
