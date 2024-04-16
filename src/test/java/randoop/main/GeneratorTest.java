package randoop.main;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.plumelib.util.SystemPlume;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
public class GeneratorTest {

    private void printList(List l){
        for (Object o : l){
            System.out.println(o);
        }
    }


    @Test
    public void test() throws IOException {
        /*
       pequeño detalle los dates parecen todos iguales pero si te fijas como esta tomando la date actual cambia un numero dentro de la clase
       (supongo que son los milisegundos en la documentacion dice que los incluye en la comparacion)
         */
        RandoopObjectGenerator rog = new RandoopObjectGenerator(Stack.class, Date.class);//Poner la clase;
        rog.setSeed(100);
        List<Object> stacks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Object o = rog.generateOneObject();
            assertThat(stacks.contains(o), CoreMatchers.is(false));
            stacks.add(o);
        }
        printList(stacks);
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void objectAmountGenerationTest(int amount, Class<?> c, int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(c);
        rog.setSeed(seed);
        assertThat(rog.generateObjects(amount).size(), CoreMatchers.is(amount));
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void noRepeatedObjectGenerationTest(int amount, Class<?> c, int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(c);
        rog.setSeed(seed);
        List<Object> list = rog.generateObjects(amount);
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
//                Arguments.of(10, TreeSet.class, 1),
//                Arguments.of(10, TreeSet.class, 10),
//                Arguments.of(10, TreeSet.class, 99),
//                Arguments.of(10, TreeSet.class, 20),
//                Arguments.of(10, TreeSet.class, 47),
//                Arguments.of(1, TreeSet.class, 100),
//                Arguments.of(10, TreeSet.class, 100),
//                Arguments.of(15, TreeSet.class, 100),
//                Arguments.of(20, TreeSet.class, 100),
//                Arguments.of(10, PriorityQueue.class, 1),
//                Arguments.of(10, PriorityQueue.class, 10),
//                Arguments.of(10, PriorityQueue.class, 99),
//                Arguments.of(10, PriorityQueue.class, 20),
//                Arguments.of(10, PriorityQueue.class, 47),
//                Arguments.of(1, PriorityQueue.class, 100),
//                Arguments.of(10, PriorityQueue.class, 100),
//                Arguments.of(15, PriorityQueue.class, 100),
//                Arguments.of(20, PriorityQueue.class, 100),
//                Arguments.of(10, OptionalDouble.class, 1),
//                Arguments.of(10, OptionalDouble.class, 10),
//                Arguments.of(10, OptionalDouble.class, 99),
//                Arguments.of(10, OptionalDouble.class, 20),
//                Arguments.of(10, OptionalDouble.class, 47),
//                Arguments.of(1, OptionalDouble.class, 100),
//                Arguments.of(10, OptionalDouble.class, 100),
//                Arguments.of(15, OptionalDouble.class, 100),
//                Arguments.of(20, OptionalDouble.class, 100),
//                Arguments.of(100, Locale.class, 967),
//                Arguments.of(10, Locale.class, 10),
//                Arguments.of(10, Locale.class, 99),
//                Arguments.of(10, Locale.class, 20),
//                Arguments.of(10, Locale.class, 47),
//                Arguments.of(1, Locale.class, 100),
//                Arguments.of(10, Locale.class, 100),
//                Arguments.of(15, Locale.class, 100),
//                Arguments.of(20, Locale.class, 100),
//                Arguments.of(10, HashMap.class, 1),
//                Arguments.of(10, HashMap.class, 10),
//                Arguments.of(10, HashMap.class, 99),
//                Arguments.of(10, HashMap.class, 20),
//                Arguments.of(10, HashMap.class, 47),
//                Arguments.of(1, HashMap.class, 100),
//                Arguments.of(10, HashMap.class, 100),
//                Arguments.of(15, HashMap.class, 100),
//                Arguments.of(20, HashMap.class, 100),
//                Arguments.of(10, BitSet.class, 1),
//                Arguments.of(10, BitSet.class, 10),
//                Arguments.of(10, BitSet.class, 99),
//                Arguments.of(10, BitSet.class, 20),
//                Arguments.of(10, BitSet.class, 47),
//                Arguments.of(1, BitSet.class, 100),
//                Arguments.of(10, BitSet.class, 100),
//                Arguments.of(15, BitSet.class, 100),
//                Arguments.of(20, BitSet.class, 100),
////[--progressintervalsteps=-1, --output-limit=100, --progressdisplay=false, --forbid-null=true, --randomseed=87, --literals-file=/home/augusto/Documents/tesis/randoopObjectGenerator/literals/lits.txt, --literals-level=ALL, --testclass=java.util.Date, --progressintervalmillis=-1]
////[--progressintervalsteps=-1, --output-limit=100, --progressdisplay=false, --forbid-null=true, --randomseed=87, --literals-file=/home/augusto/Documents/tesis/randoopObjectGenerator/literals/lits.txt, --literals-level=ALL, --testclass=java.util.Date, --progressintervalmillis=-1]
                Arguments.of(100, Date.class, 87),
                Arguments.of(10, Date.class, 10),
                Arguments.of(10, Date.class, 99),
                Arguments.of(10, Date.class, 20),
                Arguments.of(10, Date.class, 47),
                Arguments.of(1, Date.class, 100),
                Arguments.of(10, Date.class, 100),
                Arguments.of(15, Date.class, 100),
                Arguments.of(20, Date.class, 100),
//[--progressintervalsteps=-1, --output-limit=100, --progressdisplay=false, --forbid-null=true, --randomseed=431, --literals-file=/home/augusto/Documents/tesis/randoopObjectGenerator/literals/lits.txt, --literals-level=ALL, --testclass=java.util.PriorityQueue, --progressintervalmillis=-1]
                Arguments.of(100, PriorityQueue.class, 431)
        );
    }
}
