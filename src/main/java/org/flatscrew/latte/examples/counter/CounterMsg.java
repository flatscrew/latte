package org.flatscrew.latte.examples.counter;

import org.flatscrew.latte.Message;

public enum CounterMsg implements Message {
    INCREMENT, DECREMENT, INCREMENT_LATER
}