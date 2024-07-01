package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DocumentTestSamples.*;
import static com.mycompany.myapp.domain.DocumentTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DocumentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentType.class);
        DocumentType documentType1 = getDocumentTypeSample1();
        DocumentType documentType2 = new DocumentType();
        assertThat(documentType1).isNotEqualTo(documentType2);

        documentType2.setId(documentType1.getId());
        assertThat(documentType1).isEqualTo(documentType2);

        documentType2 = getDocumentTypeSample2();
        assertThat(documentType1).isNotEqualTo(documentType2);
    }

    @Test
    void documentTest() {
        DocumentType documentType = getDocumentTypeRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        documentType.addDocument(documentBack);
        assertThat(documentType.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getDocumentType()).isEqualTo(documentType);

        documentType.removeDocument(documentBack);
        assertThat(documentType.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getDocumentType()).isNull();

        documentType.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(documentType.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getDocumentType()).isEqualTo(documentType);

        documentType.setDocuments(new HashSet<>());
        assertThat(documentType.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getDocumentType()).isNull();
    }
}
