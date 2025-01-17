package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Upazila} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UpazilaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /upazilas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UpazilaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter unionId;

    private LongFilter cityCorpPouraId;

    private LongFilter districtId;

    private Boolean distinct;

    public UpazilaCriteria() {}

    public UpazilaCriteria(UpazilaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.unionId = other.optionalUnionId().map(LongFilter::copy).orElse(null);
        this.cityCorpPouraId = other.optionalCityCorpPouraId().map(LongFilter::copy).orElse(null);
        this.districtId = other.optionalDistrictId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UpazilaCriteria copy() {
        return new UpazilaCriteria(this);
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

    public LongFilter getUnionId() {
        return unionId;
    }

    public Optional<LongFilter> optionalUnionId() {
        return Optional.ofNullable(unionId);
    }

    public LongFilter unionId() {
        if (unionId == null) {
            setUnionId(new LongFilter());
        }
        return unionId;
    }

    public void setUnionId(LongFilter unionId) {
        this.unionId = unionId;
    }

    public LongFilter getCityCorpPouraId() {
        return cityCorpPouraId;
    }

    public Optional<LongFilter> optionalCityCorpPouraId() {
        return Optional.ofNullable(cityCorpPouraId);
    }

    public LongFilter cityCorpPouraId() {
        if (cityCorpPouraId == null) {
            setCityCorpPouraId(new LongFilter());
        }
        return cityCorpPouraId;
    }

    public void setCityCorpPouraId(LongFilter cityCorpPouraId) {
        this.cityCorpPouraId = cityCorpPouraId;
    }

    public LongFilter getDistrictId() {
        return districtId;
    }

    public Optional<LongFilter> optionalDistrictId() {
        return Optional.ofNullable(districtId);
    }

    public LongFilter districtId() {
        if (districtId == null) {
            setDistrictId(new LongFilter());
        }
        return districtId;
    }

    public void setDistrictId(LongFilter districtId) {
        this.districtId = districtId;
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
        final UpazilaCriteria that = (UpazilaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(unionId, that.unionId) &&
            Objects.equals(cityCorpPouraId, that.cityCorpPouraId) &&
            Objects.equals(districtId, that.districtId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unionId, cityCorpPouraId, districtId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UpazilaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalUnionId().map(f -> "unionId=" + f + ", ").orElse("") +
            optionalCityCorpPouraId().map(f -> "cityCorpPouraId=" + f + ", ").orElse("") +
            optionalDistrictId().map(f -> "districtId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
