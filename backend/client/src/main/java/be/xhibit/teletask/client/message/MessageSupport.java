package be.xhibit.teletask.client.message;

import be.xhibit.teletask.model.spec.function.Function;
import com.google.common.primitives.Bytes;

import java.io.IOException;
import java.io.OutputStream;

public abstract class MessageSupport {
    private final Function function;

    protected MessageSupport(Function function) {
        this.function = function;
    }

    private byte[] compose() {
        byte[] payload = this.getPayload();

        int msgStx = 2;                                     // STX: is this value always fixed 02h?
        int msgLength = 4 + payload.length;                 // Length: the length of the command without checksum
        int msgCommand = this.getCommand().getCode();       // Command Number
        byte msgFunction = this.getFunction().getCode();    // The function to execute

        byte[] messageBytes = Bytes.concat(new byte[]{(byte) msgStx, (byte) msgLength, (byte) msgCommand, msgFunction}, payload);

        // ChkSm: Command Number + Command Parameters + Length + STX
        byte checkSumByte = 0;
        for (byte messageByte : messageBytes) {
            checkSumByte += messageByte;
        }
        messageBytes = Bytes.concat(messageBytes, new byte[]{checkSumByte});

        return messageBytes;
    }

    public int send(OutputStream outputStream) {
        byte[] myByteArray = this.compose();

        int result;
        try {
            //Send data over socket
            outputStream.write(myByteArray);
            outputStream.flush();

            result = 1;
        } catch (IOException e){
            result = 0;
        }

        return result;
    }


    /**
     * This should return the payload without the function part of the payload.
     *
     * @return The payload after 'function'
     */
    protected abstract byte[] getPayload();

    protected abstract Command getCommand();

    private Function getFunction() {
        return this.function;
    }
}
