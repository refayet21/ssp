package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentType getDocumentTypeSample1() {
        return new DocumentType().id(1L).name("name1").description("description1");
    }

    public static DocumentType getDocumentTypeSample2() {
        return new DocumentType().id(2L).name("name2").description("description2");
    }

    public static DocumentType getDocumentTypeRandomSampleGenerator() {
        return new DocumentType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
