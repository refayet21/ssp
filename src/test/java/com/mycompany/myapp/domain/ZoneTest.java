package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AgencyTestSamples.*;
import static com.mycompany.myapp.domain.GateTestSamples.*;
import static com.mycompany.myapp.domain.ZoneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ZoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Zone.class);
        Zone zone1 = getZoneSample1();
        Zone zone2 = new Zone();
        assertThat(zone1).isNotEqualTo(zone2);

        zone2.setId(zone1.getId());
        assertThat(zone1).isEqualTo(zone2);

        zone2 = getZoneSample2();
        assertThat(zone1).isNotEqualTo(zone2);
    }

    @Test
    void authorityTest() {
        Zone zone = getZoneRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        zone.setAuthority(agencyBack);
        assertThat(zone.getAuthority()).isEqualTo(agencyBack);

        zone.authority(null);
        assertThat(zone.getAuthority()).isNull();
    }

    @Test
    void gateTest() {
        Zone zone = getZoneRandomSampleGenerator();
        Gate gateBack = getGateRandomSampleGenerator();

        zone.addGate(gateBack);
        assertThat(zone.getGates()).containsOnly(gateBack);
        assertThat(gateBack.getZone()).isEqualTo(zone);

        zone.removeGate(gateBack);
        assertThat(zone.getGates()).doesNotContain(gateBack);
        assertThat(gateBack.getZone()).isNull();

        zone.gates(new HashSet<>(Set.of(gateBack)));
        assertThat(zone.getGates()).containsOnly(gateBack);
        assertThat(gateBack.getZone()).isEqualTo(zone);

        zone.setGates(new HashSet<>());
        assertThat(zone.getGates()).doesNotContain(gateBack);
        assertThat(gateBack.getZone()).isNull();
    }
}
