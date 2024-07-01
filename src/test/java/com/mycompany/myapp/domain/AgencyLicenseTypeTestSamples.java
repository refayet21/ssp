package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgencyLicenseTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AgencyLicenseType getAgencyLicenseTypeSample1() {
        return new AgencyLicenseType().id(1L).name("name1");
    }

    public static AgencyLicenseType getAgencyLicenseTypeSample2() {
        return new AgencyLicenseType().id(2L).name("name2");
    }

    public static AgencyLicenseType getAgencyLicenseTypeRandomSampleGenerator() {
        return new AgencyLicenseType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
