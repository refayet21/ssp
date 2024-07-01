package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AddressTestSamples.*;
import static com.mycompany.myapp.domain.AgencyTestSamples.*;
import static com.mycompany.myapp.domain.CityCorpPouraTestSamples.*;
import static com.mycompany.myapp.domain.CountryTestSamples.*;
import static com.mycompany.myapp.domain.PersonTestSamples.*;
import static com.mycompany.myapp.domain.PostOfficeTestSamples.*;
import static com.mycompany.myapp.domain.UnionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = getAddressSample1();
        Address address2 = new Address();
        assertThat(address1).isNotEqualTo(address2);

        address2.setId(address1.getId());
        assertThat(address1).isEqualTo(address2);

        address2 = getAddressSample2();
        assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    void cityCorpPouraTest() {
        Address address = getAddressRandomSampleGenerator();
        CityCorpPoura cityCorpPouraBack = getCityCorpPouraRandomSampleGenerator();

        address.setCityCorpPoura(cityCorpPouraBack);
        assertThat(address.getCityCorpPoura()).isEqualTo(cityCorpPouraBack);

        address.cityCorpPoura(null);
        assertThat(address.getCityCorpPoura()).isNull();
    }

    @Test
    void unionTest() {
        Address address = getAddressRandomSampleGenerator();
        Union unionBack = getUnionRandomSampleGenerator();

        address.setUnion(unionBack);
        assertThat(address.getUnion()).isEqualTo(unionBack);

        address.union(null);
        assertThat(address.getUnion()).isNull();
    }

    @Test
    void postOfficeTest() {
        Address address = getAddressRandomSampleGenerator();
        PostOffice postOfficeBack = getPostOfficeRandomSampleGenerator();

        address.setPostOffice(postOfficeBack);
        assertThat(address.getPostOffice()).isEqualTo(postOfficeBack);

        address.postOffice(null);
        assertThat(address.getPostOffice()).isNull();
    }

    @Test
    void countryTest() {
        Address address = getAddressRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        address.setCountry(countryBack);
        assertThat(address.getCountry()).isEqualTo(countryBack);

        address.country(null);
        assertThat(address.getCountry()).isNull();
    }

    @Test
    void personTest() {
        Address address = getAddressRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        address.setPerson(personBack);
        assertThat(address.getPerson()).isEqualTo(personBack);

        address.person(null);
        assertThat(address.getPerson()).isNull();
    }

    @Test
    void agencyTest() {
        Address address = getAddressRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        address.setAgency(agencyBack);
        assertThat(address.getAgency()).isEqualTo(agencyBack);

        address.agency(null);
        assertThat(address.getAgency()).isNull();
    }
}
