package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AgencyTestSamples.*;
import static com.mycompany.myapp.domain.AssignmentTestSamples.*;
import static com.mycompany.myapp.domain.DesignationTestSamples.*;
import static com.mycompany.myapp.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assignment.class);
        Assignment assignment1 = getAssignmentSample1();
        Assignment assignment2 = new Assignment();
        assertThat(assignment1).isNotEqualTo(assignment2);

        assignment2.setId(assignment1.getId());
        assertThat(assignment1).isEqualTo(assignment2);

        assignment2 = getAssignmentSample2();
        assertThat(assignment1).isNotEqualTo(assignment2);
    }

    @Test
    void personTest() {
        Assignment assignment = getAssignmentRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        assignment.setPerson(personBack);
        assertThat(assignment.getPerson()).isEqualTo(personBack);

        assignment.person(null);
        assertThat(assignment.getPerson()).isNull();
    }

    @Test
    void designationTest() {
        Assignment assignment = getAssignmentRandomSampleGenerator();
        Designation designationBack = getDesignationRandomSampleGenerator();

        assignment.setDesignation(designationBack);
        assertThat(assignment.getDesignation()).isEqualTo(designationBack);

        assignment.designation(null);
        assertThat(assignment.getDesignation()).isNull();
    }

    @Test
    void agencyTest() {
        Assignment assignment = getAssignmentRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        assignment.setAgency(agencyBack);
        assertThat(assignment.getAgency()).isEqualTo(agencyBack);

        assignment.agency(null);
        assertThat(assignment.getAgency()).isNull();
    }
}
