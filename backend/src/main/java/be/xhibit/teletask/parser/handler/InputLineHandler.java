package be.xhibit.teletask.parser.handler;

import be.xhibit.teletask.parser.Consumer;

import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputLineHandler extends LineHandlerSupport {
    private static final Pattern INTERFACE_PATTERN = Pattern.compile("(\\d)\\s*([^\\s]*)\\s*(.*)");

    private static final InputLineHandler INSTANCE = new InputLineHandler();

    public static final Pattern START_PATTERN = Pattern.compile("\\s*I\\s\\-\\sINTERFACES\\s*(\\d*)\\s*(\\w)([^\\s]*)");

    private InputLineHandler() {
    }

    public static InputLineHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public Pattern getStartPattern() {
        return START_PATTERN;
    }

    @Override
    protected void handle(String startLine, Consumer consumer, ListIterator<String> iterator, String line, int counter) {
        Matcher matcher = this.getStartPattern().matcher(startLine);
        if (matcher.find()) {
            String autobusId = matcher.group(1);
            String autobusType = matcher.group(2);
            String autobusNumber = matcher.group(2);

//            System.out.println("");
//            System.out.println(line);
//            System.out.println(iterator.next());
//            System.out.println(iterator.next());
//            System.out.println("");
        }
    }
}
