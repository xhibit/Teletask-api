package be.xhibit.teletask.client.builder.message.messages.impl;

import be.xhibit.teletask.client.builder.SendResult;
import be.xhibit.teletask.client.builder.message.messages.FunctionStateBasedMessageSupport;
import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.StateEnum;
import be.xhibit.teletask.model.spec.StateEnumImpl;

public class LogMessage extends FunctionStateBasedMessageSupport<SendResult> {
    public LogMessage(ClientConfigSpec ClientConfig, Function function, StateEnum state) {
        super(ClientConfig, function, new StateEnumImpl(state, function));
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
    protected String[] getPayloadLogInfo() {
        return new String[]{this.formatFunction(this.getFunction()), this.formatState(this.getState())};
    }

    @Override
    protected boolean isValid() {
        return true;
    }
}
