package randoop.main.randoopflags;

public class LiteralsLevelFlag extends RandoopFlag{

    String literalLevel;//TODO: this attribute maybe be enum type.

    public LiteralsLevelFlag(String literalLevel){
        this.flagName = "literals-level";
        this.literalLevel = literalLevel;
    }

    @Override
    protected String flagParameterToString() {
        return literalLevel;
    }
}
