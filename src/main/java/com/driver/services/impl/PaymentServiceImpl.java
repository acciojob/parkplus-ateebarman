package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Optional<Reservation> oR = reservationRepository2.findById(reservationId);
        if (!oR.isPresent()) {
            throw new RuntimeException("reservation does not exist");
        }

        Reservation r = oR.get();
        PaymentMode pm;
        if (mode == null) {
            throw new RuntimeException("Payment mode not detected");
        }
        if (mode.equalsIgnoreCase("cash")) {
            pm = PaymentMode.CASH;
        } else if (mode.equalsIgnoreCase("card")) {
            pm = PaymentMode.CARD;
        } else if (mode.equalsIgnoreCase("upi")) {
            pm = PaymentMode.UPI;
        } else {
            throw new RuntimeException("Payment mode not detected");
        }

        int needed = r.getNumberOfHours() * r.getSpot().getPricePerHour();
        if (needed > amountSent) {
            throw new RuntimeException("Insufficient Amount");
        }

        r.getSpot().setOccupied(true);
        Payment p = new Payment();
        p.setPaymentCompleted(true);
        p.setReservation(r);
        p.setPaymentMode(pm);
        r.setPayment(p);

        reservationRepository2.save(r);
        paymentRepository2.save(p);

        return p;
    }
}
