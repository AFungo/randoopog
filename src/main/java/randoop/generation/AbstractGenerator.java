package randoop.generation;

import java.util.*;
import java.util.function.Predicate;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.plumelib.options.Option;
import org.plumelib.options.OptionGroup;
import org.plumelib.options.Unpublicized;
import org.plumelib.util.StringsPlume;
import org.plumelib.util.SystemPlume;
import randoop.DummyVisitor;
import randoop.ExecutionVisitor;
import randoop.MultiVisitor;
import randoop.main.GenInputsAbstract;
import randoop.main.RandoopObjectGenerator;
import randoop.operation.TypedOperation;
import randoop.sequence.ExecutableSequence;
import randoop.sequence.Sequence;
import randoop.sequence.Variable;
import randoop.test.TestCheckGenerator;
import randoop.util.Log;
import randoop.util.ProgressDisplay;
import randoop.util.ReflectionExecutor;
import randoop.util.predicate.AlwaysFalse;

import static randoop.main.GenTests.loadCUTVars;

/**
 * Algorithm template for implementing a test generator.
 *
 * <p>The main generation loop is defined in method {@code createAndClassifySequences()}, which
 * repeatedly generates a new sequence, determines if it a failing sequence, and stops the process
 * when the time or sequence limit expires. The process of generating a new sequences is left
 * abstract.
 *
 * @see ForwardGenerator
 */
public abstract class AbstractGenerator {

  /**
   * If true, dump each sequence to the log file as it is generated. Has no effect unless logging is
   * enabled.
   */
  @OptionGroup(value = "AbstractGenerator unpublicized options", unpublicized = true)
  @Unpublicized
  @Option("Dump each sequence to the log file")
  public static boolean dump_sequences = false;

  /**
   * Number of generation steps (each an attempt to generate and execute a new, distinct sequence).
   */
  public int num_steps = 0;

  /** Number of steps that returned null. */
  public int null_steps = 0;

  /** Number of sequences generated. */
  public int num_sequences_generated = 0;

  /** Number of failing sequences generated. */
  public int num_failing_sequences = 0;

  /** Number of invalid sequences generated. */
  public int invalidSequenceCount = 0;

  /** Number of sequences that failed the output test. */
  public int num_failed_output_test = 0;

  /** When the generator started (millisecond-based system timestamp). */
  private long startTime = -1;

  /** Sequences that are used in other sequences (and are thus redundant) */
  protected Set<Sequence> subsumed_sequences = new LinkedHashSet<>();

  /**
   * Elapsed time since the generator started.
   *
   * @return elapsed time since the generator started
   */
  private long elapsedTime() {
    return System.currentTimeMillis() - startTime;
  }

  /** Limits for generation, after which the generator will stop. */
  public final GenInputsAbstract.Limits limits;

  /**
   * The list of statement kinds (methods, constructors, primitive value declarations, etc.) used to
   * generate sequences. In other words, statements specifies the universe of operations from which
   * sequences are generated.
   */
  protected final List<TypedOperation> operations;

  /** Container for execution visitors used during execution of sequences. */
  protected ExecutionVisitor executionVisitor;

  /** Component manager responsible for storing previously-generated sequences. */
  public ComponentManager componentManager;

  /** Customizable stopping criterion in addition to time and sequence limits. */
  private IStopper stopper;

  /**
   * Updates the progress display message printed to the console. Null if
   * GenInputsAbstrect.progressdisplay is false.
   */
  private ProgressDisplay progressDisplay;

  /**
   * This field is set by Randoop to point to the sequence currently being executed. In the event
   * that Randoop appears to hang, this sequence is printed out to console to help the user debug
   * the cause of the hanging behavior.
   */
  public static Sequence currSeq = null;

  /**
   * The list of error test sequences to be output as JUnit tests. May include subsequences of other
   * sequences in the list.
   */
  public List<ExecutableSequence> outErrorSeqs;

  /**
   * The list of regression sequences to be output as JUnit tests. May include subsequences of other
   * sequences in the list.
   */
  public List<ExecutableSequence> outRegressionSeqs;

  /**
   * A filter to determine whether a sequence should be added to the output sequence lists. Returns
   * true if the sequence should be output.
   */
  public Predicate<ExecutableSequence> outputTest;

  /** Visitor to generate checks for a sequence. */
  protected TestCheckGenerator checkGenerator;

  protected OperationHistoryLogInterface operationHistory;

  /**
   *The list of all generated objects from the execution of the new sequences
   */
  protected final List<Object> allObjects = new LinkedList<>();

  /**
   * Amount of objects we generate in each run
   */
  protected int objectsAmount = 0;

  /**
   * This attribute have the class which  the user want to generate objects
   */
  protected Class<?> objectsClass;

  /**
   * This attribute stores all RandoopObjectGenerator for each class we have to generate sequence
   */
  protected Map<Class<?>, RandoopObjectGenerator> classesGenerators = new HashMap<>();

  /**
   * Constructs a generator with the given parameters.
   *
   * @param operations statements (e.g. methods and constructors) used to create sequences. Cannot
   *     be null.
   * @param limits maximum time and number of sequences to generate/output
   * @param componentManager the component manager to use to store sequences during component-based
   *     generation. Can be null, in which case the generator's component manager is initialized as
   *     {@code new ComponentManager()}.
   * @param stopper optional, additional stopping criterion for the generator. Can be null.
   */
  protected AbstractGenerator(
      List<TypedOperation> operations,
      GenInputsAbstract.Limits limits,
      ComponentManager componentManager,
      IStopper stopper) {
    assert operations != null;

    this.limits = limits;
    this.operations = operations;
    this.executionVisitor = new DummyVisitor();
    this.outputTest = new AlwaysFalse<>();

    if (componentManager == null) {
      this.componentManager = new ComponentManager();
    } else {
      this.componentManager = componentManager;
    }

    this.stopper = stopper;
    operationHistory = new DefaultOperationHistoryLogger();
    outRegressionSeqs = new ArrayList<>();
    outErrorSeqs = new ArrayList<>();
  }

  /**
   * Registers test predicate with this generator for use while filtering generated tests for
   * output.
   *
   * @param outputTest the predicate to be added to object
   */
  public void setTestPredicate(Predicate<ExecutableSequence> outputTest) {
    if (outputTest == null) {
      throw new IllegalArgumentException("outputTest must be non-null");
    }
    this.outputTest = outputTest;
  }

  /**
   * Registers a visitor with this object for use while executing each generated sequence.
   *
   * @param executionVisitor the visitor
   */
  public void setExecutionVisitor(ExecutionVisitor executionVisitor) {
    if (executionVisitor == null) {
      throw new IllegalArgumentException("executionVisitor must be non-null");
    }
    this.executionVisitor = executionVisitor;
  }

  /**
   * Registers a MultiVisitor of all the given visitors with this object for use while executing
   * each generated sequence.
   *
   * @param visitors the list of visitors
   */
  public void setExecutionVisitor(List<ExecutionVisitor> visitors) {
    this.executionVisitor = MultiVisitor.createMultiVisitor(visitors);
  }

  /**
   * Registers a visitor with this object to generate checks following execution of each generated
   * test sequence.
   *
   * @param checkGenerator the check generating visitor
   */
  public void setTestCheckGenerator(TestCheckGenerator checkGenerator) {
    if (checkGenerator == null) {
      throw new IllegalArgumentException("checkGenerator must be non-null");
    }
    this.checkGenerator = checkGenerator;
  }

  /**
   * Tests stopping criteria.
   *
   * @return true iff any stopping criterion is met
   */
  protected boolean shouldStop() {
    return (limits.time_limit_millis != 0 && elapsedTime() >= limits.time_limit_millis) //Este lo dejo? corta segun el tiempo transcurrido
          || (numAttemptedSequences() >= limits.attempted_limit)
          || (numGeneratedSequences() >= limits.generated_limit)
          || (numObjectsGenerated() >= limits.output_limit)//este metodo antes retornaba el tamaño del set que guarda los objetos ahora retorna la cantidad generado en la corrida parcial
//        || (numOutputSequences() >= limits.output_limit) //that is the oiginal condition
          || (GenInputsAbstract.stop_on_error_test && numErrorSequences() > 0)
          || (stopper != null && stopper.shouldStop());
  }

  /**
   * Attempt to generate a test (a sequence).
   *
   * @return a test sequence, may be null
   */
  public abstract @Nullable ExecutableSequence step();

  /**
   * Returns the count of attempts to generate a sequence so far.
   *
   * @return the number of attempts to generate a sequence so far
   */
  public int numAttemptedSequences() {
    return num_steps;
  }

  /**
   * Returns the count of sequences generated so far by the generator.
   *
   * @return the number of sequences generated
   */
  public abstract int numGeneratedSequences();

  /**
   * Returns the count of generated sequence currently for output.
   *
   * @return the sum of the number of error and regression test sequences for output
   */
  public int numOutputSequences() {
    return outRegressionSeqs.size();
  }

  /**
   * Returns the count of generated objects currently for output.
   *
   * @return the number of generated objects for output
   */
  public int numObjectsGenerated(){
    return this.objectsAmount;//this.allObjects.size();
  }

  /**
   * Returns the count of generated error-revealing sequences.
   *
   * @return the number of error test sequences
   */
  private int numErrorSequences() {
    return outErrorSeqs.size();
  }

  /**
   * Creates and executes new sequences until stopping criteria is met.
   *
   * @see AbstractGenerator#shouldStop()
   * @see AbstractGenerator#step()
   */
  public void createAndClassifySequences() {
    if (checkGenerator == null) {
      throw new Error("Generator not properly initialized - must have a TestCheckGenerator");
    }

    startTime = System.currentTimeMillis();

    if (GenInputsAbstract.progressdisplay) {
      progressDisplay = new ProgressDisplay(this, ProgressDisplay.Mode.MULTILINE);
      progressDisplay.start();
    }

    //Here we start to count the amount of generated objects
    this.objectsAmount = 0;
    while (!shouldStop()) {

      num_steps++;

      ExecutableSequence eSeq = step();

      if (dump_sequences) {
        Log.logPrintf("%nseq before run:%n%s%n", eSeq);
      }

      if (GenInputsAbstract.progressdisplay
          && GenInputsAbstract.progressintervalsteps != -1
          && num_steps % GenInputsAbstract.progressintervalsteps == 0) {
        progressDisplay.display(!GenInputsAbstract.deterministic);
      }

      if (eSeq == null) {
        null_steps++;
        continue;
      }

      num_sequences_generated++;

      boolean test;
      try {
        test = outputTest.test(eSeq);
      } catch (Throwable t) {
//        System.out.printf(
//            "%nProblem with sequence:%n%s%n%s%n", eSeq, UtilPlume.stackTraceToString(t));
//        throw t;
        //NOTE: I comment this, the principal reason is i have something like aliasing between test and use old objects from others test in the curren for example use locale object when i try to test HashMap
        //FIXME: When we can remove old objects this exception dont give any more troubles
        continue;
      }
      if (test) {
        // Classify the sequence
        if (eSeq.hasInvalidBehavior()) {
          invalidSequenceCount++;
        } else if (eSeq.hasFailure()) {
          /**
           * NOTE: aca es cuando se fija si el check rep fallo, lo agrega a las failing sequences
           * por lo que no va a ser elegido para generar una proxima secuencia
           */
          operationHistory.add(eSeq.getOperation(), OperationOutcome.ERROR_SEQUENCE);
          num_failing_sequences++;
          outErrorSeqs.add(eSeq);
        } else {
          //NOTE: para mi va aca el crear un nuevo objeto
          buildAndSaveNewObject(eSeq);
          outRegressionSeqs.add(eSeq);
          newRegressionTestHook(eSeq.sequence);
        }
      } else {
        num_failed_output_test++;
      }

      if (dump_sequences) {
        Log.logPrintf("Sequence after execution:%n%s%n", eSeq);
        Log.logPrintf("allSequences.size()=%s%n", numGeneratedSequences());
        // componentManager.log();
      }
    }

    if (GenInputsAbstract.progressdisplay && progressDisplay != null) {
      progressDisplay.display(!GenInputsAbstract.deterministic);
      progressDisplay.shouldStop = true;
    }

    if (GenInputsAbstract.progressdisplay) {
      System.out.println();
      System.out.println("Normal method executions: " + ReflectionExecutor.normalExecs());
      System.out.println("Exceptional method executions: " + ReflectionExecutor.excepExecs());
      if (!GenInputsAbstract.deterministic) {
        System.out.println();
        System.out.println(
            "Average method execution time (normal termination):      "
                + String.format("%.3g", ReflectionExecutor.normalExecAvgMillis()));
        System.out.println(
            "Average method execution time (exceptional termination): "
                + String.format("%.3g", ReflectionExecutor.excepExecAvgMillis()));
        System.out.println(
            "Approximate memory usage "
                + StringsPlume.abbreviateNumber(SystemPlume.usedMemory(false)));
      }
      System.out.println("Explorer = " + this);
    }
  }

  /**
   * This method get a sequence and try to build a new object of the looking type if you have success save it
   * @param sequence sequence for build the object
   */
  private void buildAndSaveNewObject(ExecutableSequence sequence){
//    ExecutableSequence e = new ExecutableSequence(sequence);
    Variable var = loadCUTVars(this.objectsClass, sequence);
    if (var != null) {//no se pq si cambio de orden estos ifs se cuelga para siempre
      //Check if the sequence has an exception
      if(sequence.isNormalExecution() && !sequence.hasFailure() && !sequence.hasInvalidBehavior()) {
        Object newObject = ExecutableSequence.getRuntimeValuesForVars(
                Collections.singletonList(var), sequence.executionResults)[0];
        if (!this.allObjects.contains(newObject)) {
          this.allObjects.add(newObject);
          this.objectsAmount++;
        }
      }
    }
  }
  
  /**
   * Return all sequences generated by this object.
   *
   * @return all generated sequences
   */
  public abstract Set<Sequence> getAllSequences();

  /**
   * Returns the generated regression test sequences for output. Filters out subsequences.
   *
   * @return regression test sequences that do not occur in a longer sequence
   */
  // TODO replace this with filtering during generation
  public List<ExecutableSequence> getRegressionSequences() {
    List<ExecutableSequence> unique_seqs = new ArrayList<>(outRegressionSeqs.size());
    subsumed_sequences = new LinkedHashSet<Sequence>();
    for (ExecutableSequence es : outRegressionSeqs) {
      subsumed_sequences.addAll(es.componentSequences);
    }
    for (ExecutableSequence es : outRegressionSeqs) {
      if (subsumed_sequences.contains(es.sequence)) {
        operationHistory.add(es.getOperation(), OperationOutcome.SUBSUMED);
      } else {
        operationHistory.add(es.getOperation(), OperationOutcome.REGRESSION_SEQUENCE);
        //modificado por mi aca entiendo que chequea si la secuancia generada da un onjeto de la clase stack.class.
        //Fixme: ESta hardcodeada bien fea hay que mejorarla
//        Variable var = loadCUTVars(Stack.class, es);;
//        if (var != null) {
          unique_seqs.add(es);
//        }
      }
    }
    return unique_seqs;
  }

  /**
   * Returns the generated error-revealing test sequences for output.
   *
   * @return the generated error test sequences
   */
  public List<ExecutableSequence> getErrorTestSequences() {
    return outErrorSeqs;
  }

  /**
   * Returns the total number of test sequences generated to output, including both regression tests
   * and error-revealing tests.
   *
   * @return the total number of test sequences saved for output
   */
  public int outputSequenceCount() {
    return outRegressionSeqs.size() + outErrorSeqs.size();
  }

  /**
   * Sets the current sequence during exploration.
   *
   * @param s the current sequence
   */
  void setCurrentSequence(Sequence s) {
    currSeq = s;
  }

  /**
   * Sets the operation history logger for this generator.
   *
   * @param logger the operation history logger to use for this generator
   */
  public void setOperationHistoryLogger(OperationHistoryLogInterface logger) {
    operationHistory = logger;
  }

  /**
   * Return the operation history logger for this generator.
   *
   * @return the operation history logger for this generator
   */
  public OperationHistoryLogInterface getOperationHistory() {
    return operationHistory;
  }

  /**
   * Take action based on the given {@link Sequence} that was classified as a regression test, i.e.,
   * normal behavior.
   *
   * @param sequence the new test sequence that was classified as a regression test, i.e., normal
   *     behavior
   */
  public abstract void newRegressionTestHook(Sequence sequence);

  /**
   *
   * @return all objects generated
   */
  public abstract List<Object> getAllObjects();
  
  public void setClassesGenerator(Map<Class<?>, RandoopObjectGenerator> classesGenerators) {
    this.classesGenerators = classesGenerators;
  }

}
