package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EntryLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EntryLog getEntryLogSample1() {
        return new EntryLog().id(1L);
    }

    public static EntryLog getEntryLogSample2() {
        return new EntryLog().id(2L);
    }

    public static EntryLog getEntryLogRandomSampleGenerator() {
        return new EntryLog().id(longCount.incrementAndGet());
    }
}
