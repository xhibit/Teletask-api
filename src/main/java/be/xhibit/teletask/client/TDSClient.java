package be.xhibit.teletask.client;

import be.xhibit.teletask.api.enums.Function;
import be.xhibit.teletask.api.model.Room;
import be.xhibit.teletask.api.model.TDSClientConfig;
import be.xhibit.teletask.api.model.TDSComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bruno Braes, http://www.xhibit.be
 * Date: 7/09/12
 * Time: 15:40
 *
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
 *   This mean that when such a device has a change the central unit will sent automatically a "report" to you.
 *   You only open the LOG for the devices you really need (ex. relays, dimmer, local moods, sensors)
 * - When you want to know a state a a specific device you have to send a GET command, afterwards (asynchronously) you will get a "report" with the state of the device
 *   Normally it's not necessary to use this is you opened the LOG
 * - When you want to know the state of several devices (at startup) you send a Group GET command for a specific type (ex. relays)with the numbers of all devices you want to know the state.
 *   Afterwards for every device you asked there will come a "report"
 * - These reports are coming on the socket you open, so you have to check the bytes that are coming in, but you don't have to open a listener on a port.
 * - You can send a keep alive to make sure that the central unit don't close the port because there is no activity
 */
public class TDSClient {

    static final Logger logger = LogManager.getLogger(TDSClient.class.getName());

    private Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private boolean readTDSEvents = false;

    private static TDSClient client;
    private static TDSClientConfig clientConfig;

    /**
     * Default constructor.  Responsible for reading the client config (JSON).
     * Singleton class.  Private constructor to prevent new instance creations.
     */
    private TDSClient() {
        logger.debug("##### TDSClient initialization - START");
        //TODO: find better way to load the config
        // Should we load and parse the config to POJO from a URL using JAX-RS?
        // This way the config can be loaded from webroot as a simple resource, or from any location.

        try {
            //read json file data to String
            InputStream jsonData = this.getClass().getClassLoader().getResourceAsStream("tds-config.json");

            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            //convert json string to object
            clientConfig = objectMapper.readValue(jsonData, TDSClientConfig.class);
            logger.debug("Config loaded: TDS HOST: " +clientConfig.getHost() +":" +clientConfig.getPort() +" - TESTMODE: " +clientConfig.isTestMode());

            // until a better Jackson ObjectMapper implementation, loop through all rooms and replace component number by actual object reference
            clientConfig.initRooms();

            // don't connect to the TDS server if testMode is enabled in the JSON config; his enables us to test the UI.
            if (!clientConfig.isTestMode()) {
                // TODO: define timeout for connection attempt.
                this.createSocket(clientConfig.getHost(), clientConfig.getPort());

                // retrieve all initial component states
                this.getInitialComponentStates();
            }

            logger.debug("##### TDSClient initialization - END");
        } catch (IOException e) {
            logger.error("Exception while parsing JSON config. " , e);
        }
    }

    /**
     * Create of get an instance of the TDSClient.
     * @return a new or existing TDSClient instance.
     */
    public static synchronized TDSClient getInstance() {

        if (client == null) {
            client = new TDSClient();
        }

        return client;
    }

    // ################################################ PUBLIC API FUNCTIONS

    public TDSClientConfig getConfig() {
        return clientConfig;
    }

    public int switchRelayOn(int number) {
        byte[] messageBytes = this.composeSetMessage(Function.RELAY.getCode(), number, 255);
        return this.sendBytes(messageBytes);
    }

    public int switchRelayOff(int number) {
        byte[] messageBytes = this.composeSetMessage(Function.RELAY.getCode(), number, 0);
        return this.sendBytes(messageBytes);
    }

    public int getRelayState(int number) {
        return clientConfig.getComponent(Function.RELAY, number).getState();
    }

    public int switchLocalMoodOn(int number) {
        byte[] messageBytes = this.composeSetMessage(Function.LOCMOOD.getCode(), number, 255);
        return this.sendBytes(messageBytes);
    }

    public int switchLocalMoodOff(int number) {
        byte[] messageBytes = this.composeSetMessage(Function.LOCMOOD.getCode(), number, 0);
        return this.sendBytes(messageBytes);
    }

    public int getLocalMoodState(int number) {
        return clientConfig.getComponent(Function.LOCMOOD, number).getState();
    }

    public int switchGeneralMoodOn(int number) {
        byte[] messageBytes = this.composeSetMessage(Function.GENMOOD.getCode(), number, 255);
        return this.sendBytes(messageBytes);
    }

    public int switchGeneralMoodOff(int number) {
        byte[] messageBytes = this.composeSetMessage(Function.GENMOOD.getCode(), number, 0);
        return this.sendBytes(messageBytes);
    }

    public int getGeneralMoodState(int number) {
        return clientConfig.getComponent(Function.GENMOOD, number).getState();
    }

    public int switchMotorUp(int number) {
        byte[] messageBytes = this.composeSetMessage(Function.MTRUPDOWN.getCode(), number, 255);
        return this.sendBytes(messageBytes);
    }

    public int switchMotorDown(int number) {
        byte[] messageBytes = this.composeSetMessage(Function.MTRUPDOWN.getCode(), number, 0);
        return this.sendBytes(messageBytes);
    }

    public int getMotorState(int number) {
        return clientConfig.getComponent(Function.MTRUPDOWN, number).getState();
    }

    public int getComponentState(Function function, int number) {
        return clientConfig.getComponent(function, number).getState();
    }

    public TDSComponent getComponent(Function function, int number) {
        return clientConfig.getComponent(function, number);
    }

    public void close() {
        logger.debug("Disconnecting from " + socket.getInetAddress().getHostAddress());

        // close all log events to stop reporting
        sendLogEventMessages(0);

        try {
            readTDSEvents = false;
            in.close();
            out.close();
            socket.close();
            System.exit(0);
        } catch (IOException e) {
            logger.error("Error disconnecting from host\n", e);
        }
        logger.debug("Disconnected successfully");
    }

    private void sendLogEventMessages(int state) {
        sendLogEventMessage(Function.RELAY, state);
        sendLogEventMessage(Function.LOCMOOD, state);
        sendLogEventMessage(Function.GENMOOD, state);
        sendLogEventMessage(Function.MTRUPDOWN, state);
    }

    /**
     * Retrieve all initial component states by sending GET requests.  These values will return as an EVENT and be captured.
     */
    private void getInitialComponentStates() {

        Map<Function, List<TDSComponent>> componentsTypes = clientConfig.getComponentsTypes();
        Set<Function> functions = componentsTypes.keySet();
        for (Function function : functions) {
            List<TDSComponent> components = componentsTypes.get(function);
            for (TDSComponent component : components) {
                int number = component.getNumber();
                this.sendBytes(this.composeGetMessage(function.getCode(), number));

                //Pause before making new call
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    // ################################################ PRIVATE API FUNCTIONS

    private void createSocket(String host, int port) {

        // delay to wait for the first execution, should occur immediately at startup
        final int TIMER_DELAY = 0;
        // time in milliseconds to wait between every execution: every 30 minutes
        final int TIMER_PERIOD = 30*60*1000;

        // Connect method
        logger.debug("Connecting to " + host + ":" + port);

        try {
            socket = new Socket (host, port);
            socket.setKeepAlive(true);
            socket.setSoTimeout(5000);
        } catch (UnknownHostException e) {
            logger.error("Don't know about host: " + host);
            System.exit(1);
        } catch (SocketException e) {
            logger.error("Error connecting to host: " + host);
            System.exit(1);
        } catch (IOException e) {
            logger.error("Error connecting to host: " + host);
            System.exit(1);
        }

        logger.debug("Successfully Connected");

        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.error("Couldn't get I/O for " + "the connection to: " + host + ":" + port);
            System.exit(1);
        }

        // open the log event(s), run periodically to keep the connection to the server open
        //readTDSEvents = true;
        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            public void run() {
                // open event channel for reporting back state changes
                sendLogEventMessages(1);
            }
        }, TIMER_DELAY, TIMER_PERIOD);

        // read the TDS output for log messages every XXX milliseconds
        final int readInterval = 500;
        try {

            new Thread() {
                public void run() {
                    synchronized (socket) {
                        try {
                            //logger.debug("readTDSEvents: " +readTDSEvents);

                            //while (readTDSEvents) {
                            while (true) {
                                readLogResponse(in);
                                Thread.sleep(readInterval); //pause for a defined period of time
                            }
                        } catch (Exception ex) {
                            logger.error("Exception in thread runner: " + ex.getMessage());
                        }
                    }
                }
            }.start();

        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    private void sendLogEventMessage(Function function, int state) {
        try {
            byte[] logEventMsg = composeLogEventMsg(function.getCode(), state);
            logger.debug("SEND OPEN LOG EVENT for type \"" +function.getDescription() +"\" with bytes: " + byteToString(logEventMsg));
            out.write(logEventMsg);
            out.flush();

            //logger.trace("Successfully wrote log message!");
        } catch (Exception ex) {
            logger.error("Exception writing to socket: " + ex);

        }
    }

    private static void readLogResponse(DataInputStream in) throws Exception {
        try {

            byte [] data = new byte[in.available()];
            //not sure to read what length, because sometimes an acknowledge byte is send back (actually after every GET or SET command)
            //therefore rule below no good, better to parse for fixed value "2,6,8" as a response for a RELAYS state switch: 2 following items are always relays + state.
            //byte [] data = new byte[7];

            //NOTE: should the blocking of acknowledge byte below work, then reading per 7 bytes is best option!  Always a block of 7 bytes corresponds to 1 relays state!
            in.readFully(data);

            String response = byteToString(data);
            if (response != null && !"".equals(response)) {
                //logger.debug("RECEIVED socket data: " + response);

                // Other than the Acknowledge byte, the Response is 7 bits: first three always seems to be "2, 6, 8" so we'll use that to split the responses
                // 4th bit is variable: the FUNCTION ID
                // 5th bit is number (output,  relay, motor, ...)
                // 6th bit is state (0-255 for dimmer) and 0 (off) or -1 (on) for relay etc
                // 7th bit: unsure, no use?

                String[] responseArray = response.split("2, 6, 8, ");
                if (0 < responseArray.length) {
                    for (String element : responseArray) {
                        if (element != null && !"".equals(element) && element.contains(",")) {
                            //logger.debug("\t - relays element part: " +element);
                            String[] relaysArray = element.split(", ");
                            if (3 <= relaysArray.length) {
                                Integer functionCode = Integer.valueOf(relaysArray[0]);
                                Integer number = Integer.valueOf(relaysArray[1]);
                                Integer state = Integer.valueOf(relaysArray[2]);
                                if (state == -1) {
                                    state = 1; // -1 means ON, better to use 1.
                                }

                                // get the component reference
                                Function function = Function.valueOf(functionCode);
                                TDSComponent component = clientConfig.getComponent(function, number);

                                if (component != null) {
                                    // update the component state
                                    component.setState(state);
                                    logger.debug("RECEIVED NEW STATE FROM TDS: " + state + " of function: " + function.name() + " of component number: " + number);
                                } else {
                                    logger.warn("RECEIVED NEW STATE FROM TDS for component which doesnt exist in tds-config.json.  Likely not listed there because you don't want it to show in the UI.");
                                    logger.debug("RECEIVED NEW STATE FROM TDS: " + state + " of function: " + functionCode + " of component number: " + number);
                                }
                            }
                        }
                    }
                }

            }

        } catch (Exception ex) {
            logger.error("Exception reading response: " + ex);

        }
    }

    private int sendBytes(byte[] myByteArray) {
        return sendBytes(myByteArray, 0, myByteArray.length);
    }

    private int sendBytes(byte[] myByteArray, int start, int len) {

        if (len < 0) {
            throw new IllegalArgumentException("Negative length not allowed");
        }
        if (start < 0 || start >= myByteArray.length) {
            throw new IndexOutOfBoundsException("Out of bounds: " + start);
        }
        // Other checks if needed.

        try {

            //Send data over socket
            out.write(myByteArray);
            out.flush();

            return 1;

        } catch (IOException e){
            logger.error("Read failed", e);
            return 0;
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
        bytesAsString = bytesAsString.replace("[","");
        bytesAsString = bytesAsString.replace("]","");
        bytesAsString = bytesAsString.trim();

        if (!"".equals(bytesAsString)) {
            // if starts with acknowledge byte, remove and return without this byte
            if (bytesAsString.startsWith("10, ")) {
                //logger.debug("Acknowledge byte received (no longer expected here), skipped in output: " +bytesAsString);
                bytesAsString = bytesAsString.substring(4, bytesAsString.length()-1);
            }
        }

        return bytesAsString;
    }


    // ################################################ MSG COMPOSITION FUNCTIONS

    //TODO: refactor methods below: almost identical

    private byte[] composeLogEventMsg(int function, int state) {
        // OPEN A LOG EVENT
        // When the TDS receives this command it (de-)activates it's channel for reporting the function! This function will open/close a channel for the function.
        // If you call this function with the parameter Fnc=FNC_RELAY and State=1, all changes on relays will occur as 'event'!
        // In case you set State=0 no more events will occur from relays.
        // For RELAYS, return state will be: State = 0 OR 255 (or. 1) = OFF or ON
        int MSG_VALUE_STX = 2;
        int MSG_VALUE_LENGTH = 5;
        int MSG_VALUE_COMMAND_LOG = 3;
        int[] commandBytes = {MSG_VALUE_STX, MSG_VALUE_LENGTH, MSG_VALUE_COMMAND_LOG, function, state};
        byte[] messageBytes = new byte[MSG_VALUE_LENGTH+1];

        messageBytes[0] = (byte) commandBytes[0];    // STX: is this value always fixed 02h?
        messageBytes[1] = (byte) commandBytes[1];    // Length: the length of the command
        messageBytes[2] = (byte) commandBytes[2];    // Command Number (Fnc: the function being Set: in this case "Function Log On/Off" (03h))
        messageBytes[3] = (byte) commandBytes[3];    // Parameter 1: FNC_RELAY = 1 (control the status of a relay)
        messageBytes[4] = (byte) commandBytes[4];    // Parameter 2: State (1 = all changes on relays will occur as 'event', 0 = no more events will occur from relays)

        // ChkSm: Command Number + Command Parameters + Length + STX
        int checkSumByte = 0;
        for (int commandByte : commandBytes) {
            checkSumByte += commandByte;
            //logger.debug("checkSum item count=" +i +", value:" +commandBytes[i]);
        }
        //logger.debug("checkSum val=" +checkSumByte);
        messageBytes[MSG_VALUE_LENGTH] = (byte) checkSumByte;

        //logger.debug("Writing OPEN LOG bytes: " + Arrays.toString(messageBytes));
        return messageBytes;
    }

    private byte[] composeGetMessage(int function, int number) {
        int MSG_VALUE_STX = 2;
        int MSG_VALUE_LENGTH = 5;
        int MSG_VALUE_COMMAND_GET = 2;
        int[] commandBytes = {MSG_VALUE_STX, MSG_VALUE_LENGTH, MSG_VALUE_COMMAND_GET,function,number};
        byte[] messageBytes = new byte[MSG_VALUE_LENGTH+1];

        messageBytes[0] = (byte) commandBytes[0]; 	// STX: is this value always fixed 02h
        messageBytes[1] = (byte) commandBytes[1]; 	// Length: the length of the command, does not include the ChkSm-byte.
        messageBytes[2] = (byte) commandBytes[2]; 	// Command Number (Fnc: the function being Get: in this case (02h))
        messageBytes[3] = (byte) commandBytes[3]; 	// Parameter 1: FNC_RELAY = 1 (control the status of a relay)
        messageBytes[4] = (byte) commandBytes[4];   // Parameter 2 (Outp: the relay number)

        // ChkSm: Command Number + Command Parameters + Length + STX
        int checkSumByte = 0;
        for (int commandByte : commandBytes) {
            checkSumByte += commandByte;
            //logger.debug("checkSum item count=" +i +", value:" +commandBytes[i]);
        }
        //logger.debug("checkSum val=" +checkSumByte);
        messageBytes[MSG_VALUE_LENGTH] = (byte) checkSumByte;

        return messageBytes;
    }

    private byte[] composeSetMessage(int function, int number, int state) {
        int MSG_VALUE_STX = 2;
        int MSG_VALUE_LENGTH = 6;
        int MSG_VALUE_COMMAND_SET = 1;
        int[] commandBytes = {MSG_VALUE_STX, MSG_VALUE_LENGTH, MSG_VALUE_COMMAND_SET,function,number,state};
        byte[] messageBytes = new byte[MSG_VALUE_LENGTH+1];

        messageBytes[0] = (byte) commandBytes[0]; 	// STX: is this value always fixed 02h
        messageBytes[1] = (byte) commandBytes[1]; 	// Length: the length of the command, does not include the ChkSm-byte.
        messageBytes[2] = (byte) commandBytes[2]; 	// Command Number (Fnc: the function being Set: in this case (01h))
        messageBytes[3] = (byte) commandBytes[3]; 	// Parameter 1: FNC_RELAY = 1 (control the status of a relay)
        messageBytes[4] = (byte) commandBytes[4];   // Parameter 2 (Outp: the relay number)
        messageBytes[5] = (byte) commandBytes[5]; 	// Parameter 3 (State: on/off = 255/0)

        // ChkSm: Command Number + Command Parameters + Length + STX
        int checkSumByte = 0;
        for (int commandByte : commandBytes) {
            checkSumByte += commandByte;
            //logger.debug("checkSum item count=" +i +", value:" +commandBytes[i]);
        }
        //logger.debug("checkSum val=" +checkSumByte);
        messageBytes[MSG_VALUE_LENGTH] = (byte) checkSumByte;

        return messageBytes;
    }

    /**
     * Prevent cloning.
     * @return Nothing really, because this will always result in an Exception.
     * @throws CloneNotSupportedException when called.
     */
    public TDSClient clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
        // that 'll teach 'em ;)
    }


}