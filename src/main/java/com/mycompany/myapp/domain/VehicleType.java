package com.mycompany.myapp.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * The VehicleType entity.
 * @author A true hipster
 */
@Schema(description = "The VehicleType entity.\n@author A true hipster")
@Entity
@Table(name = "vehicle_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * fieldName
     */
    @Schema(description = "fieldName")
    @Column(name = "name")
    private String name;

    @Column(name = "number_of_operators")
    private Integer numberOfOperators;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public VehicleType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfOperators() {
        return this.numberOfOperators;
    }

    public VehicleType numberOfOperators(Integer numberOfOperators) {
        this.setNumberOfOperators(numberOfOperators);
        return this;
    }

    public void setNumberOfOperators(Integer numberOfOperators) {
        this.numberOfOperators = numberOfOperators;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleType)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", numberOfOperators=" + getNumberOfOperators() +
            "}";
    }
}
