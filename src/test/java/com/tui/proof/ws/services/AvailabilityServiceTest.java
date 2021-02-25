package com.tui.proof.ws.services;

import com.tui.proof.ws.events.*;
import com.tui.proof.ws.model.FlightAvailabilityRequest;
import com.tui.proof.ws.model.FlightAvailabilityResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AvailabilityServiceTest {

    @InjectMocks
    public AvailabilityService availabilityService;

    @Test
    public void testGetFlightAvailability() {
        BookingCreationEvent event = new BookingCreationEvent();
        event.setEventId(1);
        FlightAvailabilityResponse response = availabilityService.getFlightAvailability(new FlightAvailabilityRequest());
        assertEquals(1, response.getAvailabilities().size());
        assertEquals(7, response.getAvailabilities().get(0).getToken().split("~").length);
    }

}
