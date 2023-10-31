package randoop.main.randoopflags;

public class TimeLimitFlag extends RandoopFlag{

    private int timeLimit;

    public TimeLimitFlag(int timeLimit){
        this.flagName = "time-limit";
        this.timeLimit = timeLimit;
    }
    protected String flagParameterToString() {
        return String.valueOf(timeLimit);
    }
}
