package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DirectionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The Lane entity.
 * @author A true hipster
 */
@Schema(description = "The Lane entity.\n@author A true hipster")
@Entity
@Table(name = "lane")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Lane implements Serializable {

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

    @Column(name = "short_name")
    private String shortName;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction")
    private DirectionType direction;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lane")
    @JsonIgnoreProperties(value = { "pass", "lane" }, allowSetters = true)
    private Set<EntryLog> entryLogs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lanes", "zone" }, allowSetters = true)
    private Gate gate;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "lanes")
    @JsonIgnoreProperties(value = { "lanes" }, allowSetters = true)
    private Set<AccessProfile> accessProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lane id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Lane name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Lane shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public DirectionType getDirection() {
        return this.direction;
    }

    public Lane direction(DirectionType direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Lane isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<EntryLog> getEntryLogs() {
        return this.entryLogs;
    }

    public void setEntryLogs(Set<EntryLog> entryLogs) {
        if (this.entryLogs != null) {
            this.entryLogs.forEach(i -> i.setLane(null));
        }
        if (entryLogs != null) {
            entryLogs.forEach(i -> i.setLane(this));
        }
        this.entryLogs = entryLogs;
    }

    public Lane entryLogs(Set<EntryLog> entryLogs) {
        this.setEntryLogs(entryLogs);
        return this;
    }

    public Lane addEntryLog(EntryLog entryLog) {
        this.entryLogs.add(entryLog);
        entryLog.setLane(this);
        return this;
    }

    public Lane removeEntryLog(EntryLog entryLog) {
        this.entryLogs.remove(entryLog);
        entryLog.setLane(null);
        return this;
    }

    public Gate getGate() {
        return this.gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public Lane gate(Gate gate) {
        this.setGate(gate);
        return this;
    }

    public Set<AccessProfile> getAccessProfiles() {
        return this.accessProfiles;
    }

    public void setAccessProfiles(Set<AccessProfile> accessProfiles) {
        if (this.accessProfiles != null) {
            this.accessProfiles.forEach(i -> i.removeLane(this));
        }
        if (accessProfiles != null) {
            accessProfiles.forEach(i -> i.addLane(this));
        }
        this.accessProfiles = accessProfiles;
    }

    public Lane accessProfiles(Set<AccessProfile> accessProfiles) {
        this.setAccessProfiles(accessProfiles);
        return this;
    }

    public Lane addAccessProfile(AccessProfile accessProfile) {
        this.accessProfiles.add(accessProfile);
        accessProfile.getLanes().add(this);
        return this;
    }

    public Lane removeAccessProfile(AccessProfile accessProfile) {
        this.accessProfiles.remove(accessProfile);
        accessProfile.getLanes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lane)) {
            return false;
        }
        return getId() != null && getId().equals(((Lane) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lane{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", direction='" + getDirection() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
