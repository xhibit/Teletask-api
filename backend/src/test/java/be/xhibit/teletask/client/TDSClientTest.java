package be.xhibit.teletask.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bruno
 * Date: 7/09/12
 * Time: 16:25
 */
public class TDSClientTest {

    static final Logger logger = LogManager.getLogger(TDSClientTest.class.getName());

    public static void main(String args[]) throws IOException {

        TDSClient client = TDSClient.getInstance();

        //TODO: initially retrieve ALL relays states: must not be a predefined list, but rather a configurable static size.
        // Basically we just want to retrieve ALL relays values.  What type of device is linked doesn't even matter!
        //private static final HashMap<Integer, Integer> relaysMap = null;
    /*static
    {
        relaysMap = new HashMap<Integer, Integer>();
        relaysMap.put(1, 0);	//	L03	:	slaapkamer 1, 1ste deel
        relaysMap.put(2, 0);	//	L04	:	slaapkamer 1, 2de deel
        relaysMap.put(3, 0);	//	L01	:	slaapkamer 2, 1ste deel
        relaysMap.put(4, 0);	//	L02	:	slaapkamer 2, 2de deel
        relaysMap.put(5, 0);	//	L15	:	wc boven
        relaysMap.put(6, 0);	//	L14	:	dressing
        relaysMap.put(7, 0);	//	L19	:	overloop - wandverlichting
        relaysMap.put(8, 0);	//	L31	:	berging - achteraan
        relaysMap.put(9, 0);	//	L09	:	traphal 1ste verdiep aan bureau (spot)
        relaysMap.put(10, 0);	//	L16	:	slaapkamer 1 - centraal
        relaysMap.put(11, 0);	//	L05	:	traphal 2de verdiep
        relaysMap.put(12, 0);	//	L40	:	zolder
        relaysMap.put(13, 0);	//	L24	:	wc beneden
        relaysMap.put(14, 0);	//	L12	:	badkamer - centraal
        relaysMap.put(15, 0);	//	L13	:	badkamer - douche
        relaysMap.put(16, 0);	//	L44	:	zithoek - LED's wanden
        relaysMap.put(17, 0);	//	L42	:	tuin - halogeenspot
        relaysMap.put(18, 0);	//	L43	:	tuin - verlichting (pad/planten)
        relaysMap.put(19, 0);	//	L07	:	bureau - centraal licht
        relaysMap.put(20, 0);	//	L34	:	leefkeuken - LED vals plafond
        //relaysMap.put(21, 0);	//		:	[UNDEFINED]
        relaysMap.put(22, 0);	//	L41	:	overloop - ballustrade
        relaysMap.put(23, 0);	//	L37	:	leefkeuken - boven tafel
        relaysMap.put(24, 0);	//	L29	:	living - luster
        relaysMap.put(25, 0);	//	L20	:	voordeur - spots afdak
        relaysMap.put(26, 0);	//	L21	:	voordeur - wandverlichting
        relaysMap.put(27, 0);	//	L22	:	inkomhal - spots
        relaysMap.put(28, 0);	//	L46	:	terras - LED
        relaysMap.put(29, 0);	//	L27	:	inkomhal - spot voor trap
        relaysMap.put(30, 0);	//	L28	:	living - spots
        relaysMap.put(31, 0);	//	L36	:	leefkeuken - boven eiland
        relaysMap.put(32, 0);	//	L30	:	living - wandverlichting tredes
        relaysMap.put(33, 0);	//	L25	:	berging - vooraan
        relaysMap.put(34, 0);	//	L23	:	inkomhal - wandverlichting
        relaysMap.put(35, 0);	//	L38	:	terras: afdak (gang)
        relaysMap.put(36, 0);	//	L32	:	berging - TL verlichting
        relaysMap.put(37, 0);	//	L11	:	badkamer - lavabo
        relaysMap.put(38, 0);	//	L35	:	leefkeuken - spots aan kasten
        relaysMap.put(39, 0);	//	L06	:	bureau - spots tegen muur
        relaysMap.put(40, 0);	//	L08	:	bureau - spots aan vide
        relaysMap.put(41, 0);	//	L47	:	zithoek - lichtkring 1 (zijde oprit)
        relaysMap.put(42, 0);	//	L48	:	zithoek - lichtkring 2 (zijde berging)
        relaysMap.put(43, 0);	//	L45	:	slaapkamer 1 - LED's hoofdbord
        relaysMap.put(44, 0);	//	L39	:	living - LED's nis
        relaysMap.put(45, 0);	//	L17	:	slaapkamer 1 - wandlicht aan tuinzijde
        relaysMap.put(46, 0);	//	L18	:	slaapkamer 1 - wandlicht aan schuifdeur
        //relaysMap.put(47, 0);	//	L10	:	terras - afdak (zitplaats) [UNDEFINED]
        //relaysMap.put(48, 0);	//	L33	:	leefkeuken - TL nis [UNDEFINED]
    }*/

        /*

            Motor "relay":
            - 0: Projectiescherm
            - 1: Screens Bureau
            - 2: Screen Groot Raam
            - 3: Screen Slaapkamer

         */

        //client.getRelaysStates().toString();
        //client.getInitialComponentStates();

        int testRelay = 38; // light livingroom
        int testMotor = 0; // screen cinema
        int testLocalMood = 8; // mood 'cooking'
        int testGeneralMood = 0; // mood 'leave home'

        pause();

        logger.debug("########## Switching relay on");
        client.switchRelayOn(testRelay);

        pause();

        logger.debug("########## Switching relay off");
        client.switchRelayOff(testRelay);


        //---------------------------------------

        pause();

        logger.debug("########## Switching motor down");
        client.switchMotorDown(testMotor);

        pause();

        logger.debug("########## Switching motor up");
        client.switchMotorUp(testMotor);

        //---------------------------------------

        pause();

        logger.debug("########## Switching local mood on");
        client.switchLocalMoodOn(testLocalMood);

        pause();

        logger.debug("########## Switching local mood off");
        client.switchLocalMoodOff(testLocalMood);

        //---------------------------------------

        pause();

        logger.debug("########## Switching general mood on");
        client.switchGeneralMoodOn(testGeneralMood);

        pause();

        logger.debug("########## Switching general mood off");
        client.switchGeneralMoodOff(testGeneralMood);

        //---------------------------------------

        client.close();


        /*
        NOTES:

        OUTPUT OF THIS TEST

        Successfully Connected
        Writing OPEN LOG bytes: [B@4d3f3045
        Successfully wrote log message!
        Socket data received - toString: [10]

        Writing message with bytes "[2, 6, 1, 1, 38, -1, 47]", socket connected: true, host=192.168.1.150
        Socket data received - toString: [10, 2, 6, 8, 1, 38, -1, 54]
        relayState parsed: 38, -1

        Writing message with bytes "[2, 5, 2, 1, 38, 48, 0]", socket connected: true, host=192.168.1.150
        Socket data received - toString: [10, 2, 6, 8, 1, 38, -1, 54]
        relayState parsed: 38, -1

        Writing message with bytes "[2, 6, 1, 1, 38, 0, 48]", socket connected: true, host=192.168.1.150
        Socket data received - toString: [10, 2, 6, 8, 1, 38, 0, 55]
        relayState parsed: 38, 0,

        Writing message with bytes "[2, 5, 2, 1, 38, 48, 0]", socket connected: true, host=192.168.1.150
        Socket data received - toString: [10, 2, 6, 8, 1, 38, 0, 55]
        relayState parsed: 38, 0,

        Disconnecting from 192.168.1.150
        Socket connected: true

        RESULT FOR GROUP GET:

        RECEIVED socket data: 10, 10, 10, 10, 10, 10, 2, 6, 8, 1, 2, 31, -32, 18, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 2, 6, 8, 1, 2, 0, 19, 2, 6, 8, 1, 4, 0, 21, 2, 6, 8, 1, 6, 0, 23, 2, 6, 8, 1, 8, 0, 25, 2, 6, 8, 1, 10, 0, 27, 2, 6, 8, 1, 12, 0, 29, 2, 6, 8, 1, 14, 0, 31, 2, 6, 8, 1, 17, 0, 34, 2, 6, 8, 1, 19, 0, 36, 2, 6, 8, 1, 20, 0, 3
        RECEIVED socket data: 2, 6, 8, 1, 22, 0, 39, 2, 6, 8, 1, 24, 0, 41, 2, 6, 8, 1, 26, 0, 43, 2, 6, 8, 1, 28, 0, 45, 2, 6, 8, 1, 30, 0, 47, 2, 6, 8, 1, 35, 0, 52, 2, 6, 8, 1, 33, 0, 50, 2, 6, 8, 1, 39, 0, 56, 2, 6, 8, 1, 37, 0, 54, 2, 6, 8, 1, 43, 0, 60, 2, 6, 8, 1, 41, 0, 58, 2, 6, 8, 1, 44, 0, 61



21:20:55.222 [main] DEBUG be.xhibit.teletask.client.TDSClientTest - ########## Switching relay on
21:20:55.729 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 1 of function: relay of component number: 38
21:20:58.222 [main] DEBUG be.xhibit.teletask.client.TDSClientTest - ########## Switching relay off
21:20:58.736 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 0 of function: relay of component number: 38
21:21:01.224 [main] DEBUG be.xhibit.teletask.client.TDSClientTest - ########## Switching motor down
21:21:01.740 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 0 of function: MOTOR of component number: 0
21:21:04.226 [main] DEBUG be.xhibit.teletask.client.TDSClientTest - ########## Switching motor up
21:21:04.746 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 1 of function: MOTOR of component number: 0
21:21:07.226 [main] DEBUG be.xhibit.teletask.client.TDSClientTest - ########## Switching local mood on
21:21:07.751 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 1 of function: relay of component number: 31
21:21:07.751 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 1 of function: relay of component number: 38
21:21:07.751 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 1 of function: LOCMOOD of component number: 8
21:21:10.227 [main] DEBUG be.xhibit.teletask.client.TDSClientTest - ########## Switching local mood off
21:21:10.757 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 0 of function: relay of component number: 31
21:21:10.757 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 0 of function: relay of component number: 38
21:21:10.757 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 0 of function: LOCMOOD of component number: 8
21:21:13.229 [main] DEBUG be.xhibit.teletask.client.TDSClientTest - ########## Switching general mood on
21:21:13.764 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 0 of function: relay of component number: 32
21:21:13.764 [Thread-3] DEBUG be.xhibit.teletask.client.TDSClient - RECEIVED NEW STATE FROM TDS: 0 of function: LOCMOOD of component number: 17
21:21:16.229 [main] DEBUG be.xhibit.teletask.client.TDSClientTest - ########## Switching general mood off

        */


    }

    /**
     * Private helper method for pausing between test commands.
     */
    private static void pause() {
        //Pause for 5 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
