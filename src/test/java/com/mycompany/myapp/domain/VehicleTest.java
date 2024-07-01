package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DocumentTestSamples.*;
import static com.mycompany.myapp.domain.VehicleAssignmentTestSamples.*;
import static com.mycompany.myapp.domain.VehicleTestSamples.*;
import static com.mycompany.myapp.domain.VehicleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VehicleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicle.class);
        Vehicle vehicle1 = getVehicleSample1();
        Vehicle vehicle2 = new Vehicle();
        assertThat(vehicle1).isNotEqualTo(vehicle2);

        vehicle2.setId(vehicle1.getId());
        assertThat(vehicle1).isEqualTo(vehicle2);

        vehicle2 = getVehicleSample2();
        assertThat(vehicle1).isNotEqualTo(vehicle2);
    }

    @Test
    void documentTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        vehicle.addDocument(documentBack);
        assertThat(vehicle.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getVehicle()).isEqualTo(vehicle);

        vehicle.removeDocument(documentBack);
        assertThat(vehicle.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getVehicle()).isNull();

        vehicle.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(vehicle.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getVehicle()).isEqualTo(vehicle);

        vehicle.setDocuments(new HashSet<>());
        assertThat(vehicle.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getVehicle()).isNull();
    }

    @Test
    void vehicleAssigmentTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        VehicleAssignment vehicleAssignmentBack = getVehicleAssignmentRandomSampleGenerator();

        vehicle.addVehicleAssigment(vehicleAssignmentBack);
        assertThat(vehicle.getVehicleAssigments()).containsOnly(vehicleAssignmentBack);
        assertThat(vehicleAssignmentBack.getVehicle()).isEqualTo(vehicle);

        vehicle.removeVehicleAssigment(vehicleAssignmentBack);
        assertThat(vehicle.getVehicleAssigments()).doesNotContain(vehicleAssignmentBack);
        assertThat(vehicleAssignmentBack.getVehicle()).isNull();

        vehicle.vehicleAssigments(new HashSet<>(Set.of(vehicleAssignmentBack)));
        assertThat(vehicle.getVehicleAssigments()).containsOnly(vehicleAssignmentBack);
        assertThat(vehicleAssignmentBack.getVehicle()).isEqualTo(vehicle);

        vehicle.setVehicleAssigments(new HashSet<>());
        assertThat(vehicle.getVehicleAssigments()).doesNotContain(vehicleAssignmentBack);
        assertThat(vehicleAssignmentBack.getVehicle()).isNull();
    }

    @Test
    void vehicleTypeTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        VehicleType vehicleTypeBack = getVehicleTypeRandomSampleGenerator();

        vehicle.setVehicleType(vehicleTypeBack);
        assertThat(vehicle.getVehicleType()).isEqualTo(vehicleTypeBack);

        vehicle.vehicleType(null);
        assertThat(vehicle.getVehicleType()).isNull();
    }
}
