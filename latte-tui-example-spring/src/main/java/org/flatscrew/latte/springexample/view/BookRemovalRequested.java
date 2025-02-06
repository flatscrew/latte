package org.flatscrew.latte.springexample.view;

import org.flatscrew.latte.Message;
import org.flatscrew.latte.springexample.model.Book;

public record BookRemovalRequested(Book book) implements Message {
}
