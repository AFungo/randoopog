package randoop.main.randoopflags;

import java.util.Objects;

public abstract class RandoopFlag {

    String flagName;
    public String createFlag(){
        return "--" + flagName + "=" + flagParameterToString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj != null && !(obj instanceof RandoopFlag);
    }

    public String getFlagName(){
        return flagName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }

    protected abstract String flagParameterToString();
}
