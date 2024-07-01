package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AgencyType.
 */
@Entity
@Table(name = "agency_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgencyType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agencyType")
    @JsonIgnoreProperties(
        value = { "addresses", "documents", "assignments", "licenses", "issuers", "departments", "agencyType", "zones", "passTypes" },
        allowSetters = true
    )
    private Set<Agency> agencies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgencyType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AgencyType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public AgencyType shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AgencyType isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Agency> getAgencies() {
        return this.agencies;
    }

    public void setAgencies(Set<Agency> agencies) {
        if (this.agencies != null) {
            this.agencies.forEach(i -> i.setAgencyType(null));
        }
        if (agencies != null) {
            agencies.forEach(i -> i.setAgencyType(this));
        }
        this.agencies = agencies;
    }

    public AgencyType agencies(Set<Agency> agencies) {
        this.setAgencies(agencies);
        return this;
    }

    public AgencyType addAgency(Agency agency) {
        this.agencies.add(agency);
        agency.setAgencyType(this);
        return this;
    }

    public AgencyType removeAgency(Agency agency) {
        this.agencies.remove(agency);
        agency.setAgencyType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgencyType)) {
            return false;
        }
        return getId() != null && getId().equals(((AgencyType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
