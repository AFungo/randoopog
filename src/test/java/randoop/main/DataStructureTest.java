package randoop.main;

import examples.datastructure.PilaSobreListasEnlazadas;
import examples.datastructure.Stack;
import java7.util7.Arrays;
import org.junit.jupiter.api.Test;
import randoop.sequence.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataStructureTest {

    @Test
    public void printObjectAndSequenceTest(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(PilaSobreListasEnlazadas.class);
        rog.setSeed(100);
        for (int i = 0; i < 10; i++) {
            Object o = rog.generateOneObject();
            Set<Sequence> l = rog.getSequences();
            System.out.println("-------------Object------------- \n" + o +
                    "\n-------------Sequence-------------\n" + new ArrayList<>(l).get(l.size()-1)
            );
        }
    }

    @Test
    public void printObjectTest(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(PilaSobreListasEnlazadas.class);
        rog.setSeed(100);
        for (int i = 0; i < 1000; i++) {
            Object o = rog.generateOneObject();
            System.out.println(o);
        }
    }
}
