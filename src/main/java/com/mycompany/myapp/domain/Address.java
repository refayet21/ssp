package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.AddressType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "address_line_one", nullable = false)
    private String addressLineOne;

    @Column(name = "address_line_two")
    private String addressLineTwo;

    @Column(name = "address_line_three")
    private String addressLineThree;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    private AddressType addressType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "wards", "addresses", "district", "upazila", "rmo" }, allowSetters = true)
    private CityCorpPoura cityCorpPoura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "wards", "addresses", "upazila" }, allowSetters = true)
    private Union union;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "addresses", "district" }, allowSetters = true)
    private PostOffice postOffice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "people", "addresses" }, allowSetters = true)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "internalUser", "addresses", "documents", "assignments", "nationality" }, allowSetters = true)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "addresses", "documents", "assignments", "licenses", "issuers", "departments", "agencyType", "zones", "passTypes" },
        allowSetters = true
    )
    private Agency agency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Address id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLineOne() {
        return this.addressLineOne;
    }

    public Address addressLineOne(String addressLineOne) {
        this.setAddressLineOne(addressLineOne);
        return this;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return this.addressLineTwo;
    }

    public Address addressLineTwo(String addressLineTwo) {
        this.setAddressLineTwo(addressLineTwo);
        return this;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public String getAddressLineThree() {
        return this.addressLineThree;
    }

    public Address addressLineThree(String addressLineThree) {
        this.setAddressLineThree(addressLineThree);
        return this;
    }

    public void setAddressLineThree(String addressLineThree) {
        this.addressLineThree = addressLineThree;
    }

    public AddressType getAddressType() {
        return this.addressType;
    }

    public Address addressType(AddressType addressType) {
        this.setAddressType(addressType);
        return this;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public CityCorpPoura getCityCorpPoura() {
        return this.cityCorpPoura;
    }

    public void setCityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.cityCorpPoura = cityCorpPoura;
    }

    public Address cityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.setCityCorpPoura(cityCorpPoura);
        return this;
    }

    public Union getUnion() {
        return this.union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Address union(Union union) {
        this.setUnion(union);
        return this;
    }

    public PostOffice getPostOffice() {
        return this.postOffice;
    }

    public void setPostOffice(PostOffice postOffice) {
        this.postOffice = postOffice;
    }

    public Address postOffice(PostOffice postOffice) {
        this.setPostOffice(postOffice);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Address country(Country country) {
        this.setCountry(country);
        return this;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Address person(Person person) {
        this.setPerson(person);
        return this;
    }

    public Agency getAgency() {
        return this.agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Address agency(Agency agency) {
        this.setAgency(agency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return getId() != null && getId().equals(((Address) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", addressLineOne='" + getAddressLineOne() + "'" +
            ", addressLineTwo='" + getAddressLineTwo() + "'" +
            ", addressLineThree='" + getAddressLineThree() + "'" +
            ", addressType='" + getAddressType() + "'" +
            "}";
    }
}
