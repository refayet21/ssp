package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.GateType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The Gate entity.
 * @author A true hipster
 */
@Schema(description = "The Gate entity.\n@author A true hipster")
@Entity
@Table(name = "gate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Gate implements Serializable {

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

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;

    @Enumerated(EnumType.STRING)
    @Column(name = "gate_type")
    private GateType gateType;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gate")
    @JsonIgnoreProperties(value = { "entryLogs", "gate", "accessProfiles" }, allowSetters = true)
    private Set<Lane> lanes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "authority", "gates" }, allowSetters = true)
    private Zone zone;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Gate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Gate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Gate shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Float getLat() {
        return this.lat;
    }

    public Gate lat(Float lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return this.lon;
    }

    public Gate lon(Float lon) {
        this.setLon(lon);
        return this;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public GateType getGateType() {
        return this.gateType;
    }

    public Gate gateType(GateType gateType) {
        this.setGateType(gateType);
        return this;
    }

    public void setGateType(GateType gateType) {
        this.gateType = gateType;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Gate isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Lane> getLanes() {
        return this.lanes;
    }

    public void setLanes(Set<Lane> lanes) {
        if (this.lanes != null) {
            this.lanes.forEach(i -> i.setGate(null));
        }
        if (lanes != null) {
            lanes.forEach(i -> i.setGate(this));
        }
        this.lanes = lanes;
    }

    public Gate lanes(Set<Lane> lanes) {
        this.setLanes(lanes);
        return this;
    }

    public Gate addLane(Lane lane) {
        this.lanes.add(lane);
        lane.setGate(this);
        return this;
    }

    public Gate removeLane(Lane lane) {
        this.lanes.remove(lane);
        lane.setGate(null);
        return this;
    }

    public Zone getZone() {
        return this.zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Gate zone(Zone zone) {
        this.setZone(zone);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gate)) {
            return false;
        }
        return getId() != null && getId().equals(((Gate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", lat=" + getLat() +
            ", lon=" + getLon() +
            ", gateType='" + getGateType() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
