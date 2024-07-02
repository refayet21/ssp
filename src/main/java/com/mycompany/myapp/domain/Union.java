package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Union.
 */
@Entity
@Table(name = "jhi_union")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Union implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "union")
    @JsonIgnoreProperties(value = { "cityCorpPoura", "union" }, allowSetters = true)
    private Set<Ward> wards = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "union")
    @JsonIgnoreProperties(value = { "cityCorpPoura", "union", "postOffice", "country", "person", "agency" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "unions", "cityCorpPouras" })
    private Upazila upazila;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Union id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Union name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Ward> getWards() {
        return this.wards;
    }

    public void setWards(Set<Ward> wards) {
        if (this.wards != null) {
            this.wards.forEach(i -> i.setUnion(null));
        }
        if (wards != null) {
            wards.forEach(i -> i.setUnion(this));
        }
        this.wards = wards;
    }

    public Union wards(Set<Ward> wards) {
        this.setWards(wards);
        return this;
    }

    public Union addWard(Ward ward) {
        this.wards.add(ward);
        ward.setUnion(this);
        return this;
    }

    public Union removeWard(Ward ward) {
        this.wards.remove(ward);
        ward.setUnion(null);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setUnion(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setUnion(this));
        }
        this.addresses = addresses;
    }

    public Union addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Union addAddress(Address address) {
        this.addresses.add(address);
        address.setUnion(this);
        return this;
    }

    public Union removeAddress(Address address) {
        this.addresses.remove(address);
        address.setUnion(null);
        return this;
    }

    public Upazila getUpazila() {
        return this.upazila;
    }

    public void setUpazila(Upazila upazila) {
        this.upazila = upazila;
    }

    public Union upazila(Upazila upazila) {
        this.setUpazila(upazila);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Union)) {
            return false;
        }
        return getId() != null && getId().equals(((Union) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Union{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
