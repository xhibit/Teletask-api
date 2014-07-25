package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.SendResult;
import be.xhibit.teletask.client.builder.composer.MessageHandlerFactory;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.base.Joiner;
import com.google.common.primitives.Bytes;

public class SetMessage extends FunctionStateBasedMessageSupport<SendResult> {
    private final int number;

    public SetMessage(ClientConfigSpec clientConfig, Function function, int number, State state) {
        super(clientConfig, function, state);
        this.number = number;
    }

    @Override
    protected byte[] getPayload() {
        return Bytes.concat(new byte[]{(byte) this.getMessageHandler().getFunctionConfig(this.getFunction()).getNumber()}, MessageHandlerFactory.getMessageHandler(this.getClientConfig().getCentralUnitType()).composeOutput(this.number), new byte[]{(byte) this.getMessageHandler().getStateConfig(this.getState()).getNumber()});
    }

    @Override
    protected Command getCommand() {
        return Command.SET;
    }

    @Override
    protected String getPayloadLogInfo() {
        return Joiner.on(", ").join(this.formatFunction(this.getFunction()), this.formatOutput(this.number), this.formatState(this.getState()));
    }
}
