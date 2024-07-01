package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PassTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PassType getPassTypeSample1() {
        return new PassType()
            .id(1L)
            .name("name1")
            .shortName("shortName1")
            .printedName("printedName1")
            .minimumDuration(1L)
            .maximumDuration(1L);
    }

    public static PassType getPassTypeSample2() {
        return new PassType()
            .id(2L)
            .name("name2")
            .shortName("shortName2")
            .printedName("printedName2")
            .minimumDuration(2L)
            .maximumDuration(2L);
    }

    public static PassType getPassTypeRandomSampleGenerator() {
        return new PassType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .shortName(UUID.randomUUID().toString())
            .printedName(UUID.randomUUID().toString())
            .minimumDuration(longCount.incrementAndGet())
            .maximumDuration(longCount.incrementAndGet());
    }
}
