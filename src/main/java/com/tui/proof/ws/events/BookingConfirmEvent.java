package com.tui.proof.ws.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class BookingConfirmEvent extends Event {
    private final int id;

    public BookingConfirmEvent(int id) {
        super(EventType.CONFIRM_BOOKING);
        this.id = id;
    }
}
