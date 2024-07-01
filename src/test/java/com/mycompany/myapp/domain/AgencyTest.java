package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AddressTestSamples.*;
import static com.mycompany.myapp.domain.AgencyLicenseTestSamples.*;
import static com.mycompany.myapp.domain.AgencyTestSamples.*;
import static com.mycompany.myapp.domain.AgencyTypeTestSamples.*;
import static com.mycompany.myapp.domain.AssignmentTestSamples.*;
import static com.mycompany.myapp.domain.DepartmentTestSamples.*;
import static com.mycompany.myapp.domain.DocumentTestSamples.*;
import static com.mycompany.myapp.domain.PassTypeTestSamples.*;
import static com.mycompany.myapp.domain.ZoneTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AgencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agency.class);
        Agency agency1 = getAgencySample1();
        Agency agency2 = new Agency();
        assertThat(agency1).isNotEqualTo(agency2);

        agency2.setId(agency1.getId());
        assertThat(agency1).isEqualTo(agency2);

        agency2 = getAgencySample2();
        assertThat(agency1).isNotEqualTo(agency2);
    }

    @Test
    void addressTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        agency.addAddress(addressBack);
        assertThat(agency.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getAgency()).isEqualTo(agency);

        agency.removeAddress(addressBack);
        assertThat(agency.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getAgency()).isNull();

        agency.addresses(new HashSet<>(Set.of(addressBack)));
        assertThat(agency.getAddresses()).containsOnly(addressBack);
        assertThat(addressBack.getAgency()).isEqualTo(agency);

        agency.setAddresses(new HashSet<>());
        assertThat(agency.getAddresses()).doesNotContain(addressBack);
        assertThat(addressBack.getAgency()).isNull();
    }

    @Test
    void documentTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        agency.addDocument(documentBack);
        assertThat(agency.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getAgency()).isEqualTo(agency);

        agency.removeDocument(documentBack);
        assertThat(agency.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getAgency()).isNull();

        agency.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(agency.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getAgency()).isEqualTo(agency);

        agency.setDocuments(new HashSet<>());
        assertThat(agency.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getAgency()).isNull();
    }

    @Test
    void assignmentTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        Assignment assignmentBack = getAssignmentRandomSampleGenerator();

        agency.addAssignment(assignmentBack);
        assertThat(agency.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getAgency()).isEqualTo(agency);

        agency.removeAssignment(assignmentBack);
        assertThat(agency.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getAgency()).isNull();

        agency.assignments(new HashSet<>(Set.of(assignmentBack)));
        assertThat(agency.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getAgency()).isEqualTo(agency);

        agency.setAssignments(new HashSet<>());
        assertThat(agency.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getAgency()).isNull();
    }

    @Test
    void licenseTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        AgencyLicense agencyLicenseBack = getAgencyLicenseRandomSampleGenerator();

        agency.addLicense(agencyLicenseBack);
        assertThat(agency.getLicenses()).containsOnly(agencyLicenseBack);
        assertThat(agencyLicenseBack.getBelongsTo()).isEqualTo(agency);

        agency.removeLicense(agencyLicenseBack);
        assertThat(agency.getLicenses()).doesNotContain(agencyLicenseBack);
        assertThat(agencyLicenseBack.getBelongsTo()).isNull();

        agency.licenses(new HashSet<>(Set.of(agencyLicenseBack)));
        assertThat(agency.getLicenses()).containsOnly(agencyLicenseBack);
        assertThat(agencyLicenseBack.getBelongsTo()).isEqualTo(agency);

        agency.setLicenses(new HashSet<>());
        assertThat(agency.getLicenses()).doesNotContain(agencyLicenseBack);
        assertThat(agencyLicenseBack.getBelongsTo()).isNull();
    }

    @Test
    void issuerTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        AgencyLicense agencyLicenseBack = getAgencyLicenseRandomSampleGenerator();

        agency.addIssuer(agencyLicenseBack);
        assertThat(agency.getIssuers()).containsOnly(agencyLicenseBack);
        assertThat(agencyLicenseBack.getIssuedBy()).isEqualTo(agency);

        agency.removeIssuer(agencyLicenseBack);
        assertThat(agency.getIssuers()).doesNotContain(agencyLicenseBack);
        assertThat(agencyLicenseBack.getIssuedBy()).isNull();

        agency.issuers(new HashSet<>(Set.of(agencyLicenseBack)));
        assertThat(agency.getIssuers()).containsOnly(agencyLicenseBack);
        assertThat(agencyLicenseBack.getIssuedBy()).isEqualTo(agency);

        agency.setIssuers(new HashSet<>());
        assertThat(agency.getIssuers()).doesNotContain(agencyLicenseBack);
        assertThat(agencyLicenseBack.getIssuedBy()).isNull();
    }

    @Test
    void departmentTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        Department departmentBack = getDepartmentRandomSampleGenerator();

        agency.addDepartment(departmentBack);
        assertThat(agency.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getAgency()).isEqualTo(agency);

        agency.removeDepartment(departmentBack);
        assertThat(agency.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getAgency()).isNull();

        agency.departments(new HashSet<>(Set.of(departmentBack)));
        assertThat(agency.getDepartments()).containsOnly(departmentBack);
        assertThat(departmentBack.getAgency()).isEqualTo(agency);

        agency.setDepartments(new HashSet<>());
        assertThat(agency.getDepartments()).doesNotContain(departmentBack);
        assertThat(departmentBack.getAgency()).isNull();
    }

    @Test
    void agencyTypeTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        AgencyType agencyTypeBack = getAgencyTypeRandomSampleGenerator();

        agency.setAgencyType(agencyTypeBack);
        assertThat(agency.getAgencyType()).isEqualTo(agencyTypeBack);

        agency.agencyType(null);
        assertThat(agency.getAgencyType()).isNull();
    }

    @Test
    void zoneTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        Zone zoneBack = getZoneRandomSampleGenerator();

        agency.addZone(zoneBack);
        assertThat(agency.getZones()).containsOnly(zoneBack);
        assertThat(zoneBack.getAuthority()).isEqualTo(agency);

        agency.removeZone(zoneBack);
        assertThat(agency.getZones()).doesNotContain(zoneBack);
        assertThat(zoneBack.getAuthority()).isNull();

        agency.zones(new HashSet<>(Set.of(zoneBack)));
        assertThat(agency.getZones()).containsOnly(zoneBack);
        assertThat(zoneBack.getAuthority()).isEqualTo(agency);

        agency.setZones(new HashSet<>());
        assertThat(agency.getZones()).doesNotContain(zoneBack);
        assertThat(zoneBack.getAuthority()).isNull();
    }

    @Test
    void passTypeTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        PassType passTypeBack = getPassTypeRandomSampleGenerator();

        agency.addPassType(passTypeBack);
        assertThat(agency.getPassTypes()).containsOnly(passTypeBack);
        assertThat(passTypeBack.getAgencies()).containsOnly(agency);

        agency.removePassType(passTypeBack);
        assertThat(agency.getPassTypes()).doesNotContain(passTypeBack);
        assertThat(passTypeBack.getAgencies()).doesNotContain(agency);

        agency.passTypes(new HashSet<>(Set.of(passTypeBack)));
        assertThat(agency.getPassTypes()).containsOnly(passTypeBack);
        assertThat(passTypeBack.getAgencies()).containsOnly(agency);

        agency.setPassTypes(new HashSet<>());
        assertThat(agency.getPassTypes()).doesNotContain(passTypeBack);
        assertThat(passTypeBack.getAgencies()).doesNotContain(agency);
    }
}
