package com.tui.proof.ws.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FlightAvailabilityResponse implements Serializable {
    private List<FlightAvailabilityResult> availabilities;
}
