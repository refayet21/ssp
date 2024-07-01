package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A District.
 */
@Entity
@Table(name = "district")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class District implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    @JsonIgnoreProperties(value = { "unions", "cityCorpPouras", "district" }, allowSetters = true)
    private Set<Upazila> upazilas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    @JsonIgnoreProperties(value = { "wards", "addresses", "district", "upazila", "rmo" }, allowSetters = true)
    private Set<CityCorpPoura> cityCorpPouras = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "district")
    @JsonIgnoreProperties(value = { "addresses", "district" }, allowSetters = true)
    private Set<PostOffice> postOffices = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "districts" }, allowSetters = true)
    private Division division;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public District id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public District name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Upazila> getUpazilas() {
        return this.upazilas;
    }

    public void setUpazilas(Set<Upazila> upazilas) {
        if (this.upazilas != null) {
            this.upazilas.forEach(i -> i.setDistrict(null));
        }
        if (upazilas != null) {
            upazilas.forEach(i -> i.setDistrict(this));
        }
        this.upazilas = upazilas;
    }

    public District upazilas(Set<Upazila> upazilas) {
        this.setUpazilas(upazilas);
        return this;
    }

    public District addUpazila(Upazila upazila) {
        this.upazilas.add(upazila);
        upazila.setDistrict(this);
        return this;
    }

    public District removeUpazila(Upazila upazila) {
        this.upazilas.remove(upazila);
        upazila.setDistrict(null);
        return this;
    }

    public Set<CityCorpPoura> getCityCorpPouras() {
        return this.cityCorpPouras;
    }

    public void setCityCorpPouras(Set<CityCorpPoura> cityCorpPouras) {
        if (this.cityCorpPouras != null) {
            this.cityCorpPouras.forEach(i -> i.setDistrict(null));
        }
        if (cityCorpPouras != null) {
            cityCorpPouras.forEach(i -> i.setDistrict(this));
        }
        this.cityCorpPouras = cityCorpPouras;
    }

    public District cityCorpPouras(Set<CityCorpPoura> cityCorpPouras) {
        this.setCityCorpPouras(cityCorpPouras);
        return this;
    }

    public District addCityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.cityCorpPouras.add(cityCorpPoura);
        cityCorpPoura.setDistrict(this);
        return this;
    }

    public District removeCityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.cityCorpPouras.remove(cityCorpPoura);
        cityCorpPoura.setDistrict(null);
        return this;
    }

    public Set<PostOffice> getPostOffices() {
        return this.postOffices;
    }

    public void setPostOffices(Set<PostOffice> postOffices) {
        if (this.postOffices != null) {
            this.postOffices.forEach(i -> i.setDistrict(null));
        }
        if (postOffices != null) {
            postOffices.forEach(i -> i.setDistrict(this));
        }
        this.postOffices = postOffices;
    }

    public District postOffices(Set<PostOffice> postOffices) {
        this.setPostOffices(postOffices);
        return this;
    }

    public District addPostOffice(PostOffice postOffice) {
        this.postOffices.add(postOffice);
        postOffice.setDistrict(this);
        return this;
    }

    public District removePostOffice(PostOffice postOffice) {
        this.postOffices.remove(postOffice);
        postOffice.setDistrict(null);
        return this;
    }

    public Division getDivision() {
        return this.division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public District division(Division division) {
        this.setDivision(division);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof District)) {
            return false;
        }
        return getId() != null && getId().equals(((District) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "District{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
