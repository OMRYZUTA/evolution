package il.ac.mta.zuli.evolution.engine.events;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventsEmitter {
    private final Map<String, Set<EventListener>> listeners;

    public EventsEmitter() {
        listeners = new HashMap<>();
    }

    public void addListener(@NotNull String name, @NotNull EventListener listener) {
        if (!listeners.containsKey(name)) {
            listeners.put(name, new HashSet<>());
        }

        listeners.get(name).add(listener);
    }

    public void removeListener(@NotNull String name, @NotNull EventListener listener) {
        if (!listeners.containsKey(name)) {
            return;
        }

        if (listeners.get(name).remove(listener) && listeners.get(name).size() == 0) {
            listeners.remove(name);
        }
    }

    public void fireEvent(@NotNull String name, @NotNull Event event) {
        if (!listeners.containsKey(name)) {
            return;
        }

        for (EventListener listener : listeners.get(name)) {
            listener.actionPerformed(event);
        }
    }
}
