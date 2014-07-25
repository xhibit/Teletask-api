package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.SendResult;
import be.xhibit.teletask.client.builder.message.response.ServerResponse;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.base.Joiner;

import java.util.List;

public class LogMessage extends FunctionStateBasedMessageSupport<SendResult> {
    public LogMessage(ClientConfigSpec ClientConfig, Function function, State state) {
        super(ClientConfig, function, state);
    }

    @Override
    protected byte[] getPayload() {
        return new byte[]{(byte) this.getMessageHandler().getFunctionConfig(this.getFunction()).getNumber(), (byte) this.getMessageHandler().getStateConfig(this.getState()).getNumber()};
    }

    @Override
    protected Command getCommand() {
        return Command.LOG;
    }

    @Override
    protected String getPayloadLogInfo() {
        return Joiner.on(", ").join(this.formatFunction(this.getFunction()), this.formatState(this.getState()));
    }

    @Override
    protected SendResult createResponse(List<ServerResponse> serverResponses) {
        return this.expectSingleAcknowledge(serverResponses);
    }

    @Override
    protected boolean isValid() {
        return true;
    }
}
