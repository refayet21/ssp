package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgencyLicenseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AgencyLicense getAgencyLicenseSample1() {
        return new AgencyLicense().id(1L).filePath("filePath1").serialNo("serialNo1");
    }

    public static AgencyLicense getAgencyLicenseSample2() {
        return new AgencyLicense().id(2L).filePath("filePath2").serialNo("serialNo2");
    }

    public static AgencyLicense getAgencyLicenseRandomSampleGenerator() {
        return new AgencyLicense()
            .id(longCount.incrementAndGet())
            .filePath(UUID.randomUUID().toString())
            .serialNo(UUID.randomUUID().toString());
    }
}
