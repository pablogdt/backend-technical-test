package com.tui.proof.ws.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class BookingDeleteEvent extends Event {
    private final int id;

    public BookingDeleteEvent(int id) {
        super(EventType.DELETE_BOOKING);
        this.id = id;
    }
}
