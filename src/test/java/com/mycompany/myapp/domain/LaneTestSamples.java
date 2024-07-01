package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LaneTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Lane getLaneSample1() {
        return new Lane().id(1L).name("name1").shortName("shortName1");
    }

    public static Lane getLaneSample2() {
        return new Lane().id(2L).name("name2").shortName("shortName2");
    }

    public static Lane getLaneRandomSampleGenerator() {
        return new Lane().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).shortName(UUID.randomUUID().toString());
    }
}
