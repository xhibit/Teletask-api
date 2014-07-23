package be.xhibit.teletask.client;

import be.xhibit.teletask.client.builder.message.GetMessage;
import be.xhibit.teletask.client.builder.message.LogMessage;
import be.xhibit.teletask.client.builder.SendResult;
import be.xhibit.teletask.client.builder.message.SetMessage;
import be.xhibit.teletask.model.spec.ClientConfig;
import be.xhibit.teletask.model.spec.Component;
import be.xhibit.teletask.model.spec.Function;
import be.xhibit.teletask.model.spec.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Bruno Braes, http://www.xhibit.be
 * Date: 7/09/12
 * Time: 15:40
 * <p/>
 * <p/>
 * FunctionSet(int Fnc, int Opt, int Number, int State)
 * > example to switch relays: FunctionSet(1, 0, 19, 1) -> switches relays 19 to on (=bureau).
 * <p/>
 * - Fnc: Fnc ( RELAY, DIMMER, MOTOR, MTRUPDOWN, LOCMOOD, TIMEDMOOD, GENMOOD, FLAG, PROCES, REGIME, SERVICE, MESSAGE) = see "Constants" below / Functions.cs for full list.
 * - Opt: not required for RELAYS? value 0? (dependent on the function: see Options.cs for full list)
 * - Number:
 * for Fnc = FNC_RELAY, FNC_DIMMER Number = 1 to Maximum -> Number out the output relay
 * for Fnc = FNC_TPKEY -> Number = Touch panel number you want to simulate
 * All other Fnc -> Number = 0 to Maximum -1
 * - State:
 * for Fnc=FNC_DIMMER & FNC_MOTOR -> State = 0 to 255 (always use the result from function ConvPercToDimVal, may never be 3!!!)
 * for Fnc = FNC_TPKEY
 * -> State bit 3-0 = Key number (0 to 7)
 * -> State bit 7-8 = 00 Normal Short Press, 01 Key Depressed, 10 Key Released
 * for all other Fnc
 * -> State = 0 or 255 (or 1) = OFF or ON
 * <p/>
 * Output: Return value:
 * - 0 = Message successfully transmitted
 * - 1 = Communication not opened
 * - 2 = No Answer
 * <p/>
 * All commands and messages in both directions will use the same frame format:
 * STX (02h) + Length + Command Number + Parameter 1 + ... + Parameter n + ChkSm
 * <p/>
 * The length does not include the ChkSm-byte. The ChkSm is calculated on Command Number + Command Parameters + Length + STX.
 * After the ChkSm the central unit send an acknowledge byte 0A (hex). If no acknowledge byte is send the command is not handled.
 * <p/>
 * --------------------------
 * <p/>
 * Function Set
 * - Description: This command allows the CCT to set individual functions. See “methods” for detailed descriptions
 * - Command number: 01h
 * - Length: 6
 * - Direction: From TDS to CCT.
 * - Parameter 1 = Fnc
 * - Parameter 2 = Outp
 * - Parameter 3 = State
 * <p/>
 * Function Get
 * - Description: When the TDS receives this command it reports the level of the specified load. See methods for detailed descriptions
 * - Command number: 02h
 * - Length: 5
 * - Direction: From CCT to TDS
 * - Parameter 1 = fnc
 * - Parameter 2 = Outp
 * <p/>
 * Function Log On/Off
 * - Description: When the TDS receives this command it (de-)activates it’s channel for reporting the function!
 * This function will open/close a channel for the function
 * Example: If you call this function with the parameter Fnc=FNC_RELAY and State=1, all changes on relays will occur as ‘event’! In case you set State=0 no more events will occur from relays.
 * - Command number: 03h
 * - Length: 5
 * - Direction: From CCT to TDS
 * - Parameter 1 = fnc
 * - Parameter 2 = state
 * <p/>
 * Lux values
 * To change from byte to lux = (10 (byte / 40)) - 1
 * To change from lux to byte = Log10(lux + 1) * 40
 * <p/>
 * Constants
 * The functions in the DLL use a parameter “Fnc” and can have following values
 * FNC_RELAY = 1 (control or get the status of a relay)
 * FNC_DIMMER = 2 (control or get the status of a dimmer)
 * FNC_MOTOR = 6 (control or get the status of a Motor: On/Off)
 * FNC_MTRUPDOWN = 55 (control or get the status of a Motor: Op/Down)
 * FNC_LOCMOOD = 8 (control or get the status of a Local Mood)
 * FNC_TIMEDMOOD = 9 (control or get the status of a Timed Local Mood)
 * FNC_GENMOOD = 10 (control or get the status of a General Mood)
 * FNC_FLAG = 15 (control or get the status of a Flag)
 * FNC_PROCES = 3 (control or get the status of a Process function)
 * FNC_REGIME = 14 (control or get the status of a Regime function)
 * FNC_SERVICE = 53 (control or get the status of a Service function)
 * FNC_MESSAGE = 54 (control or get the status of a Messages or Alarms)
 * FNC_COND = 60 (get the status of a Condition)
 * FNC_TPKEY = 52 (simulate a key press on an interface)
 * FNC_GETSENSTARGET = 21 (get the status of a Sensor setting)
 * <p/>
 * If you are making your own interface you have to take care of the following:
 * <p/>
 * - With the LOG function you open a kind of 'channel' from the specific device type (ex. relays) from the central unit to your device.
 * This mean that when such a device has a change the central unit will sent automatically a "report" to you.
 * You only open the LOG for the devices you really need (ex. relays, dimmer, local moods, sensors)
 * - When you want to know a state a a specific device you have to send a GET command, afterwards (asynchronously) you will get a "report" with the state of the device
 * Normally it's not necessary to use this is you opened the LOG
 * - When you want to know the state of several devices (at startup) you send a Group GET command for a specific type (ex. relays)with the numbers of all devices you want to know the state.
 * Afterwards for every device you asked there will come a "report"
 * - These reports are coming on the socket you open, so you have to check the bytes that are coming in, but you don't have to open a listener on a port.
 * - You can send a keep alive to make sure that the central unit don't close the port because there is no activity
 */
public final class TDSClient {
    /**
     * Logger responsible for logging and debugging statements.
     */
    private static final Logger LOG = LoggerFactory.getLogger(TDSClient.class);

    private static final Pattern SPLIT_PATTERN_RESPONSE_ARRAY = Pattern.compile("2, 6, 8, ");
    private static final Pattern SPLIT_PATTERN_RELAYS = Pattern.compile(", ");

    private Socket socket;

    private DataOutputStream out;
    private DataInputStream in;

    private ClientConfig clientConfig;

    private final Map<String, State> states = new HashMap<>();

    private boolean readTDSEvents = false;

    private static final Map<String, TDSClient> CLIENTS = new HashMap<>();

    /**
     * Default constructor.  Responsible for reading the client config (JSON).
     * Singleton class.  Private constructor to prevent new instance creations.
     */
    private TDSClient(ClientConfig clientConfig) {
        this.configure(clientConfig);
    }

    /**
     * Create of get an instance of the TDSClient.
     *
     * @return a new or existing TDSClient instance.
     */
    public static synchronized TDSClient getInstance(ClientConfig clientConfig) {
        String index = clientConfig.getHost() + ":" + clientConfig.getPort();
        TDSClient client = CLIENTS.get(index);

        if (client == null) {
            client = new TDSClient(clientConfig);
            CLIENTS.put(index, client);
        } else {
            client.configure(clientConfig);
        }

        return client;
    }

// ################################################ PUBLIC API FUNCTIONS

    public SendResult set(Component component, State state) {
        Function function = component.getComponentFunction();
        int number = component.getComponentNumber();

        return this.set(function, number, state);
    }

    public SendResult set(Function function, int number, State state) {
        SendResult result = new SetMessage(this.getConfig(), function, number, state).send(this.out);

        if (result == SendResult.SUCCESS) {
            this.setState(function, number, this.getState(function, number));
        }

        return result;
    }

    public State get(Component component) {
        return this.getState(component.getComponentFunction(), component.getComponentNumber());
    }

    public void close() {
        LOG.debug("Disconnecting from {}", this.socket.getInetAddress().getHostAddress());

        // close all log events to stop reporting
        this.sendLogEventMessages(State.OFF);

        try {
            this.readTDSEvents = false;
            this.in.close();
            this.out.close();
            this.socket.close();
            System.exit(0);
        } catch (IOException e) {
            LOG.error("Error disconnecting from host\n", e);
        }
        LOG.debug("Disconnected successfully");
    }

    public ClientConfig getConfig() {
        return this.clientConfig;
    }

    // ################################################ PRIVATE API FUNCTIONS

    private void configure(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    private State getState(Function function, int number) {
        return this.getState(0, function, number);
    }

    private State getState(int counter, Function function, int number) {
        State state = this.states.get(this.getStateIndex(function, number));
        if (state == null) {
            if (counter < 10) {
                this.getStateFromCentralUnit(function, number);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    LOG.error("Exception ({}) caught in getState: {}", e.getClass().getName(), e.getMessage(), e);
                }

                state = this.getState(++counter, function, number);
            } else {
                throw new RuntimeException("Could not get state for '" + function + "':'" + number + "' in a timely fashion");
            }
        }
        return state;
    }

    private void setState(Function function, int number, State state) {
        this.states.put(this.getStateIndex(function, number), state);
        Component component = this.getConfig().getComponent(function, number);
        if (component != null) {
            component.setComponentState(state);
        }
    }

    private void getStateFromCentralUnit(Function function, int number) {
        new GetMessage(this.getConfig(), function, number).send(this.out);
    }

    private String getStateIndex(Function function, int number) {
        return function + ":" + number;
    }

    private void sendLogEventMessages(State state) {
        this.sendLogEventMessage(Function.RELAY, state);
        this.sendLogEventMessage(Function.LOCMOOD, state);
        this.sendLogEventMessage(Function.GENMOOD, state);
        this.sendLogEventMessage(Function.MTRUPDOWN, state);
    }

    private void createSocket(String host, int port) {
        // delay to wait for the first execution, should occur immediately at startup
        int timerDelay = 0;
        // time in milliseconds to wait between every execution: every 30 minutes
        int timerPeriod = 30 * 60 * 1000;

        // Connect method
        LOG.debug("Connecting to {}:{}", host, port);

        try {
            this.socket = new Socket(host, port);
            this.socket.setKeepAlive(true);
            this.socket.setSoTimeout(5000);
        } catch (IOException e) {
            LOG.error("Problem connecting to host: {}", host, e);
            System.exit(1);
        }

        LOG.debug("Successfully Connected");

        try {
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            LOG.error("Couldn't get I/O for the connection to: {}:{}", host, port);
            System.exit(1);
        }

        // open the log event(s), run periodically to keep the connection to the server open
        //readTDSEvents = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                // open event channel for reporting back state changes
                TDSClient.this.sendLogEventMessages(State.EVENT);
            }
        }, timerDelay, timerPeriod);

        // read the TDS output for log messages every XXX milliseconds
        final int readInterval = 500;
        try {
            new Thread() {
                @Override
                public void run() {
                    synchronized (TDSClient.this.socket) {
                        try {
                            //LOG.debug("readTDSEvents: " +readTDSEvents);

                            while (TDSClient.this.readTDSEvents) {
                                readLogResponse(TDSClient.this);
                                Thread.sleep(readInterval); //pause for a defined period of time
                            }
                        } catch (Exception ex) {
                            LOG.error("Exception in thread runner: {}", ex.getMessage());
                        }
                    }
                }
            }.start();

        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    private void sendLogEventMessage(Function function, State state) {
        new LogMessage(this.getConfig(), function, state).send(this.out);
    }

    private static void readLogResponse(TDSClient client) throws Exception {
        DataInputStream in = client.in;
        try {
            byte[] data = new byte[in.available()];
            //not sure to read what length, because sometimes an acknowledge byte is sent back (actually after every GET or SET command)
            //therefore rule below no good, better to parse for fixed value "2,6,8" as a response for a RELAYS state switch: 2 following items are always relays + state.
            //byte [] data = new byte[7];

            //NOTE: should the blocking of acknowledge byte below work, then reading per 7 bytes is best option!  Always a block of 7 bytes corresponds to 1 relays state!
            in.readFully(data);

            String response = byteToString(data);
            if (response != null && !"".equals(response)) {
                //LOG.debug("RECEIVED socket data: " + response);

                // Other than the Acknowledge byte, the Response is 7 bits: first three always seems to be "2, 6, 8" so we'll use that to split the responses
                // 4th bit is variable: the FUNCTION ID
                // 5th bit is number (output,  relay, motor, ...)
                // 6th bit is state (0-255 for dimmer) and 0 (off) or -1 (on) for relay etc
                // 7th bit: unsure, no use?

                String[] responseArray = SPLIT_PATTERN_RESPONSE_ARRAY.split(response);
                if (responseArray.length > 0) {
                    for (String element : responseArray) {
                        if (element != null && !"".equals(element) && element.contains(",")) {
                            //LOG.debug("\t - relays element part: " +element);
                            String[] relaysArray = SPLIT_PATTERN_RELAYS.split(element);
                            if (relaysArray.length >= 3) {
                                Integer functionCode = Integer.valueOf(relaysArray[0]);
                                Integer number = Integer.valueOf(relaysArray[1]);
                                Integer state = Integer.valueOf(relaysArray[2]);
                                if (state == -1) {
                                    state = 1; // -1 means ON, better to use 1.
                                }

                                // update the component state
                                Function function = Function.valueOf(functionCode);
                                client.setState(function, number, function.getState(state));
                            }
                        }
                    }
                }

            }

        } catch (Exception ex) {
            LOG.error("Exception reading response: {}", ex);

        }
    }

    /**
     * Utility method for converting a byte array into a HEX string for representation in debugging.
     *
     * @param bytes The byte[]
     * @return A string representation of the byte[]
     */
    private static String byteToString(byte[] bytes) {
        String bytesAsString = Arrays.toString(bytes);
        bytesAsString = bytesAsString.replace("[", "");
        bytesAsString = bytesAsString.replace("]", "");
        bytesAsString = bytesAsString.trim();

        if (!"".equals(bytesAsString)) {
            // if starts with acknowledge byte, remove and return without this byte
            if (bytesAsString.startsWith("10, ")) {
                //LOG.debug("Acknowledge byte received (no longer expected here), skipped in output: " +bytesAsString);
                bytesAsString = bytesAsString.substring(4, bytesAsString.length() - 1);
            }
        }

        return bytesAsString;
    }

    /**
     * Prevent cloning.
     *
     * @return Nothing really, because this will always result in an Exception.
     * @throws CloneNotSupportedException when called.
     */
    @Override
    public final TDSClient clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}