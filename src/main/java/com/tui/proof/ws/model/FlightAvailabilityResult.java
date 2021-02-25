package com.tui.proof.ws.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tui.proof.ws.serialization.LocalDateDeserializer;
import com.tui.proof.ws.serialization.LocalDateSerializer;
import com.tui.proof.ws.serialization.LocalTimeDeserializer;
import com.tui.proof.ws.serialization.LocalTimeSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FlightAvailabilityResult implements Serializable {

    @NotNull
    private String company;

    @NotNull
    private String flightNumber;

    @NotNull
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @NotNull
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime hour;

    @NotNull
    private Monetary price;

    @NotNull
    private String token;
}
