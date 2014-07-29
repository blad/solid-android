package io.computerscience.android.androidotto.Event.Type;

/**
 * A sample event type used in the sample application. Used to denote that a button click happened.
 */
public class ButtonClickedEvent extends BusEvent<Integer>{

    protected Object source;

    /**
     * @param src The Source of the event. Generally `this`.
     * @param val The value trying to be broadcast.
     */
    public ButtonClickedEvent(Object src, int val){
        super(val);
        source = src;
    }

    /**
     * @return Source event dispatcher.
     */
    public Object getSource() {
        return source;
    }

}
