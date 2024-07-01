package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ZoneAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertZoneAllPropertiesEquals(Zone expected, Zone actual) {
        assertZoneAutoGeneratedPropertiesEquals(expected, actual);
        assertZoneAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertZoneAllUpdatablePropertiesEquals(Zone expected, Zone actual) {
        assertZoneUpdatableFieldsEquals(expected, actual);
        assertZoneUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertZoneAutoGeneratedPropertiesEquals(Zone expected, Zone actual) {
        assertThat(expected)
            .as("Verify Zone auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertZoneUpdatableFieldsEquals(Zone expected, Zone actual) {
        assertThat(expected)
            .as("Verify Zone relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getShortName()).as("check shortName").isEqualTo(actual.getShortName()))
            .satisfies(e -> assertThat(e.getLocation()).as("check location").isEqualTo(actual.getLocation()))
            .satisfies(e -> assertThat(e.getIsActive()).as("check isActive").isEqualTo(actual.getIsActive()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertZoneUpdatableRelationshipsEquals(Zone expected, Zone actual) {
        assertThat(expected)
            .as("Verify Zone relationships")
            .satisfies(e -> assertThat(e.getAuthority()).as("check authority").isEqualTo(actual.getAuthority()));
    }
}