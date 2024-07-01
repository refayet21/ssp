package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UnionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Union getUnionSample1() {
        return new Union().id(1L).name("name1");
    }

    public static Union getUnionSample2() {
        return new Union().id(2L).name("name2");
    }

    public static Union getUnionRandomSampleGenerator() {
        return new Union().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
