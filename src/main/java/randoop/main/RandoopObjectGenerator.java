package randoop.main;

import org.checkerframework.checker.signature.qual.ClassGetName;
import org.plumelib.options.Options;
import org.plumelib.util.CollectionsPlume;
import org.plumelib.util.UtilPlume;
import randoop.ExecutionVisitor;
import randoop.Globals;
import randoop.MethodReplacements;
import randoop.condition.RandoopSpecificationError;
import randoop.condition.SpecificationCollection;
import randoop.generation.*;
import randoop.instrument.CoveredClassVisitor;
import randoop.main.randoopflags.*;
import randoop.operation.TypedClassOperation;
import randoop.operation.TypedOperation;
import randoop.reflection.*;
import randoop.sequence.*;
import randoop.test.*;
import randoop.types.ClassOrInterfaceType;
import randoop.types.Type;
import randoop.types.TypeVariable;
import randoop.util.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static randoop.reflection.AccessibilityPredicate.IS_PUBLIC;

/**
 * Randoop object generator class
 */
public class RandoopObjectGenerator extends GenTests {

    /**
     * This map stores all randoop flags
     */
    private Map<String, RandoopFlag> randoopFlagMap;

    /**
     * Return type of generate object
     */
    private final Class<?> objectClass;

    /**
     * Map the type variable (T, Q) of @objectClass with the parameterization
     */
    private Map<TypeVariable, Class<?>> parameterizedClasses = new HashMap<>();

    /**
     * Object generator
     */
    private AbstractGenerator explorer;

    /**
     *
     */
    private boolean explorerIsSet = false;

    /**
     * Generators for each class necessary for generate @objectClass Objects
     */
    private final Map<Class<?>, RandoopObjectGenerator> classesGenerators = new HashMap<>();

    /**
     * Set of custom integers for use like parameter in methods of @objectClass
     */
    private Set<Integer> customIntegers = new HashSet<>();

    Integer seed;

    public RandoopObjectGenerator(Class<?> objectClass, int seed){
        super();
        this.objectClass = objectClass;
        this.seed = seed;
        randoopFlagMap = new HashMap<>();
        addFlag(new TestClassFlag(objectClass));
        addFlag(new ForbidNullFlag(true));//Try not use null
        addFlag(new ProgressiveDisplayFlag(false));//no display randoop info
        addFlag(new ProgressIntervalMillis(-1));
        addFlag(new ProgressIntervalSteps(-1));
        addFlag(new RandomSeedFlag(seed));
        addFlag(new OutputLimitFlag(1));
        addFlag(new OmitMethodsFlag("isEmpty|length|size|toList|toString|equals|hash"));
    }

    public RandoopObjectGenerator(Class<?> objectClass, Class<?> parameterizedClass, int seed){
        this(objectClass, Collections.singletonList(parameterizedClass), seed);
    }

    public RandoopObjectGenerator(Class<?> objectClass, List<Class<?>> parameterizedClass, int seed){
        this(objectClass, seed);
        List<TypeVariable> s = ClassOrInterfaceType.forClass(objectClass).getTypeParameters() ;
        if(s.size() < parameterizedClass.size())
            throw new IllegalArgumentException("More parameterized types than parameters");//Note: No se como describirlo!!!!!
        for(Class<?> c : parameterizedClass){
            this.parameterizedClasses.put(s.remove(0), c);
            this.classesGenerators.put(c, new RandoopObjectGenerator(c, seed));
        }
    }

    public void setNecessaryClasses(Set<Class<?>> classes){
        for(Class<?> c : classes){
            this.classesGenerators.put(c, new RandoopObjectGenerator(c, this.seed));
        }
        explorerIsSet = false;
    }
    public void setSeed(int seed){
        addFlag(new RandomSeedFlag(seed));
        explorerIsSet = false;
    }

    public void setRunTime(int seconds){
        addFlag(new TimeLimitFlag(seconds));
        explorerIsSet = false;
    }

//    private void setOutputLimitFlag(int limit){
//        addFlag(new OutputLimitFlag(limit));
//    }

    private String[] flagsToString(){
        return randoopFlagMap.values().stream().map(RandoopFlag::createFlag).toArray(String[]::new);
    }

    public void addFlag(RandoopFlag flag){
        randoopFlagMap.put(flag.getFlagName(), flag);
    }



    public void setCustomIntegers(Set<Integer> customIntegers){
        this.customIntegers = customIntegers;
        explorerIsSet = false;
    }

    private void addCustomIntegersToExplorer(){
        if(!customIntegers.isEmpty()) {
            MultiMap<ClassOrInterfaceType, Sequence> literals = CustomLiterals.parseIntegerLiterals(customIntegers);
            for (ClassOrInterfaceType type : literals.keySet()) {
                for (Sequence seq : literals.getValues(type)) {
                    explorer.componentManager.addClassLevelLiteral(ClassOrInterfaceType.forClass(objectClass), seq);
                }
            }
        }
    }

    public void setOmitMethods(String regex){
        OmitMethodsFlag flag = (OmitMethodsFlag) randoopFlagMap.get("omit-methods");
        flag.extendMethods(regex);
        explorerIsSet = false;
    }

    public void setNewObjectDependencyRatio(double ratio){
        GenInputsAbstract.new_dependency_object_ratio = ratio;
    }
    /**
     * This method setup explorer with all the flags in @randoopFlagMap
    */
    private void setUpGenerator(){

        String[] args = flagsToString();

        System.out.println(Arrays.toString(args));
        try {
            String[] nonargs = options.parse(args);
            if (nonargs.length > 0) {
                throw new Options.ArgException("Unrecognized command-line arguments: " + Arrays.toString(nonargs));
            }
        } catch (Options.ArgException ae) {
            // usage() exits the program by calling System.exit().
            usage("While parsing command-line arguments: %s", ae.getMessage());
        }

        if (GenInputsAbstract.progressdisplay) {
            System.out.println("Randoop for Java version " + Globals.getRandoopVersion() + ".");
        }

        checkOptionsValid();

        Randomness.setSeed(randomseed);

        // java.security.Policy policy = java.security.Policy.getPolicy();

        // This is distracting to the user as the first thing shown, and is not very informative.
        // Reinstate it with a --verbose option.
        // if (GenInputsAbstract.progressdisplay) {
        //   System.out.printf("Using security policy %s%n", policy);
        // }

        // If some properties were specified, set them
        for (String prop : GenInputsAbstract.system_props) {
            String[] pa = prop.split("=", 2);
            if (pa.length != 2) {
                usage("invalid property definition: %s%n", prop);
            }
            System.setProperty(pa[0], pa[1]);
        }

        /*
         * If there is fixture code check that it can be parsed first
         */
        if (!getFixtureCode()) {
            System.exit(1);
        }

        /*
         * Setup model of classes under test
         */

        AccessibilityPredicate accessibility;
        if (GenInputsAbstract.junit_package_name == null) {
            accessibility = IS_PUBLIC;
        } else if (GenInputsAbstract.only_test_public_members) {
            accessibility = IS_PUBLIC;
            if (GenInputsAbstract.junit_package_name != null) {
                System.out.println(
                        "Not using package "
                                + GenInputsAbstract.junit_package_name
                                + " since --only-test-public-members is set");
            }
        } else {
            accessibility =
                    new AccessibilityPredicate.PackageAccessibilityPredicate(
                            GenInputsAbstract.junit_package_name);
        }

        // Get names of classes under test
        Set<@ClassGetName String> classnames = GenInputsAbstract.getClassnamesFromArgs(accessibility);

        // Get names of classes that must be covered by output tests
        Set<@ClassGetName String> coveredClassnames =
                GenInputsAbstract.getClassNamesFromFile(require_covered_classes);

        // Get names of fields to be omitted
        Set<String> omitFields = GenInputsAbstract.getStringSetFromFile(omit_field_file, "fields");
        omitFields.addAll(omit_field);
        // Temporary, for backward compatibility
        omitFields.addAll(GenInputsAbstract.getStringSetFromFile(omit_field_list, "fields"));

        for (Path omitMethodsFile : GenInputsAbstract.omit_methods_file) {
            omit_methods.addAll(readPatterns(omitMethodsFile));
        }

        for (Path omitClassesFile : GenInputsAbstract.omit_classes_file) {
            omit_classes.addAll(readPatterns(omitClassesFile));
        }

        if (!GenInputsAbstract.dont_omit_replaced_methods) {
            omit_methods.addAll(createPatternsFromSignatures(MethodReplacements.getSignatureList()));
        }
        if (!GenInputsAbstract.omit_methods_no_defaults) {
            omit_methods.addAll(readPatternsFromResource("/omitmethods-defaults.txt"));
            omit_methods.addAll(readPatternsFromResource("/JDK-nondet-methods.txt"));
        }

        if (!GenInputsAbstract.omit_classes_no_defaults) {
            String omitClassesDefaultsFileName = "/omit-classes-defaults.txt";
            try (InputStream inputStream =
                         GenTests.class.getResourceAsStream(omitClassesDefaultsFileName)) {
                omit_classes.addAll(readPatterns(inputStream, omitClassesDefaultsFileName));
            } catch (IOException e) {
                throw new RandoopBug(e);
            }
        }

        ReflectionPredicate reflectionPredicate = new DefaultReflectionPredicate(omitFields);

        ClassNameErrorHandler classNameErrorHandler = new ThrowClassNameError();
        if (silently_ignore_bad_class_names) {
            classNameErrorHandler = new WarnOnBadClassName();
        }

        String classpath = Globals.getClassPath();

        /*
         * Setup pre/post/throws-conditions for operations.
         */
        if (GenInputsAbstract.use_jdk_specifications) {
            if (GenInputsAbstract.specifications == null) {
                GenInputsAbstract.specifications = new ArrayList<>(getJDKSpecificationFiles());
            } else {
                GenInputsAbstract.specifications.addAll(getJDKSpecificationFiles());
            }
        }
        OperationModel operationModel = null;
        try (SpecificationCollection operationSpecifications =
                     SpecificationCollection.create(GenInputsAbstract.specifications)) {

            try {
                operationModel =
                        OperationModel.createModel(
                                accessibility,
                                reflectionPredicate,
                                omit_methods,
                                Collections.singleton(this.objectClass.getName()),
                                coveredClassnames,
                                classNameErrorHandler,
                                GenInputsAbstract.literals_file,
                                operationSpecifications);
            } catch (SignatureParseException e) {
                System.out.printf("%nError: parse exception thrown %s%n", e);
                System.out.println("Exiting Randoop.");
                System.exit(1);
            } catch (NoSuchMethodException e) {
                System.out.printf("%nError building operation model: %s%n", e);
                System.out.println("Exiting Randoop.");
                System.exit(1);
            } catch (RandoopClassNameError e) {
                System.out.printf("Class Name Error: %s%n", e.getMessage());
                if (e.getMessage().startsWith("No class with name \"")) {
                    System.out.println("More specifically, none of the following files could be found:");
                    StringTokenizer tokenizer = new StringTokenizer(classpath, File.pathSeparator);
                    while (tokenizer.hasMoreTokens()) {
                        String classPathElt = tokenizer.nextToken();
                        if (classPathElt.endsWith(".jar")) {
                            String classFileName = e.className.replace(".", "/") + ".class";
                            System.out.println("  " + classFileName + " in " + classPathElt);
                        } else {
                            String classFileName = e.className.replace(".", File.separator) + ".class";
                            if (!classPathElt.endsWith(File.separator)) {
                                classPathElt += File.separator;
                            }
                            System.out.println("  " + classPathElt + classFileName);
                        }
                    }
                    System.out.println("Correct your classpath or the class name and re-run Randoop.");
                } else {
                    System.out.println("Problem in OperationModel.createModel().");
                    System.out.println("  accessibility = " + accessibility);
                    System.out.println("  reflectionPredicate = " + reflectionPredicate);
                    System.out.println("  omit_methods = " + omit_methods);
                    System.out.println("  classnames = " + classnames);
                    System.out.println("  coveredClassnames = " + coveredClassnames);
                    System.out.println("  classNameErrorHandler = " + classNameErrorHandler);
                    System.out.println(
                            "  GenInputsAbstract.literals_file = " + GenInputsAbstract.literals_file);
                    System.out.println("  operationSpecifications = " + operationSpecifications);
                    e.printStackTrace(System.out);
                }
                System.exit(1);
            }
        } catch (RandoopSpecificationError e) {
            System.out.printf("Specification Error: %s%n", e.getMessage());
            System.exit(1);
        }
        assert operationModel != null;

        List<TypedOperation> operations = operationModel.getOperations();
        Set<ClassOrInterfaceType> classesUnderTest = operationModel.getClassTypes();

        /*
         * Stop if there is only 1 operation. This will be the Object() constructor.
         */
        if (operations.size() <= 1) {
            System.out.println("You provided no methods to test, so no tests for them can be generated.");
            System.out.println();
            System.out.println("Additional diagnostis appear below.");
            operationModel.dumpModel(System.out);
            System.out.println();
            System.out.println(NO_OPERATIONS_TO_TEST);
            System.exit(1);
        }
        if (GenInputsAbstract.progressdisplay) {
            System.out.println("PUBLIC MEMBERS=" + operations.size());
        }

        /*
         * Initialize components:
         * <ul>
         *   <li>Add default seeds for primitive types
         *   <li>Add any values for TestValue annotated static fields in operationModel
         * </ul>
         */
        Set<Sequence> defaultSeeds = SeedSequences.defaultSeeds();
        Set<Sequence> annotatedTestValues = operationModel.getAnnotatedTestValues();
        Set<Sequence> components =
                new LinkedHashSet<>(
                        CollectionsPlume.mapCapacity(defaultSeeds.size() + annotatedTestValues.size()));
        components.addAll(defaultSeeds);
        components.addAll(annotatedTestValues);

        ComponentManager componentMgr = new ComponentManager(components);
        operationModel.addClassLiterals(
                componentMgr, GenInputsAbstract.literals_file, GenInputsAbstract.literals_level);


        MultiMap<Type, TypedClassOperation> sideEffectFreeMethodsByType = readSideEffectFreeMethods();

        Set<TypedOperation> sideEffectFreeMethods = new LinkedHashSet<>();
        for (Type keyType : sideEffectFreeMethodsByType.keySet()) {
            sideEffectFreeMethods.addAll(sideEffectFreeMethodsByType.getValues(keyType));
        }

        operationModel.log();

        /*
         * Create the generator for this session.
         */
        explorer = !parameterizedClasses.isEmpty()?
                new ForwardGenerator(
                        operations,
                        sideEffectFreeMethods,
                        new GenInputsAbstract.Limits(),
                        componentMgr,
                        /* stopper= */ null,
                        classesUnderTest,
                        this.objectClass,
                        this.parameterizedClasses
                )
                : new ForwardGenerator(
                operations,
                sideEffectFreeMethods,
                new GenInputsAbstract.Limits(),
                componentMgr,
                /* stopper= */ null,
                classesUnderTest,
                this.objectClass
        );

        // log setup.
        operationModel.log();
        if (GenInputsAbstract.operation_history_log != null) {
            TestUtils.setOperationLog(new PrintWriter(GenInputsAbstract.operation_history_log), explorer);
        }
        TestUtils.setSelectionLog(GenInputsAbstract.selection_log);

        // These two debugging lines make runNoOutputTest() fail:
        // operationModel.dumpModel(System.out);
        // System.out.println("isLoggingOn = " + Log.isLoggingOn());

        /*
         * Create the test check generator for the contracts and side-effect-free methods
         */
        ContractSet contracts = operationModel.getContracts();
        TestCheckGenerator testGen =
                createTestCheckGenerator(
                        accessibility,
                        contracts,
                        sideEffectFreeMethodsByType,
                        operationModel.getOmitMethodsPredicate());
        explorer.setTestCheckGenerator(testGen);

        /*
         * Setup for test predicate
         */
        // Always exclude a singleton sequence with just new Object()
        TypedOperation objectConstructor;
        try {
            objectConstructor = TypedOperation.forConstructor(Object.class.getConstructor());
        } catch (NoSuchMethodException e) {
            throw new RandoopBug("failed to get Object constructor", e);
        }

        Sequence newObj = new Sequence().extend(objectConstructor);
        Set<Sequence> excludeSet = new LinkedHashSet<>(CollectionsPlume.mapCapacity(1));
        excludeSet.add(newObj);

        // Define test predicate to decide which test sequences will be output.
        // It returns true if the sequence should be output.
        Predicate<ExecutableSequence> isOutputTest =
                createTestOutputPredicate(
                        excludeSet,
                        operationModel.getCoveredClassesGoal(),
                        GenInputsAbstract.require_classname_in_test);

        explorer.setTestPredicate(isOutputTest);

        /*
         * Setup visitors
         */
        List<ExecutionVisitor> visitors = new ArrayList<>();
        // instrumentation visitor
        if (GenInputsAbstract.require_covered_classes != null) {
            visitors.add(new CoveredClassVisitor(operationModel.getCoveredClassesGoal()));
        }
        // Install any user-specified visitors.
        if (!GenInputsAbstract.visitor.isEmpty()) {
            for (String visitorClsName : GenInputsAbstract.visitor) {
                try {
                    @SuppressWarnings("unchecked")
                    Class<ExecutionVisitor> cls = (Class<ExecutionVisitor>) Class.forName(visitorClsName);
                    ExecutionVisitor vis = cls.getDeclaredConstructor().newInstance();
                    visitors.add(vis);
                } catch (Exception e) {
                    throw new RandoopBug("Error while loading visitor class " + visitorClsName, e);
                }
            }
        }
        explorer.setExecutionVisitor(visitors);

        // Diagnostic output
        if (GenInputsAbstract.progressdisplay) {
            System.out.printf("Explorer = %s%n", explorer);
        }
        // These two debugging lines make runNoOutputTest() fail:
        // operationModel.dumpModel(System.out);
        // System.out.println("isLoggingOn = " + Log.isLoggingOn());
        if (Log.isLoggingOn()) {
            Log.logPrintf("Initial sequences (seeds):%n");
            componentMgr.log();
        }

        addCustomIntegersToExplorer();
        explorer.setClassesGenerator(this.classesGenerators);
        explorerIsSet = true;
    }

    public Object generate(){
        if(!explorerIsSet){
            setUpGenerator();
        }

        return explorer.generateObject();
    }

    public Set<Sequence> getSequences(){
        return explorer.getAllSequences();
    }

    public Sequence getLastSequence(){
        return explorer.getLastSequence();
    }
}

