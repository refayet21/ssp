package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentAllPropertiesEquals(Document expected, Document actual) {
        assertDocumentAutoGeneratedPropertiesEquals(expected, actual);
        assertDocumentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentAllUpdatablePropertiesEquals(Document expected, Document actual) {
        assertDocumentUpdatableFieldsEquals(expected, actual);
        assertDocumentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentAutoGeneratedPropertiesEquals(Document expected, Document actual) {
        assertThat(expected)
            .as("Verify Document auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentUpdatableFieldsEquals(Document expected, Document actual) {
        assertThat(expected)
            .as("Verify Document relevant properties")
            .satisfies(e -> assertThat(e.getIsPrimary()).as("check isPrimary").isEqualTo(actual.getIsPrimary()))
            .satisfies(e -> assertThat(e.getSerial()).as("check serial").isEqualTo(actual.getSerial()))
            .satisfies(e -> assertThat(e.getIssueDate()).as("check issueDate").isEqualTo(actual.getIssueDate()))
            .satisfies(e -> assertThat(e.getExpiryDate()).as("check expiryDate").isEqualTo(actual.getExpiryDate()))
            .satisfies(e -> assertThat(e.getFilePath()).as("check filePath").isEqualTo(actual.getFilePath()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDocumentUpdatableRelationshipsEquals(Document expected, Document actual) {
        assertThat(expected)
            .as("Verify Document relationships")
            .satisfies(e -> assertThat(e.getDocumentType()).as("check documentType").isEqualTo(actual.getDocumentType()))
            .satisfies(e -> assertThat(e.getPerson()).as("check person").isEqualTo(actual.getPerson()))
            .satisfies(e -> assertThat(e.getVehicle()).as("check vehicle").isEqualTo(actual.getVehicle()))
            .satisfies(e -> assertThat(e.getAgency()).as("check agency").isEqualTo(actual.getAgency()));
    }
}
