package randoop.main.randoopflags;

public class JDKSpecificationsFlag extends RandoopFlag{

    boolean useJDKSpecifications;

    public JDKSpecificationsFlag(boolean useJDKSpecifications) {
        this.flagName = "use-jdk-specifications";
        this.useJDKSpecifications = useJDKSpecifications;
    }

    @Override
    protected String flagParameterToString() {
        return "" + useJDKSpecifications;
    }
}
