package com.tui.proof.ws.services;

import com.tui.proof.ws.model.*;
import com.tui.proof.ws.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

@Service
@Log4j2
public class AvailabilityService {

    public FlightAvailabilityResponse getFlightAvailability(FlightAvailabilityRequest flightAvailability) {
        FlightAvailabilityResponse flightAvailabilityResponse = new FlightAvailabilityResponse();
        flightAvailabilityResponse.setAvailabilities(new ArrayList<>());
        FlightAvailabilityResult flightAvailabilityResult = new FlightAvailabilityResult();
        flightAvailabilityResult.setFlightNumber("2932");
        flightAvailabilityResult.setCompany("IB");
        flightAvailabilityResult.setDate(LocalDate.now().plus(3, ChronoUnit.DAYS));
        flightAvailabilityResult.setHour(LocalTime.of(15, 0));
        Monetary monetary = new Monetary();
        monetary.setCurrency("EUR");
        monetary.setAmount(BigDecimal.valueOf(200.00 * flightAvailability.getPaxes().getAdults()));
        flightAvailabilityResult.setPrice(monetary);
        flightAvailabilityResult.setToken(createToken(flightAvailabilityResult));
        flightAvailabilityResponse.getAvailabilities().add(flightAvailabilityResult);
        return flightAvailabilityResponse;
    }

    private String createToken(FlightAvailabilityResult flightAvailabilityResult) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new Date().getTime());
        stringBuilder.append(Constants.SEPARATOR);
        stringBuilder.append(flightAvailabilityResult.getDate());
        stringBuilder.append(Constants.SEPARATOR);
        stringBuilder.append(flightAvailabilityResult.getHour());
        stringBuilder.append(Constants.SEPARATOR);
        stringBuilder.append(flightAvailabilityResult.getCompany());
        stringBuilder.append(Constants.SEPARATOR);
        stringBuilder.append(flightAvailabilityResult.getPrice().getAmount());
        stringBuilder.append(Constants.SEPARATOR);
        stringBuilder.append(flightAvailabilityResult.getPrice().getCurrency());
        stringBuilder.append(Constants.SEPARATOR);
        stringBuilder.append(flightAvailabilityResult.getFlightNumber());
        return stringBuilder.toString();
    }
}
