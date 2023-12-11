package randoop.reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.checkerframework.checker.signature.qual.ClassGetName;
import randoop.Globals;
import randoop.operation.NonreceiverTerm;
import randoop.operation.OperationParseException;
import randoop.operation.TypedOperation;
import randoop.sequence.Sequence;
import randoop.sequence.Variable;
import randoop.types.ClassOrInterfaceType;
import randoop.util.MultiMap;
import randoop.util.RecordListReader;
import randoop.util.RecordProcessor;

/**
 * Method {@link #parse} reads a file specifying literal values to use during generation. The text
 * file should contain one or more records of the form:
 *
 * <pre>
 * START CLASSLITERALS
 * CLASSNAME
 * classname
 * LITERALS
 * type:value
 * ...
 * type:value
 * END CLASSLITERALS
 * </pre>
 *
 * Capitalized text must appear literally. Lowercase text is as follows:
 *
 * <ul>
 *   <li>classname is the name of a class in Class.getName format. More specifically,
 *       TypeNames.getTypeForName(classname) must return a valid Class object.
 *   <li>Each type:value pair describes the type and value of a literal (for example, {@code
 *       int:3}).
 * </ul>
 *
 * Blank lines and comment lines (lines starting with "#") are ignored, both between records and
 * inside records.
 *
 * <p>An example literals file appears in file randoop/systemTests/resources/literalsfile.txt.
 *
 * <p>LIMITATIONS:
 *
 * <p>Error messages do not include line numbers pointing to location of the error. There is no way
 * to specify literals that are not related to any class in particular, or literals that are related
 * to only specific methods within a class.
 */
public class LiteralFileReader {
  private static final List<String> stringList = Arrays.asList(
          "String:\"8080\"",
          "String:\"1234\"",
          "String:\"hi!\"",
          "String:\"hello\"",
          "String:\"bye\"",
          "String:\"randoop\"",
          "String:\"metamorphic relation\"" ,
          "String:\"spec\"" ,
          "String:\"property\"" ,
          "String:\"oracle\"" ,
          "String:\"apple\"" ,
          "String:\"banana\"" ,
          "String:\"cherry\"" ,
          "String:\"date\"" ,
          "String:\"grape\"" ,
          "String:\"kiwi\"" ,
          "String:\"lemon\"" ,
          "String:\"melon\"" ,
          "String:\"orange\"" ,
          "String:\"peach\"" ,
          "String:\"pear\"" ,
          "String:\"plum\"" ,
          "String:\"strawberry\"" ,
          "String:\"tomato\"" ,
          "String:\"watermelon\"" ,
          "String:\"blueberry\"" ,
          "String:\"raspberry\"" ,
          "String:\"blackberry\"" ,
          "String:\"pomegranate\"" ,
          "String:\"apricot\"" ,
          "String:\"mango\"" ,
          "String:\"pineapple\"" ,
          "String:\"peanut\"" ,
          "String:\"cashew\"" ,
          "String:\"almond\"" ,
          "String:\"walnut\"" ,
          "String:\"pecan\"" ,
          "String:\"hazelnut\"" ,
          "String:\"pistachio\"" ,
          "String:\"macadamia\"" ,
          "String:\"coconut\"" ,
          "String:\"chestnut\"" ,
          "String:\"hickory\"" ,
          "String:\"maple\"" ,
          "String:\"oak\"" ,
          "String:\"cedar\"" ,
          "String:\"pine\"" ,
          "String:\"fir\"" ,
          "String:\"cypress\"" ,
          "String:\"redwood\"" ,
          "String:\"birch\"" ,
          "String:\"aspen\"" ,
          "String:\"willow\"" ,
          "String:\"poplar\"" ,
          "String:\"sycamore\"" ,
          "String:\"elm\"" ,
          "String:\"ash\"" ,
          "String:\"sugar\"" ,
          "String:\"silver\"" ,
          "String:\"white\"" ,
          "String:\"red\"" ,
          "String:\"blue\"" ,
          "String:\"green\"" ,
          "String:\"yellow\"" ,
          "String:\"orange\"" ,
          "String:\"purple\"" ,
          "String:\"violet\"" ,
          "String:\"pink\"" ,
          "String:\"brown\"" ,
          "String:\"black\"" ,
          "String:\"gray\"" ,
          "String:\"cream\"" ,
          "String:\"beige\"" ,
          "String:\"taupe\"" ,
          "String:\"mauve\"" ,
          "String:\"teal\"" ,
          "String:\"indigo\"" ,
          "String:\"lavender\"" ,
          "String:\"maroon\"" ,
          "String:\"navy\"" ,
          "String:\"olive\"" ,
          "String:\"turquoise\""
  );

  private LiteralFileReader() {
    throw new Error("Do not instantiate");
  }


  // Add more strings...
  /**
   * Returns a map from class to list of constants.
   *
   * @return the map from types to literal values
   */
  public static MultiMap<ClassOrInterfaceType, Sequence> loadDefaultLiterals() {
    final MultiMap<ClassOrInterfaceType, Sequence> map = new MultiMap<>();
    try {
      for (String str : stringList){
        TypedOperation operation = NonreceiverTerm.parse(str);
        map.add(ClassOrInterfaceType.forClass(String.class), new Sequence().extend(operation, new ArrayList<Variable>(0)));
      }
    }catch (OperationParseException e) {
      throwRecordSyntaxError(e);
    }
    return map;
  }
  /**
   * Returns a map from class to list of constants.
   *
   * @param inFile the input file
   * @return the map from types to literal values
   */
  public static MultiMap<ClassOrInterfaceType, Sequence> parse(String inFile) {

    final MultiMap<ClassOrInterfaceType, Sequence> map = new MultiMap<>();

    RecordProcessor processor =
        new RecordProcessor() {
          @Override
          public void processRecord(List<String> lines) {

            if (!(lines.size() >= 1
                && lines.get(0).trim().toUpperCase(Locale.getDefault()).equals("CLASSNAME"))) {
              throwRecordSyntaxError("record does not begin with \"CLASSNAME\"", lines, 0);
            }

            if (!(lines.size() >= 2)) {
              throwRecordSyntaxError("class name missing", lines, 1);
            }

            Class<?> cls = null;
            try {
              @SuppressWarnings("signature") // reading from file, checked & exception thrown below
              @ClassGetName String className = lines.get(1);
              cls = TypeNames.getTypeForName(className);
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
              throwRecordSyntaxError(e);
            }
            assert cls != null;
            ClassOrInterfaceType classType = ClassOrInterfaceType.forClass(cls);

            if (!(lines.size() >= 3
                && lines.get(2).trim().toUpperCase(Locale.getDefault()).equals("LITERALS"))) {
              throwRecordSyntaxError("Missing field \"LITERALS\"", lines, 2);
            }

            for (int i = 3; i < lines.size(); i++) {
              try {
                TypedOperation operation = NonreceiverTerm.parse(lines.get(i));
                map.add(classType, new Sequence().extend(operation, new ArrayList<Variable>(0)));
              } catch (OperationParseException e) {
                throwRecordSyntaxError(e);
              }
            }
          }
        };

    RecordListReader reader = new RecordListReader("CLASSLITERALS", processor);
    reader.parse(inFile);

    return map;
  }

  /**
   * Throw an error with the given exception as its cause.
   *
   * @param e the cause
   */
  private static void throwRecordSyntaxError(Throwable e) {
    throw new Error(e);
  }

  private static void throwRecordSyntaxError(String string, List<String> lines, int i) {
    StringBuilder b = new StringBuilder();
    b.append("RECORD PROCESSING ERROR: ").append(string).append(Globals.lineSep);
    appendRecord(b, lines, i);
    throw new Error(b.toString());
  }

  private static void appendRecord(StringBuilder b, List<String> lines, int i) {
    // This printout is less than ideal (it does not include the START/END
    // delimiters) and has no line number data, a limitation inherited from
    // RecordProcessor/RecordListReader.
    b.append("INVALID RECORD (error is at index ").append(i).append("):").append(Globals.lineSep);
    b.append("------------------------------").append(Globals.lineSep);
    for (String l : lines) {
      b.append("   ").append(l).append(Globals.lineSep);
    }
    b.append("------------------------------").append(Globals.lineSep);
  }
}
