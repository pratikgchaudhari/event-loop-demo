package event.loop.demo;

public final class Event {
    public final String key;
    public final String data;
    public final boolean asynchronous;

    public Event(String key, String data, boolean asynchronous) {
        this.key = key;
        this.data = data;
        this.asynchronous = asynchronous;
    }
}