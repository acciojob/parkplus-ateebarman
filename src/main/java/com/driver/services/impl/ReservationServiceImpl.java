package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Optional<User> oU = userRepository3.findById(userId);
        Optional<ParkingLot> oP = parkingLotRepository3.findById(parkingLotId);
        if(!oU.isPresent() || !oP.isPresent()){
            throw new RuntimeException("either user or parking lot does not exist");
        }
        User u = oU.get();
        ParkingLot p = oP.get();
        SpotType st;
        if(numberOfWheels ==2){
            st = SpotType.TWO_WHEELER;
        }else if(numberOfWheels == 4){
            st=SpotType.FOUR_WHEELER;
        }else{
            st = SpotType.OTHERS;
        }
        int min = Integer.MAX_VALUE;
        Spot spot = null;
        for(Spot s : p.getSpotList()){
            if(s.getSpotType() == st && s.getPricePerHour() < min && s.getOccupied() == false){
                min = s.getPricePerHour();
                spot = s;
            }
        }
        if(spot == null){
            throw new RuntimeException("no available spots");
        }

        Reservation r = new Reservation();
        r.setSpot(spot);
        r.setNumberOfHours(timeInHours);
        r.setUser(u);
        reservationRepository3.save(r);
        u.getReservationList().add(r);
        spot.getReservationList().add(r);
        userRepository3.save(u);
        spotRepository3.save(spot);
        return r;



    }
}
