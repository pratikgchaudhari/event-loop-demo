package event.loop.demo;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Deque;
import java.util.function.Function;

public class EventLoop {
    private Deque<Event> events;
    private Map<String, Function<String, String>> handlers;
    private Deque<EventResult> processedEvents;

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

    public void stop() {
        Thread.currentThread().interrupt();
    }

    public void run() {

        if (newEventsAreAvailable(events)) {
            Event event = events.poll();

            System.out.println(String.format("Received Event: %s\n", event.key));

            if (handlers.containsKey(event.key)) {
                var startTime = Instant.now();

                if (event.asynchronous) {
                    processAsynchronously(event);
                } else {
                    processSynchronously(event);
                }

                var endTime = Instant.now();

                System.out.println(String.format("Event Loop was blocked for %s ms due to this operation \n",
                        Duration.between(startTime, endTime).toMillis()));
            } else {
                System.out.println(String.format("No handler found for %s", event.key));
            }
        }

        if (resultsOfAsyncEventsAreAvailable(processedEvents)) {
            produceOutputFor(processedEvents.poll());
        }
    }

    private void processAsynchronously(Event event) {
        new Thread(() -> processedEvents.add(new EventResult(event.key, handlers.get(event.key).apply(event.data)))).start();
    }

    private void processSynchronously(Event event) {
        produceOutputFor(new EventResult(event.key, handlers.get(event.key).apply(event.data)));
    }

    private void produceOutputFor(EventResult eventResult) {
        System.out.println(String.format("Output for Event %s : %s\n", eventResult.key, eventResult.result));
    }

    private boolean newEventsAreAvailable(Deque<Event> events) {
        return !events.isEmpty();
    }

    private boolean resultsOfAsyncEventsAreAvailable(Deque<EventResult> events) {
        return !processedEvents.isEmpty();
    }
}
