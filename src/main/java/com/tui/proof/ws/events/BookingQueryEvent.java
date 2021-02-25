package com.tui.proof.ws.events;

import com.tui.proof.ws.model.BookingResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class BookingQueryEvent extends Event {
    private final int id;
    @Setter
    private BookingResponse bookingResponse;

    public BookingQueryEvent(int id) {
        super(EventType.QUERY_BOOKING);
        this.id = id;
    }
}
