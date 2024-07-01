package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AddressTestSamples.*;
import static com.mycompany.myapp.domain.CountryTestSamples.*;
import static com.mycompany.myapp.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void personTest() {
        Country country = getCountryRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        country.addPerson(personBack);
        assertThat(country.getPeople()).containsOnly(personBack);
        assertThat(personBack.getNationality()).isEqualTo(country);

        country.removePerson(personBack);
        assertThat(country.getPeople()).doesNotContain(personBack);
        assertThat(personBack.getNationality()).isNull();

        country.people(new HashSet<>(Set.of(personBack)));
        assertThat(country.getPeople()).containsOnly(personBack);
        assertThat(personBack.getNationality()).isEqualTo(country);

        country.setPeople(new HashSet<>());
        assertThat(country.getPeople()).doesNotContain(personBack);
        assertThat(personBack.getNationality()).isNull();
    }

    @Test
    void addressTest() {
        Country country = getCountryRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        country.addAddress(addressBack);
        assertThat(country.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCountry()).isEqualTo(country);

        country.removeAddress(addressBack);
        assertThat(country.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCountry()).isNull();

        country.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(country.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getCountry()).isEqualTo(country);

        country.setAddresses(new HashSet<>());
        assertThat(country.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getCountry()).isNull();
    }
}
