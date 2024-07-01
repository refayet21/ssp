package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentTypeCriteriaTest {

    @Test
    void newDocumentTypeCriteriaHasAllFiltersNullTest() {
        var documentTypeCriteria = new DocumentTypeCriteria();
        assertThat(documentTypeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void documentTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var documentTypeCriteria = new DocumentTypeCriteria();

        setAllFilters(documentTypeCriteria);

        assertThat(documentTypeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void documentTypeCriteriaCopyCreatesNullFilterTest() {
        var documentTypeCriteria = new DocumentTypeCriteria();
        var copy = documentTypeCriteria.copy();

        assertThat(documentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(documentTypeCriteria)
        );
    }

    @Test
    void documentTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentTypeCriteria = new DocumentTypeCriteria();
        setAllFilters(documentTypeCriteria);

        var copy = documentTypeCriteria.copy();

        assertThat(documentTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(documentTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentTypeCriteria = new DocumentTypeCriteria();

        assertThat(documentTypeCriteria).hasToString("DocumentTypeCriteria{}");
    }

    private static void setAllFilters(DocumentTypeCriteria documentTypeCriteria) {
        documentTypeCriteria.id();
        documentTypeCriteria.name();
        documentTypeCriteria.isActive();
        documentTypeCriteria.description();
        documentTypeCriteria.documentMasterType();
        documentTypeCriteria.requiresVerification();
        documentTypeCriteria.documentId();
        documentTypeCriteria.distinct();
    }

    private static Condition<DocumentTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDocumentMasterType()) &&
                condition.apply(criteria.getRequiresVerification()) &&
                condition.apply(criteria.getDocumentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentTypeCriteria> copyFiltersAre(
        DocumentTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDocumentMasterType(), copy.getDocumentMasterType()) &&
                condition.apply(criteria.getRequiresVerification(), copy.getRequiresVerification()) &&
                condition.apply(criteria.getDocumentId(), copy.getDocumentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
