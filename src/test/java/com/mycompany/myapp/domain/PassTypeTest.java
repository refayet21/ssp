package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AgencyTestSamples.*;
import static com.mycompany.myapp.domain.PassTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PassTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PassType.class);
        PassType passType1 = getPassTypeSample1();
        PassType passType2 = new PassType();
        assertThat(passType1).isNotEqualTo(passType2);

        passType2.setId(passType1.getId());
        assertThat(passType1).isEqualTo(passType2);

        passType2 = getPassTypeSample2();
        assertThat(passType1).isNotEqualTo(passType2);
    }

    @Test
    void agencyTest() {
        PassType passType = getPassTypeRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        passType.addAgency(agencyBack);
        assertThat(passType.getAgencies()).containsOnly(agencyBack);

        passType.removeAgency(agencyBack);
        assertThat(passType.getAgencies()).doesNotContain(agencyBack);

        passType.agencies(new HashSet<>(Set.of(agencyBack)));
        assertThat(passType.getAgencies()).containsOnly(agencyBack);

        passType.setAgencies(new HashSet<>());
        assertThat(passType.getAgencies()).doesNotContain(agencyBack);
    }
}
