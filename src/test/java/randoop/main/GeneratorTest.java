package randoop.main;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;


public class GeneratorTest {



    @Test
    public void test() throws IOException {
        RandoopObjectGenerator rog = new RandoopObjectGenerator(java.util.Stack.class);//Poner la clase;
        rog.setSeed(100);//Con la semilla 100 la mayoria son iguales, son [coconut] o []
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
        RandoopObjectGenerator rog = new RandoopObjectGenerator(c);//Poner la clase;
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
        Assertions.assertThat(list).doesNotHaveDuplicates();
    }

    public static Stream<Arguments> amountAndSeedGenerator() {
        return Stream.of(
                Arguments.of(10, java.util.Stack.class, 1),
                Arguments.of(10, java.util.Queue.class, 1),
                Arguments.of(10, java.util.Stack.class, 2),
                Arguments.of(10, java.util.Queue.class, 2),
                Arguments.of(10, java.util.Stack.class, 10),
                Arguments.of(10, java.util.Queue.class, 10),
                Arguments.of(10, java.util.Stack.class, 99),
                Arguments.of(10, java.util.Queue.class, 99),
                Arguments.of(10, java.util.Stack.class, 20),
                Arguments.of(10, java.util.Queue.class, 20),
                Arguments.of(10, java.util.Stack.class, 50),
                Arguments.of(10, java.util.Queue.class, 50),
                Arguments.of(10, java.util.Stack.class, 47),
                Arguments.of(10, java.util.Queue.class, 47),
                Arguments.of(1, java.util.Stack.class, 100),
                Arguments.of(2, java.util.Stack.class, 100),
                Arguments.of(3, java.util.Stack.class, 100),
                Arguments.of(4, java.util.Stack.class, 100),
                Arguments.of(5, java.util.Stack.class, 100),
                Arguments.of(10, java.util.Stack.class, 100),
                Arguments.of(15, java.util.Stack.class, 100),
                Arguments.of(20, java.util.Stack.class, 100),
                Arguments.of(1, java.util.Queue.class, 100),
                Arguments.of(2, java.util.Queue.class, 100),
                Arguments.of(3, java.util.Queue.class, 100),
                Arguments.of(4, java.util.Queue.class, 100),
                Arguments.of(5, java.util.Queue.class, 100),
                Arguments.of(10, java.util.Queue.class, 100),
                Arguments.of(15, java.util.Queue.class, 100),
                Arguments.of(20, java.util.Queue.class, 100)
        );
    }
}
