package be.xhibit.teletask.client.message.v3_1;

import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.MessageComposer;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Bytes;

import java.util.Map;

public class MicrosPlusMessageComposer implements MessageComposer {
    private static final Map<Command, Integer> COMMAND_MAP = ImmutableMap.<Command, Integer>builder()
            .put(Command.SET, 1)
            .put(Command.GET, 2)
            .put(Command.LOG, 3)
            .build();

    @Override
    public byte[] compose(Command command, Function function, byte[] payload) {
        int msgStx = 2;                             // STX: is this value always fixed 02h?
        int msgLength = 4 + payload.length;         // Length: the length of the command without checksum
        int msgCommand = COMMAND_MAP.get(command);  // Command Number
        byte msgFunction = function.getCode();      // The function to execute

        byte[] messageBytes = Bytes.concat(new byte[]{(byte) msgStx, (byte) msgLength, (byte) msgCommand, msgFunction}, payload);

        // ChkSm: Command Number + Command Parameters + Length + STX
        byte checkSumByte = 0;
        for (byte messageByte : messageBytes) {
            checkSumByte += messageByte;
        }
        messageBytes = Bytes.concat(messageBytes, new byte[]{checkSumByte});

        return messageBytes;
    }
}
