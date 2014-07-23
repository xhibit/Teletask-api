package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.primitives.Bytes;

public class SetMessage extends MessageSupport {
    private final int number;
    private final State state;

    public SetMessage(ClientConfig clientConfig, Function function, int number, State state) {
        super(clientConfig, function);
        this.number = number;
        this.state = state;
    }

    @Override
    protected byte[] getPayload() {
        return Bytes.concat(this.getMessageComposer().composeOutput(this.number), new byte[]{this.state.getCode()});
    }

    @Override
    protected Command getCommand() {
        return Command.SET;
    }

    @Override
    protected String getPayloadLogInfo() {
        return Joiner.on(", ").join(this.formatOutput(this.number), this.formatState(this.state));
    }
}
