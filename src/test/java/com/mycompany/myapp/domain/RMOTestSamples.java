package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RMOTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RMO getRMOSample1() {
        return new RMO().id(1L).name("name1").code("code1");
    }

    public static RMO getRMOSample2() {
        return new RMO().id(2L).name("name2").code("code2");
    }

    public static RMO getRMORandomSampleGenerator() {
        return new RMO().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).code(UUID.randomUUID().toString());
    }
}
