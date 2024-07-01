package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AssignmentTestSamples.*;
import static com.mycompany.myapp.domain.DesignationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DesignationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Designation.class);
        Designation designation1 = getDesignationSample1();
        Designation designation2 = new Designation();
        assertThat(designation1).isNotEqualTo(designation2);

        designation2.setId(designation1.getId());
        assertThat(designation1).isEqualTo(designation2);

        designation2 = getDesignationSample2();
        assertThat(designation1).isNotEqualTo(designation2);
    }

    @Test
    void assignmentTest() {
        Designation designation = getDesignationRandomSampleGenerator();
        Assignment assignmentBack = getAssignmentRandomSampleGenerator();

        designation.addAssignment(assignmentBack);
        assertThat(designation.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getDesignation()).isEqualTo(designation);

        designation.removeAssignment(assignmentBack);
        assertThat(designation.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getDesignation()).isNull();

        designation.assignments(new HashSet<>(Set.of(assignmentBack)));
        assertThat(designation.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getDesignation()).isEqualTo(designation);

        designation.setAssignments(new HashSet<>());
        assertThat(designation.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getDesignation()).isNull();
    }
}
