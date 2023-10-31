package randoop.main.randoopflags;

public class ProgressiveDisplayFlag extends RandoopFlag{

    private boolean display;

    public ProgressiveDisplayFlag(boolean display){
        this.flagName = "progressdisplay";
        this.display = display;
    }
    @Override
    protected String flagParameterToString() {
        return String.valueOf(display);
    }
}
