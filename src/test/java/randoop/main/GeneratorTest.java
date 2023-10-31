package randoop.main;

import org.junit.Test;
import randoop.main.randoopflags.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GeneratorTest {
    @Test
    public void test() throws IOException {

        RandoopObjectGenerator rog = new RandoopObjectGenerator(java.util.Stack.class);//Poner la clase;

//        rog.addFlag(new TestClassFlag("java.util.Stack"));
//        rog.addFlag(new OutputLimitFlag(500));
        rog.setSeed(100);

        Set<Object> list = new HashSet<>(rog.generateObjects());
        for (Object o:list) {
            System.out.println(o);
        }
    }
}
