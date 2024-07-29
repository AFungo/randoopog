package randoop.main;

import examples.datastructure.PilaSobreListasEnlazadas;
import examples.datastructure.PilasTuple;
import org.junit.jupiter.api.Test;
import randoop.sequence.Sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataStructureTest {

    @Test
    public void printObjectAndSequenceTest(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(PilaSobreListasEnlazadas.class, 10);
//        rog.setOmitMethods("isEmpty|length|top|toList|toString|equals|hash");
        for (int i = 0; i < 10; i++) {
            Object o = rog.generate();
            Sequence seq = rog.getLastSequence();
            System.out.println("-------------Object------------- \n" + o +
                    "\n-------------Sequence-------------\n" + seq
            );
        }
    }

    @Test
    public void printObjectTest(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(PilaSobreListasEnlazadas.class, 231);
        rog.setOmitMethods("isEmpty|length|top|toList|toString|equals|hash");
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        for (int i = 0; i < 100; i++) {
            PilaSobreListasEnlazadas o = (PilaSobreListasEnlazadas) rog.generate();
            System.out.println(o + " - size = " +  o.length());
        }
    }

    @Test
    public void pilasTupleTest(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(PilasTuple.class, 231);
        rog.setOmitMethods("toString|equals|hash");
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);

        rog.setNecessaryClasses(Collections.singleton(PilaSobreListasEnlazadas.class));

        for (int i = 0; i < 100; i++) {
            PilasTuple o = (PilasTuple) rog.generate();
            System.out.println("----------\n" + o + "\n" + rog.getLastSequence() + "\n----------\n");
        }
    }
}
