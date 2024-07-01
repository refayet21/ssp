package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AgencyTestSamples.*;
import static com.mycompany.myapp.domain.DocumentTestSamples.*;
import static com.mycompany.myapp.domain.DocumentTypeTestSamples.*;
import static com.mycompany.myapp.domain.PersonTestSamples.*;
import static com.mycompany.myapp.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = getDocumentSample1();
        Document document2 = new Document();
        assertThat(document1).isNotEqualTo(document2);

        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);

        document2 = getDocumentSample2();
        assertThat(document1).isNotEqualTo(document2);
    }

    @Test
    void documentTypeTest() {
        Document document = getDocumentRandomSampleGenerator();
        DocumentType documentTypeBack = getDocumentTypeRandomSampleGenerator();

        document.setDocumentType(documentTypeBack);
        assertThat(document.getDocumentType()).isEqualTo(documentTypeBack);

        document.documentType(null);
        assertThat(document.getDocumentType()).isNull();
    }

    @Test
    void personTest() {
        Document document = getDocumentRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        document.setPerson(personBack);
        assertThat(document.getPerson()).isEqualTo(personBack);

        document.person(null);
        assertThat(document.getPerson()).isNull();
    }

    @Test
    void vehicleTest() {
        Document document = getDocumentRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        document.setVehicle(vehicleBack);
        assertThat(document.getVehicle()).isEqualTo(vehicleBack);

        document.vehicle(null);
        assertThat(document.getVehicle()).isNull();
    }

    @Test
    void agencyTest() {
        Document document = getDocumentRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        document.setAgency(agencyBack);
        assertThat(document.getAgency()).isEqualTo(agencyBack);

        document.agency(null);
        assertThat(document.getAgency()).isNull();
    }
}
