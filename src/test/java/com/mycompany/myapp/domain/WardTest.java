package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CityCorpPouraTestSamples.*;
import static com.mycompany.myapp.domain.UnionTestSamples.*;
import static com.mycompany.myapp.domain.WardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ward.class);
        Ward ward1 = getWardSample1();
        Ward ward2 = new Ward();
        assertThat(ward1).isNotEqualTo(ward2);

        ward2.setId(ward1.getId());
        assertThat(ward1).isEqualTo(ward2);

        ward2 = getWardSample2();
        assertThat(ward1).isNotEqualTo(ward2);
    }

    @Test
    void cityCorpPouraTest() {
        Ward ward = getWardRandomSampleGenerator();
        CityCorpPoura cityCorpPouraBack = getCityCorpPouraRandomSampleGenerator();

        ward.setCityCorpPoura(cityCorpPouraBack);
        assertThat(ward.getCityCorpPoura()).isEqualTo(cityCorpPouraBack);

        ward.cityCorpPoura(null);
        assertThat(ward.getCityCorpPoura()).isNull();
    }

    @Test
    void unionTest() {
        Ward ward = getWardRandomSampleGenerator();
        Union unionBack = getUnionRandomSampleGenerator();

        ward.setUnion(unionBack);
        assertThat(ward.getUnion()).isEqualTo(unionBack);

        ward.union(null);
        assertThat(ward.getUnion()).isNull();
    }
}
