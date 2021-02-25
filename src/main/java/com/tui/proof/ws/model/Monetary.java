package com.tui.proof.ws.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Monetary implements Serializable {
    @NotNull
    private BigDecimal amount;

    @NotNull
    private String currency;
}
