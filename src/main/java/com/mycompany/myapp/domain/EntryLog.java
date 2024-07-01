package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.ActionType;
import com.mycompany.myapp.domain.enumeration.DirectionType;
import com.mycompany.myapp.domain.enumeration.PassStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * The EntryLog entity.
 * @author A true hipster
 */
@Schema(description = "The EntryLog entity.\n@author A true hipster")
@Entity
@Table(name = "entry_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntryLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * fieldName
     */
    @Schema(description = "fieldName")
    @Column(name = "event_time")
    private Instant eventTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction")
    private DirectionType direction;

    @Enumerated(EnumType.STRING)
    @Column(name = "pass_status")
    private PassStatusType passStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "entryLogs", "passType", "requestedBy", "assignment", "vehicleAssignment" }, allowSetters = true)
    private Pass pass;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "entryLogs", "gate", "accessProfiles" }, allowSetters = true)
    private Lane lane;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EntryLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEventTime() {
        return this.eventTime;
    }

    public EntryLog eventTime(Instant eventTime) {
        this.setEventTime(eventTime);
        return this;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public DirectionType getDirection() {
        return this.direction;
    }

    public EntryLog direction(DirectionType direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public PassStatusType getPassStatus() {
        return this.passStatus;
    }

    public EntryLog passStatus(PassStatusType passStatus) {
        this.setPassStatus(passStatus);
        return this;
    }

    public void setPassStatus(PassStatusType passStatus) {
        this.passStatus = passStatus;
    }

    public ActionType getActionType() {
        return this.actionType;
    }

    public EntryLog actionType(ActionType actionType) {
        this.setActionType(actionType);
        return this;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Pass getPass() {
        return this.pass;
    }

    public void setPass(Pass pass) {
        this.pass = pass;
    }

    public EntryLog pass(Pass pass) {
        this.setPass(pass);
        return this;
    }

    public Lane getLane() {
        return this.lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    public EntryLog lane(Lane lane) {
        this.setLane(lane);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntryLog)) {
            return false;
        }
        return getId() != null && getId().equals(((EntryLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntryLog{" +
            "id=" + getId() +
            ", eventTime='" + getEventTime() + "'" +
            ", direction='" + getDirection() + "'" +
            ", passStatus='" + getPassStatus() + "'" +
            ", actionType='" + getActionType() + "'" +
            "}";
    }
}
