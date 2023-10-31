package randoop.main.randoopflags;

public class ProgressIntervalMillis extends RandoopFlag{
    long milliseconds;//-1 means no display

    public ProgressIntervalMillis(long milliseconds){
        this.flagName = "progressintervalmillis";
        this.milliseconds = milliseconds;
    }


    @Override
    protected String flagParameterToString() {
        return String.valueOf(milliseconds);
    }
}
