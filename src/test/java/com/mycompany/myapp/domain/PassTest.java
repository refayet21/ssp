package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AssignmentTestSamples.*;
import static com.mycompany.myapp.domain.EntryLogTestSamples.*;
import static com.mycompany.myapp.domain.PassTestSamples.*;
import static com.mycompany.myapp.domain.PassTypeTestSamples.*;
import static com.mycompany.myapp.domain.VehicleAssignmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PassTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pass.class);
        Pass pass1 = getPassSample1();
        Pass pass2 = new Pass();
        assertThat(pass1).isNotEqualTo(pass2);

        pass2.setId(pass1.getId());
        assertThat(pass1).isEqualTo(pass2);

        pass2 = getPassSample2();
        assertThat(pass1).isNotEqualTo(pass2);
    }

    @Test
    void entryLogTest() {
        Pass pass = getPassRandomSampleGenerator();
        EntryLog entryLogBack = getEntryLogRandomSampleGenerator();

        pass.addEntryLog(entryLogBack);
        assertThat(pass.getEntryLogs()).containsOnly(entryLogBack);
        assertThat(entryLogBack.getPass()).isEqualTo(pass);

        pass.removeEntryLog(entryLogBack);
        assertThat(pass.getEntryLogs()).doesNotContain(entryLogBack);
        assertThat(entryLogBack.getPass()).isNull();

        pass.entryLogs(new HashSet<>(Set.of(entryLogBack)));
        assertThat(pass.getEntryLogs()).containsOnly(entryLogBack);
        assertThat(entryLogBack.getPass()).isEqualTo(pass);

        pass.setEntryLogs(new HashSet<>());
        assertThat(pass.getEntryLogs()).doesNotContain(entryLogBack);
        assertThat(entryLogBack.getPass()).isNull();
    }

    @Test
    void passTypeTest() {
        Pass pass = getPassRandomSampleGenerator();
        PassType passTypeBack = getPassTypeRandomSampleGenerator();

        pass.setPassType(passTypeBack);
        assertThat(pass.getPassType()).isEqualTo(passTypeBack);

        pass.passType(null);
        assertThat(pass.getPassType()).isNull();
    }

    @Test
    void assignmentTest() {
        Pass pass = getPassRandomSampleGenerator();
        Assignment assignmentBack = getAssignmentRandomSampleGenerator();

        pass.setAssignment(assignmentBack);
        assertThat(pass.getAssignment()).isEqualTo(assignmentBack);

        pass.assignment(null);
        assertThat(pass.getAssignment()).isNull();
    }

    @Test
    void vehicleAssignmentTest() {
        Pass pass = getPassRandomSampleGenerator();
        VehicleAssignment vehicleAssignmentBack = getVehicleAssignmentRandomSampleGenerator();

        pass.setVehicleAssignment(vehicleAssignmentBack);
        assertThat(pass.getVehicleAssignment()).isEqualTo(vehicleAssignmentBack);

        pass.vehicleAssignment(null);
        assertThat(pass.getVehicleAssignment()).isNull();
    }
}
