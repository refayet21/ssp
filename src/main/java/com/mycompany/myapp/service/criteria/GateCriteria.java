package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.GateType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Gate} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.GateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GateCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GateType
     */
    public static class GateTypeFilter extends Filter<GateType> {

        public GateTypeFilter() {}

        public GateTypeFilter(GateTypeFilter filter) {
            super(filter);
        }

        @Override
        public GateTypeFilter copy() {
            return new GateTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter shortName;

    private FloatFilter lat;

    private FloatFilter lon;

    private GateTypeFilter gateType;

    private BooleanFilter isActive;

    private LongFilter laneId;

    private LongFilter zoneId;

    private Boolean distinct;

    public GateCriteria() {}

    public GateCriteria(GateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.shortName = other.optionalShortName().map(StringFilter::copy).orElse(null);
        this.lat = other.optionalLat().map(FloatFilter::copy).orElse(null);
        this.lon = other.optionalLon().map(FloatFilter::copy).orElse(null);
        this.gateType = other.optionalGateType().map(GateTypeFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.laneId = other.optionalLaneId().map(LongFilter::copy).orElse(null);
        this.zoneId = other.optionalZoneId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public GateCriteria copy() {
        return new GateCriteria(this);
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

    public FloatFilter getLat() {
        return lat;
    }

    public Optional<FloatFilter> optionalLat() {
        return Optional.ofNullable(lat);
    }

    public FloatFilter lat() {
        if (lat == null) {
            setLat(new FloatFilter());
        }
        return lat;
    }

    public void setLat(FloatFilter lat) {
        this.lat = lat;
    }

    public FloatFilter getLon() {
        return lon;
    }

    public Optional<FloatFilter> optionalLon() {
        return Optional.ofNullable(lon);
    }

    public FloatFilter lon() {
        if (lon == null) {
            setLon(new FloatFilter());
        }
        return lon;
    }

    public void setLon(FloatFilter lon) {
        this.lon = lon;
    }

    public GateTypeFilter getGateType() {
        return gateType;
    }

    public Optional<GateTypeFilter> optionalGateType() {
        return Optional.ofNullable(gateType);
    }

    public GateTypeFilter gateType() {
        if (gateType == null) {
            setGateType(new GateTypeFilter());
        }
        return gateType;
    }

    public void setGateType(GateTypeFilter gateType) {
        this.gateType = gateType;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getLaneId() {
        return laneId;
    }

    public Optional<LongFilter> optionalLaneId() {
        return Optional.ofNullable(laneId);
    }

    public LongFilter laneId() {
        if (laneId == null) {
            setLaneId(new LongFilter());
        }
        return laneId;
    }

    public void setLaneId(LongFilter laneId) {
        this.laneId = laneId;
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
        final GateCriteria that = (GateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(shortName, that.shortName) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(lon, that.lon) &&
            Objects.equals(gateType, that.gateType) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(laneId, that.laneId) &&
            Objects.equals(zoneId, that.zoneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shortName, lat, lon, gateType, isActive, laneId, zoneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalShortName().map(f -> "shortName=" + f + ", ").orElse("") +
            optionalLat().map(f -> "lat=" + f + ", ").orElse("") +
            optionalLon().map(f -> "lon=" + f + ", ").orElse("") +
            optionalGateType().map(f -> "gateType=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalLaneId().map(f -> "laneId=" + f + ", ").orElse("") +
            optionalZoneId().map(f -> "zoneId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
