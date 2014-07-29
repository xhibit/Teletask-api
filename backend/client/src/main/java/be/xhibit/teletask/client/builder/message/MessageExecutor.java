package be.xhibit.teletask.client.builder.message;

import java.io.OutputStream;

public class MessageExecutor<R> implements Runnable {
    private final MessageSupport message;
    private final OutputStream out;

    private MessageExecutor(MessageSupport message, OutputStream out) {
        this.message = message;
        this.out = out;
    }

    public static <S> MessageExecutor<S> of(MessageSupport message, OutputStream out) {
        return new MessageExecutor<>(message, out);
    }

    @Override
    public void run() {
        this.message.execute(this.out);
    }
}
