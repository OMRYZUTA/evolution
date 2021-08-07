package il.ac.mta.zuli.evolution.engine.events;

public class ErrorEvent extends Event {
    private final Exception error;

    public ErrorEvent(String message, Exception error) {
        super(message);
        this.error = error;
    }

    public Exception getError() {
        return error;
    }
}
