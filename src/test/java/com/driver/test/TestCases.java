package com.driver.test;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.impl.PaymentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TestCases {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    public void pay_shouldCreatePaymentWhenAmountIsSufficientAndModeIsValid() throws Exception {
        Spot spot = new Spot();
        spot.setPricePerHour(100);
        spot.setOccupied(false);

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setNumberOfHours(2);
        reservation.setSpot(spot);

        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.pay(1, 250, "cAsH");

        assertNotNull(payment);
        assertTrue(payment.isPaymentCompleted());
        assertEquals(PaymentMode.CASH, payment.getPaymentMode());
        assertEquals(reservation, payment.getReservation());
        assertTrue(reservation.getSpot().getOccupied());
        verify(reservationRepository).save(reservation);
        verify(paymentRepository).save(payment);
    }

    @Test
    public void pay_shouldThrowExceptionWhenAmountIsInsufficient() throws Exception {
        Spot spot = new Spot();
        spot.setPricePerHour(100);
        spot.setOccupied(false);

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setNumberOfHours(2);
        reservation.setSpot(spot);

        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        try {
            paymentService.pay(1, 150, "card");
            fail("Expected insufficient amount exception");
        } catch (RuntimeException ex) {
            assertEquals("Insufficient Amount", ex.getMessage());
        }
    }

    @Test
    public void pay_shouldThrowExceptionWhenPaymentModeIsInvalid() throws Exception {
        Spot spot = new Spot();
        spot.setPricePerHour(100);
        spot.setOccupied(false);

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setNumberOfHours(2);
        reservation.setSpot(spot);

        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        try {
            paymentService.pay(1, 250, "bitcoin");
            fail("Expected invalid mode exception");
        } catch (RuntimeException ex) {
            assertEquals("Payment mode not detected", ex.getMessage());
        }
    }
}

