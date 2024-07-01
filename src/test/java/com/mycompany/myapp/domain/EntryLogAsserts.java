package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EntryLogAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEntryLogAllPropertiesEquals(EntryLog expected, EntryLog actual) {
        assertEntryLogAutoGeneratedPropertiesEquals(expected, actual);
        assertEntryLogAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEntryLogAllUpdatablePropertiesEquals(EntryLog expected, EntryLog actual) {
        assertEntryLogUpdatableFieldsEquals(expected, actual);
        assertEntryLogUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEntryLogAutoGeneratedPropertiesEquals(EntryLog expected, EntryLog actual) {
        assertThat(expected)
            .as("Verify EntryLog auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEntryLogUpdatableFieldsEquals(EntryLog expected, EntryLog actual) {
        assertThat(expected)
            .as("Verify EntryLog relevant properties")
            .satisfies(e -> assertThat(e.getEventTime()).as("check eventTime").isEqualTo(actual.getEventTime()))
            .satisfies(e -> assertThat(e.getDirection()).as("check direction").isEqualTo(actual.getDirection()))
            .satisfies(e -> assertThat(e.getPassStatus()).as("check passStatus").isEqualTo(actual.getPassStatus()))
            .satisfies(e -> assertThat(e.getActionType()).as("check actionType").isEqualTo(actual.getActionType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEntryLogUpdatableRelationshipsEquals(EntryLog expected, EntryLog actual) {
        assertThat(expected)
            .as("Verify EntryLog relationships")
            .satisfies(e -> assertThat(e.getPass()).as("check pass").isEqualTo(actual.getPass()))
            .satisfies(e -> assertThat(e.getLane()).as("check lane").isEqualTo(actual.getLane()));
    }
}
