package com.tui.proof.ws.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AvailabilityToken implements Serializable {
    private FlightAvailabilityResult flightAvailabilityResult;
    private long timestamp;
}
