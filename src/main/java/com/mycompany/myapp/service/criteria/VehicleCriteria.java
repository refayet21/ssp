package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.LogStatusType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Vehicle} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.VehicleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vehicles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LogStatusType
     */
    public static class LogStatusTypeFilter extends Filter<LogStatusType> {

        public LogStatusTypeFilter() {}

        public LogStatusTypeFilter(LogStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public LogStatusTypeFilter copy() {
            return new LogStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter regNo;

    private StringFilter zone;

    private StringFilter category;

    private StringFilter serialNo;

    private StringFilter vehicleNo;

    private StringFilter chasisNo;

    private BooleanFilter isPersonal;

    private BooleanFilter isBlackListed;

    private LogStatusTypeFilter logStatus;

    private LongFilter documentId;

    private LongFilter vehicleAssigmentId;

    private LongFilter vehicleTypeId;

    private Boolean distinct;

    public VehicleCriteria() {}

    public VehicleCriteria(VehicleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.regNo = other.optionalRegNo().map(StringFilter::copy).orElse(null);
        this.zone = other.optionalZone().map(StringFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(StringFilter::copy).orElse(null);
        this.serialNo = other.optionalSerialNo().map(StringFilter::copy).orElse(null);
        this.vehicleNo = other.optionalVehicleNo().map(StringFilter::copy).orElse(null);
        this.chasisNo = other.optionalChasisNo().map(StringFilter::copy).orElse(null);
        this.isPersonal = other.optionalIsPersonal().map(BooleanFilter::copy).orElse(null);
        this.isBlackListed = other.optionalIsBlackListed().map(BooleanFilter::copy).orElse(null);
        this.logStatus = other.optionalLogStatus().map(LogStatusTypeFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.vehicleAssigmentId = other.optionalVehicleAssigmentId().map(LongFilter::copy).orElse(null);
        this.vehicleTypeId = other.optionalVehicleTypeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VehicleCriteria copy() {
        return new VehicleCriteria(this);
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

    public StringFilter getRegNo() {
        return regNo;
    }

    public Optional<StringFilter> optionalRegNo() {
        return Optional.ofNullable(regNo);
    }

    public StringFilter regNo() {
        if (regNo == null) {
            setRegNo(new StringFilter());
        }
        return regNo;
    }

    public void setRegNo(StringFilter regNo) {
        this.regNo = regNo;
    }

    public StringFilter getZone() {
        return zone;
    }

    public Optional<StringFilter> optionalZone() {
        return Optional.ofNullable(zone);
    }

    public StringFilter zone() {
        if (zone == null) {
            setZone(new StringFilter());
        }
        return zone;
    }

    public void setZone(StringFilter zone) {
        this.zone = zone;
    }

    public StringFilter getCategory() {
        return category;
    }

    public Optional<StringFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public StringFilter category() {
        if (category == null) {
            setCategory(new StringFilter());
        }
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
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

    public StringFilter getVehicleNo() {
        return vehicleNo;
    }

    public Optional<StringFilter> optionalVehicleNo() {
        return Optional.ofNullable(vehicleNo);
    }

    public StringFilter vehicleNo() {
        if (vehicleNo == null) {
            setVehicleNo(new StringFilter());
        }
        return vehicleNo;
    }

    public void setVehicleNo(StringFilter vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public StringFilter getChasisNo() {
        return chasisNo;
    }

    public Optional<StringFilter> optionalChasisNo() {
        return Optional.ofNullable(chasisNo);
    }

    public StringFilter chasisNo() {
        if (chasisNo == null) {
            setChasisNo(new StringFilter());
        }
        return chasisNo;
    }

    public void setChasisNo(StringFilter chasisNo) {
        this.chasisNo = chasisNo;
    }

    public BooleanFilter getIsPersonal() {
        return isPersonal;
    }

    public Optional<BooleanFilter> optionalIsPersonal() {
        return Optional.ofNullable(isPersonal);
    }

    public BooleanFilter isPersonal() {
        if (isPersonal == null) {
            setIsPersonal(new BooleanFilter());
        }
        return isPersonal;
    }

    public void setIsPersonal(BooleanFilter isPersonal) {
        this.isPersonal = isPersonal;
    }

    public BooleanFilter getIsBlackListed() {
        return isBlackListed;
    }

    public Optional<BooleanFilter> optionalIsBlackListed() {
        return Optional.ofNullable(isBlackListed);
    }

    public BooleanFilter isBlackListed() {
        if (isBlackListed == null) {
            setIsBlackListed(new BooleanFilter());
        }
        return isBlackListed;
    }

    public void setIsBlackListed(BooleanFilter isBlackListed) {
        this.isBlackListed = isBlackListed;
    }

    public LogStatusTypeFilter getLogStatus() {
        return logStatus;
    }

    public Optional<LogStatusTypeFilter> optionalLogStatus() {
        return Optional.ofNullable(logStatus);
    }

    public LogStatusTypeFilter logStatus() {
        if (logStatus == null) {
            setLogStatus(new LogStatusTypeFilter());
        }
        return logStatus;
    }

    public void setLogStatus(LogStatusTypeFilter logStatus) {
        this.logStatus = logStatus;
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

    public LongFilter getVehicleAssigmentId() {
        return vehicleAssigmentId;
    }

    public Optional<LongFilter> optionalVehicleAssigmentId() {
        return Optional.ofNullable(vehicleAssigmentId);
    }

    public LongFilter vehicleAssigmentId() {
        if (vehicleAssigmentId == null) {
            setVehicleAssigmentId(new LongFilter());
        }
        return vehicleAssigmentId;
    }

    public void setVehicleAssigmentId(LongFilter vehicleAssigmentId) {
        this.vehicleAssigmentId = vehicleAssigmentId;
    }

    public LongFilter getVehicleTypeId() {
        return vehicleTypeId;
    }

    public Optional<LongFilter> optionalVehicleTypeId() {
        return Optional.ofNullable(vehicleTypeId);
    }

    public LongFilter vehicleTypeId() {
        if (vehicleTypeId == null) {
            setVehicleTypeId(new LongFilter());
        }
        return vehicleTypeId;
    }

    public void setVehicleTypeId(LongFilter vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
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
        final VehicleCriteria that = (VehicleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(regNo, that.regNo) &&
            Objects.equals(zone, that.zone) &&
            Objects.equals(category, that.category) &&
            Objects.equals(serialNo, that.serialNo) &&
            Objects.equals(vehicleNo, that.vehicleNo) &&
            Objects.equals(chasisNo, that.chasisNo) &&
            Objects.equals(isPersonal, that.isPersonal) &&
            Objects.equals(isBlackListed, that.isBlackListed) &&
            Objects.equals(logStatus, that.logStatus) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(vehicleAssigmentId, that.vehicleAssigmentId) &&
            Objects.equals(vehicleTypeId, that.vehicleTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            regNo,
            zone,
            category,
            serialNo,
            vehicleNo,
            chasisNo,
            isPersonal,
            isBlackListed,
            logStatus,
            documentId,
            vehicleAssigmentId,
            vehicleTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalRegNo().map(f -> "regNo=" + f + ", ").orElse("") +
            optionalZone().map(f -> "zone=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalSerialNo().map(f -> "serialNo=" + f + ", ").orElse("") +
            optionalVehicleNo().map(f -> "vehicleNo=" + f + ", ").orElse("") +
            optionalChasisNo().map(f -> "chasisNo=" + f + ", ").orElse("") +
            optionalIsPersonal().map(f -> "isPersonal=" + f + ", ").orElse("") +
            optionalIsBlackListed().map(f -> "isBlackListed=" + f + ", ").orElse("") +
            optionalLogStatus().map(f -> "logStatus=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalVehicleAssigmentId().map(f -> "vehicleAssigmentId=" + f + ", ").orElse("") +
            optionalVehicleTypeId().map(f -> "vehicleTypeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
