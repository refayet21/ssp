package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.VehicleTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleType.class);
        VehicleType vehicleType1 = getVehicleTypeSample1();
        VehicleType vehicleType2 = new VehicleType();
        assertThat(vehicleType1).isNotEqualTo(vehicleType2);

        vehicleType2.setId(vehicleType1.getId());
        assertThat(vehicleType1).isEqualTo(vehicleType2);

        vehicleType2 = getVehicleTypeSample2();
        assertThat(vehicleType1).isNotEqualTo(vehicleType2);
    }
}
