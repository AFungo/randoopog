package randoop.main;

import examples.datastructure.PilaSobreListasEnlazadas;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;

public class  ParameterizedObjectGeneratorTest{

    //Tener en cuenta que cuando usamos date parece que genera siempre el mismo objeto pero lo que pasa
    //es que en el toString es igual pero por debajo cambian los milisegundos (suele pasar cuando toma la fecha actual)
    @Test
    public void printObjectAndSequenceTest(){
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(Stack.class, String.class, 100);
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(Stack.class, Integer.class);
        RandoopObjectGenerator rog = new RandoopObjectGenerator(Stack.class, PilaSobreListasEnlazadas.class, 100);
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(HashMap.class, Arrays.asList(Stack.class, String.class));
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(HashMap.class, Arrays.asList(Integer.class, String.class));
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(HashMap.class, Arrays.asList(Integer.class, Date.class));
        for (int i = 0; i < 10; i++) {
            Object o = rog.generate();
            System.out.println("Object = " + o);
//            System.out.println("Object = " + o +
//                    "\n Sequence = " + new ArrayList<>(rog.getSequences()).get(i)
//            );
        }
    }

//    @ParameterizedTest
//    @MethodSource("amountAndSeedGenerator")
//    public void objectAmountGenerationTest(int amount, Class<?> clazz, List<Class<?>> parameterizedClazz,int seed){
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, parameterizedClazz, seed);
//        assertThat(rog.generateObjects(amount).size(), CoreMatchers.is(amount));
//    }

//    @ParameterizedTest
//    @MethodSource("amountAndSeedGenerator")
//    public void noRepeatedObjectGenerationTest(int amount, Class<?> clazz, List<Class<?>> parameterizedClazz, int seed){
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, parameterizedClazz, seed);
//        List<Object> list = rog.generateObjects(amount);
//        Assertions.assertThat(list).doesNotHaveDuplicates();
//    }

//    @ParameterizedTest
//    @MethodSource("amountAndSeedGenerator")
//    public void classMatchWithGeneratedObjectTest(int amount, Class<?> clazz, List<Class<?>> parameterizedClazz, int seed){
//        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, parameterizedClazz, seed);
//        List<Object> list = rog.generateObjects(amount);
//        Assertions.assertThat(list).allMatch(clazz::isInstance);
//    }
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
