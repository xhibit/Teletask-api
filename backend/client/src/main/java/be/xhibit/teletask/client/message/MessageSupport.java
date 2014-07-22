package be.xhibit.teletask.client.message;

import be.xhibit.teletask.client.message.v2_8.MicrosMessageComposer;
import be.xhibit.teletask.client.message.v3_1.MicrosPlusMessageComposer;
import be.xhibit.teletask.model.spec.CentralUnitType;
import be.xhibit.teletask.model.spec.Command;
import be.xhibit.teletask.model.spec.Function;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public abstract class MessageSupport {
    private static final Map<CentralUnitType, MessageComposer> COMPOSERS = ImmutableMap.<CentralUnitType, MessageComposer>builder()
            .put(CentralUnitType.MICROS, new MicrosMessageComposer())
            .put(CentralUnitType.MICROS_PLUS, new MicrosPlusMessageComposer())
            .build();

    private final Function function;
    private final CentralUnitType centralUnitType;

    protected MessageSupport(CentralUnitType centralUnitType, Function function) {
        this.centralUnitType = centralUnitType;
        this.function = function;
    }

    public SendResult send(OutputStream outputStream) {
        byte[] myByteArray = this.getMessageComposer().compose(this.getCommand(), this.getFunction(), this.getPayload());

        SendResult result;
        try {
            //Send data over socket
            outputStream.write(myByteArray);
            outputStream.flush();

            result = SendResult.SUCCESS;
        } catch (IOException e){
            result = SendResult.FAILED;
        }

        return result;
    }

    private MessageComposer getMessageComposer() {
        return null;
    }

    public CentralUnitType getCentralUnitType() {
        return this.centralUnitType;
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
