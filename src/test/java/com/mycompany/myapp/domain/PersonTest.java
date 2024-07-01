package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AddressTestSamples.*;
import static com.mycompany.myapp.domain.AssignmentTestSamples.*;
import static com.mycompany.myapp.domain.CountryTestSamples.*;
import static com.mycompany.myapp.domain.DocumentTestSamples.*;
import static com.mycompany.myapp.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Person.class);
        Person person1 = getPersonSample1();
        Person person2 = new Person();
        assertThat(person1).isNotEqualTo(person2);

        person2.setId(person1.getId());
        assertThat(person1).isEqualTo(person2);

        person2 = getPersonSample2();
        assertThat(person1).isNotEqualTo(person2);
    }

    @Test
    void addressTest() {
        Person person = getPersonRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        person.addAddress(addressBack);
        assertThat(person.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getPerson()).isEqualTo(person);

        person.removeAddress(addressBack);
        assertThat(person.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getPerson()).isNull();

        person.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(person.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getPerson()).isEqualTo(person);

        person.setAddresses(new HashSet<>());
        assertThat(person.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getPerson()).isNull();
    }

    @Test
    void documentTest() {
        Person person = getPersonRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        person.addDocument(documentBack);
        assertThat(person.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getPerson()).isEqualTo(person);

        person.removeDocument(documentBack);
        assertThat(person.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getPerson()).isNull();

        person.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(person.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getPerson()).isEqualTo(person);

        person.setDocuments(new HashSet<>());
        assertThat(person.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getPerson()).isNull();
    }

    @Test
    void assignmentTest() {
        Person person = getPersonRandomSampleGenerator();
        Assignment assignmentBack = getAssignmentRandomSampleGenerator();

        person.addAssignment(assignmentBack);
        assertThat(person.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getPerson()).isEqualTo(person);

        person.removeAssignment(assignmentBack);
        assertThat(person.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getPerson()).isNull();

        person.assignments(new HashSet<>(Set.of(assignmentBack)));
        assertThat(person.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getPerson()).isEqualTo(person);

        person.setAssignments(new HashSet<>());
        assertThat(person.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getPerson()).isNull();
    }

    @Test
    void nationalityTest() {
        Person person = getPersonRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        person.setNationality(countryBack);
        assertThat(person.getNationality()).isEqualTo(countryBack);

        person.nationality(null);
        assertThat(person.getNationality()).isNull();
    }
}
