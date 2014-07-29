package io.computerscience.android.androidotto.Event.Type;

/**
 * Base class for all events. All events must have a value associated with them.
 *
 * Base class is used to ensure we can write method that accepts any type of event.
 *
 * @param <T> Type for the value of the event. Can be any type.
 */
public abstract class BusEvent<T> {
    protected T value;

    public BusEvent(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
