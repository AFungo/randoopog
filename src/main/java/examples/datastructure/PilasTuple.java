package examples.datastructure;

import java.util.Objects;

public class PilasTuple {

    PilaSobreListasEnlazadas stack;
    Integer number;

    public PilasTuple(PilaSobreListasEnlazadas stack, Integer number) {
        this.stack = stack;
        this.number = number;
    }

    public PilaSobreListasEnlazadas getStack(){
        return stack;
    }

    public void setPilas(PilaSobreListasEnlazadas stack) {
        this.stack = stack;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void applyPush(){
        stack.push(number);
    }

    @Override
    public String toString(){
        return "(" + stack.toString() + ", " + number + ")";
    }

    @Override
    public boolean equals(Object o){
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof PilasTuple)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        PilasTuple c = (PilasTuple) o;

        return stack.equals(c.stack) && number.equals(c.number);
    }

    @Override
    public int hashCode(){
        return Objects.hash(number) * 13 + stack.hashCode();
    }
}