package no.stonehill.preppers.utils;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager<T> {
    public interface Listener<T> {
        void onEvent(T payload);
    }

    public void trigger(T payload) {
        for (Listener<T> listener : listeners) {
            listener.onEvent(payload);
        }
    }

    List<Listener<T>> listeners = new ArrayList<>();

    public void add(Listener<T> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void remove(Listener<T> listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
}
