package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CityCorpPouraTestSamples.*;
import static com.mycompany.myapp.domain.DistrictTestSamples.*;
import static com.mycompany.myapp.domain.UnionTestSamples.*;
import static com.mycompany.myapp.domain.UpazilaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UpazilaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Upazila.class);
        Upazila upazila1 = getUpazilaSample1();
        Upazila upazila2 = new Upazila();
        assertThat(upazila1).isNotEqualTo(upazila2);

        upazila2.setId(upazila1.getId());
        assertThat(upazila1).isEqualTo(upazila2);

        upazila2 = getUpazilaSample2();
        assertThat(upazila1).isNotEqualTo(upazila2);
    }

    @Test
    void unionTest() {
        Upazila upazila = getUpazilaRandomSampleGenerator();
        Union unionBack = getUnionRandomSampleGenerator();

        upazila.addUnion(unionBack);
        assertThat(upazila.getUnions()).containsOnly(unionBack);
        assertThat(unionBack.getUpazila()).isEqualTo(upazila);

        upazila.removeUnion(unionBack);
        assertThat(upazila.getUnions()).doesNotContain(unionBack);
        assertThat(unionBack.getUpazila()).isNull();

        upazila.unions(new HashSet<>(Set.of(unionBack)));
        assertThat(upazila.getUnions()).containsOnly(unionBack);
        assertThat(unionBack.getUpazila()).isEqualTo(upazila);

        upazila.setUnions(new HashSet<>());
        assertThat(upazila.getUnions()).doesNotContain(unionBack);
        assertThat(unionBack.getUpazila()).isNull();
    }

    @Test
    void cityCorpPouraTest() {
        Upazila upazila = getUpazilaRandomSampleGenerator();
        CityCorpPoura cityCorpPouraBack = getCityCorpPouraRandomSampleGenerator();

        upazila.addCityCorpPoura(cityCorpPouraBack);
        assertThat(upazila.getCityCorpPouras()).containsOnly(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getUpazila()).isEqualTo(upazila);

        upazila.removeCityCorpPoura(cityCorpPouraBack);
        assertThat(upazila.getCityCorpPouras()).doesNotContain(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getUpazila()).isNull();

        upazila.cityCorpPouras(new HashSet<>(Set.of(cityCorpPouraBack)));
        assertThat(upazila.getCityCorpPouras()).containsOnly(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getUpazila()).isEqualTo(upazila);

        upazila.setCityCorpPouras(new HashSet<>());
        assertThat(upazila.getCityCorpPouras()).doesNotContain(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getUpazila()).isNull();
    }

    @Test
    void districtTest() {
        Upazila upazila = getUpazilaRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        upazila.setDistrict(districtBack);
        assertThat(upazila.getDistrict()).isEqualTo(districtBack);

        upazila.district(null);
        assertThat(upazila.getDistrict()).isNull();
    }
}
