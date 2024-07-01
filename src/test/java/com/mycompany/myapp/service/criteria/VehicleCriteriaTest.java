package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VehicleCriteriaTest {

    @Test
    void newVehicleCriteriaHasAllFiltersNullTest() {
        var vehicleCriteria = new VehicleCriteria();
        assertThat(vehicleCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void vehicleCriteriaFluentMethodsCreatesFiltersTest() {
        var vehicleCriteria = new VehicleCriteria();

        setAllFilters(vehicleCriteria);

        assertThat(vehicleCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void vehicleCriteriaCopyCreatesNullFilterTest() {
        var vehicleCriteria = new VehicleCriteria();
        var copy = vehicleCriteria.copy();

        assertThat(vehicleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(vehicleCriteria)
        );
    }

    @Test
    void vehicleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var vehicleCriteria = new VehicleCriteria();
        setAllFilters(vehicleCriteria);

        var copy = vehicleCriteria.copy();

        assertThat(vehicleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(vehicleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var vehicleCriteria = new VehicleCriteria();

        assertThat(vehicleCriteria).hasToString("VehicleCriteria{}");
    }

    private static void setAllFilters(VehicleCriteria vehicleCriteria) {
        vehicleCriteria.id();
        vehicleCriteria.name();
        vehicleCriteria.regNo();
        vehicleCriteria.zone();
        vehicleCriteria.category();
        vehicleCriteria.serialNo();
        vehicleCriteria.vehicleNo();
        vehicleCriteria.chasisNo();
        vehicleCriteria.isPersonal();
        vehicleCriteria.isBlackListed();
        vehicleCriteria.logStatus();
        vehicleCriteria.documentId();
        vehicleCriteria.vehicleAssigmentId();
        vehicleCriteria.vehicleTypeId();
        vehicleCriteria.distinct();
    }

    private static Condition<VehicleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getRegNo()) &&
                condition.apply(criteria.getZone()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getSerialNo()) &&
                condition.apply(criteria.getVehicleNo()) &&
                condition.apply(criteria.getChasisNo()) &&
                condition.apply(criteria.getIsPersonal()) &&
                condition.apply(criteria.getIsBlackListed()) &&
                condition.apply(criteria.getLogStatus()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getVehicleAssigmentId()) &&
                condition.apply(criteria.getVehicleTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VehicleCriteria> copyFiltersAre(VehicleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getRegNo(), copy.getRegNo()) &&
                condition.apply(criteria.getZone(), copy.getZone()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getSerialNo(), copy.getSerialNo()) &&
                condition.apply(criteria.getVehicleNo(), copy.getVehicleNo()) &&
                condition.apply(criteria.getChasisNo(), copy.getChasisNo()) &&
                condition.apply(criteria.getIsPersonal(), copy.getIsPersonal()) &&
                condition.apply(criteria.getIsBlackListed(), copy.getIsBlackListed()) &&
                condition.apply(criteria.getLogStatus(), copy.getLogStatus()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getVehicleAssigmentId(), copy.getVehicleAssigmentId()) &&
                condition.apply(criteria.getVehicleTypeId(), copy.getVehicleTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
