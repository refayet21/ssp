package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CityCorpPouraTestSamples.*;
import static com.mycompany.myapp.domain.DistrictTestSamples.*;
import static com.mycompany.myapp.domain.DivisionTestSamples.*;
import static com.mycompany.myapp.domain.PostOfficeTestSamples.*;
import static com.mycompany.myapp.domain.UpazilaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DistrictTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(District.class);
        District district1 = getDistrictSample1();
        District district2 = new District();
        assertThat(district1).isNotEqualTo(district2);

        district2.setId(district1.getId());
        assertThat(district1).isEqualTo(district2);

        district2 = getDistrictSample2();
        assertThat(district1).isNotEqualTo(district2);
    }

    @Test
    void upazilaTest() {
        District district = getDistrictRandomSampleGenerator();
        Upazila upazilaBack = getUpazilaRandomSampleGenerator();

        district.addUpazila(upazilaBack);
        assertThat(district.getUpazilas()).containsOnly(upazilaBack);
        assertThat(upazilaBack.getDistrict()).isEqualTo(district);

        district.removeUpazila(upazilaBack);
        assertThat(district.getUpazilas()).doesNotContain(upazilaBack);
        assertThat(upazilaBack.getDistrict()).isNull();

        district.upazilas(new HashSet<>(Set.of(upazilaBack)));
        assertThat(district.getUpazilas()).containsOnly(upazilaBack);
        assertThat(upazilaBack.getDistrict()).isEqualTo(district);

        district.setUpazilas(new HashSet<>());
        assertThat(district.getUpazilas()).doesNotContain(upazilaBack);
        assertThat(upazilaBack.getDistrict()).isNull();
    }

    @Test
    void cityCorpPouraTest() {
        District district = getDistrictRandomSampleGenerator();
        CityCorpPoura cityCorpPouraBack = getCityCorpPouraRandomSampleGenerator();

        district.addCityCorpPoura(cityCorpPouraBack);
        assertThat(district.getCityCorpPouras()).containsOnly(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getDistrict()).isEqualTo(district);

        district.removeCityCorpPoura(cityCorpPouraBack);
        assertThat(district.getCityCorpPouras()).doesNotContain(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getDistrict()).isNull();

        district.cityCorpPouras(new HashSet<>(Set.of(cityCorpPouraBack)));
        assertThat(district.getCityCorpPouras()).containsOnly(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getDistrict()).isEqualTo(district);

        district.setCityCorpPouras(new HashSet<>());
        assertThat(district.getCityCorpPouras()).doesNotContain(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getDistrict()).isNull();
    }

    @Test
    void postOfficeTest() {
        District district = getDistrictRandomSampleGenerator();
        PostOffice postOfficeBack = getPostOfficeRandomSampleGenerator();

        district.addPostOffice(postOfficeBack);
        assertThat(district.getPostOffices()).containsOnly(postOfficeBack);
        assertThat(postOfficeBack.getDistrict()).isEqualTo(district);

        district.removePostOffice(postOfficeBack);
        assertThat(district.getPostOffices()).doesNotContain(postOfficeBack);
        assertThat(postOfficeBack.getDistrict()).isNull();

        district.postOffices(new HashSet<>(Set.of(postOfficeBack)));
        assertThat(district.getPostOffices()).containsOnly(postOfficeBack);
        assertThat(postOfficeBack.getDistrict()).isEqualTo(district);

        district.setPostOffices(new HashSet<>());
        assertThat(district.getPostOffices()).doesNotContain(postOfficeBack);
        assertThat(postOfficeBack.getDistrict()).isNull();
    }

    @Test
    void divisionTest() {
        District district = getDistrictRandomSampleGenerator();
        Division divisionBack = getDivisionRandomSampleGenerator();

        district.setDivision(divisionBack);
        assertThat(district.getDivision()).isEqualTo(divisionBack);

        district.division(null);
        assertThat(district.getDivision()).isNull();
    }
}
