package com.tui.proof.ws.services;

import com.tui.proof.ws.events.*;
import com.tui.proof.ws.interfaces.TaskOperationsSubscriber;
import com.tui.proof.ws.model.BookingResponse;
import com.tui.proof.ws.model.Task;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.tui.proof.ws.events.EventType.ADD_TASK;
import static com.tui.proof.ws.utils.Constants.*;

@Service
@Log4j2
public class TaskService implements TaskOperationsSubscriber {

    private final Set<Event> eventAudit = new HashSet<>();

    @Override
    public void getNotified(Event event) {
        if (event.getEventType().equals(ADD_TASK)) {
            eventAudit.add(((AddTaskEvent)event).getSourceEvent());
        }
        event.setProcessed(true);
    }

    public Task getTaskStatus(Integer id) {
        Event event = eventAudit.stream().filter(eventElement -> eventElement.getEventId().equals(id)).findFirst().orElse(null);
        if (event != null) {
            Task task = new Task(id);
            task.setCompleted(event.isProcessed());
            if (event.getEventType().equals(EventType.QUERY_BOOKING)) {
                task.setResultsUrl(TASK_PATH+"/"+event.getEventId()+RESULTS_PATH+BOOKINGS_PATH);
            }
            return task;
        }
        return null;
    }

    public BookingResponse getEventResult(Integer id) {
        Event event = eventAudit.stream().filter(eventElement -> eventElement.getEventId().equals(id)).findFirst().orElse(null);
        if (event instanceof BookingQueryEvent) {
            return ((BookingQueryEvent)event).getBookingResponse();
        }
        return null;
    }
}
