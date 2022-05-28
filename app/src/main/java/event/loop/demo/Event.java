package event.loop.demo;

public final class Event {
    private final String key;
    private final Object data;

    public Event(String key, Object data) {
        this.key = key;
        this.data = data;
    }
}