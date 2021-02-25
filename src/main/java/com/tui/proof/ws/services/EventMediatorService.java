package com.tui.proof.ws.services;

import com.tui.proof.ws.events.*;
import com.tui.proof.ws.interfaces.BookingOperationsSubscriber;
import com.tui.proof.ws.interfaces.Subscriber;
import com.tui.proof.ws.interfaces.TaskOperationsSubscriber;
import com.tui.proof.ws.model.Task;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class EventMediatorService {
    private static final AtomicInteger counter = new AtomicInteger(0);

    private final Map<EventType, Set<Subscriber>> eventSubscribers = new ConcurrentHashMap<>();
    private final Queue<Event> eventQueue = new LinkedBlockingQueue<>();

    @Autowired
    private List<BookingOperationsSubscriber> bookingSubscribers;

    @Autowired
    private List<TaskOperationsSubscriber> taskSubscribers;

    @PostConstruct
    public void init() {
        bookingSubscribers.forEach(subscriber -> register(subscriber, EventType.CREATE_BOOKING));
        bookingSubscribers.forEach(subscriber -> register(subscriber, EventType.ADD_FLIGHT));
        bookingSubscribers.forEach(subscriber -> register(subscriber, EventType.DELETE_FLIGHT));
        bookingSubscribers.forEach(subscriber -> register(subscriber, EventType.CONFIRM_BOOKING));
        bookingSubscribers.forEach(subscriber -> register(subscriber, EventType.DELETE_BOOKING));
        bookingSubscribers.forEach(subscriber -> register(subscriber, EventType.QUERY_BOOKING));
        taskSubscribers.forEach(subscriber -> register(subscriber, EventType.ADD_TASK));
    }

    private void register(Subscriber subscriber, EventType eventType) {
        Set<Subscriber> subscriberSet = eventSubscribers.getOrDefault(eventType, new HashSet<>());
        subscriberSet.add(subscriber);
        eventSubscribers.putIfAbsent(eventType, subscriberSet);
        log.debug("Subscriber registered to " + eventType + " channel");
    }

    public Task publishEvent(Event event) {
        eventQueue.add(event);
        Integer eventId = counter.getAndIncrement();
        event.setEventId(eventId);
        log.debug("Event published: " + event);
        AddTaskEvent addTaskEvent = new AddTaskEvent(eventId);
        addTaskEvent.setSourceEvent(event);
        eventQueue.add(addTaskEvent);
        log.debug("Event add task published: " + addTaskEvent);
        return new Task(eventId);
    }

    @Scheduled(fixedRate = 500)
    public void checkEventQueue() {
        Event eventPeeked = eventQueue.poll();
        while (eventPeeked != null) {
            Set<Subscriber> subscriberSet = eventSubscribers.get(eventPeeked.getEventType());
            for (Subscriber subscriber : subscriberSet) {
                subscriber.getNotified(eventPeeked);
            }
            eventPeeked = eventQueue.poll();
        }
    }
}
