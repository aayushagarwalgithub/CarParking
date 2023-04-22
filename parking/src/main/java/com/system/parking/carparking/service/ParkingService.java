package com.system.parking.carparking.service;

import com.system.parking.carparking.dao.ParkingDataHistoryDao;
import com.system.parking.carparking.dao.ParkingSlotDao;
import com.system.parking.carparking.entity.ParkingDataHistory;
import com.system.parking.carparking.entity.ParkingSlot;
import com.system.parking.carparking.helper.ParkingHelper;
import com.system.parking.carparking.repository.ParkingDataHistoryRepository;
import com.system.parking.carparking.repository.ParkingSlotRepository;
import com.system.parking.carparking.util.ParkingFare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingService {

    @Autowired
    ParkingSlotRepository parkingSlotRepository;
    @Autowired
    ParkingDataHistoryRepository dataHistoryRepository;

    @Autowired
    ParkingHelper helper;

    public ParkingSlotDao parkVehicle(ParkingSlotDao parkingSlotDao) {
        ParkingSlot entityObj = helper.getParkingSlotfromDao(parkingSlotDao);
        if(entityObj.getEntryTime() == null)
            entityObj.setEntryTime(LocalDateTime.now());
        entityObj = parkingSlotRepository.save(entityObj);
        parkingSlotDao.setSlotId(entityObj.getSlotId());
        parkingSlotDao.setEntryTime(entityObj.getEntryTime());
        return parkingSlotDao;

    }

    public ParkingSlotDao unparkVehicle(ParkingSlotDao parkingSlotDao) {
        long diffInHours;
        ParkingSlot entityObj = helper.getParkingSlotfromDao(parkingSlotDao);
        if(entityObj.getExitTime() == null)
            entityObj.setExitTime(LocalDateTime.now());
        entityObj = parkingSlotRepository.save(entityObj);
        if(entityObj != null) {
            diffInHours = java.time.Duration.between(entityObj.getEntryTime(), entityObj.getExitTime()).toHours();
            if(diffInHours >= 9)
                entityObj.setFare((double)(diffInHours * ParkingFare.PerDayFare.getFare()));
            else
                entityObj.setFare((double)(diffInHours * ParkingFare.PerHourFare.getFare()));
            entityObj = parkingSlotRepository.save(entityObj);
            ParkingDataHistory dataHistory = helper.getParkingHistoryFromParkingSlot(entityObj);
            dataHistoryRepository.save(dataHistory);
            parkingSlotDao.setEntryTime(entityObj.getEntryTime());
            parkingSlotDao.setExitTime(entityObj.getExitTime());
            parkingSlotDao.setFare(entityObj.getFare());
            entityObj = helper.unParkVehicle(entityObj);
            parkingSlotRepository.save(entityObj);
        }
        return parkingSlotDao;

    }

    public List<ParkingSlotDao> checkAndGetAvailableParkingSlot() {
        List<ParkingSlot> parkingSlotList = parkingSlotRepository.findAll(/* where(availableParkingSlot()) */);
        return helper.getParkingSlotDaoFromEntity(parkingSlotList);

    }

    public ParkingSlotDao checkSpecificParkingSlot(Long slotId) {
        Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findById(slotId);
        if(parkingSlot.isPresent())
            return helper.getParkingSlotDaofromEntity(parkingSlot.get());
        else return null;

    }

    public boolean checkSpecificParkingSlotIsAvailable(Long slotId) {
        Optional<ParkingSlot> parkingSlot = parkingSlotRepository.findById(slotId);
        if(parkingSlot.isPresent())
            return parkingSlot.get().getCarNumber().equals("");
        else return false;

    }

    public List<ParkingDataHistoryDao> getAllParkingHistory() {
        List<ParkingDataHistory> parkingHistory = dataHistoryRepository.findAll();
        return helper.getParkingDataHistoryDao(parkingHistory);
    }

}