package randoop.main;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;


public class GeneratorTest {



    @Test
    public void test() throws IOException {
        RandoopObjectGenerator rog = new RandoopObjectGenerator(java.util.Stack.class);//Poner la clase;
        rog.setSeed(1);//Con la semilla 100 la mayoria son iguales, son [coconut] o []
        rog.setOutputLimitFlag(10);
        List<Object> list = rog.generateObjects();
        for (Object o: list) {
            System.out.println(o);
        }
        assertThat(list.size(), CoreMatchers.is(10));

    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void objectAmountGenerationTest(int amount, Class<?> c, int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(c);
        rog.setSeed(seed);
        rog.setOutputLimitFlag(amount);
        assertThat(rog.generateObjects().size(), CoreMatchers.is(amount));
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void noRepeatedObjectGenerationTest(int amount, Class<?> c, int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(c);
        rog.setSeed(seed);
        rog.setOutputLimitFlag(amount);
        List<Object> list = rog.generateObjects();
        for (Object o: list) {
            System.out.println(o);
        }
        Assertions.assertThat(list).doesNotHaveDuplicates();
    }

    public static Stream<Arguments> amountAndSeedGenerator() {
        return Stream.of(
                Arguments.of(10, java.util.Stack.class, 1),
                Arguments.of(10, java.util.Stack.class, 10),
                Arguments.of(10, java.util.Stack.class, 99),
                Arguments.of(10, java.util.Stack.class, 20),
                Arguments.of(10, java.util.Stack.class, 47),
                Arguments.of(1, java.util.Stack.class, 100),
                Arguments.of(10, java.util.Stack.class, 100),
                Arguments.of(15, java.util.Stack.class, 100),
                Arguments.of(20, java.util.Stack.class, 100),
                Arguments.of(10, TreeSet.class, 1),
                Arguments.of(10, TreeSet.class, 10),
                Arguments.of(10, TreeSet.class, 99),
                Arguments.of(10, TreeSet.class, 20),
                Arguments.of(10, TreeSet.class, 47),
                Arguments.of(1, TreeSet.class, 100),
                Arguments.of(10, TreeSet.class, 100),
                Arguments.of(15, TreeSet.class, 100),
                Arguments.of(20, TreeSet.class, 100),
                Arguments.of(10, PriorityQueue.class, 1),
                Arguments.of(10, PriorityQueue.class, 10),
                Arguments.of(10, PriorityQueue.class, 99),
                Arguments.of(10, PriorityQueue.class, 20),
                Arguments.of(10, PriorityQueue.class, 47),
                Arguments.of(1, PriorityQueue.class, 100),
                Arguments.of(10, PriorityQueue.class, 100),
                Arguments.of(15, PriorityQueue.class, 100),
                Arguments.of(20, PriorityQueue.class, 100),
//                Arguments.of(10, OptionalDouble.class, 1),
//                Arguments.of(10, OptionalDouble.class, 10),
//                Arguments.of(10, OptionalDouble.class, 99),
//                Arguments.of(10, OptionalDouble.class, 20),
//                Arguments.of(10, OptionalDouble.class, 47),
//                Arguments.of(1, OptionalDouble.class, 100),
//                Arguments.of(10, OptionalDouble.class, 100),
//                Arguments.of(15, OptionalDouble.class, 100),
//                Arguments.of(20, OptionalDouble.class, 100),
                Arguments.of(10, Locale.class, 1),
                Arguments.of(10, Locale.class, 10),
                Arguments.of(10, Locale.class, 99),
                Arguments.of(10, Locale.class, 20),
                Arguments.of(10, Locale.class, 47),
                Arguments.of(1, Locale.class, 100),
                Arguments.of(10, Locale.class, 100),
                Arguments.of(15, Locale.class, 100),
                Arguments.of(20, Locale.class, 100),
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
                Arguments.of(10, Date.class, 1),
                Arguments.of(10, Date.class, 10),
                Arguments.of(10, Date.class, 99),
                Arguments.of(10, Date.class, 20),
                Arguments.of(10, Date.class, 47),
                Arguments.of(1, Date.class, 100),
                Arguments.of(10, Date.class, 100),
                Arguments.of(15, Date.class, 100),
                Arguments.of(20, Date.class, 100)
        );
    }
}
