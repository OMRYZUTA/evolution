package il.ac.mta.zuli.evolution.engine.events;

public class ErrorEvent extends Event {
    private final Throwable error;

    public ErrorEvent(String message, Throwable error) {
        super(message);
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }
}
