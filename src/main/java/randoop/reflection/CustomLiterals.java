package randoop.reflection;

import java.math.BigInteger;
import java.util.*;
import randoop.operation.NonreceiverTerm;
import randoop.operation.OperationParseException;
import randoop.operation.TypedOperation;
import randoop.sequence.Sequence;
import randoop.sequence.Variable;
import randoop.types.ClassOrInterfaceType;
import randoop.types.Type;
import randoop.util.MultiMap;

public class CustomLiterals {
  private static final List<String> integerList =
      Arrays.asList(
          "int:-10", "int:-99", "int:-98", "int:-97", "int:-96", "int:-95", "int:-94", "int:-93",
          "int:-92", "int:-91", "int:-90", "int:-89", "int:-88", "int:-87", "int:-86", "int:-85",
          "int:-84", "int:0", "int:1", "int:2", "int:3", "int:4", "int:5", "int:6", "int:7",
          "int:8", "int:9", "int:10", "int:100", "int:200", "int:300", "int:400", "int:500");
  private static final List<String> stringList =
      Arrays.asList(
          "String:\"8080\"",
          "String:\"1234\"",
          "String:\"hi!\"",
          "String:\"hello\"",
          "String:\"bye\"",
          "String:\"randoop\"",
          "String:\"metamorphic relation\"",
          "String:\"spec\"",
          "String:\"property\"",
          "String:\"oracle\"",
          "String:\"apple\"",
          "String:\"banana\"",
          "String:\"cherry\"",
          "String:\"date\"",
          "String:\"grape\"",
          "String:\"kiwi\"",
          "String:\"lemon\"",
          "String:\"melon\"",
          "String:\"orange\"",
          "String:\"peach\"",
          "String:\"pear\"",
          "String:\"plum\"",
          "String:\"strawberry\"",
          "String:\"tomato\"",
          "String:\"watermelon\"",
          "String:\"blueberry\"",
          "String:\"raspberry\"",
          "String:\"blackberry\"",
          "String:\"pomegranate\"",
          "String:\"apricot\"",
          "String:\"mango\"",
          "String:\"pineapple\"",
          "String:\"peanut\"",
          "String:\"cashew\"",
          "String:\"almond\"",
          "String:\"walnut\"",
          "String:\"pecan\"",
          "String:\"hazelnut\"",
          "String:\"pistachio\"",
          "String:\"macadamia\"",
          "String:\"coconut\"",
          "String:\"chestnut\"",
          "String:\"hickory\"",
          "String:\"maple\"",
          "String:\"oak\"",
          "String:\"cedar\"",
          "String:\"pine\"",
          "String:\"fir\"",
          "String:\"cypress\"",
          "String:\"redwood\"",
          "String:\"birch\"",
          "String:\"aspen\"",
          "String:\"willow\"",
          "String:\"poplar\"",
          "String:\"sycamore\"",
          "String:\"elm\"",
          "String:\"ash\"",
          "String:\"sugar\"",
          "String:\"silver\"",
          "String:\"white\"",
          "String:\"red\"",
          "String:\"blue\"",
          "String:\"green\"",
          "String:\"yellow\"",
          "String:\"orange\"",
          "String:\"purple\"",
          "String:\"violet\"",
          "String:\"pink\"",
          "String:\"brown\"",
          "String:\"black\"",
          "String:\"gray\"",
          "String:\"cream\"",
          "String:\"beige\"",
          "String:\"taupe\"",
          "String:\"mauve\"",
          "String:\"teal\"",
          "String:\"indigo\"",
          "String:\"lavender\"",
          "String:\"maroon\"",
          "String:\"navy\"",
          "String:\"olive\"",
          "String:\"turquoise\"");

  private CustomLiterals() {
    throw new Error("Do not instantiate");
  }

  private Set<Class<?>> integerTypes = new HashSet<>();

  public static MultiMap<ClassOrInterfaceType, Sequence> parseIntegerLiterals(
      Set<Integer> integerSet) {
    final MultiMap<ClassOrInterfaceType, Sequence> map = new MultiMap<>();
    for (Integer i : integerSet) {
      if (i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE) {
        TypedOperation operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(byte.class), i.byteValue()));
        map.add(ClassOrInterfaceType.forClass(Byte.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
        operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(Byte.class), i.byteValue()));
        map.add(ClassOrInterfaceType.forClass(Byte.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
      }
      if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
        TypedOperation operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(short.class), i.shortValue()));
        map.add(ClassOrInterfaceType.forClass(Short.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
        operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(Short.class), i.shortValue()));
        map.add(ClassOrInterfaceType.forClass(Short.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
      }
      { // TODO: Maybe we can take BigIntegers as range, so in that case we will have a if for this block
        TypedOperation operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(int.class), i));
        map.add(ClassOrInterfaceType.forClass(Integer.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
        operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(Integer.class), i));
        map.add(ClassOrInterfaceType.forClass(Integer.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
      }
      {
        TypedOperation operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(long.class), i.longValue()));
        map.add(ClassOrInterfaceType.forClass(Long.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
        operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(Long.class), i.longValue()));
        map.add(ClassOrInterfaceType.forClass(Long.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
      }
    }
    return map;
  }

  public static MultiMap<ClassOrInterfaceType, Sequence> parseDoubleLiterals(
      Set<Double> doubleSet) {
    final MultiMap<ClassOrInterfaceType, Sequence> map = new MultiMap<>();
    for (Double i : doubleSet) {
      if ((double)i.floatValue() == i) {
        TypedOperation operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(float.class), i));
        map.add(ClassOrInterfaceType.forClass(Float.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
        operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(Float.class), i));
        map.add(ClassOrInterfaceType.forClass(Float.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
      }
      {
        TypedOperation operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(double.class), i));
        map.add(ClassOrInterfaceType.forClass(Double.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
        operation = TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(Double.class), i));
        map.add(ClassOrInterfaceType.forClass(Double.class),
                new Sequence().extend(operation, new ArrayList<>(0)));
      }
    }
    return map;
  }

  public static MultiMap<ClassOrInterfaceType, Sequence> parseStringLiterals(
      Set<String> stringSet) {
    final MultiMap<ClassOrInterfaceType, Sequence> map = new MultiMap<>();
    try {
      for (String i : stringSet) {
        String str = "String:\"" + i + "\"";
        NonreceiverTerm.parse(str);
        TypedOperation operation =
            TypedOperation.createNonreceiverInitialization(
                new NonreceiverTerm(Type.forClass(String.class), i));
        map.add(
            ClassOrInterfaceType.forClass(String.class),
            new Sequence().extend(operation, new ArrayList<Variable>(0)));
      }
    } catch (OperationParseException e) {
      throw new Error(e);
    }
    return map;
  }

  /**
   * Returns a map from class to list of constants.
   *
   * @return the map from types to literal values
   */
  public static MultiMap<ClassOrInterfaceType, Sequence> loadDefaultLiterals() {
    final MultiMap<ClassOrInterfaceType, Sequence> map = new MultiMap<>();
    try {
      for (String str : stringList) {
        TypedOperation operation = NonreceiverTerm.parse(str);
        map.add(
            ClassOrInterfaceType.forClass(String.class),
            new Sequence().extend(operation, new ArrayList<Variable>(0)));
      }
      for (String str : integerList) {
        TypedOperation operation = NonreceiverTerm.parse(str);
        map.add(
            ClassOrInterfaceType.forClass(Integer.class),
            new Sequence().extend(operation, new ArrayList<Variable>(0)));
      }
    } catch (OperationParseException e) {
      throw new Error(e);
    }
    return map;
  }
}
