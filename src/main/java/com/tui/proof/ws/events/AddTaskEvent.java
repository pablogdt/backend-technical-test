package com.tui.proof.ws.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class AddTaskEvent extends Event {
    private final int id;
    @Setter
    private Event sourceEvent;

    public AddTaskEvent(int id) {
        super(EventType.ADD_TASK);
        this.id = id;
    }
}
