package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.DocumentMasterType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The DocumentType entity.
 * @author A true hipster
 */
@Schema(description = "The DocumentType entity.\n@author A true hipster")
@Entity
@Table(name = "document_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * fieldName
     */
    @Schema(description = "fieldName", required = true)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "document_master_type", nullable = false)
    private DocumentMasterType documentMasterType;

    @Column(name = "requires_verification")
    private Boolean requiresVerification;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "documentType")
    @JsonIgnoreProperties(value = { "verifiedBy", "documentType", "person", "vehicle", "agency" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DocumentType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public DocumentType isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return this.description;
    }

    public DocumentType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentMasterType getDocumentMasterType() {
        return this.documentMasterType;
    }

    public DocumentType documentMasterType(DocumentMasterType documentMasterType) {
        this.setDocumentMasterType(documentMasterType);
        return this;
    }

    public void setDocumentMasterType(DocumentMasterType documentMasterType) {
        this.documentMasterType = documentMasterType;
    }

    public Boolean getRequiresVerification() {
        return this.requiresVerification;
    }

    public DocumentType requiresVerification(Boolean requiresVerification) {
        this.setRequiresVerification(requiresVerification);
        return this;
    }

    public void setRequiresVerification(Boolean requiresVerification) {
        this.requiresVerification = requiresVerification;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setDocumentType(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setDocumentType(this));
        }
        this.documents = documents;
    }

    public DocumentType documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public DocumentType addDocument(Document document) {
        this.documents.add(document);
        document.setDocumentType(this);
        return this;
    }

    public DocumentType removeDocument(Document document) {
        this.documents.remove(document);
        document.setDocumentType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentType)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", description='" + getDescription() + "'" +
            ", documentMasterType='" + getDocumentMasterType() + "'" +
            ", requiresVerification='" + getRequiresVerification() + "'" +
            "}";
    }
}
