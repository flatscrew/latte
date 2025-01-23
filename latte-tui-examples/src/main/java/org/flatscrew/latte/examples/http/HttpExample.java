package org.flatscrew.latte.examples.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;
import org.flatscrew.latte.Model;
import org.flatscrew.latte.Program;
import org.flatscrew.latte.UpdateResult;
import org.flatscrew.latte.message.ErrorMessage;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.QuitMessage;

import java.io.IOException;

record StatusMessage(int statusCode, String message) implements Message {

}


public class HttpExample implements Model {

    private final static String URL = "http://flatscrew.org";

    private StatusMessage statusMessage;
    private Throwable error;

    @Override
    public Command init() {
        return checkServer();
    }

    private Command checkServer() {
        return () -> {
            OkHttpClient client = new OkHttpClient.Builder()
                    .hostnameVerifier((hostname, session) -> true)
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return new StatusMessage(response.code(), response.message());
            } catch (IOException e) {
                return new ErrorMessage(e);
            }
        };
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (keyPressMessage.key().equals("q")) {
                return UpdateResult.from(this, QuitMessage::new);
            }
            return UpdateResult.from(this);
        } else if (msg instanceof StatusMessage statusMsg) {
            this.statusMessage = statusMsg;
            return UpdateResult.from(this, QuitMessage::new);
        } else if (msg instanceof ErrorMessage errorMessage) {
            this.error = errorMessage.error();
            return UpdateResult.from(this);
        }
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        StringBuilder buffer = new StringBuilder("Checking %s...".formatted(URL));
        if (error != null) {
            buffer.append("something went wrong: %s".formatted(error.getMessage()));
        } else if (statusMessage != null) {
            buffer.append("%d %s".formatted(statusMessage.statusCode(), statusMessage.message()));
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        new Program(new HttpExample()).run();
    }
}