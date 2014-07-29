package be.xhibit.teletask.client.builder.message;

import be.xhibit.teletask.model.spec.ClientConfigSpec;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.ComponentSpec;
import be.xhibit.teletask.model.spec.Function;

import java.util.List;

public class GroupGetMessage extends GetMessageSupport<List<ComponentSpec>> {
    public GroupGetMessage(ClientConfigSpec clientConfig, Function function, int... number) {
        super(function, clientConfig, number);
    }

    @Override
    public Command getCommand() {
        return Command.GROUPGET;
    }

    @Override
    protected String getLogHeaderName(int index) {
        String name = super.getLogHeaderName(index);
        return name == null ? this.getMessageHandler().getOutputLogHeaderName(index) : name;
    }

}
