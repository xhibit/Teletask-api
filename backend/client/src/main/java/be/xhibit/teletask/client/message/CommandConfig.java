package be.xhibit.teletask.client.message;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class CommandConfig {
    private final int number;
    private final Map<Integer, String> paramNames;

    public CommandConfig(int number, String... paramNames) {
        this.number = number;

        ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            builder.put(i + 1, paramName);
        }

        this.paramNames = builder.build();
    }

    public int getNumber() {
        return this.number;
    }

    public Map<Integer, String> getParamNames() {
        return this.paramNames;
    }
}
