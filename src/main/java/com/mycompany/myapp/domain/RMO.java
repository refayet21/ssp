package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A RMO.
 */
@Entity
@Table(name = "rmo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RMO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rmo")
    @JsonIgnoreProperties(value = { "wards", "addresses", "district", "upazila", "rmo" }, allowSetters = true)
    private Set<CityCorpPoura> cityCorpPouras = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RMO id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RMO name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public RMO code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<CityCorpPoura> getCityCorpPouras() {
        return this.cityCorpPouras;
    }

    public void setCityCorpPouras(Set<CityCorpPoura> cityCorpPouras) {
        if (this.cityCorpPouras != null) {
            this.cityCorpPouras.forEach(i -> i.setRmo(null));
        }
        if (cityCorpPouras != null) {
            cityCorpPouras.forEach(i -> i.setRmo(this));
        }
        this.cityCorpPouras = cityCorpPouras;
    }

    public RMO cityCorpPouras(Set<CityCorpPoura> cityCorpPouras) {
        this.setCityCorpPouras(cityCorpPouras);
        return this;
    }

    public RMO addCityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.cityCorpPouras.add(cityCorpPoura);
        cityCorpPoura.setRmo(this);
        return this;
    }

    public RMO removeCityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.cityCorpPouras.remove(cityCorpPoura);
        cityCorpPoura.setRmo(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RMO)) {
            return false;
        }
        return getId() != null && getId().equals(((RMO) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RMO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
