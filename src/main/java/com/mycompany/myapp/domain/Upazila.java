package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Upazila.
 */
@Entity
@Table(name = "upazila")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Upazila implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "upazila")
    @JsonIgnoreProperties(value = { "wards", "addresses", "upazila" }, allowSetters = true)
    private Set<Union> unions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "upazila")
    @JsonIgnoreProperties(value = { "wards", "addresses", "district", "upazila", "rmo" }, allowSetters = true)
    private Set<CityCorpPoura> cityCorpPouras = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "upazilas", "cityCorpPouras", "postOffices", "division" }, allowSetters = true)
    private District district;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Upazila id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Upazila name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Union> getUnions() {
        return this.unions;
    }

    public void setUnions(Set<Union> unions) {
        if (this.unions != null) {
            this.unions.forEach(i -> i.setUpazila(null));
        }
        if (unions != null) {
            unions.forEach(i -> i.setUpazila(this));
        }
        this.unions = unions;
    }

    public Upazila unions(Set<Union> unions) {
        this.setUnions(unions);
        return this;
    }

    public Upazila addUnion(Union union) {
        this.unions.add(union);
        union.setUpazila(this);
        return this;
    }

    public Upazila removeUnion(Union union) {
        this.unions.remove(union);
        union.setUpazila(null);
        return this;
    }

    public Set<CityCorpPoura> getCityCorpPouras() {
        return this.cityCorpPouras;
    }

    public void setCityCorpPouras(Set<CityCorpPoura> cityCorpPouras) {
        if (this.cityCorpPouras != null) {
            this.cityCorpPouras.forEach(i -> i.setUpazila(null));
        }
        if (cityCorpPouras != null) {
            cityCorpPouras.forEach(i -> i.setUpazila(this));
        }
        this.cityCorpPouras = cityCorpPouras;
    }

    public Upazila cityCorpPouras(Set<CityCorpPoura> cityCorpPouras) {
        this.setCityCorpPouras(cityCorpPouras);
        return this;
    }

    public Upazila addCityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.cityCorpPouras.add(cityCorpPoura);
        cityCorpPoura.setUpazila(this);
        return this;
    }

    public Upazila removeCityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.cityCorpPouras.remove(cityCorpPoura);
        cityCorpPoura.setUpazila(null);
        return this;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Upazila district(District district) {
        this.setDistrict(district);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Upazila)) {
            return false;
        }
        return getId() != null && getId().equals(((Upazila) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Upazila{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
