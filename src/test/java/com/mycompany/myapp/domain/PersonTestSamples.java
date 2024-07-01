package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Person getPersonSample1() {
        return new Person()
            .id(1L)
            .name("name1")
            .shortName("shortName1")
            .email("email1")
            .fatherName("fatherName1")
            .motherName("motherName1");
    }

    public static Person getPersonSample2() {
        return new Person()
            .id(2L)
            .name("name2")
            .shortName("shortName2")
            .email("email2")
            .fatherName("fatherName2")
            .motherName("motherName2");
    }

    public static Person getPersonRandomSampleGenerator() {
        return new Person()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .shortName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .fatherName(UUID.randomUUID().toString())
            .motherName(UUID.randomUUID().toString());
    }
}
