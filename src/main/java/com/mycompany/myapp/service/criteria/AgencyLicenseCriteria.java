package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.AgencyLicense} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AgencyLicenseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agency-licenses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgencyLicenseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter filePath;

    private StringFilter serialNo;

    private LocalDateFilter issueDate;

    private LocalDateFilter expiryDate;

    private LongFilter verifiedById;

    private LongFilter agencyLicenseTypeId;

    private LongFilter belongsToId;

    private LongFilter issuedById;

    private Boolean distinct;

    public AgencyLicenseCriteria() {}

    public AgencyLicenseCriteria(AgencyLicenseCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.filePath = other.optionalFilePath().map(StringFilter::copy).orElse(null);
        this.serialNo = other.optionalSerialNo().map(StringFilter::copy).orElse(null);
        this.issueDate = other.optionalIssueDate().map(LocalDateFilter::copy).orElse(null);
        this.expiryDate = other.optionalExpiryDate().map(LocalDateFilter::copy).orElse(null);
        this.verifiedById = other.optionalVerifiedById().map(LongFilter::copy).orElse(null);
        this.agencyLicenseTypeId = other.optionalAgencyLicenseTypeId().map(LongFilter::copy).orElse(null);
        this.belongsToId = other.optionalBelongsToId().map(LongFilter::copy).orElse(null);
        this.issuedById = other.optionalIssuedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AgencyLicenseCriteria copy() {
        return new AgencyLicenseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFilePath() {
        return filePath;
    }

    public Optional<StringFilter> optionalFilePath() {
        return Optional.ofNullable(filePath);
    }

    public StringFilter filePath() {
        if (filePath == null) {
            setFilePath(new StringFilter());
        }
        return filePath;
    }

    public void setFilePath(StringFilter filePath) {
        this.filePath = filePath;
    }

    public StringFilter getSerialNo() {
        return serialNo;
    }

    public Optional<StringFilter> optionalSerialNo() {
        return Optional.ofNullable(serialNo);
    }

    public StringFilter serialNo() {
        if (serialNo == null) {
            setSerialNo(new StringFilter());
        }
        return serialNo;
    }

    public void setSerialNo(StringFilter serialNo) {
        this.serialNo = serialNo;
    }

    public LocalDateFilter getIssueDate() {
        return issueDate;
    }

    public Optional<LocalDateFilter> optionalIssueDate() {
        return Optional.ofNullable(issueDate);
    }

    public LocalDateFilter issueDate() {
        if (issueDate == null) {
            setIssueDate(new LocalDateFilter());
        }
        return issueDate;
    }

    public void setIssueDate(LocalDateFilter issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateFilter getExpiryDate() {
        return expiryDate;
    }

    public Optional<LocalDateFilter> optionalExpiryDate() {
        return Optional.ofNullable(expiryDate);
    }

    public LocalDateFilter expiryDate() {
        if (expiryDate == null) {
            setExpiryDate(new LocalDateFilter());
        }
        return expiryDate;
    }

    public void setExpiryDate(LocalDateFilter expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LongFilter getVerifiedById() {
        return verifiedById;
    }

    public Optional<LongFilter> optionalVerifiedById() {
        return Optional.ofNullable(verifiedById);
    }

    public LongFilter verifiedById() {
        if (verifiedById == null) {
            setVerifiedById(new LongFilter());
        }
        return verifiedById;
    }

    public void setVerifiedById(LongFilter verifiedById) {
        this.verifiedById = verifiedById;
    }

    public LongFilter getAgencyLicenseTypeId() {
        return agencyLicenseTypeId;
    }

    public Optional<LongFilter> optionalAgencyLicenseTypeId() {
        return Optional.ofNullable(agencyLicenseTypeId);
    }

    public LongFilter agencyLicenseTypeId() {
        if (agencyLicenseTypeId == null) {
            setAgencyLicenseTypeId(new LongFilter());
        }
        return agencyLicenseTypeId;
    }

    public void setAgencyLicenseTypeId(LongFilter agencyLicenseTypeId) {
        this.agencyLicenseTypeId = agencyLicenseTypeId;
    }

    public LongFilter getBelongsToId() {
        return belongsToId;
    }

    public Optional<LongFilter> optionalBelongsToId() {
        return Optional.ofNullable(belongsToId);
    }

    public LongFilter belongsToId() {
        if (belongsToId == null) {
            setBelongsToId(new LongFilter());
        }
        return belongsToId;
    }

    public void setBelongsToId(LongFilter belongsToId) {
        this.belongsToId = belongsToId;
    }

    public LongFilter getIssuedById() {
        return issuedById;
    }

    public Optional<LongFilter> optionalIssuedById() {
        return Optional.ofNullable(issuedById);
    }

    public LongFilter issuedById() {
        if (issuedById == null) {
            setIssuedById(new LongFilter());
        }
        return issuedById;
    }

    public void setIssuedById(LongFilter issuedById) {
        this.issuedById = issuedById;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AgencyLicenseCriteria that = (AgencyLicenseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(serialNo, that.serialNo) &&
            Objects.equals(issueDate, that.issueDate) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(verifiedById, that.verifiedById) &&
            Objects.equals(agencyLicenseTypeId, that.agencyLicenseTypeId) &&
            Objects.equals(belongsToId, that.belongsToId) &&
            Objects.equals(issuedById, that.issuedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            filePath,
            serialNo,
            issueDate,
            expiryDate,
            verifiedById,
            agencyLicenseTypeId,
            belongsToId,
            issuedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyLicenseCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFilePath().map(f -> "filePath=" + f + ", ").orElse("") +
            optionalSerialNo().map(f -> "serialNo=" + f + ", ").orElse("") +
            optionalIssueDate().map(f -> "issueDate=" + f + ", ").orElse("") +
            optionalExpiryDate().map(f -> "expiryDate=" + f + ", ").orElse("") +
            optionalVerifiedById().map(f -> "verifiedById=" + f + ", ").orElse("") +
            optionalAgencyLicenseTypeId().map(f -> "agencyLicenseTypeId=" + f + ", ").orElse("") +
            optionalBelongsToId().map(f -> "belongsToId=" + f + ", ").orElse("") +
            optionalIssuedById().map(f -> "issuedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
