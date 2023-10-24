package randoop.main.randoopflags;

public class TestClassFlag extends RandoopFlag {

    String testClass;//maybe this can be Class<Object>, but i don't know how to include class package,
                    // can i put another attribute for package? I don't look it well

    public TestClassFlag(String testClass){
        this.flagName = "testclass";
        this.testClass = testClass;
    }
    @Override
    protected String flagParameterToString() {
        return testClass;
    }
}
