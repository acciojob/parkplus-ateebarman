package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLotRepository1.save(parkingLot);
        return null;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        Optional<ParkingLot> p = parkingLotRepository1.findById(parkingLotId);
        if(!p.isPresent()){
            throw new RuntimeException("parking lot not found");
        }
        Spot spot = new Spot();
        spot.setPricePerHour(pricePerHour);
        if(numberOfWheels == 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }else if(numberOfWheels == 4){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else{
            spot.setSpotType(SpotType.OTHERS);
        }
        spot.setOccupied(false);
        ParkingLot parkingLot = p.get();

        parkingLot.getSpotList().add(spot);
        spot.setParkingLot(parkingLot);
        parkingLotRepository1.save(parkingLot);


        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Optional<Spot> s = spotRepository1.findById(spotId);
        if(!s.isPresent()){
            throw new RuntimeException("spot not found");
        }
        Spot spot = s.get();
        ParkingLot parkingLot = spot.getParkingLot();
        parkingLot.getSpotList().remove(spot);

        spotRepository1.deleteById(spotId);

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

        Optional<Spot> sp = spotRepository1.findById(spotId);
        if(!sp.isPresent()){
            throw new RuntimeException("no such spot there");
        }
        Spot spot = sp.get();

        spot.setPricePerHour(pricePerHour);
        spotRepository1.save(spot);

        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {

        Optional<ParkingLot> op = parkingLotRepository1.findById(parkingLotId);
        if(!op.isPresent()){
            throw new RuntimeException("no such parking exist");
        }

        parkingLotRepository1.deleteById(parkingLotId);

    }
}
