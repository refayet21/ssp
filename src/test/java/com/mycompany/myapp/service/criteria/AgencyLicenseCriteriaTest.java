package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgencyLicenseCriteriaTest {

    @Test
    void newAgencyLicenseCriteriaHasAllFiltersNullTest() {
        var agencyLicenseCriteria = new AgencyLicenseCriteria();
        assertThat(agencyLicenseCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void agencyLicenseCriteriaFluentMethodsCreatesFiltersTest() {
        var agencyLicenseCriteria = new AgencyLicenseCriteria();

        setAllFilters(agencyLicenseCriteria);

        assertThat(agencyLicenseCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void agencyLicenseCriteriaCopyCreatesNullFilterTest() {
        var agencyLicenseCriteria = new AgencyLicenseCriteria();
        var copy = agencyLicenseCriteria.copy();

        assertThat(agencyLicenseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(agencyLicenseCriteria)
        );
    }

    @Test
    void agencyLicenseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agencyLicenseCriteria = new AgencyLicenseCriteria();
        setAllFilters(agencyLicenseCriteria);

        var copy = agencyLicenseCriteria.copy();

        assertThat(agencyLicenseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(agencyLicenseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agencyLicenseCriteria = new AgencyLicenseCriteria();

        assertThat(agencyLicenseCriteria).hasToString("AgencyLicenseCriteria{}");
    }

    private static void setAllFilters(AgencyLicenseCriteria agencyLicenseCriteria) {
        agencyLicenseCriteria.id();
        agencyLicenseCriteria.filePath();
        agencyLicenseCriteria.serialNo();
        agencyLicenseCriteria.issueDate();
        agencyLicenseCriteria.expiryDate();
        agencyLicenseCriteria.verifiedById();
        agencyLicenseCriteria.agencyLicenseTypeId();
        agencyLicenseCriteria.belongsToId();
        agencyLicenseCriteria.issuedById();
        agencyLicenseCriteria.distinct();
    }

    private static Condition<AgencyLicenseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFilePath()) &&
                condition.apply(criteria.getSerialNo()) &&
                condition.apply(criteria.getIssueDate()) &&
                condition.apply(criteria.getExpiryDate()) &&
                condition.apply(criteria.getVerifiedById()) &&
                condition.apply(criteria.getAgencyLicenseTypeId()) &&
                condition.apply(criteria.getBelongsToId()) &&
                condition.apply(criteria.getIssuedById()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgencyLicenseCriteria> copyFiltersAre(
        AgencyLicenseCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFilePath(), copy.getFilePath()) &&
                condition.apply(criteria.getSerialNo(), copy.getSerialNo()) &&
                condition.apply(criteria.getIssueDate(), copy.getIssueDate()) &&
                condition.apply(criteria.getExpiryDate(), copy.getExpiryDate()) &&
                condition.apply(criteria.getVerifiedById(), copy.getVerifiedById()) &&
                condition.apply(criteria.getAgencyLicenseTypeId(), copy.getAgencyLicenseTypeId()) &&
                condition.apply(criteria.getBelongsToId(), copy.getBelongsToId()) &&
                condition.apply(criteria.getIssuedById(), copy.getIssuedById()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
