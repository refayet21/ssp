package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Document} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter isPrimary;

    private StringFilter serial;

    private LocalDateFilter issueDate;

    private LocalDateFilter expiryDate;

    private StringFilter filePath;

    private LongFilter verifiedById;

    private LongFilter documentTypeId;

    private LongFilter personId;

    private LongFilter vehicleId;

    private LongFilter agencyId;

    private Boolean distinct;

    public DocumentCriteria() {}

    public DocumentCriteria(DocumentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.isPrimary = other.optionalIsPrimary().map(BooleanFilter::copy).orElse(null);
        this.serial = other.optionalSerial().map(StringFilter::copy).orElse(null);
        this.issueDate = other.optionalIssueDate().map(LocalDateFilter::copy).orElse(null);
        this.expiryDate = other.optionalExpiryDate().map(LocalDateFilter::copy).orElse(null);
        this.filePath = other.optionalFilePath().map(StringFilter::copy).orElse(null);
        this.verifiedById = other.optionalVerifiedById().map(LongFilter::copy).orElse(null);
        this.documentTypeId = other.optionalDocumentTypeId().map(LongFilter::copy).orElse(null);
        this.personId = other.optionalPersonId().map(LongFilter::copy).orElse(null);
        this.vehicleId = other.optionalVehicleId().map(LongFilter::copy).orElse(null);
        this.agencyId = other.optionalAgencyId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentCriteria copy() {
        return new DocumentCriteria(this);
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

    public BooleanFilter getIsPrimary() {
        return isPrimary;
    }

    public Optional<BooleanFilter> optionalIsPrimary() {
        return Optional.ofNullable(isPrimary);
    }

    public BooleanFilter isPrimary() {
        if (isPrimary == null) {
            setIsPrimary(new BooleanFilter());
        }
        return isPrimary;
    }

    public void setIsPrimary(BooleanFilter isPrimary) {
        this.isPrimary = isPrimary;
    }

    public StringFilter getSerial() {
        return serial;
    }

    public Optional<StringFilter> optionalSerial() {
        return Optional.ofNullable(serial);
    }

    public StringFilter serial() {
        if (serial == null) {
            setSerial(new StringFilter());
        }
        return serial;
    }

    public void setSerial(StringFilter serial) {
        this.serial = serial;
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

    public LongFilter getDocumentTypeId() {
        return documentTypeId;
    }

    public Optional<LongFilter> optionalDocumentTypeId() {
        return Optional.ofNullable(documentTypeId);
    }

    public LongFilter documentTypeId() {
        if (documentTypeId == null) {
            setDocumentTypeId(new LongFilter());
        }
        return documentTypeId;
    }

    public void setDocumentTypeId(LongFilter documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public LongFilter getPersonId() {
        return personId;
    }

    public Optional<LongFilter> optionalPersonId() {
        return Optional.ofNullable(personId);
    }

    public LongFilter personId() {
        if (personId == null) {
            setPersonId(new LongFilter());
        }
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }

    public LongFilter getVehicleId() {
        return vehicleId;
    }

    public Optional<LongFilter> optionalVehicleId() {
        return Optional.ofNullable(vehicleId);
    }

    public LongFilter vehicleId() {
        if (vehicleId == null) {
            setVehicleId(new LongFilter());
        }
        return vehicleId;
    }

    public void setVehicleId(LongFilter vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LongFilter getAgencyId() {
        return agencyId;
    }

    public Optional<LongFilter> optionalAgencyId() {
        return Optional.ofNullable(agencyId);
    }

    public LongFilter agencyId() {
        if (agencyId == null) {
            setAgencyId(new LongFilter());
        }
        return agencyId;
    }

    public void setAgencyId(LongFilter agencyId) {
        this.agencyId = agencyId;
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
        final DocumentCriteria that = (DocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(isPrimary, that.isPrimary) &&
            Objects.equals(serial, that.serial) &&
            Objects.equals(issueDate, that.issueDate) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(filePath, that.filePath) &&
            Objects.equals(verifiedById, that.verifiedById) &&
            Objects.equals(documentTypeId, that.documentTypeId) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(agencyId, that.agencyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            isPrimary,
            serial,
            issueDate,
            expiryDate,
            filePath,
            verifiedById,
            documentTypeId,
            personId,
            vehicleId,
            agencyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIsPrimary().map(f -> "isPrimary=" + f + ", ").orElse("") +
            optionalSerial().map(f -> "serial=" + f + ", ").orElse("") +
            optionalIssueDate().map(f -> "issueDate=" + f + ", ").orElse("") +
            optionalExpiryDate().map(f -> "expiryDate=" + f + ", ").orElse("") +
            optionalFilePath().map(f -> "filePath=" + f + ", ").orElse("") +
            optionalVerifiedById().map(f -> "verifiedById=" + f + ", ").orElse("") +
            optionalDocumentTypeId().map(f -> "documentTypeId=" + f + ", ").orElse("") +
            optionalPersonId().map(f -> "personId=" + f + ", ").orElse("") +
            optionalVehicleId().map(f -> "vehicleId=" + f + ", ").orElse("") +
            optionalAgencyId().map(f -> "agencyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
