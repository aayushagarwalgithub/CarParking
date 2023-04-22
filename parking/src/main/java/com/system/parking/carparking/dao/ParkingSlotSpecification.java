package com.system.parking.carparking.dao;

import com.system.parking.carparking.entity.ParkingSlot;
import org.springframework.data.jpa.domain.Specification;

public class ParkingSlotSpecification
{
    public static Specification<ParkingSlot> availableParkingSlot() {
        return (parkingslot, cq, cb) -> cb.equal(parkingslot.get("carNumber"), "");
    }
}
