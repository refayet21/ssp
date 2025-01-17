package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AgencyTypeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgencyTypeAllPropertiesEquals(AgencyType expected, AgencyType actual) {
        assertAgencyTypeAutoGeneratedPropertiesEquals(expected, actual);
        assertAgencyTypeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgencyTypeAllUpdatablePropertiesEquals(AgencyType expected, AgencyType actual) {
        assertAgencyTypeUpdatableFieldsEquals(expected, actual);
        assertAgencyTypeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgencyTypeAutoGeneratedPropertiesEquals(AgencyType expected, AgencyType actual) {
        assertThat(expected)
            .as("Verify AgencyType auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgencyTypeUpdatableFieldsEquals(AgencyType expected, AgencyType actual) {
        assertThat(expected)
            .as("Verify AgencyType relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getShortName()).as("check shortName").isEqualTo(actual.getShortName()))
            .satisfies(e -> assertThat(e.getIsActive()).as("check isActive").isEqualTo(actual.getIsActive()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgencyTypeUpdatableRelationshipsEquals(AgencyType expected, AgencyType actual) {}
}
