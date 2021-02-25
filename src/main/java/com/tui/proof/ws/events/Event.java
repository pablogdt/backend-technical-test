package com.tui.proof.ws.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Event {
    private EventType eventType;
    private Integer eventId;
    private boolean processed;
    private boolean success;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }
}
