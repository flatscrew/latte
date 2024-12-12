package org.flatscrew.latte;

public record UpdateResult<M extends Model>(M model, Command command) {

    public static <M extends Model> UpdateResult<M> from(M model, Command cmd) {
        return new UpdateResult<>(model, cmd);
    }

    public static <M extends Model> UpdateResult<M> from(M model) {
        return UpdateResult.from(model, null);
    }
}
