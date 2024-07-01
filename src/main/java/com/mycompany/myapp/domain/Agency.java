package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The Entity entity.
 * @author A true hipster
 */
@Schema(description = "The Entity entity.\n@author A true hipster")
@Entity
@Table(name = "agency")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * fieldName
     */
    @Schema(description = "fieldName", required = true)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "is_internal")
    private Boolean isInternal;

    @Column(name = "is_dummy")
    private Boolean isDummy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @JsonIgnoreProperties(value = { "cityCorpPoura", "union", "postOffice", "country", "person", "agency" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @JsonIgnoreProperties(value = { "verifiedBy", "documentType", "person", "vehicle", "agency" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @JsonIgnoreProperties(value = { "person", "designation", "agency" }, allowSetters = true)
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "belongsTo")
    @JsonIgnoreProperties(value = { "verifiedBy", "agencyLicenseType", "belongsTo", "issuedBy" }, allowSetters = true)
    private Set<AgencyLicense> licenses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "issuedBy")
    @JsonIgnoreProperties(value = { "verifiedBy", "agencyLicenseType", "belongsTo", "issuedBy" }, allowSetters = true)
    private Set<AgencyLicense> issuers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @JsonIgnoreProperties(value = { "agency" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "agencies" }, allowSetters = true)
    private AgencyType agencyType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authority")
    @JsonIgnoreProperties(value = { "authority", "gates" }, allowSetters = true)
    private Set<Zone> zones = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "agencies")
    @JsonIgnoreProperties(value = { "agencies" }, allowSetters = true)
    private Set<PassType> passTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Agency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Agency name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Agency shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Boolean getIsInternal() {
        return this.isInternal;
    }

    public Agency isInternal(Boolean isInternal) {
        this.setIsInternal(isInternal);
        return this;
    }

    public void setIsInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }

    public Boolean getIsDummy() {
        return this.isDummy;
    }

    public Agency isDummy(Boolean isDummy) {
        this.setIsDummy(isDummy);
        return this;
    }

    public void setIsDummy(Boolean isDummy) {
        this.isDummy = isDummy;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setAgency(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setAgency(this));
        }
        this.addresses = addresses;
    }

    public Agency addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Agency addAddress(Address address) {
        this.addresses.add(address);
        address.setAgency(this);
        return this;
    }

    public Agency removeAddress(Address address) {
        this.addresses.remove(address);
        address.setAgency(null);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setAgency(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setAgency(this));
        }
        this.documents = documents;
    }

    public Agency documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Agency addDocument(Document document) {
        this.documents.add(document);
        document.setAgency(this);
        return this;
    }

    public Agency removeDocument(Document document) {
        this.documents.remove(document);
        document.setAgency(null);
        return this;
    }

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        if (this.assignments != null) {
            this.assignments.forEach(i -> i.setAgency(null));
        }
        if (assignments != null) {
            assignments.forEach(i -> i.setAgency(this));
        }
        this.assignments = assignments;
    }

    public Agency assignments(Set<Assignment> assignments) {
        this.setAssignments(assignments);
        return this;
    }

    public Agency addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setAgency(this);
        return this;
    }

    public Agency removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.setAgency(null);
        return this;
    }

    public Set<AgencyLicense> getLicenses() {
        return this.licenses;
    }

    public void setLicenses(Set<AgencyLicense> agencyLicenses) {
        if (this.licenses != null) {
            this.licenses.forEach(i -> i.setBelongsTo(null));
        }
        if (agencyLicenses != null) {
            agencyLicenses.forEach(i -> i.setBelongsTo(this));
        }
        this.licenses = agencyLicenses;
    }

    public Agency licenses(Set<AgencyLicense> agencyLicenses) {
        this.setLicenses(agencyLicenses);
        return this;
    }

    public Agency addLicense(AgencyLicense agencyLicense) {
        this.licenses.add(agencyLicense);
        agencyLicense.setBelongsTo(this);
        return this;
    }

    public Agency removeLicense(AgencyLicense agencyLicense) {
        this.licenses.remove(agencyLicense);
        agencyLicense.setBelongsTo(null);
        return this;
    }

    public Set<AgencyLicense> getIssuers() {
        return this.issuers;
    }

    public void setIssuers(Set<AgencyLicense> agencyLicenses) {
        if (this.issuers != null) {
            this.issuers.forEach(i -> i.setIssuedBy(null));
        }
        if (agencyLicenses != null) {
            agencyLicenses.forEach(i -> i.setIssuedBy(this));
        }
        this.issuers = agencyLicenses;
    }

    public Agency issuers(Set<AgencyLicense> agencyLicenses) {
        this.setIssuers(agencyLicenses);
        return this;
    }

    public Agency addIssuer(AgencyLicense agencyLicense) {
        this.issuers.add(agencyLicense);
        agencyLicense.setIssuedBy(this);
        return this;
    }

    public Agency removeIssuer(AgencyLicense agencyLicense) {
        this.issuers.remove(agencyLicense);
        agencyLicense.setIssuedBy(null);
        return this;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(Set<Department> departments) {
        if (this.departments != null) {
            this.departments.forEach(i -> i.setAgency(null));
        }
        if (departments != null) {
            departments.forEach(i -> i.setAgency(this));
        }
        this.departments = departments;
    }

    public Agency departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public Agency addDepartment(Department department) {
        this.departments.add(department);
        department.setAgency(this);
        return this;
    }

    public Agency removeDepartment(Department department) {
        this.departments.remove(department);
        department.setAgency(null);
        return this;
    }

    public AgencyType getAgencyType() {
        return this.agencyType;
    }

    public void setAgencyType(AgencyType agencyType) {
        this.agencyType = agencyType;
    }

    public Agency agencyType(AgencyType agencyType) {
        this.setAgencyType(agencyType);
        return this;
    }

    public Set<Zone> getZones() {
        return this.zones;
    }

    public void setZones(Set<Zone> zones) {
        if (this.zones != null) {
            this.zones.forEach(i -> i.setAuthority(null));
        }
        if (zones != null) {
            zones.forEach(i -> i.setAuthority(this));
        }
        this.zones = zones;
    }

    public Agency zones(Set<Zone> zones) {
        this.setZones(zones);
        return this;
    }

    public Agency addZone(Zone zone) {
        this.zones.add(zone);
        zone.setAuthority(this);
        return this;
    }

    public Agency removeZone(Zone zone) {
        this.zones.remove(zone);
        zone.setAuthority(null);
        return this;
    }

    public Set<PassType> getPassTypes() {
        return this.passTypes;
    }

    public void setPassTypes(Set<PassType> passTypes) {
        if (this.passTypes != null) {
            this.passTypes.forEach(i -> i.removeAgency(this));
        }
        if (passTypes != null) {
            passTypes.forEach(i -> i.addAgency(this));
        }
        this.passTypes = passTypes;
    }

    public Agency passTypes(Set<PassType> passTypes) {
        this.setPassTypes(passTypes);
        return this;
    }

    public Agency addPassType(PassType passType) {
        this.passTypes.add(passType);
        passType.getAgencies().add(this);
        return this;
    }

    public Agency removePassType(PassType passType) {
        this.passTypes.remove(passType);
        passType.getAgencies().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agency)) {
            return false;
        }
        return getId() != null && getId().equals(((Agency) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agency{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", isInternal='" + getIsInternal() + "'" +
            ", isDummy='" + getIsDummy() + "'" +
            "}";
    }
}
