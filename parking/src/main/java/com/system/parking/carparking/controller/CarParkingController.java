package com.system.parking.carparking.controller;

import com.system.parking.carparking.dao.ParkingDataHistoryDao;
import com.system.parking.carparking.dao.ParkingSlotDao;
import com.system.parking.carparking.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carparking/slot/")
public class CarParkingController {

    @Autowired
    ParkingService parkingService;

    @GetMapping
    @ResponseBody
    List<ParkingSlotDao> getAvailableSlot() {
        return parkingService.checkAndGetAvailableParkingSlot();
    }

    @GetMapping("info/{slotId}")
    @ResponseBody
    ParkingSlotDao specificParkingSlotInfo(@PathVariable Long slotId) {
        return parkingService.checkSpecificParkingSlot(slotId);
    }

    @GetMapping("check/{slotId}")
    String specificParkingSlotAvailablityCheck(@PathVariable Long slotId) {
        boolean isAvailable = parkingService.checkSpecificParkingSlotIsAvailable(slotId);
        if(isAvailable)
            return ("The Parking slot "+slotId+" is Available") ;
        else return( "The Parking slot "+slotId+" is not Available");
    }

    @PostMapping("parking")
    @ResponseBody ParkingSlotDao parkVehicle(@RequestBody ParkingSlotDao parkingSlotDao) {
        return parkingService.parkVehicle(parkingSlotDao);

    }

    @PutMapping("unparking")
    @ResponseBody ParkingSlotDao unParkVehicle(@RequestBody ParkingSlotDao parkingSlotDao) {
        return parkingService.unparkVehicle(parkingSlotDao);

    }

    @GetMapping("parking/history")
    @ResponseBody List<ParkingDataHistoryDao> getParkingSlotHistory() {
        return parkingService.getAllParkingHistory();
    }


}
