package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgencyTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AgencyType getAgencyTypeSample1() {
        return new AgencyType().id(1L).name("name1").shortName("shortName1");
    }

    public static AgencyType getAgencyTypeSample2() {
        return new AgencyType().id(2L).name("name2").shortName("shortName2");
    }

    public static AgencyType getAgencyTypeRandomSampleGenerator() {
        return new AgencyType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).shortName(UUID.randomUUID().toString());
    }
}
