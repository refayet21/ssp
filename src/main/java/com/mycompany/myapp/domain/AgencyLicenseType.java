package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The License entity.
 * @author A true hipster
 */
@Schema(description = "The License entity.\n@author A true hipster")
@Entity
@Table(name = "agency_license_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgencyLicenseType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * fieldName
     */
    @Schema(description = "fieldName", required = true)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agencyLicenseType")
    @JsonIgnoreProperties(value = { "verifiedBy", "agencyLicenseType", "belongsTo", "issuedBy" }, allowSetters = true)
    private Set<AgencyLicense> agencyLicenses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgencyLicenseType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AgencyLicenseType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public AgencyLicenseType isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<AgencyLicense> getAgencyLicenses() {
        return this.agencyLicenses;
    }

    public void setAgencyLicenses(Set<AgencyLicense> agencyLicenses) {
        if (this.agencyLicenses != null) {
            this.agencyLicenses.forEach(i -> i.setAgencyLicenseType(null));
        }
        if (agencyLicenses != null) {
            agencyLicenses.forEach(i -> i.setAgencyLicenseType(this));
        }
        this.agencyLicenses = agencyLicenses;
    }

    public AgencyLicenseType agencyLicenses(Set<AgencyLicense> agencyLicenses) {
        this.setAgencyLicenses(agencyLicenses);
        return this;
    }

    public AgencyLicenseType addAgencyLicense(AgencyLicense agencyLicense) {
        this.agencyLicenses.add(agencyLicense);
        agencyLicense.setAgencyLicenseType(this);
        return this;
    }

    public AgencyLicenseType removeAgencyLicense(AgencyLicense agencyLicense) {
        this.agencyLicenses.remove(agencyLicense);
        agencyLicense.setAgencyLicenseType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgencyLicenseType)) {
            return false;
        }
        return getId() != null && getId().equals(((AgencyLicenseType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyLicenseType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
