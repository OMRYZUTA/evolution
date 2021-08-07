package il.ac.mta.zuli.evolution.engine.events;

public class Event {
    private final String message;

    public Event(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
