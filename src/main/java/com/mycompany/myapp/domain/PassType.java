package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.IssueChannelType;
import com.mycompany.myapp.domain.enumeration.PassMediaType;
import com.mycompany.myapp.domain.enumeration.PassUserType;
import com.mycompany.myapp.domain.enumeration.TaxCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The PassType entity.
 * @author A true hipster
 */
@Schema(description = "The PassType entity.\n@author A true hipster")
@Entity
@Table(name = "pass_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PassType implements Serializable {

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

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "printed_name")
    private String printedName;

    @Column(name = "issue_fee")
    private Double issueFee;

    @Column(name = "renew_fee")
    private Double renewFee;

    @Column(name = "cancel_fee")
    private Double cancelFee;

    @Column(name = "minimum_duration")
    private Long minimumDuration;

    @Column(name = "maximum_duration")
    private Long maximumDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_channel_type")
    private IssueChannelType issueChannelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "tax_code")
    private TaxCodeType taxCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "pass_media_type")
    private PassMediaType passMediaType;

    @Enumerated(EnumType.STRING)
    @Column(name = "pass_user_type")
    private PassUserType passUserType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_pass_type__agency",
        joinColumns = @JoinColumn(name = "pass_type_id"),
        inverseJoinColumns = @JoinColumn(name = "agency_id")
    )
    @JsonIgnoreProperties(
        value = { "addresses", "documents", "assignments", "licenses", "issuers", "departments", "agencyType", "zones", "passTypes" },
        allowSetters = true
    )
    private Set<Agency> agencies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PassType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PassType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public PassType shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public PassType isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getPrintedName() {
        return this.printedName;
    }

    public PassType printedName(String printedName) {
        this.setPrintedName(printedName);
        return this;
    }

    public void setPrintedName(String printedName) {
        this.printedName = printedName;
    }

    public Double getIssueFee() {
        return this.issueFee;
    }

    public PassType issueFee(Double issueFee) {
        this.setIssueFee(issueFee);
        return this;
    }

    public void setIssueFee(Double issueFee) {
        this.issueFee = issueFee;
    }

    public Double getRenewFee() {
        return this.renewFee;
    }

    public PassType renewFee(Double renewFee) {
        this.setRenewFee(renewFee);
        return this;
    }

    public void setRenewFee(Double renewFee) {
        this.renewFee = renewFee;
    }

    public Double getCancelFee() {
        return this.cancelFee;
    }

    public PassType cancelFee(Double cancelFee) {
        this.setCancelFee(cancelFee);
        return this;
    }

    public void setCancelFee(Double cancelFee) {
        this.cancelFee = cancelFee;
    }

    public Long getMinimumDuration() {
        return this.minimumDuration;
    }

    public PassType minimumDuration(Long minimumDuration) {
        this.setMinimumDuration(minimumDuration);
        return this;
    }

    public void setMinimumDuration(Long minimumDuration) {
        this.minimumDuration = minimumDuration;
    }

    public Long getMaximumDuration() {
        return this.maximumDuration;
    }

    public PassType maximumDuration(Long maximumDuration) {
        this.setMaximumDuration(maximumDuration);
        return this;
    }

    public void setMaximumDuration(Long maximumDuration) {
        this.maximumDuration = maximumDuration;
    }

    public IssueChannelType getIssueChannelType() {
        return this.issueChannelType;
    }

    public PassType issueChannelType(IssueChannelType issueChannelType) {
        this.setIssueChannelType(issueChannelType);
        return this;
    }

    public void setIssueChannelType(IssueChannelType issueChannelType) {
        this.issueChannelType = issueChannelType;
    }

    public TaxCodeType getTaxCode() {
        return this.taxCode;
    }

    public PassType taxCode(TaxCodeType taxCode) {
        this.setTaxCode(taxCode);
        return this;
    }

    public void setTaxCode(TaxCodeType taxCode) {
        this.taxCode = taxCode;
    }

    public PassMediaType getPassMediaType() {
        return this.passMediaType;
    }

    public PassType passMediaType(PassMediaType passMediaType) {
        this.setPassMediaType(passMediaType);
        return this;
    }

    public void setPassMediaType(PassMediaType passMediaType) {
        this.passMediaType = passMediaType;
    }

    public PassUserType getPassUserType() {
        return this.passUserType;
    }

    public PassType passUserType(PassUserType passUserType) {
        this.setPassUserType(passUserType);
        return this;
    }

    public void setPassUserType(PassUserType passUserType) {
        this.passUserType = passUserType;
    }

    public Set<Agency> getAgencies() {
        return this.agencies;
    }

    public void setAgencies(Set<Agency> agencies) {
        this.agencies = agencies;
    }

    public PassType agencies(Set<Agency> agencies) {
        this.setAgencies(agencies);
        return this;
    }

    public PassType addAgency(Agency agency) {
        this.agencies.add(agency);
        return this;
    }

    public PassType removeAgency(Agency agency) {
        this.agencies.remove(agency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PassType)) {
            return false;
        }
        return getId() != null && getId().equals(((PassType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PassType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", printedName='" + getPrintedName() + "'" +
            ", issueFee=" + getIssueFee() +
            ", renewFee=" + getRenewFee() +
            ", cancelFee=" + getCancelFee() +
            ", minimumDuration=" + getMinimumDuration() +
            ", maximumDuration=" + getMaximumDuration() +
            ", issueChannelType='" + getIssueChannelType() + "'" +
            ", taxCode='" + getTaxCode() + "'" +
            ", passMediaType='" + getPassMediaType() + "'" +
            ", passUserType='" + getPassUserType() + "'" +
            "}";
    }
}
