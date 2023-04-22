package com.system.parking.carparking.repository;


import com.system.parking.carparking.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long>, JpaSpecificationExecutor<ParkingSlot> {




}
