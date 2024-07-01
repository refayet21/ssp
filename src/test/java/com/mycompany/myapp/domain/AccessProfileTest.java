package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AccessProfileTestSamples.*;
import static com.mycompany.myapp.domain.LaneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AccessProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccessProfile.class);
        AccessProfile accessProfile1 = getAccessProfileSample1();
        AccessProfile accessProfile2 = new AccessProfile();
        assertThat(accessProfile1).isNotEqualTo(accessProfile2);

        accessProfile2.setId(accessProfile1.getId());
        assertThat(accessProfile1).isEqualTo(accessProfile2);

        accessProfile2 = getAccessProfileSample2();
        assertThat(accessProfile1).isNotEqualTo(accessProfile2);
    }

    @Test
    void laneTest() {
        AccessProfile accessProfile = getAccessProfileRandomSampleGenerator();
        Lane laneBack = getLaneRandomSampleGenerator();

        accessProfile.addLane(laneBack);
        assertThat(accessProfile.getLanes()).containsOnly(laneBack);

        accessProfile.removeLane(laneBack);
        assertThat(accessProfile.getLanes()).doesNotContain(laneBack);

        accessProfile.lanes(new HashSet<>(Set.of(laneBack)));
        assertThat(accessProfile.getLanes()).containsOnly(laneBack);

        accessProfile.setLanes(new HashSet<>());
        assertThat(accessProfile.getLanes()).doesNotContain(laneBack);
    }
}
