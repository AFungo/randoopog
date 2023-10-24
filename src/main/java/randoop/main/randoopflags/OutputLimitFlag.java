package randoop.main.randoopflags;

public class OutputLimitFlag extends RandoopFlag {

    int outputLimit;

    public OutputLimitFlag(int outputLimit){
        this.flagName = "output-limit";
        this.outputLimit = outputLimit;
    }

    @Override
    protected String flagParameterToString() {
        return "" + outputLimit;
    }
}
