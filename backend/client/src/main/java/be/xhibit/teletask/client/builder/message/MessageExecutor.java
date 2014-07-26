package be.xhibit.teletask.client.builder.message;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

public class MessageExecutor<R> implements Callable<R> {
    private final MessageSupport<R> message;
    private final OutputStream out;
    private final InputStream in;

    private MessageExecutor(MessageSupport<R> message, OutputStream out, InputStream in) {
        this.message = message;
        this.out = out;
        this.in = in;
    }

    public static <S> MessageExecutor<S> of(MessageSupport<S> message, OutputStream out, InputStream in) {
        return new MessageExecutor<>(message, out, in);
    }

    @Override
    public R call() throws Exception {
        return this.message.execute(this.out, in);
    }
}
