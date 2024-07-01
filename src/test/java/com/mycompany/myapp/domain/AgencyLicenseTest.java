package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AgencyLicenseTestSamples.*;
import static com.mycompany.myapp.domain.AgencyLicenseTypeTestSamples.*;
import static com.mycompany.myapp.domain.AgencyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgencyLicenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgencyLicense.class);
        AgencyLicense agencyLicense1 = getAgencyLicenseSample1();
        AgencyLicense agencyLicense2 = new AgencyLicense();
        assertThat(agencyLicense1).isNotEqualTo(agencyLicense2);

        agencyLicense2.setId(agencyLicense1.getId());
        assertThat(agencyLicense1).isEqualTo(agencyLicense2);

        agencyLicense2 = getAgencyLicenseSample2();
        assertThat(agencyLicense1).isNotEqualTo(agencyLicense2);
    }

    @Test
    void agencyLicenseTypeTest() {
        AgencyLicense agencyLicense = getAgencyLicenseRandomSampleGenerator();
        AgencyLicenseType agencyLicenseTypeBack = getAgencyLicenseTypeRandomSampleGenerator();

        agencyLicense.setAgencyLicenseType(agencyLicenseTypeBack);
        assertThat(agencyLicense.getAgencyLicenseType()).isEqualTo(agencyLicenseTypeBack);

        agencyLicense.agencyLicenseType(null);
        assertThat(agencyLicense.getAgencyLicenseType()).isNull();
    }

    @Test
    void belongsToTest() {
        AgencyLicense agencyLicense = getAgencyLicenseRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        agencyLicense.setBelongsTo(agencyBack);
        assertThat(agencyLicense.getBelongsTo()).isEqualTo(agencyBack);

        agencyLicense.belongsTo(null);
        assertThat(agencyLicense.getBelongsTo()).isNull();
    }

    @Test
    void issuedByTest() {
        AgencyLicense agencyLicense = getAgencyLicenseRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        agencyLicense.setIssuedBy(agencyBack);
        assertThat(agencyLicense.getIssuedBy()).isEqualTo(agencyBack);

        agencyLicense.issuedBy(null);
        assertThat(agencyLicense.getIssuedBy()).isNull();
    }
}
