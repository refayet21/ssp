package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CityCorpPouraTestSamples.*;
import static com.mycompany.myapp.domain.RMOTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RMOTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RMO.class);
        RMO rMO1 = getRMOSample1();
        RMO rMO2 = new RMO();
        assertThat(rMO1).isNotEqualTo(rMO2);

        rMO2.setId(rMO1.getId());
        assertThat(rMO1).isEqualTo(rMO2);

        rMO2 = getRMOSample2();
        assertThat(rMO1).isNotEqualTo(rMO2);
    }

    @Test
    void cityCorpPouraTest() {
        RMO rMO = getRMORandomSampleGenerator();
        CityCorpPoura cityCorpPouraBack = getCityCorpPouraRandomSampleGenerator();

        rMO.addCityCorpPoura(cityCorpPouraBack);
        assertThat(rMO.getCityCorpPouras()).containsOnly(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getRmo()).isEqualTo(rMO);

        rMO.removeCityCorpPoura(cityCorpPouraBack);
        assertThat(rMO.getCityCorpPouras()).doesNotContain(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getRmo()).isNull();

        rMO.cityCorpPouras(new HashSet<>(Set.of(cityCorpPouraBack)));
        assertThat(rMO.getCityCorpPouras()).containsOnly(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getRmo()).isEqualTo(rMO);

        rMO.setCityCorpPouras(new HashSet<>());
        assertThat(rMO.getCityCorpPouras()).doesNotContain(cityCorpPouraBack);
        assertThat(cityCorpPouraBack.getRmo()).isNull();
    }
}
