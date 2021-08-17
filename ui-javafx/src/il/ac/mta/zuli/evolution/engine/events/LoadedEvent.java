package il.ac.mta.zuli.evolution.engine.events;

public class LoadedEvent extends Event {
    private final String path;

    public LoadedEvent(String message, String path) {
        super(message);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
