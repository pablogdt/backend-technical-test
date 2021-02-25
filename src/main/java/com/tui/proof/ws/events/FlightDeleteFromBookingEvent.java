package com.tui.proof.ws.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FlightDeleteFromBookingEvent extends Event {
    private final int id;
    private final String flightToken;

    public FlightDeleteFromBookingEvent(int id, String flightToken) {
        super(EventType.DELETE_FLIGHT);
        this.id = id;
        this.flightToken = flightToken;
    }
}
