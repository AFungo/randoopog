package randoop.main;

import examples.datastructure.PilaSobreListasEnlazadas;
import org.junit.jupiter.api.Test;
import randoop.sequence.Sequence;

import java.util.ArrayList;
import java.util.Set;

public class DataStructureTest {

    @Test
    public void printObjectAndSequenceTest(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(PilaSobreListasEnlazadas.class, 10);
        rog.setOmitMethods("isEmpty|length|top|toList|toString|equals|hash");
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
        RandoopObjectGenerator rog = new RandoopObjectGenerator(PilaSobreListasEnlazadas.class, 231);
        rog.setOmitMethods("isEmpty|length|top|toList|toString|equals|hash");
        rog.setIntegerRange(140, 150);
        for (int i = 0; i < 100; i++) {
            PilaSobreListasEnlazadas o = (PilaSobreListasEnlazadas) rog.generateOneObject();
            System.out.println(o + " - size = " +  o.length());
        }
    }
}
