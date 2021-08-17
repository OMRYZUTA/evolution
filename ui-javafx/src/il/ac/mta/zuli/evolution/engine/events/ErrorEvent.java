package il.ac.mta.zuli.evolution.engine.events;

public class ErrorEvent extends Event {
    private final ErrorType type;
    private Throwable error = null;

    public ErrorEvent(String message, ErrorType type, Throwable error) {
        super(message);
        this.type = type;
        this.error = error;
    }

    public ErrorType getType() {
        return type;
    }

    public Throwable getError() {
        return error;
    }
}
