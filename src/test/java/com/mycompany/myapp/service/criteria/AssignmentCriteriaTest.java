package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AssignmentCriteriaTest {

    @Test
    void newAssignmentCriteriaHasAllFiltersNullTest() {
        var assignmentCriteria = new AssignmentCriteria();
        assertThat(assignmentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void assignmentCriteriaFluentMethodsCreatesFiltersTest() {
        var assignmentCriteria = new AssignmentCriteria();

        setAllFilters(assignmentCriteria);

        assertThat(assignmentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void assignmentCriteriaCopyCreatesNullFilterTest() {
        var assignmentCriteria = new AssignmentCriteria();
        var copy = assignmentCriteria.copy();

        assertThat(assignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(assignmentCriteria)
        );
    }

    @Test
    void assignmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var assignmentCriteria = new AssignmentCriteria();
        setAllFilters(assignmentCriteria);

        var copy = assignmentCriteria.copy();

        assertThat(assignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(assignmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var assignmentCriteria = new AssignmentCriteria();

        assertThat(assignmentCriteria).hasToString("AssignmentCriteria{}");
    }

    private static void setAllFilters(AssignmentCriteria assignmentCriteria) {
        assignmentCriteria.id();
        assignmentCriteria.startDate();
        assignmentCriteria.endDate();
        assignmentCriteria.isPrimary();
        assignmentCriteria.personId();
        assignmentCriteria.designationId();
        assignmentCriteria.agencyId();
        assignmentCriteria.distinct();
    }

    private static Condition<AssignmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getIsPrimary()) &&
                condition.apply(criteria.getPersonId()) &&
                condition.apply(criteria.getDesignationId()) &&
                condition.apply(criteria.getAgencyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AssignmentCriteria> copyFiltersAre(AssignmentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getIsPrimary(), copy.getIsPrimary()) &&
                condition.apply(criteria.getPersonId(), copy.getPersonId()) &&
                condition.apply(criteria.getDesignationId(), copy.getDesignationId()) &&
                condition.apply(criteria.getAgencyId(), copy.getAgencyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
