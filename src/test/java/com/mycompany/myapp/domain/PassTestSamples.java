package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PassTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pass getPassSample1() {
        return new Pass().id(1L).passNumber(1L).mediaSerial("mediaSerial1");
    }

    public static Pass getPassSample2() {
        return new Pass().id(2L).passNumber(2L).mediaSerial("mediaSerial2");
    }

    public static Pass getPassRandomSampleGenerator() {
        return new Pass().id(longCount.incrementAndGet()).passNumber(longCount.incrementAndGet()).mediaSerial(UUID.randomUUID().toString());
    }
}
