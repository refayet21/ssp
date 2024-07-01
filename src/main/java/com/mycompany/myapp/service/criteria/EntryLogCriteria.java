package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.ActionType;
import com.mycompany.myapp.domain.enumeration.DirectionType;
import com.mycompany.myapp.domain.enumeration.PassStatusType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.EntryLog} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.EntryLogResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /entry-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntryLogCriteria implements Serializable, Criteria {

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

    /**
     * Class for filtering ActionType
     */
    public static class ActionTypeFilter extends Filter<ActionType> {

        public ActionTypeFilter() {}

        public ActionTypeFilter(ActionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ActionTypeFilter copy() {
            return new ActionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter eventTime;

    private DirectionTypeFilter direction;

    private PassStatusTypeFilter passStatus;

    private ActionTypeFilter actionType;

    private LongFilter passId;

    private LongFilter laneId;

    private Boolean distinct;

    public EntryLogCriteria() {}

    public EntryLogCriteria(EntryLogCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.eventTime = other.optionalEventTime().map(InstantFilter::copy).orElse(null);
        this.direction = other.optionalDirection().map(DirectionTypeFilter::copy).orElse(null);
        this.passStatus = other.optionalPassStatus().map(PassStatusTypeFilter::copy).orElse(null);
        this.actionType = other.optionalActionType().map(ActionTypeFilter::copy).orElse(null);
        this.passId = other.optionalPassId().map(LongFilter::copy).orElse(null);
        this.laneId = other.optionalLaneId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EntryLogCriteria copy() {
        return new EntryLogCriteria(this);
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

    public InstantFilter getEventTime() {
        return eventTime;
    }

    public Optional<InstantFilter> optionalEventTime() {
        return Optional.ofNullable(eventTime);
    }

    public InstantFilter eventTime() {
        if (eventTime == null) {
            setEventTime(new InstantFilter());
        }
        return eventTime;
    }

    public void setEventTime(InstantFilter eventTime) {
        this.eventTime = eventTime;
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

    public PassStatusTypeFilter getPassStatus() {
        return passStatus;
    }

    public Optional<PassStatusTypeFilter> optionalPassStatus() {
        return Optional.ofNullable(passStatus);
    }

    public PassStatusTypeFilter passStatus() {
        if (passStatus == null) {
            setPassStatus(new PassStatusTypeFilter());
        }
        return passStatus;
    }

    public void setPassStatus(PassStatusTypeFilter passStatus) {
        this.passStatus = passStatus;
    }

    public ActionTypeFilter getActionType() {
        return actionType;
    }

    public Optional<ActionTypeFilter> optionalActionType() {
        return Optional.ofNullable(actionType);
    }

    public ActionTypeFilter actionType() {
        if (actionType == null) {
            setActionType(new ActionTypeFilter());
        }
        return actionType;
    }

    public void setActionType(ActionTypeFilter actionType) {
        this.actionType = actionType;
    }

    public LongFilter getPassId() {
        return passId;
    }

    public Optional<LongFilter> optionalPassId() {
        return Optional.ofNullable(passId);
    }

    public LongFilter passId() {
        if (passId == null) {
            setPassId(new LongFilter());
        }
        return passId;
    }

    public void setPassId(LongFilter passId) {
        this.passId = passId;
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
        final EntryLogCriteria that = (EntryLogCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventTime, that.eventTime) &&
            Objects.equals(direction, that.direction) &&
            Objects.equals(passStatus, that.passStatus) &&
            Objects.equals(actionType, that.actionType) &&
            Objects.equals(passId, that.passId) &&
            Objects.equals(laneId, that.laneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventTime, direction, passStatus, actionType, passId, laneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntryLogCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEventTime().map(f -> "eventTime=" + f + ", ").orElse("") +
            optionalDirection().map(f -> "direction=" + f + ", ").orElse("") +
            optionalPassStatus().map(f -> "passStatus=" + f + ", ").orElse("") +
            optionalActionType().map(f -> "actionType=" + f + ", ").orElse("") +
            optionalPassId().map(f -> "passId=" + f + ", ").orElse("") +
            optionalLaneId().map(f -> "laneId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
