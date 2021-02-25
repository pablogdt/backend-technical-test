package com.tui.proof.ws.services;

import com.tui.proof.ws.events.*;
import com.tui.proof.ws.interfaces.BookingOperationsSubscriber;
import com.tui.proof.ws.model.*;
import com.tui.proof.ws.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.tui.proof.ws.serialization.LocalDateDeserializer.DATE_FORMATTER;
import static com.tui.proof.ws.serialization.LocalTimeDeserializer.TIME_FORMATTER;

@Service
@Log4j2
public class BookingService implements BookingOperationsSubscriber {

    private Map<Integer, BookingResponse> bookings = new ConcurrentHashMap<>();

    private BookingResponse populateBooking() {
        BookingResponse booking = new BookingResponse();
        Holder holder = new Holder();
        holder.setName("Pablo");
        holder.setLastName("Gomez");
        holder.setAddress("Calle Mayor, 3");
        holder.setEmail("test@gmail.com");
        holder.setPostalCode("33011");
        holder.setCountry("Spain");
        List<String> telephones = new ArrayList<>();
        telephones.add("654121524");
        holder.setTelephones(telephones);
        booking.setHolder(holder);
        booking.setFlights(new ArrayList<>());
        return booking;
    }

    private void createBooking(BookingCreationEvent event) {
        BookingResponse bookingResponse = bookings.get(event.getEventId());
        if (bookingResponse == null) {
            bookings.put(event.getEventId(), populateBooking());
            event.setSuccess(true);
            log.debug("Booking created with id: " + event.getEventId());
        } else {
            event.setSuccess(false);
            log.debug("Booking exists with same id: " + event.getEventId());
        }
    }

    private void addFlightToBooking(FlightAddToBookingEvent event) {
        BookingResponse bookingResponse = bookings.get(event.getId());
        if (bookingResponse != null) {
            AvailabilityToken availabilityToken = decryptAvailabilityToken(event.getFlightToken());
            if (availabilityToken != null) {
                availabilityToken.getFlightAvailabilityResult().setToken(event.getFlightToken());
                event.setSuccess(bookingResponse.getFlights().add(availabilityToken.getFlightAvailabilityResult()));
            }
        } else {
            log.debug("Cannot add flight '{}' to booking {} because it does not exist", event.getFlightToken(), event.getId());
        }
    }

    private void deleteFlightFromBooking(FlightDeleteFromBookingEvent event) {
        BookingResponse bookingResponse = bookings.get(event.getId());
        if (bookingResponse != null) {
            Optional<FlightAvailabilityResult> flightAvailabilityResultOptional = bookingResponse.getFlights().stream().filter(flight -> event.getFlightToken().equals(flight.getToken())).findFirst();
            if (flightAvailabilityResultOptional.isPresent()) {
                event.setSuccess(bookingResponse.getFlights().remove(flightAvailabilityResultOptional.get()));
            } else {
                event.setSuccess(false);
                log.debug("Flight {} does not exist within booking {}", event.getFlightToken(), event.getId());
            }
        } else {
            event.setSuccess(false);
            log.debug("Booking {} does not exist", event.getId());
        }
    }

    private void deleteBooking(BookingDeleteEvent event) {
        event.setSuccess(bookings.remove(event.getId()) != null);
    }

    private void queryBooking(BookingQueryEvent event) {
        BookingResponse bookingResponse = bookings.get(event.getId());
        event.setSuccess(bookingResponse != null);
        event.setBookingResponse(bookingResponse);
    }

    private boolean validateBooking(BookingResponse bookingResponse) {
        Holder holder = bookingResponse.getHolder();
        return holder != null && isValidField(holder.getName()) && isValidField(holder.getLastName()) && isValidField(holder.getEmail())
                && isValidField(holder.getAddress()) && isValidField(holder.getCountry()) && isValidField(holder.getPostalCode())
                && holder.getTelephones() != null && !holder.getTelephones().isEmpty() && holder.getTelephones().stream().noneMatch(String::isEmpty)
                && bookingResponse.getFlights() != null && !bookingResponse.getFlights().isEmpty() && validFlights(bookingResponse.getFlights());
    }

    private void confirmBooking(BookingConfirmEvent event) {
        BookingResponse bookingResponse = bookings.get(event.getId());
        if (bookingResponse != null) {
            event.setSuccess(validateBooking(bookingResponse));
        } else {
            event.setSuccess(false);
        }
    }

    private boolean validFlights(List<FlightAvailabilityResult> flights) {
        if (flights == null || flights.isEmpty()) {
            return false;
        }
        for (FlightAvailabilityResult flight : flights) {
            AvailabilityToken availabilityToken = decryptAvailabilityToken(flight.getToken());
            if (availabilityToken != null) {
                long millisElapsed = new Date().getTime() - availabilityToken.getTimestamp();
                if ((double)millisElapsed / (60 * 1000) >= 15.0d) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean isValidField(String field) {
        return field != null && !field.isEmpty();
    }

    @Override
    public void getNotified(Event event) {
        switch (event.getEventType()) {
            case CREATE_BOOKING:
                BookingCreationEvent bookingCreationEvent = (BookingCreationEvent) event;
                createBooking(bookingCreationEvent);
                break;
            case ADD_FLIGHT:
                FlightAddToBookingEvent flightAddToBookingEvent = (FlightAddToBookingEvent) event;
                addFlightToBooking(flightAddToBookingEvent);
                break;
            case DELETE_FLIGHT:
                FlightDeleteFromBookingEvent flightDeleteFromBookingEvent = (FlightDeleteFromBookingEvent) event;
                deleteFlightFromBooking(flightDeleteFromBookingEvent);
                break;
            case CONFIRM_BOOKING:
                BookingConfirmEvent bookingConfirmEvent = (BookingConfirmEvent) event;
                confirmBooking(bookingConfirmEvent);
                break;
            case DELETE_BOOKING:
                BookingDeleteEvent bookingDeleteEvent = (BookingDeleteEvent) event;
                deleteBooking(bookingDeleteEvent);
                break;
            case QUERY_BOOKING:
                BookingQueryEvent bookingQueryEvent = (BookingQueryEvent) event;
                queryBooking(bookingQueryEvent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + event.getEventType());
        }
        event.setProcessed(true);
    }

    private AvailabilityToken decryptAvailabilityToken(String flightToken) {
        AvailabilityToken availabilityToken = new AvailabilityToken();
        if (flightToken == null) {
            return null;
        }
        String[] parts = flightToken.split(Constants.SEPARATOR);
        if (parts.length != 7) {
            return null;
        }
        availabilityToken.setTimestamp(Long.parseLong(parts[0]));
        FlightAvailabilityResult flightAvailability = new FlightAvailabilityResult();
        flightAvailability.setDate(LocalDate.parse(parts[1], DATE_FORMATTER));
        flightAvailability.setHour(LocalTime.parse(parts[2], TIME_FORMATTER));
        flightAvailability.setCompany(parts[3]);
        Monetary monetary = new Monetary();
        monetary.setAmount(new BigDecimal(parts[4]));
        monetary.setCurrency(parts[5]);
        flightAvailability.setPrice(monetary);
        flightAvailability.setFlightNumber(parts[6]);
        availabilityToken.setFlightAvailabilityResult(flightAvailability);
        return availabilityToken;
    }
}
