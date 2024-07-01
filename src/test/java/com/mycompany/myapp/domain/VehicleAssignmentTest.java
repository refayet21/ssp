package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AgencyTestSamples.*;
import static com.mycompany.myapp.domain.PassTestSamples.*;
import static com.mycompany.myapp.domain.VehicleAssignmentTestSamples.*;
import static com.mycompany.myapp.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VehicleAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleAssignment.class);
        VehicleAssignment vehicleAssignment1 = getVehicleAssignmentSample1();
        VehicleAssignment vehicleAssignment2 = new VehicleAssignment();
        assertThat(vehicleAssignment1).isNotEqualTo(vehicleAssignment2);

        vehicleAssignment2.setId(vehicleAssignment1.getId());
        assertThat(vehicleAssignment1).isEqualTo(vehicleAssignment2);

        vehicleAssignment2 = getVehicleAssignmentSample2();
        assertThat(vehicleAssignment1).isNotEqualTo(vehicleAssignment2);
    }

    @Test
    void passTest() {
        VehicleAssignment vehicleAssignment = getVehicleAssignmentRandomSampleGenerator();
        Pass passBack = getPassRandomSampleGenerator();

        vehicleAssignment.addPass(passBack);
        assertThat(vehicleAssignment.getPasses()).containsOnly(passBack);
        assertThat(passBack.getVehicleAssignment()).isEqualTo(vehicleAssignment);

        vehicleAssignment.removePass(passBack);
        assertThat(vehicleAssignment.getPasses()).doesNotContain(passBack);
        assertThat(passBack.getVehicleAssignment()).isNull();

        vehicleAssignment.passes(new HashSet<>(Set.of(passBack)));
        assertThat(vehicleAssignment.getPasses()).containsOnly(passBack);
        assertThat(passBack.getVehicleAssignment()).isEqualTo(vehicleAssignment);

        vehicleAssignment.setPasses(new HashSet<>());
        assertThat(vehicleAssignment.getPasses()).doesNotContain(passBack);
        assertThat(passBack.getVehicleAssignment()).isNull();
    }

    @Test
    void agencyTest() {
        VehicleAssignment vehicleAssignment = getVehicleAssignmentRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        vehicleAssignment.setAgency(agencyBack);
        assertThat(vehicleAssignment.getAgency()).isEqualTo(agencyBack);

        vehicleAssignment.agency(null);
        assertThat(vehicleAssignment.getAgency()).isNull();
    }

    @Test
    void vehicleTest() {
        VehicleAssignment vehicleAssignment = getVehicleAssignmentRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        vehicleAssignment.setVehicle(vehicleBack);
        assertThat(vehicleAssignment.getVehicle()).isEqualTo(vehicleBack);

        vehicleAssignment.vehicle(null);
        assertThat(vehicleAssignment.getVehicle()).isNull();
    }
}
