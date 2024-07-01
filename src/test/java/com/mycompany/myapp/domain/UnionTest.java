package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AddressTestSamples.*;
import static com.mycompany.myapp.domain.UnionTestSamples.*;
import static com.mycompany.myapp.domain.UpazilaTestSamples.*;
import static com.mycompany.myapp.domain.WardTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UnionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Union.class);
        Union union1 = getUnionSample1();
        Union union2 = new Union();
        assertThat(union1).isNotEqualTo(union2);

        union2.setId(union1.getId());
        assertThat(union1).isEqualTo(union2);

        union2 = getUnionSample2();
        assertThat(union1).isNotEqualTo(union2);
    }

    @Test
    void wardTest() {
        Union union = getUnionRandomSampleGenerator();
        Ward wardBack = getWardRandomSampleGenerator();

        union.addWard(wardBack);
        assertThat(union.getWards()).containsOnly(wardBack);
        assertThat(wardBack.getUnion()).isEqualTo(union);

        union.removeWard(wardBack);
        assertThat(union.getWards()).doesNotContain(wardBack);
        assertThat(wardBack.getUnion()).isNull();

        union.wards(new HashSet<>(Set.of(wardBack)));
        assertThat(union.getWards()).containsOnly(wardBack);
        assertThat(wardBack.getUnion()).isEqualTo(union);

        union.setWards(new HashSet<>());
        assertThat(union.getWards()).doesNotContain(wardBack);
        assertThat(wardBack.getUnion()).isNull();
    }

    @Test
    void addressTest() {
        Union union = getUnionRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        union.addAddress(addressBack);
        assertThat(union.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getUnion()).isEqualTo(union);

        union.removeAddress(addressBack);
        assertThat(union.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getUnion()).isNull();

        union.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(union.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getUnion()).isEqualTo(union);

        union.setAddresses(new HashSet<>());
        assertThat(union.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getUnion()).isNull();
    }

    @Test
    void upazilaTest() {
        Union union = getUnionRandomSampleGenerator();
        Upazila upazilaBack = getUpazilaRandomSampleGenerator();

        union.setUpazila(upazilaBack);
        assertThat(union.getUpazila()).isEqualTo(upazilaBack);

        union.upazila(null);
        assertThat(union.getUpazila()).isNull();
    }
}
