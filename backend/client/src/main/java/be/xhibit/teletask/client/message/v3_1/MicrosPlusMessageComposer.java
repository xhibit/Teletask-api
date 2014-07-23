package be.xhibit.teletask.client.message.v3_1;

import be.xhibit.teletask.client.message.CommandConfig;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.client.message.MessageComposer;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Bytes;

import java.nio.ByteBuffer;
import java.util.Map;

public class MicrosPlusMessageComposer implements MessageComposer {
    private static final Map<Command, CommandConfig> COMMAND_CONFIG = ImmutableMap.<Command, CommandConfig>builder()
            .put(Command.SET, new CommandConfig(7, "Central Unit", "Fnc", "Outp1", "Outp2", "Sate"))
            .put(Command.GET, new CommandConfig(6, "Central Unit", "Fnc", "Outp1", "Outp2"))
            .put(Command.LOG, new CommandConfig(3, "Central Unit", "Fnc", "Sate"))
            .build();

    @Override
    public byte[] compose(Command command, Function function, byte[] payload) {
        int msgStx = 2;                                                     // STX: is this value always fixed 02h?
        int msgLength = 5 + payload.length;                                 // Length: the length of the command without checksum
        int msgCommand = this.getCommandConfig().get(command).getNumber();  // Command Number
        int msgCentralUnit = 1;                                             // Now we only support 1 central unit per
        byte msgFunction = function.getCode();                              // The function to execute

        byte[] messageBytes = Bytes.concat(new byte[]{(byte) msgStx, (byte) msgLength, (byte) msgCommand, (byte) msgCentralUnit, msgFunction}, payload);

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
        byte[] array = ByteBuffer.allocate(4).putInt(number).array();
        return new byte[]{array[2], array[3]};
    }
}
