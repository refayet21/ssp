package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgencyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Agency getAgencySample1() {
        return new Agency().id(1L).name("name1").shortName("shortName1");
    }

    public static Agency getAgencySample2() {
        return new Agency().id(2L).name("name2").shortName("shortName2");
    }

    public static Agency getAgencyRandomSampleGenerator() {
        return new Agency().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).shortName(UUID.randomUUID().toString());
    }
}
