package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AddressTestSamples.*;
import static com.mycompany.myapp.domain.DistrictTestSamples.*;
import static com.mycompany.myapp.domain.PostOfficeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PostOfficeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostOffice.class);
        PostOffice postOffice1 = getPostOfficeSample1();
        PostOffice postOffice2 = new PostOffice();
        assertThat(postOffice1).isNotEqualTo(postOffice2);

        postOffice2.setId(postOffice1.getId());
        assertThat(postOffice1).isEqualTo(postOffice2);

        postOffice2 = getPostOfficeSample2();
        assertThat(postOffice1).isNotEqualTo(postOffice2);
    }

    @Test
    void addressTest() {
        PostOffice postOffice = getPostOfficeRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        postOffice.addAddress(addressBack);
        assertThat(postOffice.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getPostOffice()).isEqualTo(postOffice);

        postOffice.removeAddress(addressBack);
        assertThat(postOffice.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getPostOffice()).isNull();

        postOffice.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(postOffice.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getPostOffice()).isEqualTo(postOffice);

        postOffice.setAddresses(new HashSet<>());
        assertThat(postOffice.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getPostOffice()).isNull();
    }

    @Test
    void districtTest() {
        PostOffice postOffice = getPostOfficeRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        postOffice.setDistrict(districtBack);
        assertThat(postOffice.getDistrict()).isEqualTo(districtBack);

        postOffice.district(null);
        assertThat(postOffice.getDistrict()).isNull();
    }
}
