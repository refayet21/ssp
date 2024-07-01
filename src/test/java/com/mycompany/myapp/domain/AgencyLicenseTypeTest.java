package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AgencyLicenseTestSamples.*;
import static com.mycompany.myapp.domain.AgencyLicenseTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AgencyLicenseTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgencyLicenseType.class);
        AgencyLicenseType agencyLicenseType1 = getAgencyLicenseTypeSample1();
        AgencyLicenseType agencyLicenseType2 = new AgencyLicenseType();
        assertThat(agencyLicenseType1).isNotEqualTo(agencyLicenseType2);

        agencyLicenseType2.setId(agencyLicenseType1.getId());
        assertThat(agencyLicenseType1).isEqualTo(agencyLicenseType2);

        agencyLicenseType2 = getAgencyLicenseTypeSample2();
        assertThat(agencyLicenseType1).isNotEqualTo(agencyLicenseType2);
    }

    @Test
    void agencyLicenseTest() {
        AgencyLicenseType agencyLicenseType = getAgencyLicenseTypeRandomSampleGenerator();
        AgencyLicense agencyLicenseBack = getAgencyLicenseRandomSampleGenerator();

        agencyLicenseType.addAgencyLicense(agencyLicenseBack);
        assertThat(agencyLicenseType.getAgencyLicenses()).containsOnly(agencyLicenseBack);
        assertThat(agencyLicenseBack.getAgencyLicenseType()).isEqualTo(agencyLicenseType);

        agencyLicenseType.removeAgencyLicense(agencyLicenseBack);
        assertThat(agencyLicenseType.getAgencyLicenses()).doesNotContain(agencyLicenseBack);
        assertThat(agencyLicenseBack.getAgencyLicenseType()).isNull();

        agencyLicenseType.agencyLicenses(new HashSet<>(Set.of(agencyLicenseBack)));
        assertThat(agencyLicenseType.getAgencyLicenses()).containsOnly(agencyLicenseBack);
        assertThat(agencyLicenseBack.getAgencyLicenseType()).isEqualTo(agencyLicenseType);

        agencyLicenseType.setAgencyLicenses(new HashSet<>());
        assertThat(agencyLicenseType.getAgencyLicenses()).doesNotContain(agencyLicenseBack);
        assertThat(agencyLicenseBack.getAgencyLicenseType()).isNull();
    }
}
