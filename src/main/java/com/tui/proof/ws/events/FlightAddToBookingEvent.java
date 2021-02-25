package com.tui.proof.ws.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FlightAddToBookingEvent extends Event {
    private final int id;
    private final String flightToken;

    public FlightAddToBookingEvent(int id, String flightToken) {
        super(EventType.ADD_FLIGHT);
        this.id = id;
        this.flightToken = flightToken;
    }
}
