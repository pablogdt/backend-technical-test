package com.tui.proof.ws.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AddFlightRequest implements Serializable {
    @NotNull
    private String token;
}
