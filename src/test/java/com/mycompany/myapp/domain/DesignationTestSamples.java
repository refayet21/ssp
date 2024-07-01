package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DesignationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Designation getDesignationSample1() {
        return new Designation().id(1L).name("name1").shortName("shortName1");
    }

    public static Designation getDesignationSample2() {
        return new Designation().id(2L).name("name2").shortName("shortName2");
    }

    public static Designation getDesignationRandomSampleGenerator() {
        return new Designation().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).shortName(UUID.randomUUID().toString());
    }
}
