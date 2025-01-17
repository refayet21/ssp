package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UnionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUnionAllPropertiesEquals(Union expected, Union actual) {
        assertUnionAutoGeneratedPropertiesEquals(expected, actual);
        assertUnionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUnionAllUpdatablePropertiesEquals(Union expected, Union actual) {
        assertUnionUpdatableFieldsEquals(expected, actual);
        assertUnionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUnionAutoGeneratedPropertiesEquals(Union expected, Union actual) {
        assertThat(expected)
            .as("Verify Union auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUnionUpdatableFieldsEquals(Union expected, Union actual) {
        assertThat(expected)
            .as("Verify Union relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUnionUpdatableRelationshipsEquals(Union expected, Union actual) {
        assertThat(expected)
            .as("Verify Union relationships")
            .satisfies(e -> assertThat(e.getUpazila()).as("check upazila").isEqualTo(actual.getUpazila()));
    }
}
