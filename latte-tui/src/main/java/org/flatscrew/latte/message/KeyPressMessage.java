package org.flatscrew.latte.message;

import org.flatscrew.latte.Message;

public record KeyPressMessage(int key) implements Message {}
