package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AccessProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AccessProfile getAccessProfileSample1() {
        return new AccessProfile().id(1L).name("name1").description("description1").startTimeOfDay(1).endTimeOfDay(1).dayOfWeek(1);
    }

    public static AccessProfile getAccessProfileSample2() {
        return new AccessProfile().id(2L).name("name2").description("description2").startTimeOfDay(2).endTimeOfDay(2).dayOfWeek(2);
    }

    public static AccessProfile getAccessProfileRandomSampleGenerator() {
        return new AccessProfile()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .startTimeOfDay(intCount.incrementAndGet())
            .endTimeOfDay(intCount.incrementAndGet())
            .dayOfWeek(intCount.incrementAndGet());
    }
}
