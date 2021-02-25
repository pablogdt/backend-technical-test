package com.tui.proof.ws.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class BookingResponse implements Serializable {
    @NotNull
    private Holder holder;
    @NotNull
    private List<FlightAvailabilityResult> flights;
}
