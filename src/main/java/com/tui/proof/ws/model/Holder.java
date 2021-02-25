package com.tui.proof.ws.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Holder implements Serializable {
    private String name;
    private String lastName;
    private String address;
    private String postalCode;
    private String country;
    private String email;
    private List<String> telephones;
}
