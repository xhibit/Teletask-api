package be.xhibit.teletask.model;

import be.xhibit.teletask.api.enums.Function;
import be.xhibit.teletask.api.model.TDSClientConfig;
import be.xhibit.teletask.api.model.TDSComponent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Quick JUnit class for testing the JSON marshal/unmarshal using Jackson.
 */
public class JSONMappingTest {

    @Test
    public void testJSONUnMarshal() throws Exception {
        InputStream jsonData = this.getClass().getClassLoader().getResourceAsStream("tds-config.json");

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //convert json string to object
        TDSClientConfig clientConfig = objectMapper.readValue(jsonData, TDSClientConfig.class);

        assertEquals(clientConfig.getHost(), "192.168.1.150");
        assertEquals(clientConfig.getPort(), 4660);
        assertEquals(clientConfig.getRooms().size(), 19);
        assertEquals(clientConfig.getComponentsTypes().size(), 6);

        TDSComponent component = clientConfig.getComponent(Function.RELAY, 20);
        assertNotNull(component);
        assertNotNull(component.getDescription());
        assertEquals(component.getDescription(), "L34: leefkeuken - LED vals plafond");
        assertEquals(component.getNumber(), 20);
        assertEquals(component.getState(), 0);
        assertEquals(component.getFunction(), Function.RELAY);

    }
}
