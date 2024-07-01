package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgencyCriteriaTest {

    @Test
    void newAgencyCriteriaHasAllFiltersNullTest() {
        var agencyCriteria = new AgencyCriteria();
        assertThat(agencyCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void agencyCriteriaFluentMethodsCreatesFiltersTest() {
        var agencyCriteria = new AgencyCriteria();

        setAllFilters(agencyCriteria);

        assertThat(agencyCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void agencyCriteriaCopyCreatesNullFilterTest() {
        var agencyCriteria = new AgencyCriteria();
        var copy = agencyCriteria.copy();

        assertThat(agencyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(agencyCriteria)
        );
    }

    @Test
    void agencyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agencyCriteria = new AgencyCriteria();
        setAllFilters(agencyCriteria);

        var copy = agencyCriteria.copy();

        assertThat(agencyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(agencyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agencyCriteria = new AgencyCriteria();

        assertThat(agencyCriteria).hasToString("AgencyCriteria{}");
    }

    private static void setAllFilters(AgencyCriteria agencyCriteria) {
        agencyCriteria.id();
        agencyCriteria.name();
        agencyCriteria.shortName();
        agencyCriteria.isInternal();
        agencyCriteria.isDummy();
        agencyCriteria.addressId();
        agencyCriteria.documentId();
        agencyCriteria.assignmentId();
        agencyCriteria.licenseId();
        agencyCriteria.issuerId();
        agencyCriteria.departmentId();
        agencyCriteria.agencyTypeId();
        agencyCriteria.zoneId();
        agencyCriteria.passTypeId();
        agencyCriteria.distinct();
    }

    private static Condition<AgencyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getShortName()) &&
                condition.apply(criteria.getIsInternal()) &&
                condition.apply(criteria.getIsDummy()) &&
                condition.apply(criteria.getAddressId()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getAssignmentId()) &&
                condition.apply(criteria.getLicenseId()) &&
                condition.apply(criteria.getIssuerId()) &&
                condition.apply(criteria.getDepartmentId()) &&
                condition.apply(criteria.getAgencyTypeId()) &&
                condition.apply(criteria.getZoneId()) &&
                condition.apply(criteria.getPassTypeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgencyCriteria> copyFiltersAre(AgencyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getShortName(), copy.getShortName()) &&
                condition.apply(criteria.getIsInternal(), copy.getIsInternal()) &&
                condition.apply(criteria.getIsDummy(), copy.getIsDummy()) &&
                condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getAssignmentId(), copy.getAssignmentId()) &&
                condition.apply(criteria.getLicenseId(), copy.getLicenseId()) &&
                condition.apply(criteria.getIssuerId(), copy.getIssuerId()) &&
                condition.apply(criteria.getDepartmentId(), copy.getDepartmentId()) &&
                condition.apply(criteria.getAgencyTypeId(), copy.getAgencyTypeId()) &&
                condition.apply(criteria.getZoneId(), copy.getZoneId()) &&
                condition.apply(criteria.getPassTypeId(), copy.getPassTypeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
