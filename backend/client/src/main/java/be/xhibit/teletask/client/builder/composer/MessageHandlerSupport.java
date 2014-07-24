package be.xhibit.teletask.client.builder.composer;

import com.google.common.primitives.Bytes;
import org.slf4j.Logger;

public abstract class MessageHandlerSupport implements MessageHandler {
    private String getMessageChecksumCalculationSteps(byte[] message) {
        StringBuilder builder = new StringBuilder(100);
        boolean first = true;
        for (byte messageByte : message) {
            if (!first) {
                builder.append(" + ");
            }
            builder.append(messageByte);
            first = false;
        }
        return builder.toString();
    }

    protected byte[] getMessageWithChecksum(byte[] messageBytes) {
        // ChkSm: Command Number + Command Parameters + Length + STX
        byte checkSumByte = 0;
        for (byte messageByte : messageBytes) {
            checkSumByte += messageByte;
        }
        this.getLogger().debug("Checksum calculation: {} = {}", this.getMessageChecksumCalculationSteps(messageBytes), checkSumByte);
        messageBytes = Bytes.concat(messageBytes, new byte[]{checkSumByte});
        return messageBytes;
    }

    @Override
    public int getStart() {
        return 2;
    }

    protected abstract Logger getLogger();
}
