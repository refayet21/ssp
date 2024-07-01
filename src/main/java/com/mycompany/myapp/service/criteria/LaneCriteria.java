package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.DirectionType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Lane} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LaneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lanes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LaneCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DirectionType
     */
    public static class DirectionTypeFilter extends Filter<DirectionType> {

        public DirectionTypeFilter() {}

        public DirectionTypeFilter(DirectionTypeFilter filter) {
            super(filter);
        }

        @Override
        public DirectionTypeFilter copy() {
            return new DirectionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter shortName;

    private DirectionTypeFilter direction;

    private BooleanFilter isActive;

    private LongFilter entryLogId;

    private LongFilter gateId;

    private LongFilter accessProfileId;

    private Boolean distinct;

    public LaneCriteria() {}

    public LaneCriteria(LaneCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.shortName = other.optionalShortName().map(StringFilter::copy).orElse(null);
        this.direction = other.optionalDirection().map(DirectionTypeFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.entryLogId = other.optionalEntryLogId().map(LongFilter::copy).orElse(null);
        this.gateId = other.optionalGateId().map(LongFilter::copy).orElse(null);
        this.accessProfileId = other.optionalAccessProfileId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LaneCriteria copy() {
        return new LaneCriteria(this);
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

    public DirectionTypeFilter getDirection() {
        return direction;
    }

    public Optional<DirectionTypeFilter> optionalDirection() {
        return Optional.ofNullable(direction);
    }

    public DirectionTypeFilter direction() {
        if (direction == null) {
            setDirection(new DirectionTypeFilter());
        }
        return direction;
    }

    public void setDirection(DirectionTypeFilter direction) {
        this.direction = direction;
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

    public LongFilter getGateId() {
        return gateId;
    }

    public Optional<LongFilter> optionalGateId() {
        return Optional.ofNullable(gateId);
    }

    public LongFilter gateId() {
        if (gateId == null) {
            setGateId(new LongFilter());
        }
        return gateId;
    }

    public void setGateId(LongFilter gateId) {
        this.gateId = gateId;
    }

    public LongFilter getAccessProfileId() {
        return accessProfileId;
    }

    public Optional<LongFilter> optionalAccessProfileId() {
        return Optional.ofNullable(accessProfileId);
    }

    public LongFilter accessProfileId() {
        if (accessProfileId == null) {
            setAccessProfileId(new LongFilter());
        }
        return accessProfileId;
    }

    public void setAccessProfileId(LongFilter accessProfileId) {
        this.accessProfileId = accessProfileId;
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
        final LaneCriteria that = (LaneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(shortName, that.shortName) &&
            Objects.equals(direction, that.direction) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(entryLogId, that.entryLogId) &&
            Objects.equals(gateId, that.gateId) &&
            Objects.equals(accessProfileId, that.accessProfileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shortName, direction, isActive, entryLogId, gateId, accessProfileId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LaneCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalShortName().map(f -> "shortName=" + f + ", ").orElse("") +
            optionalDirection().map(f -> "direction=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalEntryLogId().map(f -> "entryLogId=" + f + ", ").orElse("") +
            optionalGateId().map(f -> "gateId=" + f + ", ").orElse("") +
            optionalAccessProfileId().map(f -> "accessProfileId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
