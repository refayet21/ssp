package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.LogStatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 30)
    @Column(name = "short_name", length = 30)
    private String shortName;

    @NotNull
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "is_black_listed")
    private Boolean isBlackListed;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_status")
    private LogStatusType logStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User internalUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @JsonIgnoreProperties(value = { "cityCorpPoura", "union", "postOffice", "country", "person", "agency" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @JsonIgnoreProperties(value = { "verifiedBy", "documentType", "person", "vehicle", "agency" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @JsonIgnoreProperties(value = { "person", "designation", "agency" }, allowSetters = true)
    private Set<Assignment> assignments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "people", "addresses" }, allowSetters = true)
    private Country nationality;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Person name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Person shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public Person dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return this.email;
    }

    public Person email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsBlackListed() {
        return this.isBlackListed;
    }

    public Person isBlackListed(Boolean isBlackListed) {
        this.setIsBlackListed(isBlackListed);
        return this;
    }

    public void setIsBlackListed(Boolean isBlackListed) {
        this.isBlackListed = isBlackListed;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public Person fatherName(String fatherName) {
        this.setFatherName(fatherName);
        return this;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return this.motherName;
    }

    public Person motherName(String motherName) {
        this.setMotherName(motherName);
        return this;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public LogStatusType getLogStatus() {
        return this.logStatus;
    }

    public Person logStatus(LogStatusType logStatus) {
        this.setLogStatus(logStatus);
        return this;
    }

    public void setLogStatus(LogStatusType logStatus) {
        this.logStatus = logStatus;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Person internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setPerson(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setPerson(this));
        }
        this.addresses = addresses;
    }

    public Person addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Person addAddress(Address address) {
        this.addresses.add(address);
        address.setPerson(this);
        return this;
    }

    public Person removeAddress(Address address) {
        this.addresses.remove(address);
        address.setPerson(null);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setPerson(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setPerson(this));
        }
        this.documents = documents;
    }

    public Person documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Person addDocument(Document document) {
        this.documents.add(document);
        document.setPerson(this);
        return this;
    }

    public Person removeDocument(Document document) {
        this.documents.remove(document);
        document.setPerson(null);
        return this;
    }

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        if (this.assignments != null) {
            this.assignments.forEach(i -> i.setPerson(null));
        }
        if (assignments != null) {
            assignments.forEach(i -> i.setPerson(this));
        }
        this.assignments = assignments;
    }

    public Person assignments(Set<Assignment> assignments) {
        this.setAssignments(assignments);
        return this;
    }

    public Person addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setPerson(this);
        return this;
    }

    public Person removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.setPerson(null);
        return this;
    }

    public Country getNationality() {
        return this.nationality;
    }

    public void setNationality(Country country) {
        this.nationality = country;
    }

    public Person nationality(Country country) {
        this.setNationality(country);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return getId() != null && getId().equals(((Person) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", dob='" + getDob() + "'" +
            ", email='" + getEmail() + "'" +
            ", isBlackListed='" + getIsBlackListed() + "'" +
            ", fatherName='" + getFatherName() + "'" +
            ", motherName='" + getMotherName() + "'" +
            ", logStatus='" + getLogStatus() + "'" +
            "}";
    }
}
