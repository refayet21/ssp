package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AddressTestSamples.*;
import static com.mycompany.myapp.domain.CityCorpPouraTestSamples.*;
import static com.mycompany.myapp.domain.DistrictTestSamples.*;
import static com.mycompany.myapp.domain.RMOTestSamples.*;
import static com.mycompany.myapp.domain.UpazilaTestSamples.*;
import static com.mycompany.myapp.domain.WardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CityCorpPouraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CityCorpPoura.class);
        CityCorpPoura cityCorpPoura1 = getCityCorpPouraSample1();
        CityCorpPoura cityCorpPoura2 = new CityCorpPoura();
        assertThat(cityCorpPoura1).isNotEqualTo(cityCorpPoura2);

        cityCorpPoura2.setId(cityCorpPoura1.getId());
        assertThat(cityCorpPoura1).isEqualTo(cityCorpPoura2);

        cityCorpPoura2 = getCityCorpPouraSample2();
        assertThat(cityCorpPoura1).isNotEqualTo(cityCorpPoura2);
    }

    @Test
    void wardTest() {
        CityCorpPoura cityCorpPoura = getCityCorpPouraRandomSampleGenerator();
        Ward wardBack = getWardRandomSampleGenerator();

        cityCorpPoura.addWard(wardBack);
        assertThat(cityCorpPoura.getWards()).containsOnly(wardBack);
        assertThat(wardBack.getCityCorpPoura()).isEqualTo(cityCorpPoura);

        cityCorpPoura.removeWard(wardBack);
        assertThat(cityCorpPoura.getWards()).doesNotContain(wardBack);
        assertThat(wardBack.getCityCorpPoura()).isNull();

        cityCorpPoura.wards(new HashSet<>(Set.of(wardBack)));
        assertThat(cityCorpPoura.getWards()).containsOnly(wardBack);
        assertThat(wardBack.getCityCorpPoura()).isEqualTo(cityCorpPoura);

        cityCorpPoura.setWards(new HashSet<>());
        assertThat(cityCorpPoura.getWards()).doesNotContain(wardBack);
        assertThat(wardBack.getCityCorpPoura()).isNull();
    }

    @Test
    void addressTest() {
        CityCorpPoura cityCorpPoura = getCityCorpPouraRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        cityCorpPoura.addAddress(addressBack);
        assertThat(cityCorpPoura.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCityCorpPoura()).isEqualTo(cityCorpPoura);

        cityCorpPoura.removeAddress(addressBack);
        assertThat(cityCorpPoura.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCityCorpPoura()).isNull();

        cityCorpPoura.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(cityCorpPoura.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCityCorpPoura()).isEqualTo(cityCorpPoura);

        cityCorpPoura.setAddresses(new HashSet<>());
        assertThat(cityCorpPoura.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCityCorpPoura()).isNull();
    }

    @Test
    void districtTest() {
        CityCorpPoura cityCorpPoura = getCityCorpPouraRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        cityCorpPoura.setDistrict(districtBack);
        assertThat(cityCorpPoura.getDistrict()).isEqualTo(districtBack);

        cityCorpPoura.district(null);
        assertThat(cityCorpPoura.getDistrict()).isNull();
    }

    @Test
    void upazilaTest() {
        CityCorpPoura cityCorpPoura = getCityCorpPouraRandomSampleGenerator();
        Upazila upazilaBack = getUpazilaRandomSampleGenerator();

        cityCorpPoura.setUpazila(upazilaBack);
        assertThat(cityCorpPoura.getUpazila()).isEqualTo(upazilaBack);

        cityCorpPoura.upazila(null);
        assertThat(cityCorpPoura.getUpazila()).isNull();
    }

    @Test
    void rmoTest() {
        CityCorpPoura cityCorpPoura = getCityCorpPouraRandomSampleGenerator();
        RMO rMOBack = getRMORandomSampleGenerator();

        cityCorpPoura.setRmo(rMOBack);
        assertThat(cityCorpPoura.getRmo()).isEqualTo(rMOBack);

        cityCorpPoura.rmo(null);
        assertThat(cityCorpPoura.getRmo()).isNull();
    }
}
