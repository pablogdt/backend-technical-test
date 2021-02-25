package com.tui.proof.ws.services;

import com.tui.proof.ws.events.*;
import com.tui.proof.ws.model.BookingResponse;
import com.tui.proof.ws.model.FlightAvailabilityResult;
import com.tui.proof.ws.model.Holder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    @InjectMocks
    public BookingService bookingService;

    @Test
    public void testCreateBooking() {
        BookingCreationEvent event = new BookingCreationEvent();
        event.setEventId(1);
        bookingService.getNotified(event);
        Map<Integer, BookingResponse> bookings = (Map<Integer, BookingResponse>) ReflectionTestUtils.getField(bookingService, "bookings");
        assertEquals(1, bookings.size());
        BookingResponse bookingResponse = bookings.get(1);
        assertTrue(bookingResponse.getFlights().isEmpty());
        Holder expectedHolder = new Holder();
        expectedHolder.setName("Pablo");
        expectedHolder.setLastName("Gomez");
        expectedHolder.setAddress("Calle Mayor, 3");
        expectedHolder.setEmail("test@gmail.com");
        expectedHolder.setPostalCode("33011");
        expectedHolder.setCountry("Spain");
        List<String> telephones = new ArrayList<>();
        telephones.add("654121524");
        expectedHolder.setTelephones(telephones);
        assertEquals(expectedHolder, bookingResponse.getHolder());
    }

    @Test
    public void testAddFlightToBooking() {
        FlightAddToBookingEvent event = new FlightAddToBookingEvent(0, "1614283242780~2021-02-28~15:00~IB~200.0~EUR~2932");
        event.setEventId(1);
        Map<Integer, BookingResponse> bookings = new HashMap<>();
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setFlights(new ArrayList<>());
        bookings.put(0, bookingResponse);
        ReflectionTestUtils.setField(bookingService, "bookings", bookings);
        bookingService.getNotified(event);
        assertEquals(1, bookingResponse.getFlights().size());
    }

    @Test
    public void testDeleteFlightFromBooking() {
        FlightDeleteFromBookingEvent event = new FlightDeleteFromBookingEvent(0, "1614283242780~2021-02-28~15:00~IB~200.0~EUR~2932");
        event.setEventId(1);
        Map<Integer, BookingResponse> bookings = new HashMap<>();
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setFlights(new ArrayList<>());
        FlightAvailabilityResult flight = new FlightAvailabilityResult();
        flight.setToken("1614283242780~2021-02-28~15:00~IB~200.0~EUR~2932");
        bookingResponse.getFlights().add(flight);
        bookings.put(0, bookingResponse);
        ReflectionTestUtils.setField(bookingService, "bookings", bookings);
        bookingService.getNotified(event);
        assertEquals(0, bookingResponse.getFlights().size());
    }

    @Test
    public void testConfirmBooking() {
        BookingConfirmEvent event = new BookingConfirmEvent(0);
        event.setEventId(1);
        Map<Integer, BookingResponse> bookings = new HashMap<>();
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setFlights(new ArrayList<>());
        FlightAvailabilityResult flight = new FlightAvailabilityResult();
        flight.setToken(new Date().getTime()+"~2021-02-28~15:00~IB~200.0~EUR~2932");
        bookingResponse.getFlights().add(flight);
        Holder expectedHolder = new Holder();
        expectedHolder.setName("Pablo");
        expectedHolder.setLastName("Gomez");
        expectedHolder.setAddress("Calle Mayor, 3");
        expectedHolder.setEmail("test@gmail.com");
        expectedHolder.setPostalCode("33011");
        expectedHolder.setCountry("Spain");
        List<String> telephones = new ArrayList<>();
        telephones.add("654121524");
        expectedHolder.setTelephones(telephones);
        bookingResponse.setHolder(expectedHolder);
        bookings.put(0, bookingResponse);
        ReflectionTestUtils.setField(bookingService, "bookings", bookings);
        bookingService.getNotified(event);
        assertTrue(event.isSuccess());
        assertEquals(1, bookingResponse.getFlights().size());
    }

    @Test
    public void testDeleteBooking() {
        BookingDeleteEvent event = new BookingDeleteEvent(0);
        event.setEventId(1);
        Map<Integer, BookingResponse> bookings = new HashMap<>();
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setFlights(new ArrayList<>());
        FlightAvailabilityResult flight = new FlightAvailabilityResult();
        flight.setToken(new Date().getTime()+"~2021-02-28~15:00~IB~200.0~EUR~2932");
        bookingResponse.getFlights().add(flight);
        bookings.put(0, bookingResponse);
        ReflectionTestUtils.setField(bookingService, "bookings", bookings);
        bookingService.getNotified(event);
        assertTrue(event.isSuccess());
        assertEquals(0, bookings.size());
    }

    @Test
    public void testQueryBooking() {
        BookingQueryEvent event = new BookingQueryEvent(0);
        event.setEventId(1);
        Map<Integer, BookingResponse> bookings = new HashMap<>();
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setFlights(new ArrayList<>());
        FlightAvailabilityResult flight = new FlightAvailabilityResult();
        flight.setToken(new Date().getTime()+"~2021-02-28~15:00~IB~200.0~EUR~2932");
        bookingResponse.getFlights().add(flight);
        bookings.put(0, bookingResponse);
        ReflectionTestUtils.setField(bookingService, "bookings", bookings);
        bookingService.getNotified(event);
        assertTrue(event.isSuccess());
        assertEquals(bookingResponse, event.getBookingResponse());
    }

}
