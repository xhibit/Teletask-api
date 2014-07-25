package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.message.response.EventMessageServerResponse;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.base.Joiner;
import com.google.common.primitives.Bytes;

public abstract class GetMessageSupport<R> extends FunctionBasedMessageSupport<R> {
    private final int[] numbers;

    protected GetMessageSupport(Function function, ClientConfigSpec clientConfig, int... numbers) {
        super(clientConfig, function);
        this.numbers = numbers;
    }

    protected int[] getNumbers() {
        return this.numbers;
    }

    @Override
    protected byte[] getPayload() {
        return Bytes.concat(new byte[]{(byte) this.getMessageHandler().getFunctionConfig(this.getFunction()).getNumber()}, this.getMessageHandler().composeOutput(this.getNumbers()));
    }

    @Override
    protected Command getCommand() {
        return Command.GET;
    }

    @Override
    protected String getPayloadLogInfo() {
        return Joiner.on(", ").join(this.formatFunction(this.getFunction()), this.formatOutput(this.getNumbers()));
    }
}
