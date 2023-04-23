package event.loop.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Deque;
import java.util.function.Function;

public class EventLoop {
    private final Deque<Event> events;
    private final Map<String, Function<String, String>> handlers;
    private final Deque<EventResult> processedEvents;

    EventLoop() {
        events = new ArrayDeque<>();
        handlers = new HashMap<>();
        processedEvents = new ArrayDeque<>();
    }

    public EventLoop on(String key, Function<String, String> handler) {
        handlers.put(key, handler);
        return this;
    }

    public void dispatch(Event event) {
        events.add(event);
    }

    public void run() {

        Event event = events.poll();

        if (event != null) {
            System.out.printf("%nReceived Event: %s%n", event.key);

            if (handlers.containsKey(event.key)) {
                var startTime = Instant.now();

                if (event.asynchronous) {
                    processAsynchronously(event);
                } else {
                    processSynchronously(event);
                }

                var endTime = Instant.now();

                System.out.printf("%nEvent Loop was blocked for %s ms due to this operation %n%n",
                        Duration.between(startTime, endTime).toMillis());
            } else {
                System.out.printf("No handler found for %s%n%n", event.key);
            }
        }

        var processedEvent = processedEvents.poll();

        if (processedEvent != null) {
            produceOutputFor(processedEvent);
        }
    }

    private void processAsynchronously(Event event) {
        new Thread(() -> processedEvents.add(new EventResult(event.key, handlers.get(event.key).apply(event.data)))).start();
    }

    private void processSynchronously(Event event) {
        produceOutputFor(new EventResult(event.key, handlers.get(event.key).apply(event.data)));
    }

    private void produceOutputFor(EventResult eventResult) {
        System.out.printf("%nOutput for Event %s : %s%n", eventResult.key, eventResult.result);
    }
}
