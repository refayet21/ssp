package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.PassStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * The Pass entity.
 * @author A true hipster
 */
@Schema(description = "The Pass entity.\n@author A true hipster")
@Entity
@Table(name = "pass")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * fieldName
     */
    @Schema(description = "fieldName")
    @Column(name = "collected_fee")
    private Double collectedFee;

    @Column(name = "from_date")
    private Instant fromDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PassStatusType status;

    @Column(name = "pass_number")
    private Long passNumber;

    @Column(name = "media_serial")
    private String mediaSerial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pass")
    @JsonIgnoreProperties(value = { "pass", "lane" }, allowSetters = true)
    private Set<EntryLog> entryLogs = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "agencies" }, allowSetters = true)
    private PassType passType;

    @ManyToOne(optional = false)
    @NotNull
    private User requestedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "person", "designation", "agency" }, allowSetters = true)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "passes", "agency", "vehicle" }, allowSetters = true)
    private VehicleAssignment vehicleAssignment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pass id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCollectedFee() {
        return this.collectedFee;
    }

    public Pass collectedFee(Double collectedFee) {
        this.setCollectedFee(collectedFee);
        return this;
    }

    public void setCollectedFee(Double collectedFee) {
        this.collectedFee = collectedFee;
    }

    public Instant getFromDate() {
        return this.fromDate;
    }

    public Pass fromDate(Instant fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Pass endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public PassStatusType getStatus() {
        return this.status;
    }

    public Pass status(PassStatusType status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PassStatusType status) {
        this.status = status;
    }

    public Long getPassNumber() {
        return this.passNumber;
    }

    public Pass passNumber(Long passNumber) {
        this.setPassNumber(passNumber);
        return this;
    }

    public void setPassNumber(Long passNumber) {
        this.passNumber = passNumber;
    }

    public String getMediaSerial() {
        return this.mediaSerial;
    }

    public Pass mediaSerial(String mediaSerial) {
        this.setMediaSerial(mediaSerial);
        return this;
    }

    public void setMediaSerial(String mediaSerial) {
        this.mediaSerial = mediaSerial;
    }

    public Set<EntryLog> getEntryLogs() {
        return this.entryLogs;
    }

    public void setEntryLogs(Set<EntryLog> entryLogs) {
        if (this.entryLogs != null) {
            this.entryLogs.forEach(i -> i.setPass(null));
        }
        if (entryLogs != null) {
            entryLogs.forEach(i -> i.setPass(this));
        }
        this.entryLogs = entryLogs;
    }

    public Pass entryLogs(Set<EntryLog> entryLogs) {
        this.setEntryLogs(entryLogs);
        return this;
    }

    public Pass addEntryLog(EntryLog entryLog) {
        this.entryLogs.add(entryLog);
        entryLog.setPass(this);
        return this;
    }

    public Pass removeEntryLog(EntryLog entryLog) {
        this.entryLogs.remove(entryLog);
        entryLog.setPass(null);
        return this;
    }

    public PassType getPassType() {
        return this.passType;
    }

    public void setPassType(PassType passType) {
        this.passType = passType;
    }

    public Pass passType(PassType passType) {
        this.setPassType(passType);
        return this;
    }

    public User getRequestedBy() {
        return this.requestedBy;
    }

    public void setRequestedBy(User user) {
        this.requestedBy = user;
    }

    public Pass requestedBy(User user) {
        this.setRequestedBy(user);
        return this;
    }

    public Assignment getAssignment() {
        return this.assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Pass assignment(Assignment assignment) {
        this.setAssignment(assignment);
        return this;
    }

    public VehicleAssignment getVehicleAssignment() {
        return this.vehicleAssignment;
    }

    public void setVehicleAssignment(VehicleAssignment vehicleAssignment) {
        this.vehicleAssignment = vehicleAssignment;
    }

    public Pass vehicleAssignment(VehicleAssignment vehicleAssignment) {
        this.setVehicleAssignment(vehicleAssignment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pass)) {
            return false;
        }
        return getId() != null && getId().equals(((Pass) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pass{" +
            "id=" + getId() +
            ", collectedFee=" + getCollectedFee() +
            ", fromDate='" + getFromDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", passNumber=" + getPassNumber() +
            ", mediaSerial='" + getMediaSerial() + "'" +
            "}";
    }
}
