package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The Zone entity.
 * @author A true hipster
 */
@Schema(description = "The Zone entity.\n@author A true hipster")
@Entity
@Table(name = "zone")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Zone implements Serializable {

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

    @Column(name = "location")
    private String location;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "addresses", "documents", "assignments", "licenses", "issuers", "departments", "agencyType", "zones", "passTypes" },
        allowSetters = true
    )
    private Agency authority;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "zone")
    @JsonIgnoreProperties(value = { "lanes", "zone" }, allowSetters = true)
    private Set<Gate> gates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Zone id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Zone name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Zone shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLocation() {
        return this.location;
    }

    public Zone location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Zone isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Agency getAuthority() {
        return this.authority;
    }

    public void setAuthority(Agency agency) {
        this.authority = agency;
    }

    public Zone authority(Agency agency) {
        this.setAuthority(agency);
        return this;
    }

    public Set<Gate> getGates() {
        return this.gates;
    }

    public void setGates(Set<Gate> gates) {
        if (this.gates != null) {
            this.gates.forEach(i -> i.setZone(null));
        }
        if (gates != null) {
            gates.forEach(i -> i.setZone(this));
        }
        this.gates = gates;
    }

    public Zone gates(Set<Gate> gates) {
        this.setGates(gates);
        return this;
    }

    public Zone addGate(Gate gate) {
        this.gates.add(gate);
        gate.setZone(this);
        return this;
    }

    public Zone removeGate(Gate gate) {
        this.gates.remove(gate);
        gate.setZone(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Zone)) {
            return false;
        }
        return getId() != null && getId().equals(((Zone) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Zone{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", location='" + getLocation() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
