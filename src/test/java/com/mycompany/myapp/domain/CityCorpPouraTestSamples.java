package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CityCorpPouraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CityCorpPoura getCityCorpPouraSample1() {
        return new CityCorpPoura().id(1L).name("name1");
    }

    public static CityCorpPoura getCityCorpPouraSample2() {
        return new CityCorpPoura().id(2L).name("name2");
    }

    public static CityCorpPoura getCityCorpPouraRandomSampleGenerator() {
        return new CityCorpPoura().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
