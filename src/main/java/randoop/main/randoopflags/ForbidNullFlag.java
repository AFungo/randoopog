package randoop.main.randoopflags;

public class ForbidNullFlag extends RandoopFlag{

    boolean forbidNull;

    public ForbidNullFlag(boolean forbidNull){
        this.flagName = "forbid-null";
        this.forbidNull = forbidNull;
    }

    @Override
    protected String flagParameterToString() {
        return "" + forbidNull;
    }
}
