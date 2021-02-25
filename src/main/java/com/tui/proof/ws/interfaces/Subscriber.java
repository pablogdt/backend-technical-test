package com.tui.proof.ws.interfaces;

import com.tui.proof.ws.events.Event;

public interface Subscriber {
    void getNotified(Event event);
}
