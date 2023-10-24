package randoop.main.randoopflags;

public class LiteralsFileFlag extends RandoopFlag{

    String filePath;

    public LiteralsFileFlag(String filePath){
        this.flagName = "literals-file";
        this.filePath = filePath;
    }

    @Override
    protected String flagParameterToString() {
        return filePath;
    }
}
