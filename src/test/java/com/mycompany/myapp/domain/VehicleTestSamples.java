package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vehicle getVehicleSample1() {
        return new Vehicle()
            .id(1L)
            .name("name1")
            .regNo("regNo1")
            .zone("zone1")
            .category("category1")
            .serialNo("serialNo1")
            .vehicleNo("vehicleNo1")
            .chasisNo("chasisNo1");
    }

    public static Vehicle getVehicleSample2() {
        return new Vehicle()
            .id(2L)
            .name("name2")
            .regNo("regNo2")
            .zone("zone2")
            .category("category2")
            .serialNo("serialNo2")
            .vehicleNo("vehicleNo2")
            .chasisNo("chasisNo2");
    }

    public static Vehicle getVehicleRandomSampleGenerator() {
        return new Vehicle()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .regNo(UUID.randomUUID().toString())
            .zone(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .serialNo(UUID.randomUUID().toString())
            .vehicleNo(UUID.randomUUID().toString())
            .chasisNo(UUID.randomUUID().toString());
    }
}
