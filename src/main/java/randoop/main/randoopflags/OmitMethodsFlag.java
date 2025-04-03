package randoop.main.randoopflags;

public class OmitMethodsFlag extends RandoopFlag {

  String regex;

  public OmitMethodsFlag(String regex) {
    this.flagName = "omit-methods";
    this.regex = regex;
  }

  @Override
  protected String flagParameterToString() {
    return regex;
  }

  public void extendMethods(String regex) {
    this.regex += "|" + regex;
  }
}
