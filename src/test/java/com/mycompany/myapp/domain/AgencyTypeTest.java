package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AgencyTestSamples.*;
import static com.mycompany.myapp.domain.AgencyTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AgencyTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgencyType.class);
        AgencyType agencyType1 = getAgencyTypeSample1();
        AgencyType agencyType2 = new AgencyType();
        assertThat(agencyType1).isNotEqualTo(agencyType2);

        agencyType2.setId(agencyType1.getId());
        assertThat(agencyType1).isEqualTo(agencyType2);

        agencyType2 = getAgencyTypeSample2();
        assertThat(agencyType1).isNotEqualTo(agencyType2);
    }

    @Test
    void agencyTest() {
        AgencyType agencyType = getAgencyTypeRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        agencyType.addAgency(agencyBack);
        assertThat(agencyType.getAgencies()).containsOnly(agencyBack);
        assertThat(agencyBack.getAgencyType()).isEqualTo(agencyType);

        agencyType.removeAgency(agencyBack);
        assertThat(agencyType.getAgencies()).doesNotContain(agencyBack);
        assertThat(agencyBack.getAgencyType()).isNull();

        agencyType.agencies(new HashSet<>(Set.of(agencyBack)));
        assertThat(agencyType.getAgencies()).containsOnly(agencyBack);
        assertThat(agencyBack.getAgencyType()).isEqualTo(agencyType);

        agencyType.setAgencies(new HashSet<>());
        assertThat(agencyType.getAgencies()).doesNotContain(agencyBack);
        assertThat(agencyBack.getAgencyType()).isNull();
    }
}
