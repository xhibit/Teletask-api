package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.base.Joiner;
import com.google.common.primitives.Bytes;

public class GetMessage extends FunctionBasedMessageSupport {
    private final int[] number;

    public GetMessage(ClientConfigSpec clientConfig, Function function, int number) {
        this(function, clientConfig, number);
    }

    protected GetMessage(Function function, ClientConfigSpec clientConfig, int... number) {
        super(clientConfig, function);
        this.number = number;
    }

    @Override
    protected byte[] getPayload() {
        return Bytes.concat(new byte[]{(byte) this.getMessageHandler().getFunctionConfig(this.getFunction()).getNumber()}, this.getMessageHandler().composeOutput(this.number));
    }

    @Override
    protected Command getCommand() {
        return Command.GET;
    }

    @Override
    protected String getPayloadLogInfo() {
        return Joiner.on(", ").join(this.formatFunction(this.getFunction()), this.formatOutput(this.number));
    }
}
