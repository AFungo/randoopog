package randoop.main.randoopflags;

public class RandomSeedFlag extends RandoopFlag {

  int seed;

  public RandomSeedFlag(int seed) {
    this.flagName = "randomseed";
    this.seed = seed;
  }

  @Override
  protected String flagParameterToString() {
    return String.valueOf(seed);
  }
}
