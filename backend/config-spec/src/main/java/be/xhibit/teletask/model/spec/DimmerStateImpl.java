package be.xhibit.teletask.model.spec;

import java.util.ArrayList;
import java.util.List;

public class DimmerStateImpl implements State {
    public static final List<DimmerStateImpl> DIMMER_STATES = new ArrayList<>();
    static {
        for (int i = 0; i <= 100; i++) {
            DIMMER_STATES.add(new DimmerStateImpl(i));
        }
    }
    private final int percentage;

    private DimmerStateImpl(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String getValue() {
        return String.valueOf(this.percentage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DimmerStateImpl that = (DimmerStateImpl) o;

        if (percentage != that.percentage) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return percentage;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
