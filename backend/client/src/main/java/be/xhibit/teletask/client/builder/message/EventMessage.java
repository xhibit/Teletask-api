package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.client.builder.SendResult;
import be.xhibit.teletask.client.builder.message.response.ServerResponse;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Joiner;
import com.google.common.primitives.Bytes;

import java.util.List;

public class EventMessage extends FunctionBasedMessageSupport<SendResult> {
    private final int number;
    private final State state;
    private final byte[] rawBytes;

    public EventMessage(ClientConfigSpec clientConfig, byte[] rawBytes, Function function, int number, State state) {
        super(clientConfig, function);
        this.rawBytes = rawBytes;
        this.number = number;
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    public int getNumber() {
        return this.number;
    }

    @JsonIgnore
    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    @Override
    protected byte[] getPayload() {
        return Bytes.concat(new byte[]{(byte) this.getMessageHandler().getFunctionConfig(this.getFunction()).getNumber()}, this.getMessageHandler().composeOutput(this.getNumber()), new byte[]{(byte) this.getMessageHandler().getStateConfig(this.getState()).getNumber()});
    }

    @Override
    protected Command getCommand() {
        return Command.EVENT;
    }

    @Override
    protected String getPayloadLogInfo() {
        return Joiner.on(", ").join(this.formatFunction(this.getFunction()), this.formatOutput(this.getNumber()), this.formatState(this.getState()));
    }

    @Override
    protected SendResult createResponse(List<ServerResponse> serverResponses) {
        return this.expectSingleAcknowledge(serverResponses);
    }
}
