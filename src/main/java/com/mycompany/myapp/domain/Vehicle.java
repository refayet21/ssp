package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.LogStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The Vehicle entity.
 * @author A true hipster
 */
@Schema(description = "The Vehicle entity.\n@author A true hipster")
@Entity
@Table(name = "vehicle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

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

    @Column(name = "reg_no")
    private String regNo;

    @Column(name = "zone")
    private String zone;

    @Column(name = "category")
    private String category;

    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "vehicle_no")
    private String vehicleNo;

    @Column(name = "chasis_no")
    private String chasisNo;

    @Column(name = "is_personal")
    private Boolean isPersonal;

    @Column(name = "is_black_listed")
    private Boolean isBlackListed;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_status")
    private LogStatusType logStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @JsonIgnoreProperties(value = { "verifiedBy", "documentType", "person", "vehicle", "agency" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @JsonIgnoreProperties(value = { "passes", "agency", "vehicle" }, allowSetters = true)
    private Set<VehicleAssignment> vehicleAssigments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType vehicleType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Vehicle name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return this.regNo;
    }

    public Vehicle regNo(String regNo) {
        this.setRegNo(regNo);
        return this;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getZone() {
        return this.zone;
    }

    public Vehicle zone(String zone) {
        this.setZone(zone);
        return this;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCategory() {
        return this.category;
    }

    public Vehicle category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public Vehicle serialNo(String serialNo) {
        this.setSerialNo(serialNo);
        return this;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getVehicleNo() {
        return this.vehicleNo;
    }

    public Vehicle vehicleNo(String vehicleNo) {
        this.setVehicleNo(vehicleNo);
        return this;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getChasisNo() {
        return this.chasisNo;
    }

    public Vehicle chasisNo(String chasisNo) {
        this.setChasisNo(chasisNo);
        return this;
    }

    public void setChasisNo(String chasisNo) {
        this.chasisNo = chasisNo;
    }

    public Boolean getIsPersonal() {
        return this.isPersonal;
    }

    public Vehicle isPersonal(Boolean isPersonal) {
        this.setIsPersonal(isPersonal);
        return this;
    }

    public void setIsPersonal(Boolean isPersonal) {
        this.isPersonal = isPersonal;
    }

    public Boolean getIsBlackListed() {
        return this.isBlackListed;
    }

    public Vehicle isBlackListed(Boolean isBlackListed) {
        this.setIsBlackListed(isBlackListed);
        return this;
    }

    public void setIsBlackListed(Boolean isBlackListed) {
        this.isBlackListed = isBlackListed;
    }

    public LogStatusType getLogStatus() {
        return this.logStatus;
    }

    public Vehicle logStatus(LogStatusType logStatus) {
        this.setLogStatus(logStatus);
        return this;
    }

    public void setLogStatus(LogStatusType logStatus) {
        this.logStatus = logStatus;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setVehicle(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setVehicle(this));
        }
        this.documents = documents;
    }

    public Vehicle documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Vehicle addDocument(Document document) {
        this.documents.add(document);
        document.setVehicle(this);
        return this;
    }

    public Vehicle removeDocument(Document document) {
        this.documents.remove(document);
        document.setVehicle(null);
        return this;
    }

    public Set<VehicleAssignment> getVehicleAssigments() {
        return this.vehicleAssigments;
    }

    public void setVehicleAssigments(Set<VehicleAssignment> vehicleAssignments) {
        if (this.vehicleAssigments != null) {
            this.vehicleAssigments.forEach(i -> i.setVehicle(null));
        }
        if (vehicleAssignments != null) {
            vehicleAssignments.forEach(i -> i.setVehicle(this));
        }
        this.vehicleAssigments = vehicleAssignments;
    }

    public Vehicle vehicleAssigments(Set<VehicleAssignment> vehicleAssignments) {
        this.setVehicleAssigments(vehicleAssignments);
        return this;
    }

    public Vehicle addVehicleAssigment(VehicleAssignment vehicleAssignment) {
        this.vehicleAssigments.add(vehicleAssignment);
        vehicleAssignment.setVehicle(this);
        return this;
    }

    public Vehicle removeVehicleAssigment(VehicleAssignment vehicleAssignment) {
        this.vehicleAssigments.remove(vehicleAssignment);
        vehicleAssignment.setVehicle(null);
        return this;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Vehicle vehicleType(VehicleType vehicleType) {
        this.setVehicleType(vehicleType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", regNo='" + getRegNo() + "'" +
            ", zone='" + getZone() + "'" +
            ", category='" + getCategory() + "'" +
            ", serialNo='" + getSerialNo() + "'" +
            ", vehicleNo='" + getVehicleNo() + "'" +
            ", chasisNo='" + getChasisNo() + "'" +
            ", isPersonal='" + getIsPersonal() + "'" +
            ", isBlackListed='" + getIsBlackListed() + "'" +
            ", logStatus='" + getLogStatus() + "'" +
            "}";
    }
}
