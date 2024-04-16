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
        List<Class<?>> l = Arrays.asList(Integer.class);
        RandoopObjectGenerator rog = new RandoopObjectGenerator(Stack.class, l);//Poner la clase;
        rog.setSeed(100);//Con la semilla 100 la mayoria son iguales, son [coconut] o []
//        rog.setOutputLimitFlag(10);
        List<Object> list = rog.generateObjects(10);
        printList(list);
//        System.out.println(list);
        assertThat(list.size(), CoreMatchers.is(10));
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void objectAmountGenerationTest(int amount, Class<?> clazz, List<Class<?>> parameterizedClazz,int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, parameterizedClazz);
        rog.setSeed(seed);
        assertThat(rog.generateObjects(amount).size(), CoreMatchers.is(amount));
    }

    @ParameterizedTest
    @MethodSource("amountAndSeedGenerator")
    public void noRepeatedObjectGenerationTest(int amount, Class<?> clazz, List<Class<?>> parameterizedClazz, int seed){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, parameterizedClazz);
        rog.setSeed(seed);
        List<Object> list = rog.generateObjects(amount);
        printList(list);
        Assertions.assertThat(list).doesNotHaveDuplicates();
    }
    public static Stream<Arguments> amountAndSeedGenerator() {
        return Stream.of(
                Arguments.of(100, java.util.Stack.class, Arrays.asList(Date.class), 1),
                Arguments.of(100, java.util.Stack.class, Arrays.asList(Date.class), 10),
                Arguments.of(100, java.util.Stack.class, Arrays.asList(Date.class), 99),
                Arguments.of(100, java.util.Stack.class, Arrays.asList(Integer.class), 20),
                Arguments.of(100, java.util.Stack.class, Arrays.asList(Integer.class), 47),
                Arguments.of(100, java.util.Stack.class, Arrays.asList(String.class), 100),
                Arguments.of(200, java.util.Stack.class, Arrays.asList(String.class), 100),
                Arguments.of(150, java.util.Stack.class, Arrays.asList(Integer.class), 100),
                Arguments.of(50, java.util.Stack.class, Arrays.asList(String.class), 100),
                Arguments.of(100, TreeSet.class, Arrays.asList(Date.class), 1),
                Arguments.of(100, TreeSet.class, Arrays.asList(Date.class), 10),
                Arguments.of(100, TreeSet.class, Arrays.asList(Integer.class), 99),
                Arguments.of(100, TreeSet.class, Arrays.asList(String.class), 20),
                Arguments.of(100, TreeSet.class, Arrays.asList(Integer.class), 47),
                Arguments.of(100, TreeSet.class, Arrays.asList(Date.class), 100),
                Arguments.of(200, TreeSet.class, Arrays.asList(String.class), 100),
                Arguments.of(150, TreeSet.class, Arrays.asList(Integer.class), 100),
                Arguments.of(200, TreeSet.class, Arrays.asList(Date.class), 100),
                Arguments.of(100, PriorityQueue.class, Arrays.asList(Date.class), 1),
                Arguments.of(100, PriorityQueue.class, Arrays.asList(Integer.class), 10),
                Arguments.of(100, PriorityQueue.class, Arrays.asList(String.class), 99),
                Arguments.of(100, PriorityQueue.class, Arrays.asList(Date.class), 20),
                Arguments.of(100, PriorityQueue.class, Arrays.asList(String.class), 47),
                Arguments.of(100, PriorityQueue.class, Arrays.asList(Integer.class), 100),
                Arguments.of(150, PriorityQueue.class, Arrays.asList(Date.class), 100),
                Arguments.of(75, PriorityQueue.class, Arrays.asList(String.class), 100),
                Arguments.of(200, PriorityQueue.class, Arrays.asList(Date.class), 100),
                Arguments.of(100, PriorityQueue.class, Arrays.asList(Date.class), 431),
                Arguments.of(10, HashMap.class,Arrays.asList(Integer.class, Date.class), 1),
                Arguments.of(10, HashMap.class,Arrays.asList(String.class, ArrayList.class), 10),
                Arguments.of(10, HashMap.class,Arrays.asList(Integer.class, String.class), 99),
                Arguments.of(10, HashMap.class,Arrays.asList(Date.class, ArrayList.class), 20),
                Arguments.of(10, HashMap.class,Arrays.asList(String.class, Date.class), 47),
                Arguments.of(1, HashMap.class,Arrays.asList(String.class, String.class), 100),
                Arguments.of(10, HashMap.class,Arrays.asList(Date.class, Date.class), 100),
                Arguments.of(15, HashMap.class,Arrays.asList(List.class, Integer.class), 100),
                Arguments.of(20, HashMap.class,Arrays.asList(Integer.class, Integer.class), 100)
        );
    }
}
