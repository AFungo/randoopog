package randoop.main.randoopflags;

public class ProgressIntervalSteps extends RandoopFlag{

    long steps;

    public ProgressIntervalSteps(long steps){
        this.flagName = "progressintervalsteps";
        this.steps = steps;
    }

    @Override
    protected String flagParameterToString() {
        return String.valueOf(steps);
    }
}
