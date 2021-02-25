package com.tui.proof.ws.model;

import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class PaxesDistribution implements Serializable {
    @Min(1)
    private int adults;
    private int children;
    private int infants;
}
