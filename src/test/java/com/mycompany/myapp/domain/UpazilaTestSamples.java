package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UpazilaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Upazila getUpazilaSample1() {
        return new Upazila().id(1L).name("name1");
    }

    public static Upazila getUpazilaSample2() {
        return new Upazila().id(2L).name("name2");
    }

    public static Upazila getUpazilaRandomSampleGenerator() {
        return new Upazila().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
