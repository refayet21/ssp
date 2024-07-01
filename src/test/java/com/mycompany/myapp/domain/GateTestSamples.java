package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Gate getGateSample1() {
        return new Gate().id(1L).name("name1").shortName("shortName1");
    }

    public static Gate getGateSample2() {
        return new Gate().id(2L).name("name2").shortName("shortName2");
    }

    public static Gate getGateRandomSampleGenerator() {
        return new Gate().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).shortName(UUID.randomUUID().toString());
    }
}