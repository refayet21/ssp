package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * The VehicleAssigment entity.
 * @author A true hipster
 */
@Schema(description = "The VehicleAssigment entity.\n@author A true hipster")
@Entity
@Table(name = "vehicle_assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "is_rental")
    private Boolean isRental;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicleAssignment")
    @JsonIgnoreProperties(value = { "entryLogs", "passType", "requestedBy", "assignment", "vehicleAssignment" }, allowSetters = true)
    private Set<Pass> passes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "addresses", "documents", "assignments", "licenses", "issuers", "departments", "agencyType", "zones", "passTypes" },
        allowSetters = true
    )
    private Agency agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "documents", "vehicleAssigments", "vehicleType" }, allowSetters = true)
    private Vehicle vehicle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public VehicleAssignment startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public VehicleAssignment endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsPrimary() {
        return this.isPrimary;
    }

    public VehicleAssignment isPrimary(Boolean isPrimary) {
        this.setIsPrimary(isPrimary);
        return this;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Boolean getIsRental() {
        return this.isRental;
    }

    public VehicleAssignment isRental(Boolean isRental) {
        this.setIsRental(isRental);
        return this;
    }

    public void setIsRental(Boolean isRental) {
        this.isRental = isRental;
    }

    public Set<Pass> getPasses() {
        return this.passes;
    }

    public void setPasses(Set<Pass> passes) {
        if (this.passes != null) {
            this.passes.forEach(i -> i.setVehicleAssignment(null));
        }
        if (passes != null) {
            passes.forEach(i -> i.setVehicleAssignment(this));
        }
        this.passes = passes;
    }

    public VehicleAssignment passes(Set<Pass> passes) {
        this.setPasses(passes);
        return this;
    }

    public VehicleAssignment addPass(Pass pass) {
        this.passes.add(pass);
        pass.setVehicleAssignment(this);
        return this;
    }

    public VehicleAssignment removePass(Pass pass) {
        this.passes.remove(pass);
        pass.setVehicleAssignment(null);
        return this;
    }

    public Agency getAgency() {
        return this.agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public VehicleAssignment agency(Agency agency) {
        this.setAgency(agency);
        return this;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public VehicleAssignment vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleAssignment{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isPrimary='" + getIsPrimary() + "'" +
            ", isRental='" + getIsRental() + "'" +
            "}";
    }
}
