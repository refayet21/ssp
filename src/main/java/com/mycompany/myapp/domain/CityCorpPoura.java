package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CityCorpPoura.
 */
@Entity
@Table(name = "city_corp_poura")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CityCorpPoura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cityCorpPoura")
    @JsonIgnoreProperties(value = { "cityCorpPoura", "union" }, allowSetters = true)
    private Set<Ward> wards = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cityCorpPoura")
    @JsonIgnoreProperties(value = { "cityCorpPoura", "union", "postOffice", "country", "person", "agency" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "upazilas", "cityCorpPouras", "postOffices", "division" }, allowSetters = true)
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "unions", "cityCorpPouras", "district" }, allowSetters = true)
    private Upazila upazila;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cityCorpPouras" }, allowSetters = true)
    private RMO rmo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CityCorpPoura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CityCorpPoura name(String name) {
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
            this.wards.forEach(i -> i.setCityCorpPoura(null));
        }
        if (wards != null) {
            wards.forEach(i -> i.setCityCorpPoura(this));
        }
        this.wards = wards;
    }

    public CityCorpPoura wards(Set<Ward> wards) {
        this.setWards(wards);
        return this;
    }

    public CityCorpPoura addWard(Ward ward) {
        this.wards.add(ward);
        ward.setCityCorpPoura(this);
        return this;
    }

    public CityCorpPoura removeWard(Ward ward) {
        this.wards.remove(ward);
        ward.setCityCorpPoura(null);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setCityCorpPoura(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setCityCorpPoura(this));
        }
        this.addresses = addresses;
    }

    public CityCorpPoura addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public CityCorpPoura addAddress(Address address) {
        this.addresses.add(address);
        address.setCityCorpPoura(this);
        return this;
    }

    public CityCorpPoura removeAddress(Address address) {
        this.addresses.remove(address);
        address.setCityCorpPoura(null);
        return this;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public CityCorpPoura district(District district) {
        this.setDistrict(district);
        return this;
    }

    public Upazila getUpazila() {
        return this.upazila;
    }

    public void setUpazila(Upazila upazila) {
        this.upazila = upazila;
    }

    public CityCorpPoura upazila(Upazila upazila) {
        this.setUpazila(upazila);
        return this;
    }

    public RMO getRmo() {
        return this.rmo;
    }

    public void setRmo(RMO rMO) {
        this.rmo = rMO;
    }

    public CityCorpPoura rmo(RMO rMO) {
        this.setRmo(rMO);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityCorpPoura)) {
            return false;
        }
        return getId() != null && getId().equals(((CityCorpPoura) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityCorpPoura{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
