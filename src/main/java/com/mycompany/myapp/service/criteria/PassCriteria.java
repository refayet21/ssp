package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.PassStatusType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Pass} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PassResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /passes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PassCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PassStatusType
     */
    public static class PassStatusTypeFilter extends Filter<PassStatusType> {

        public PassStatusTypeFilter() {}

        public PassStatusTypeFilter(PassStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public PassStatusTypeFilter copy() {
            return new PassStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter collectedFee;

    private InstantFilter fromDate;

    private InstantFilter endDate;

    private PassStatusTypeFilter status;

    private LongFilter passNumber;

    private StringFilter mediaSerial;

    private LongFilter entryLogId;

    private LongFilter passTypeId;

    private LongFilter requestedById;

    private LongFilter assignmentId;

    private LongFilter vehicleAssignmentId;

    private Boolean distinct;

    public PassCriteria() {}

    public PassCriteria(PassCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.collectedFee = other.optionalCollectedFee().map(DoubleFilter::copy).orElse(null);
        this.fromDate = other.optionalFromDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(PassStatusTypeFilter::copy).orElse(null);
        this.passNumber = other.optionalPassNumber().map(LongFilter::copy).orElse(null);
        this.mediaSerial = other.optionalMediaSerial().map(StringFilter::copy).orElse(null);
        this.entryLogId = other.optionalEntryLogId().map(LongFilter::copy).orElse(null);
        this.passTypeId = other.optionalPassTypeId().map(LongFilter::copy).orElse(null);
        this.requestedById = other.optionalRequestedById().map(LongFilter::copy).orElse(null);
        this.assignmentId = other.optionalAssignmentId().map(LongFilter::copy).orElse(null);
        this.vehicleAssignmentId = other.optionalVehicleAssignmentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PassCriteria copy() {
        return new PassCriteria(this);
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

    public DoubleFilter getCollectedFee() {
        return collectedFee;
    }

    public Optional<DoubleFilter> optionalCollectedFee() {
        return Optional.ofNullable(collectedFee);
    }

    public DoubleFilter collectedFee() {
        if (collectedFee == null) {
            setCollectedFee(new DoubleFilter());
        }
        return collectedFee;
    }

    public void setCollectedFee(DoubleFilter collectedFee) {
        this.collectedFee = collectedFee;
    }

    public InstantFilter getFromDate() {
        return fromDate;
    }

    public Optional<InstantFilter> optionalFromDate() {
        return Optional.ofNullable(fromDate);
    }

    public InstantFilter fromDate() {
        if (fromDate == null) {
            setFromDate(new InstantFilter());
        }
        return fromDate;
    }

    public void setFromDate(InstantFilter fromDate) {
        this.fromDate = fromDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public PassStatusTypeFilter getStatus() {
        return status;
    }

    public Optional<PassStatusTypeFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public PassStatusTypeFilter status() {
        if (status == null) {
            setStatus(new PassStatusTypeFilter());
        }
        return status;
    }

    public void setStatus(PassStatusTypeFilter status) {
        this.status = status;
    }

    public LongFilter getPassNumber() {
        return passNumber;
    }

    public Optional<LongFilter> optionalPassNumber() {
        return Optional.ofNullable(passNumber);
    }

    public LongFilter passNumber() {
        if (passNumber == null) {
            setPassNumber(new LongFilter());
        }
        return passNumber;
    }

    public void setPassNumber(LongFilter passNumber) {
        this.passNumber = passNumber;
    }

    public StringFilter getMediaSerial() {
        return mediaSerial;
    }

    public Optional<StringFilter> optionalMediaSerial() {
        return Optional.ofNullable(mediaSerial);
    }

    public StringFilter mediaSerial() {
        if (mediaSerial == null) {
            setMediaSerial(new StringFilter());
        }
        return mediaSerial;
    }

    public void setMediaSerial(StringFilter mediaSerial) {
        this.mediaSerial = mediaSerial;
    }

    public LongFilter getEntryLogId() {
        return entryLogId;
    }

    public Optional<LongFilter> optionalEntryLogId() {
        return Optional.ofNullable(entryLogId);
    }

    public LongFilter entryLogId() {
        if (entryLogId == null) {
            setEntryLogId(new LongFilter());
        }
        return entryLogId;
    }

    public void setEntryLogId(LongFilter entryLogId) {
        this.entryLogId = entryLogId;
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

    public LongFilter getRequestedById() {
        return requestedById;
    }

    public Optional<LongFilter> optionalRequestedById() {
        return Optional.ofNullable(requestedById);
    }

    public LongFilter requestedById() {
        if (requestedById == null) {
            setRequestedById(new LongFilter());
        }
        return requestedById;
    }

    public void setRequestedById(LongFilter requestedById) {
        this.requestedById = requestedById;
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

    public LongFilter getVehicleAssignmentId() {
        return vehicleAssignmentId;
    }

    public Optional<LongFilter> optionalVehicleAssignmentId() {
        return Optional.ofNullable(vehicleAssignmentId);
    }

    public LongFilter vehicleAssignmentId() {
        if (vehicleAssignmentId == null) {
            setVehicleAssignmentId(new LongFilter());
        }
        return vehicleAssignmentId;
    }

    public void setVehicleAssignmentId(LongFilter vehicleAssignmentId) {
        this.vehicleAssignmentId = vehicleAssignmentId;
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
        final PassCriteria that = (PassCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(collectedFee, that.collectedFee) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(passNumber, that.passNumber) &&
            Objects.equals(mediaSerial, that.mediaSerial) &&
            Objects.equals(entryLogId, that.entryLogId) &&
            Objects.equals(passTypeId, that.passTypeId) &&
            Objects.equals(requestedById, that.requestedById) &&
            Objects.equals(assignmentId, that.assignmentId) &&
            Objects.equals(vehicleAssignmentId, that.vehicleAssignmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            collectedFee,
            fromDate,
            endDate,
            status,
            passNumber,
            mediaSerial,
            entryLogId,
            passTypeId,
            requestedById,
            assignmentId,
            vehicleAssignmentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PassCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCollectedFee().map(f -> "collectedFee=" + f + ", ").orElse("") +
            optionalFromDate().map(f -> "fromDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPassNumber().map(f -> "passNumber=" + f + ", ").orElse("") +
            optionalMediaSerial().map(f -> "mediaSerial=" + f + ", ").orElse("") +
            optionalEntryLogId().map(f -> "entryLogId=" + f + ", ").orElse("") +
            optionalPassTypeId().map(f -> "passTypeId=" + f + ", ").orElse("") +
            optionalRequestedById().map(f -> "requestedById=" + f + ", ").orElse("") +
            optionalAssignmentId().map(f -> "assignmentId=" + f + ", ").orElse("") +
            optionalVehicleAssignmentId().map(f -> "vehicleAssignmentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
