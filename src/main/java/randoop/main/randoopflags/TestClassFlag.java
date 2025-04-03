package randoop.main.randoopflags;

public class TestClassFlag extends RandoopFlag {

  private Class<?> testClass;

  public TestClassFlag(Class<?> testClass) {
    this.flagName = "testclass";
    this.testClass = testClass;
  }

  @Override
  protected String flagParameterToString() {
    return testClass.getName();
  }
}
