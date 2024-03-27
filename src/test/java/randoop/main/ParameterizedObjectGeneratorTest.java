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
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
public class  ParameterizedObjectGeneratorTest{

    private void printList(List l){
        for (Object o : l){
            System.out.println(o);
        }
    }

    /*
    Pasarle a randoop la clase de la parametriacion en un atributo
     */
    @Test
    public void test() throws IOException {
        Stack<String> s = new Stack<>();
        RandoopObjectGenerator rog = new RandoopObjectGenerator(Stack.class, String.class);//Poner la clase;
        rog.setSeed(1);//Con la semilla 100 la mayoria son iguales, son [coconut] o []
//        rog.setOutputLimitFlag(10);
        List<Object> list = rog.generateObjects(10);
        printList(list);
        //        System.out.println(list);

        assertThat(list.size(), CoreMatchers.is(3));
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void objectAmountGenerationTest(int amount, Class<?> clazz, Class<?> parameterizedClazz,int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, parameterizedClazz);
        rog.setSeed(seed);
        assertThat(rog.generateObjects(amount).size(), CoreMatchers.is(amount));
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void noRepeatedObjectGenerationTest(int amount, Class<?> clazz, Class<?> parameterizedClazz, int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, parameterizedClazz);
        rog.setSeed(seed);
        List<Object> list = rog.generateObjects(amount);
        for (Object o: list) {
            System.out.println(o);
        }
        Assertions.assertThat(list).doesNotHaveDuplicates();
    }
    public static Stream<Arguments> amountAndSeedGenerator() {
        return Stream.of(
                Arguments.of(100, java.util.Stack.class, Date.class, 1),
                Arguments.of(100, java.util.Stack.class, Date.class, 10),
                Arguments.of(100, java.util.Stack.class, Date.class, 99),
                Arguments.of(100, java.util.Stack.class, Integer.class, 20),
                Arguments.of(100, java.util.Stack.class, Integer.class, 47),
                Arguments.of(100, java.util.Stack.class, String.class, 100),
                Arguments.of(200, java.util.Stack.class, String.class, 100),
                Arguments.of(150, java.util.Stack.class, Integer.class, 100),
                Arguments.of(50, java.util.Stack.class, String.class, 100),
                Arguments.of(100, TreeSet.class, Date.class, 1),
                Arguments.of(100, TreeSet.class, Date.class, 10),
                Arguments.of(100, TreeSet.class, Integer.class, 99),
                Arguments.of(100, TreeSet.class, String.class, 20),
                Arguments.of(100, TreeSet.class, Integer.class, 47),
                Arguments.of(100, TreeSet.class, Date.class, 100),
                Arguments.of(200, TreeSet.class, String.class, 100),
                Arguments.of(150, TreeSet.class, Integer.class, 100),
                Arguments.of(200, TreeSet.class, Date.class, 100),
                Arguments.of(100, PriorityQueue.class, Date.class, 1),
                Arguments.of(100, PriorityQueue.class, Integer.class, 10),
                Arguments.of(100, PriorityQueue.class, String.class, 99),
                Arguments.of(100, PriorityQueue.class, Date.class, 20),
                Arguments.of(100, PriorityQueue.class, String.class, 47),
                Arguments.of(100, PriorityQueue.class, Integer.class, 100),
                Arguments.of(150, PriorityQueue.class, Date.class, 100),
                Arguments.of(75, PriorityQueue.class, String.class, 100),
                Arguments.of(200, PriorityQueue.class, Date.class, 100),
                Arguments.of(100, PriorityQueue.class, Date.class, 431)
//                Arguments.of(10, HashMap.class, 1),
//                Arguments.of(10, HashMap.class, 10),
//                Arguments.of(10, HashMap.class, 99),
//                Arguments.of(10, HashMap.class, 20),
//                Arguments.of(10, HashMap.class, 47),
//                Arguments.of(1, HashMap.class, 100),
//                Arguments.of(10, HashMap.class, 100),
//                Arguments.of(15, HashMap.class, 100),
//                Arguments.of(20, HashMap.class, 100),
        );
    }
}
