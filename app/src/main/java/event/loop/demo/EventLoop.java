package event.loop.demo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

public class EventLoop {
    private ConcurrentLinkedDeque<Event> events = new ConcurrentLinkedDeque<>();
    private ConcurrentHashMap<String, Consumer<Object>> handlers = new ConcurrentHashMap<>();

    public EventLoop on(String key, Consumer<Object> handler) {
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

        do {
            if (!events.isEmpty()) {
                Event event = events.pop();

                System.out.println(String.format("Received Event: %s", event.key));

                if (handlers.containsKey(event.key)) {
                    handlers.get(event.key).accept(event.data);
                } else {
                    System.out.println(String.format("No handler found for %s", event.key));
                }
            }

        } while (true);

    }
}
