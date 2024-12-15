package org.flatscrew.latte;

public interface Model {

    Command init();
    UpdateResult<? extends Model> update(Message msg);
    String view();
}
