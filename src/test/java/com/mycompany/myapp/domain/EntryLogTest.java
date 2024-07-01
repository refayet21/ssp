package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EntryLogTestSamples.*;
import static com.mycompany.myapp.domain.LaneTestSamples.*;
import static com.mycompany.myapp.domain.PassTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntryLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntryLog.class);
        EntryLog entryLog1 = getEntryLogSample1();
        EntryLog entryLog2 = new EntryLog();
        assertThat(entryLog1).isNotEqualTo(entryLog2);

        entryLog2.setId(entryLog1.getId());
        assertThat(entryLog1).isEqualTo(entryLog2);

        entryLog2 = getEntryLogSample2();
        assertThat(entryLog1).isNotEqualTo(entryLog2);
    }

    @Test
    void passTest() {
        EntryLog entryLog = getEntryLogRandomSampleGenerator();
        Pass passBack = getPassRandomSampleGenerator();

        entryLog.setPass(passBack);
        assertThat(entryLog.getPass()).isEqualTo(passBack);

        entryLog.pass(null);
        assertThat(entryLog.getPass()).isNull();
    }

    @Test
    void laneTest() {
        EntryLog entryLog = getEntryLogRandomSampleGenerator();
        Lane laneBack = getLaneRandomSampleGenerator();

        entryLog.setLane(laneBack);
        assertThat(entryLog.getLane()).isEqualTo(laneBack);

        entryLog.lane(null);
        assertThat(entryLog.getLane()).isNull();
    }
}
