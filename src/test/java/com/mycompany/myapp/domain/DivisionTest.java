package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DistrictTestSamples.*;
import static com.mycompany.myapp.domain.DivisionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DivisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Division.class);
        Division division1 = getDivisionSample1();
        Division division2 = new Division();
        assertThat(division1).isNotEqualTo(division2);

        division2.setId(division1.getId());
        assertThat(division1).isEqualTo(division2);

        division2 = getDivisionSample2();
        assertThat(division1).isNotEqualTo(division2);
    }

    @Test
    void districtTest() {
        Division division = getDivisionRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        division.addDistrict(districtBack);
        assertThat(division.getDistricts()).containsOnly(districtBack);
        assertThat(districtBack.getDivision()).isEqualTo(division);

        division.removeDistrict(districtBack);
        assertThat(division.getDistricts()).doesNotContain(districtBack);
        assertThat(districtBack.getDivision()).isNull();

        division.districts(new HashSet<>(Set.of(districtBack)));
        assertThat(division.getDistricts()).containsOnly(districtBack);
        assertThat(districtBack.getDivision()).isEqualTo(division);

        division.setDistricts(new HashSet<>());
        assertThat(division.getDistricts()).doesNotContain(districtBack);
        assertThat(districtBack.getDivision()).isNull();
    }
}
