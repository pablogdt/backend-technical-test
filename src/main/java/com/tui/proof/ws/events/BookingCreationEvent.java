package com.tui.proof.ws.events;

import lombok.Getter;

@Getter
public class BookingCreationEvent extends Event {

    public BookingCreationEvent() {
        super(EventType.CREATE_BOOKING);
    }
}
