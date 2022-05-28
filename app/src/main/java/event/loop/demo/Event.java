package event.loop.demo;

public final class Event {
    public final String key;
    public final Object data;

    public Event(String key, Object data) {
        this.key = key;
        this.data = data;
    }
}