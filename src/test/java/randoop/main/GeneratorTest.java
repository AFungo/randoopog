package randoop.main;

import org.junit.Test;
import randoop.main.randoopflags.*;

import java.io.IOException;
import java.util.List;

public class GeneratorTest {
    @Test
    public void test() throws IOException {

        RandoopObjectGenerator rog = new RandoopObjectGenerator();

        rog.addFlag(new TestClassFlag("java.util.Stack"));
        rog.addFlag(new OutputLimitFlag(500));
        rog.addFlag(new LiteralsLevelFlag("ALL"));
        rog.addFlag(new LiteralsFileFlag("/home/augusto/Documents/tesis/randoop/literals/lits.txt"));
        rog.addFlag(new ForbidNullFlag(true));

//        List<Object> list = rog.generateObjects();
//        System.out.println(list);
    }
}
