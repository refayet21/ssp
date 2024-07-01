package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.DocumentMasterType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.DocumentType} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DocumentTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentTypeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DocumentMasterType
     */
    public static class DocumentMasterTypeFilter extends Filter<DocumentMasterType> {

        public DocumentMasterTypeFilter() {}

        public DocumentMasterTypeFilter(DocumentMasterTypeFilter filter) {
            super(filter);
        }

        @Override
        public DocumentMasterTypeFilter copy() {
            return new DocumentMasterTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter isActive;

    private StringFilter description;

    private DocumentMasterTypeFilter documentMasterType;

    private BooleanFilter requiresVerification;

    private LongFilter documentId;

    private Boolean distinct;

    public DocumentTypeCriteria() {}

    public DocumentTypeCriteria(DocumentTypeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.documentMasterType = other.optionalDocumentMasterType().map(DocumentMasterTypeFilter::copy).orElse(null);
        this.requiresVerification = other.optionalRequiresVerification().map(BooleanFilter::copy).orElse(null);
        this.documentId = other.optionalDocumentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentTypeCriteria copy() {
        return new DocumentTypeCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DocumentMasterTypeFilter getDocumentMasterType() {
        return documentMasterType;
    }

    public Optional<DocumentMasterTypeFilter> optionalDocumentMasterType() {
        return Optional.ofNullable(documentMasterType);
    }

    public DocumentMasterTypeFilter documentMasterType() {
        if (documentMasterType == null) {
            setDocumentMasterType(new DocumentMasterTypeFilter());
        }
        return documentMasterType;
    }

    public void setDocumentMasterType(DocumentMasterTypeFilter documentMasterType) {
        this.documentMasterType = documentMasterType;
    }

    public BooleanFilter getRequiresVerification() {
        return requiresVerification;
    }

    public Optional<BooleanFilter> optionalRequiresVerification() {
        return Optional.ofNullable(requiresVerification);
    }

    public BooleanFilter requiresVerification() {
        if (requiresVerification == null) {
            setRequiresVerification(new BooleanFilter());
        }
        return requiresVerification;
    }

    public void setRequiresVerification(BooleanFilter requiresVerification) {
        this.requiresVerification = requiresVerification;
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
        final DocumentTypeCriteria that = (DocumentTypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(description, that.description) &&
            Objects.equals(documentMasterType, that.documentMasterType) &&
            Objects.equals(requiresVerification, that.requiresVerification) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isActive, description, documentMasterType, requiresVerification, documentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTypeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDocumentMasterType().map(f -> "documentMasterType=" + f + ", ").orElse("") +
            optionalRequiresVerification().map(f -> "requiresVerification=" + f + ", ").orElse("") +
            optionalDocumentId().map(f -> "documentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
