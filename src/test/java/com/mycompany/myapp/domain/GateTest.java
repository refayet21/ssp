package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.GateTestSamples.*;
import static com.mycompany.myapp.domain.LaneTestSamples.*;
import static com.mycompany.myapp.domain.ZoneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gate.class);
        Gate gate1 = getGateSample1();
        Gate gate2 = new Gate();
        assertThat(gate1).isNotEqualTo(gate2);

        gate2.setId(gate1.getId());
        assertThat(gate1).isEqualTo(gate2);

        gate2 = getGateSample2();
        assertThat(gate1).isNotEqualTo(gate2);
    }

    @Test
    void laneTest() {
        Gate gate = getGateRandomSampleGenerator();
        Lane laneBack = getLaneRandomSampleGenerator();

        gate.addLane(laneBack);
        assertThat(gate.getLanes()).containsOnly(laneBack);
        assertThat(laneBack.getGate()).isEqualTo(gate);

        gate.removeLane(laneBack);
        assertThat(gate.getLanes()).doesNotContain(laneBack);
        assertThat(laneBack.getGate()).isNull();

        gate.lanes(new HashSet<>(Set.of(laneBack)));
        assertThat(gate.getLanes()).containsOnly(laneBack);
        assertThat(laneBack.getGate()).isEqualTo(gate);

        gate.setLanes(new HashSet<>());
        assertThat(gate.getLanes()).doesNotContain(laneBack);
        assertThat(laneBack.getGate()).isNull();
    }

    @Test
    void zoneTest() {
        Gate gate = getGateRandomSampleGenerator();
        Zone zoneBack = getZoneRandomSampleGenerator();

        gate.setZone(zoneBack);
        assertThat(gate.getZone()).isEqualTo(zoneBack);

        gate.zone(null);
        assertThat(gate.getZone()).isNull();
    }
}
