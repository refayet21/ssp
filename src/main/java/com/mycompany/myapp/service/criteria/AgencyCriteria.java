package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Agency} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AgencyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agencies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgencyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter shortName;

    private BooleanFilter isInternal;

    private BooleanFilter isDummy;

    private LongFilter addressId;

    private LongFilter documentId;

    private LongFilter assignmentId;

    private LongFilter licenseId;

    private LongFilter issuerId;

    private LongFilter departmentId;

    private LongFilter agencyTypeId;

    private LongFilter zoneId;

    private LongFilter passTypeId;

    private Boolean distinct;

    public AgencyCriteria() {}

    public AgencyCriteria(AgencyCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.shortName = other.optionalShortName().map(StringFilter::copy).orElse(null);
        this.isInternal = other.optionalIsInternal().map(BooleanFilter::copy).orElse(null);
        this.isDummy = other.optionalIsDummy().map(BooleanFilter::copy).orElse(null);
        this.addressId = other.optionalAddressId().map(LongFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.assignmentId = other.optionalAssignmentId().map(LongFilter::copy).orElse(null);
        this.licenseId = other.optionalLicenseId().map(LongFilter::copy).orElse(null);
        this.issuerId = other.optionalIssuerId().map(LongFilter::copy).orElse(null);
        this.departmentId = other.optionalDepartmentId().map(LongFilter::copy).orElse(null);
        this.agencyTypeId = other.optionalAgencyTypeId().map(LongFilter::copy).orElse(null);
        this.zoneId = other.optionalZoneId().map(LongFilter::copy).orElse(null);
        this.passTypeId = other.optionalPassTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AgencyCriteria copy() {
        return new AgencyCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getShortName() {
        return shortName;
    }

    public Optional<StringFilter> optionalShortName() {
        return Optional.ofNullable(shortName);
    }

    public StringFilter shortName() {
        if (shortName == null) {
            setShortName(new StringFilter());
        }
        return shortName;
    }

    public void setShortName(StringFilter shortName) {
        this.shortName = shortName;
    }

    public BooleanFilter getIsInternal() {
        return isInternal;
    }

    public Optional<BooleanFilter> optionalIsInternal() {
        return Optional.ofNullable(isInternal);
    }

    public BooleanFilter isInternal() {
        if (isInternal == null) {
            setIsInternal(new BooleanFilter());
        }
        return isInternal;
    }

    public void setIsInternal(BooleanFilter isInternal) {
        this.isInternal = isInternal;
    }

    public BooleanFilter getIsDummy() {
        return isDummy;
    }

    public Optional<BooleanFilter> optionalIsDummy() {
        return Optional.ofNullable(isDummy);
    }

    public BooleanFilter isDummy() {
        if (isDummy == null) {
            setIsDummy(new BooleanFilter());
        }
        return isDummy;
    }

    public void setIsDummy(BooleanFilter isDummy) {
        this.isDummy = isDummy;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public Optional<LongFilter> optionalAddressId() {
        return Optional.ofNullable(addressId);
    }

    public LongFilter addressId() {
        if (addressId == null) {
            setAddressId(new LongFilter());
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getDocumentId() {
        return documentId;
    }

    public Optional<LongFilter> optionalDocumentId() {
        return Optional.ofNullable(documentId);
    }

    public LongFilter documentId() {
        if (documentId == null) {
            setDocumentId(new LongFilter());
        }
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
    }

    public LongFilter getAssignmentId() {
        return assignmentId;
    }

    public Optional<LongFilter> optionalAssignmentId() {
        return Optional.ofNullable(assignmentId);
    }

    public LongFilter assignmentId() {
        if (assignmentId == null) {
            setAssignmentId(new LongFilter());
        }
        return assignmentId;
    }

    public void setAssignmentId(LongFilter assignmentId) {
        this.assignmentId = assignmentId;
    }

    public LongFilter getLicenseId() {
        return licenseId;
    }

    public Optional<LongFilter> optionalLicenseId() {
        return Optional.ofNullable(licenseId);
    }

    public LongFilter licenseId() {
        if (licenseId == null) {
            setLicenseId(new LongFilter());
        }
        return licenseId;
    }

    public void setLicenseId(LongFilter licenseId) {
        this.licenseId = licenseId;
    }

    public LongFilter getIssuerId() {
        return issuerId;
    }

    public Optional<LongFilter> optionalIssuerId() {
        return Optional.ofNullable(issuerId);
    }

    public LongFilter issuerId() {
        if (issuerId == null) {
            setIssuerId(new LongFilter());
        }
        return issuerId;
    }

    public void setIssuerId(LongFilter issuerId) {
        this.issuerId = issuerId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public Optional<LongFilter> optionalDepartmentId() {
        return Optional.ofNullable(departmentId);
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            setDepartmentId(new LongFilter());
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
    }

    public LongFilter getAgencyTypeId() {
        return agencyTypeId;
    }

    public Optional<LongFilter> optionalAgencyTypeId() {
        return Optional.ofNullable(agencyTypeId);
    }

    public LongFilter agencyTypeId() {
        if (agencyTypeId == null) {
            setAgencyTypeId(new LongFilter());
        }
        return agencyTypeId;
    }

    public void setAgencyTypeId(LongFilter agencyTypeId) {
        this.agencyTypeId = agencyTypeId;
    }

    public LongFilter getZoneId() {
        return zoneId;
    }

    public Optional<LongFilter> optionalZoneId() {
        return Optional.ofNullable(zoneId);
    }

    public LongFilter zoneId() {
        if (zoneId == null) {
            setZoneId(new LongFilter());
        }
        return zoneId;
    }

    public void setZoneId(LongFilter zoneId) {
        this.zoneId = zoneId;
    }

    public LongFilter getPassTypeId() {
        return passTypeId;
    }

    public Optional<LongFilter> optionalPassTypeId() {
        return Optional.ofNullable(passTypeId);
    }

    public LongFilter passTypeId() {
        if (passTypeId == null) {
            setPassTypeId(new LongFilter());
        }
        return passTypeId;
    }

    public void setPassTypeId(LongFilter passTypeId) {
        this.passTypeId = passTypeId;
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
        final AgencyCriteria that = (AgencyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(shortName, that.shortName) &&
            Objects.equals(isInternal, that.isInternal) &&
            Objects.equals(isDummy, that.isDummy) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(assignmentId, that.assignmentId) &&
            Objects.equals(licenseId, that.licenseId) &&
            Objects.equals(issuerId, that.issuerId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(agencyTypeId, that.agencyTypeId) &&
            Objects.equals(zoneId, that.zoneId) &&
            Objects.equals(passTypeId, that.passTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            shortName,
            isInternal,
            isDummy,
            addressId,
            documentId,
            assignmentId,
            licenseId,
            issuerId,
            departmentId,
            agencyTypeId,
            zoneId,
            passTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalShortName().map(f -> "shortName=" + f + ", ").orElse("") +
            optionalIsInternal().map(f -> "isInternal=" + f + ", ").orElse("") +
            optionalIsDummy().map(f -> "isDummy=" + f + ", ").orElse("") +
            optionalAddressId().map(f -> "addressId=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalAssignmentId().map(f -> "assignmentId=" + f + ", ").orElse("") +
            optionalLicenseId().map(f -> "licenseId=" + f + ", ").orElse("") +
            optionalIssuerId().map(f -> "issuerId=" + f + ", ").orElse("") +
            optionalDepartmentId().map(f -> "departmentId=" + f + ", ").orElse("") +
            optionalAgencyTypeId().map(f -> "agencyTypeId=" + f + ", ").orElse("") +
            optionalZoneId().map(f -> "zoneId=" + f + ", ").orElse("") +
            optionalPassTypeId().map(f -> "passTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
