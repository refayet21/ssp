package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DivisionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Division getDivisionSample1() {
        return new Division().id(1L).name("name1");
    }

    public static Division getDivisionSample2() {
        return new Division().id(2L).name("name2");
    }

    public static Division getDivisionRandomSampleGenerator() {
        return new Division().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
