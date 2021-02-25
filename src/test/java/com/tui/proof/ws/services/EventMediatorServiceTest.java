package com.tui.proof.ws.services;

import com.tui.proof.ws.events.AddTaskEvent;
import com.tui.proof.ws.events.Event;
import com.tui.proof.ws.events.EventType;
import com.tui.proof.ws.interfaces.Subscriber;
import com.tui.proof.ws.interfaces.TaskOperationsSubscriber;
import com.tui.proof.ws.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class EventMediatorServiceTest {

    @InjectMocks
    public EventMediatorService eventMediatorService;

    @Mock
    private TaskService taskService;

    @Test
    public void testPublishEvent() {
        List<TaskOperationsSubscriber> subscribers = new ArrayList<>();
        subscribers.add(taskService);
        ReflectionTestUtils.setField(eventMediatorService, "taskSubscribers", subscribers);
        Task task = eventMediatorService.publishEvent(new AddTaskEvent(2));
        assertEquals(0, task.getId().intValue());
        Task secondTask = eventMediatorService.publishEvent(new AddTaskEvent(5));
        assertEquals(1, secondTask.getId().intValue());
        Queue<Event> eventQueue = (Queue<Event>) ReflectionTestUtils.getField(eventMediatorService, "eventQueue");
        assertEquals(4, eventQueue.size());
    }

    @Test
    public void testCheckEventQueue() {
        Queue<Event> eventQueue = new ArrayBlockingQueue<>(4);
        eventQueue.add(new AddTaskEvent(3));
        ReflectionTestUtils.setField(eventMediatorService, "eventQueue", eventQueue);
        Map<EventType, Set<Subscriber>> eventSubscribers = new HashMap<>();
        Set<Subscriber> subscribers = new HashSet<>();
        subscribers.add(taskService);
        eventSubscribers.put(EventType.ADD_TASK, subscribers);
        ReflectionTestUtils.setField(eventMediatorService, "eventSubscribers", eventSubscribers);
        eventMediatorService.checkEventQueue();
        assertEquals(0, eventQueue.size());
    }

}
