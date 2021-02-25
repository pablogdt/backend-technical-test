package com.tui.proof.ws.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Task implements Serializable {
    private boolean isCompleted = false;
    private Integer id;
    private String resultsUrl;

    public Task(Integer id) {
        this.id = id;
    }
}
