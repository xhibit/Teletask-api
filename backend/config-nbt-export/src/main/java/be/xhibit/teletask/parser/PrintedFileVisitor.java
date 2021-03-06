package be.xhibit.teletask.parser;

import be.xhibit.teletask.parser.handler.CentralUnitLineHandler;
import be.xhibit.teletask.parser.handler.ConditionLineHandler;
import be.xhibit.teletask.parser.handler.DimmerLineHandler;
import be.xhibit.teletask.parser.handler.GeneralMoodLineHandler;
import be.xhibit.teletask.parser.handler.InputInterfaceLineHandler;
import be.xhibit.teletask.parser.handler.InputLineHandler;
import be.xhibit.teletask.parser.handler.LineHandler;
import be.xhibit.teletask.parser.handler.LocalMoodLineHandler;
import be.xhibit.teletask.parser.handler.MotorLineHandler;
import be.xhibit.teletask.parser.handler.OutputInterfaceLineHandler;
import be.xhibit.teletask.parser.handler.RelayLineHandler;
import be.xhibit.teletask.parser.handler.RoomLineHandler;
import be.xhibit.teletask.parser.handler.SensorLineHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharSource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintedFileVisitor {
    private static final PrintedFileVisitor INSTANCE = new PrintedFileVisitor();

    private static final Map<Pattern, LineHandler> LINE_HANDLER_MAP = ImmutableMap.<Pattern, LineHandler>builder()
            .put(RoomLineHandler.getInstance().getStartPattern(), RoomLineHandler.getInstance())
            .put(OutputInterfaceLineHandler.getInstance().getStartPattern(), OutputInterfaceLineHandler.getInstance())
            .put(InputInterfaceLineHandler.getInstance().getStartPattern(), InputInterfaceLineHandler.getInstance())
            .put(CentralUnitLineHandler.getInstance().getStartPattern(), CentralUnitLineHandler.getInstance())
            .put(InputLineHandler.getInstance().getStartPattern(), InputLineHandler.getInstance())
            .put(RelayLineHandler.getInstance().getStartPattern(), RelayLineHandler.getInstance())
            .put(LocalMoodLineHandler.getInstance().getStartPattern(), LocalMoodLineHandler.getInstance())
            .put(MotorLineHandler.getInstance().getStartPattern(), MotorLineHandler.getInstance())
            .put(GeneralMoodLineHandler.getInstance().getStartPattern(), GeneralMoodLineHandler.getInstance())
            .put(DimmerLineHandler.getInstance().getStartPattern(), DimmerLineHandler.getInstance())
            .put(ConditionLineHandler.getInstance().getStartPattern(), ConditionLineHandler.getInstance())
            .put(SensorLineHandler.getInstance().getStartPattern(), SensorLineHandler.getInstance())
            .build();

    private PrintedFileVisitor() {
    }

    public void visit(Consumer consumer, final InputStream stream) throws IOException {
        CharSource source = new CharSource() {
            @Override
            public Reader openStream() throws IOException {
                return new BufferedReader(new InputStreamReader(stream));
            }
        };
        ImmutableList<String> lines = source.readLines();
        for (ListIterator<String> iterator = lines.listIterator(); iterator.hasNext(); ) {
            String line = iterator.next();
            LineHandler handler = this.getLineHandler(line);
            if (handler != null) {
                handler.handle(line, consumer, iterator);
            }
        }
    }

    private LineHandler getLineHandler(String line) {
        LineHandler handler = null;
        for (Iterator<Map.Entry<Pattern, LineHandler>> iterator = LINE_HANDLER_MAP.entrySet().iterator(); iterator.hasNext() && handler == null; ) {
            Map.Entry<Pattern, LineHandler> entry = iterator.next();
            Matcher matcher = entry.getKey().matcher(line);
            if (matcher.matches()) {
                handler = entry.getValue();
            }
        }
        return handler;
    }

    public static PrintedFileVisitor getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) throws IOException {
//        FullProprietaryModelConsumerImpl consumer = new FullProprietaryModelConsumerImpl();
        InterestingNbtModelConsumerImpl consumer = new InterestingNbtModelConsumerImpl();
        getInstance().visit(consumer, new FileInputStream("/home/ridiekel/Projects/git/Teletask-api/backend/config-nbt-export/src/main/resources/centrale.ttt"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(System.out, consumer.getCentralUnit());
    }
}
