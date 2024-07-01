package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Assignment getAssignmentSample1() {
        return new Assignment().id(1L);
    }

    public static Assignment getAssignmentSample2() {
        return new Assignment().id(2L);
    }

    public static Assignment getAssignmentRandomSampleGenerator() {
        return new Assignment().id(longCount.incrementAndGet());
    }
}
