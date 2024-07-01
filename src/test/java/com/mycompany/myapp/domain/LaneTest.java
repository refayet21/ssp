package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AccessProfileTestSamples.*;
import static com.mycompany.myapp.domain.EntryLogTestSamples.*;
import static com.mycompany.myapp.domain.GateTestSamples.*;
import static com.mycompany.myapp.domain.LaneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LaneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lane.class);
        Lane lane1 = getLaneSample1();
        Lane lane2 = new Lane();
        assertThat(lane1).isNotEqualTo(lane2);

        lane2.setId(lane1.getId());
        assertThat(lane1).isEqualTo(lane2);

        lane2 = getLaneSample2();
        assertThat(lane1).isNotEqualTo(lane2);
    }

    @Test
    void entryLogTest() {
        Lane lane = getLaneRandomSampleGenerator();
        EntryLog entryLogBack = getEntryLogRandomSampleGenerator();

        lane.addEntryLog(entryLogBack);
        assertThat(lane.getEntryLogs()).containsOnly(entryLogBack);
        assertThat(entryLogBack.getLane()).isEqualTo(lane);

        lane.removeEntryLog(entryLogBack);
        assertThat(lane.getEntryLogs()).doesNotContain(entryLogBack);
        assertThat(entryLogBack.getLane()).isNull();

        lane.entryLogs(new HashSet<>(Set.of(entryLogBack)));
        assertThat(lane.getEntryLogs()).containsOnly(entryLogBack);
        assertThat(entryLogBack.getLane()).isEqualTo(lane);

        lane.setEntryLogs(new HashSet<>());
        assertThat(lane.getEntryLogs()).doesNotContain(entryLogBack);
        assertThat(entryLogBack.getLane()).isNull();
    }

    @Test
    void gateTest() {
        Lane lane = getLaneRandomSampleGenerator();
        Gate gateBack = getGateRandomSampleGenerator();

        lane.setGate(gateBack);
        assertThat(lane.getGate()).isEqualTo(gateBack);

        lane.gate(null);
        assertThat(lane.getGate()).isNull();
    }

    @Test
    void accessProfileTest() {
        Lane lane = getLaneRandomSampleGenerator();
        AccessProfile accessProfileBack = getAccessProfileRandomSampleGenerator();

        lane.addAccessProfile(accessProfileBack);
        assertThat(lane.getAccessProfiles()).containsOnly(accessProfileBack);
        assertThat(accessProfileBack.getLanes()).containsOnly(lane);

        lane.removeAccessProfile(accessProfileBack);
        assertThat(lane.getAccessProfiles()).doesNotContain(accessProfileBack);
        assertThat(accessProfileBack.getLanes()).doesNotContain(lane);

        lane.accessProfiles(new HashSet<>(Set.of(accessProfileBack)));
        assertThat(lane.getAccessProfiles()).containsOnly(accessProfileBack);
        assertThat(accessProfileBack.getLanes()).containsOnly(lane);

        lane.setAccessProfiles(new HashSet<>());
        assertThat(lane.getAccessProfiles()).doesNotContain(accessProfileBack);
        assertThat(accessProfileBack.getLanes()).doesNotContain(lane);
    }
}
