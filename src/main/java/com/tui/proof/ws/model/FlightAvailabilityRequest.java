package com.tui.proof.ws.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tui.proof.ws.serialization.LocalDateDeserializer;
import com.tui.proof.ws.serialization.LocalDateSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class FlightAvailabilityRequest implements Serializable {

    @NotNull
    private String originAirport;

    @NotNull
    private String destinationAirport;

    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateFrom;

    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateTo;

    @NotNull
    private PaxesDistribution paxes;
}
