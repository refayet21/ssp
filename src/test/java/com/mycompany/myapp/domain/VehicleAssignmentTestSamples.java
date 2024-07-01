package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VehicleAssignment getVehicleAssignmentSample1() {
        return new VehicleAssignment().id(1L);
    }

    public static VehicleAssignment getVehicleAssignmentSample2() {
        return new VehicleAssignment().id(2L);
    }

    public static VehicleAssignment getVehicleAssignmentRandomSampleGenerator() {
        return new VehicleAssignment().id(longCount.incrementAndGet());
    }
}
