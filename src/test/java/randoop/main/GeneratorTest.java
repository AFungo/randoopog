package randoop.main;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import randoop.sequence.Sequence;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;

public class GeneratorTest {

    /**
     * Este test es solo para debuggin
     * las clases importantes de ver de como funcionan las cosas son:
     *              RandoopObjectGenerator
     *              AbstractGenerator
     *              FowardGenerator (extiende la definicion del abstract)
     */
    @Test
    public void printObjectAndSequenceTest(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(Stack.class, 100);
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(Date.class);
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(HashMap.class);
        for (int i = 0; i < 10; i++) {
            Object o = rog.generateOneObject();
            Set<Sequence> l = rog.getSequences();
            System.out.println("Object = " + o +
                    "\n Sequence = " + new ArrayList<>(l).get(l.size()-1)
            );
        }
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void objectAmountGenerationTest(int amount, Class<?> c, int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(c, seed);
        assertThat(rog.generateObjects(amount).size(), CoreMatchers.is(amount));
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void noRepeatedObjectGenerationTest(int amount, Class<?> c, int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(c, seed);
        List<Object> list = rog.generateObjects(amount);
        Assertions.assertThat(list).doesNotHaveDuplicates();
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void classMatchWithGeneratedObjectTest(int amount, Class<?> clazz, int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, seed);
        List<Object> list = rog.generateObjects(amount);
        Assertions.assertThat(list).allMatch(clazz::isInstance);
    }


    public static Stream<Arguments> amountAndSeedGenerator() {
        return Stream.of(
                Arguments.of(100, java.util.Stack.class, 1),
                Arguments.of(100, java.util.Stack.class, 10),
                Arguments.of(100, java.util.Stack.class, 99),
                Arguments.of(100, java.util.Stack.class, 20),
                Arguments.of(100, java.util.Stack.class, 47),
                Arguments.of(1, java.util.Stack.class, 100),
                Arguments.of(100, java.util.Stack.class, 100),
                Arguments.of(150, java.util.Stack.class, 100),
                Arguments.of(200, java.util.Stack.class, 100),
                Arguments.of(100, TreeSet.class, 1),
                Arguments.of(100, TreeSet.class, 10),
                Arguments.of(100, TreeSet.class, 99),
                Arguments.of(100, TreeSet.class, 20),
                Arguments.of(100, TreeSet.class, 47),
                Arguments.of(1, TreeSet.class, 100),
                Arguments.of(100, TreeSet.class, 100),
                Arguments.of(150, TreeSet.class, 100),
                Arguments.of(200, TreeSet.class, 100),
                Arguments.of(100, PriorityQueue.class, 1),
                Arguments.of(100, PriorityQueue.class, 10),
                Arguments.of(100, PriorityQueue.class, 99),
                Arguments.of(100, PriorityQueue.class, 20),
                Arguments.of(100, PriorityQueue.class, 47),
                Arguments.of(1, PriorityQueue.class, 100),
                Arguments.of(10, PriorityQueue.class, 100),
                Arguments.of(15, PriorityQueue.class, 100),
                Arguments.of(20, PriorityQueue.class, 100),
                Arguments.of(10, OptionalDouble.class, 1),
                Arguments.of(10, OptionalDouble.class, 10),
                Arguments.of(10, OptionalDouble.class, 99),
                Arguments.of(10, OptionalDouble.class, 20),
                Arguments.of(10, OptionalDouble.class, 47),
                Arguments.of(1, OptionalDouble.class, 100),
                Arguments.of(10, OptionalDouble.class, 100),
                Arguments.of(15, OptionalDouble.class, 100),
                Arguments.of(20, OptionalDouble.class, 100),
                Arguments.of(10, HashMap.class, 1),
                Arguments.of(10, HashMap.class, 10),
                Arguments.of(10, HashMap.class, 99),
                Arguments.of(10, HashMap.class, 20),
                Arguments.of(10, HashMap.class, 47),
                Arguments.of(1, HashMap.class, 100),
                Arguments.of(10, HashMap.class, 100),
                Arguments.of(15, HashMap.class, 100),
                Arguments.of(20, HashMap.class, 100),
                Arguments.of(10, BitSet.class, 1),
                Arguments.of(10, BitSet.class, 10),
                Arguments.of(10, BitSet.class, 99),
                Arguments.of(10, BitSet.class, 20),
                Arguments.of(10, BitSet.class, 47),
                Arguments.of(1, BitSet.class, 100),
                Arguments.of(10, BitSet.class, 100),
                Arguments.of(15, BitSet.class, 100),
                Arguments.of(20, BitSet.class, 100),
                Arguments.of(1000, Date.class, 87),
                Arguments.of(10, Date.class, 10),
                Arguments.of(10, Date.class, 99),
                Arguments.of(10, Date.class, 20),
                Arguments.of(10, Date.class, 47),
                Arguments.of(1, Date.class, 100),
                Arguments.of(10, Date.class, 100),
                Arguments.of(15, Date.class, 100),
                Arguments.of(20, Date.class, 100),
                Arguments.of(100, PriorityQueue.class, 431)
        );
    }
}
