package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * The License entity.
 * @author A true hipster
 */
@Schema(description = "The License entity.\n@author A true hipster")
@Entity
@Table(name = "agency_license")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgencyLicense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * fieldName
     */
    @Schema(description = "fieldName")
    @Column(name = "file_path")
    private String filePath;

    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User verifiedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "agencyLicenses" }, allowSetters = true)
    private AgencyLicenseType agencyLicenseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "addresses", "documents", "assignments", "licenses", "issuers", "departments", "agencyType", "zones", "passTypes" },
        allowSetters = true
    )
    private Agency belongsTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "addresses", "documents", "assignments", "licenses", "issuers", "departments", "agencyType", "zones", "passTypes" },
        allowSetters = true
    )
    private Agency issuedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgencyLicense id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public AgencyLicense filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public AgencyLicense serialNo(String serialNo) {
        this.setSerialNo(serialNo);
        return this;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public LocalDate getIssueDate() {
        return this.issueDate;
    }

    public AgencyLicense issueDate(LocalDate issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    public AgencyLicense expiryDate(LocalDate expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getVerifiedBy() {
        return this.verifiedBy;
    }

    public void setVerifiedBy(User user) {
        this.verifiedBy = user;
    }

    public AgencyLicense verifiedBy(User user) {
        this.setVerifiedBy(user);
        return this;
    }

    public AgencyLicenseType getAgencyLicenseType() {
        return this.agencyLicenseType;
    }

    public void setAgencyLicenseType(AgencyLicenseType agencyLicenseType) {
        this.agencyLicenseType = agencyLicenseType;
    }

    public AgencyLicense agencyLicenseType(AgencyLicenseType agencyLicenseType) {
        this.setAgencyLicenseType(agencyLicenseType);
        return this;
    }

    public Agency getBelongsTo() {
        return this.belongsTo;
    }

    public void setBelongsTo(Agency agency) {
        this.belongsTo = agency;
    }

    public AgencyLicense belongsTo(Agency agency) {
        this.setBelongsTo(agency);
        return this;
    }

    public Agency getIssuedBy() {
        return this.issuedBy;
    }

    public void setIssuedBy(Agency agency) {
        this.issuedBy = agency;
    }

    public AgencyLicense issuedBy(Agency agency) {
        this.setIssuedBy(agency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgencyLicense)) {
            return false;
        }
        return getId() != null && getId().equals(((AgencyLicense) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyLicense{" +
            "id=" + getId() +
            ", filePath='" + getFilePath() + "'" +
            ", serialNo='" + getSerialNo() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            "}";
    }
}
