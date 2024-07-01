package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.ActionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The AccessProfile entity.
 * @author A true hipster
 */
@Schema(description = "The AccessProfile entity.\n@author A true hipster")
@Entity
@Table(name = "access_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccessProfile implements Serializable {

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

    @Column(name = "description")
    private String description;

    @Column(name = "start_time_of_day")
    private Integer startTimeOfDay;

    @Column(name = "end_time_of_day")
    private Integer endTimeOfDay;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private ActionType action;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_access_profile__lane",
        joinColumns = @JoinColumn(name = "access_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "lane_id")
    )
    @JsonIgnoreProperties(value = { "entryLogs", "gate", "accessProfiles" }, allowSetters = true)
    private Set<Lane> lanes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccessProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AccessProfile name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public AccessProfile description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStartTimeOfDay() {
        return this.startTimeOfDay;
    }

    public AccessProfile startTimeOfDay(Integer startTimeOfDay) {
        this.setStartTimeOfDay(startTimeOfDay);
        return this;
    }

    public void setStartTimeOfDay(Integer startTimeOfDay) {
        this.startTimeOfDay = startTimeOfDay;
    }

    public Integer getEndTimeOfDay() {
        return this.endTimeOfDay;
    }

    public AccessProfile endTimeOfDay(Integer endTimeOfDay) {
        this.setEndTimeOfDay(endTimeOfDay);
        return this;
    }

    public void setEndTimeOfDay(Integer endTimeOfDay) {
        this.endTimeOfDay = endTimeOfDay;
    }

    public Integer getDayOfWeek() {
        return this.dayOfWeek;
    }

    public AccessProfile dayOfWeek(Integer dayOfWeek) {
        this.setDayOfWeek(dayOfWeek);
        return this;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public ActionType getAction() {
        return this.action;
    }

    public AccessProfile action(ActionType action) {
        this.setAction(action);
        return this;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public Set<Lane> getLanes() {
        return this.lanes;
    }

    public void setLanes(Set<Lane> lanes) {
        this.lanes = lanes;
    }

    public AccessProfile lanes(Set<Lane> lanes) {
        this.setLanes(lanes);
        return this;
    }

    public AccessProfile addLane(Lane lane) {
        this.lanes.add(lane);
        return this;
    }

    public AccessProfile removeLane(Lane lane) {
        this.lanes.remove(lane);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((AccessProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccessProfile{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", startTimeOfDay=" + getStartTimeOfDay() +
            ", endTimeOfDay=" + getEndTimeOfDay() +
            ", dayOfWeek=" + getDayOfWeek() +
            ", action='" + getAction() + "'" +
            "}";
    }
}
