package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PassAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassAllPropertiesEquals(Pass expected, Pass actual) {
        assertPassAutoGeneratedPropertiesEquals(expected, actual);
        assertPassAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassAllUpdatablePropertiesEquals(Pass expected, Pass actual) {
        assertPassUpdatableFieldsEquals(expected, actual);
        assertPassUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassAutoGeneratedPropertiesEquals(Pass expected, Pass actual) {
        assertThat(expected)
            .as("Verify Pass auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassUpdatableFieldsEquals(Pass expected, Pass actual) {
        assertThat(expected)
            .as("Verify Pass relevant properties")
            .satisfies(e -> assertThat(e.getCollectedFee()).as("check collectedFee").isEqualTo(actual.getCollectedFee()))
            .satisfies(e -> assertThat(e.getFromDate()).as("check fromDate").isEqualTo(actual.getFromDate()))
            .satisfies(e -> assertThat(e.getEndDate()).as("check endDate").isEqualTo(actual.getEndDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getPassNumber()).as("check passNumber").isEqualTo(actual.getPassNumber()))
            .satisfies(e -> assertThat(e.getMediaSerial()).as("check mediaSerial").isEqualTo(actual.getMediaSerial()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPassUpdatableRelationshipsEquals(Pass expected, Pass actual) {
        assertThat(expected)
            .as("Verify Pass relationships")
            .satisfies(e -> assertThat(e.getPassType()).as("check passType").isEqualTo(actual.getPassType()))
            .satisfies(e -> assertThat(e.getAssignment()).as("check assignment").isEqualTo(actual.getAssignment()))
            .satisfies(e -> assertThat(e.getVehicleAssignment()).as("check vehicleAssignment").isEqualTo(actual.getVehicleAssignment()));
    }
}
