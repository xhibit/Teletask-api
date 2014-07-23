package be.xhibit.teletask.client.builder.composer.v2_8;

import be.xhibit.teletask.client.builder.CommandConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.client.builder.composer.MessageComposer;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Bytes;

import java.util.Map;

public class MicrosMessageComposer implements MessageComposer {
    private static final Map<Command, CommandConfig> COMMAND_CONFIG = ImmutableMap.<Command, CommandConfig>builder()
            .put(Command.SET, new CommandConfig(1, "Fnc", "Outp", "Sate"))
            .put(Command.GET, new CommandConfig(2, "Fnc", "Outp"))
            .put(Command.LOG, new CommandConfig(3, "Fnc", "Sate"))
            .build();

    @Override
    public byte[] compose(Command command, Function function, byte[] payload) {
        int msgStx = 2;                                                     // STX: is this value always fixed 02h?
        int msgLength = 4 + payload.length;                                 // Length: the length of the command without checksum
        int msgCommand = this.getCommandConfig().get(command).getNumber();  // Command Number
        byte msgFunction = function.getCode();                              // The function to execute

        byte[] messageBytes = Bytes.concat(new byte[]{(byte) msgStx, (byte) msgLength, (byte) msgCommand, msgFunction}, payload);

        // ChkSm: Command Number + Command Parameters + Length + STX
        byte checkSumByte = 0;
        for (byte messageByte : messageBytes) {
            checkSumByte += messageByte;
        }
        messageBytes = Bytes.concat(messageBytes, new byte[]{checkSumByte});

        return messageBytes;
    }

    @Override
    public Map<Command, CommandConfig> getCommandConfig() {
        return COMMAND_CONFIG;
    }

    @Override
    public byte[] composeOutput(int number) {
        return new byte[]{(byte) number};
    }
}
