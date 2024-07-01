package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Ward.
 */
@Entity
@Table(name = "ward")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "wards", "addresses", "district", "upazila", "rmo" }, allowSetters = true)
    private CityCorpPoura cityCorpPoura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "wards", "addresses", "upazila" }, allowSetters = true)
    private Union union;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ward id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Ward name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CityCorpPoura getCityCorpPoura() {
        return this.cityCorpPoura;
    }

    public void setCityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.cityCorpPoura = cityCorpPoura;
    }

    public Ward cityCorpPoura(CityCorpPoura cityCorpPoura) {
        this.setCityCorpPoura(cityCorpPoura);
        return this;
    }

    public Union getUnion() {
        return this.union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Ward union(Union union) {
        this.setUnion(union);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ward)) {
            return false;
        }
        return getId() != null && getId().equals(((Ward) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ward{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
