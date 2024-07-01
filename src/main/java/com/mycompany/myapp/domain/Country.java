package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "iso_code")
    private String isoCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nationality")
    @JsonIgnoreProperties(value = { "internalUser", "addresses", "documents", "assignments", "nationality" }, allowSetters = true)
    private Set<Person> people = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
    @JsonIgnoreProperties(value = { "cityCorpPoura", "union", "postOffice", "country", "person", "agency" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Country id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Country name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode() {
        return this.isoCode;
    }

    public Country isoCode(String isoCode) {
        this.setIsoCode(isoCode);
        return this;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public void setPeople(Set<Person> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.setNationality(null));
        }
        if (people != null) {
            people.forEach(i -> i.setNationality(this));
        }
        this.people = people;
    }

    public Country people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public Country addPerson(Person person) {
        this.people.add(person);
        person.setNationality(this);
        return this;
    }

    public Country removePerson(Person person) {
        this.people.remove(person);
        person.setNationality(null);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCountry(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setCountry(this));
        }
        this.addresses = addresses;
    }

    public Country addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Country addAddress(Address address) {
        this.addresses.add(address);
        address.setCountry(this);
        return this;
    }

    public Country removeAddress(Address address) {
        this.addresses.remove(address);
        address.setCountry(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return getId() != null && getId().equals(((Country) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isoCode='" + getIsoCode() + "'" +
            "}";
    }
}
