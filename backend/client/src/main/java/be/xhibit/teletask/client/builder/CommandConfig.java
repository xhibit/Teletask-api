package be.xhibit.teletask.client.builder;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class CommandConfig extends ConfigSupport {
    private final Map<Integer, String> paramNames;
    private final boolean needsCentralUnitParameter;

    public CommandConfig(int number, boolean needsCentralUnitParameter, String... paramNames) {
        super(number);

        ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];
            builder.put(i + 1, paramName);
        }

        this.paramNames = builder.build();
        this.needsCentralUnitParameter = needsCentralUnitParameter;
    }

    public Map<Integer, String> getParamNames() {
        return this.paramNames;
    }

    public boolean needsCentralUnitParameter() {
        return this.needsCentralUnitParameter;
    }
}
