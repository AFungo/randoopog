package examples.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementacion del TAD Pila, usando  una lista simplemente encadenada.
 * Esta clase implementa los métodos abstractos declarados en Pila.
 */
public class PilaSobreListasEnlazadas implements Stack {
	
	
    private Entry top; // usado como la cabeza del a lista enlazada que
	// almacena los elementos de la pila. the linked list 
    
	private int numItems; // indica la cantidad de elementos en la pila.
	
	/** 
	 * Constructor de la clase PilaSobreListasEnlazadas.   
	 * pre. true.
	 * post. Se inicializa la estructura, dejando tope en null
	 * y numItems en 0.  
	 */
    public PilaSobreListasEnlazadas() {
		
        top = null;
		numItems = 0;
    } 
	
	/** 
	 * Indica si la pila es vacía o no.
	 * @return true ssi la pila no tiene elementos
	 * pre. true.
	 * post. Retorna true ssi la pila no tiene elementos
	 */
    public boolean isEmpty() {
		
        return (numItems==0);    
		
    } 
	
	/** 
	 * Retorna la cantidad de elementos de la pila.
	 * @return retorna la cantidad de elementos de la pila.
	 * pre. true.
	 * post. retorna la cantidad de elementos de la pila.
	 */
    public int length() {
		
		return numItems;
		
    } 
	
	/** 
	 * Elimina todos los elementos de la pila.
	 * @pre. true.
	 * @post. elimina todos los elementos de la pila.
	 */	
    public void clean() {
		
        top = null;
		numItems = 0;
		
    } 
	
	/** 
	 * inserta item al tope de la pila de elementos.
	 * @param item es el objeto a insertar en la pila.
	 * pre. true
	 * post. Si la estructura subyacente a la pila no está agotada en espacio,
	 * se inserta item al tope de la pila. Si la inserción falla,por algun motivo
	 *  lanza una excepción de tipo PilaException.
	 */
    public void push(Integer item) throws PilaException {
		
        try {
			Entry nuevoNodo = new Entry();
			nuevoNodo.setInfo(item);
			nuevoNodo.setNext(top);
		
			top = nuevoNodo;
			numItems++;
		}
		catch (Exception err){
            throw new 
            PilaException("PilaSobreListasEnlazadas.apilar: ocurrio un error.");
		}
		
    } 
	
	/** 
	 * elimina el elemento al tope de la pila.
	 * pre. longitud()>0
	 * post. si la pila es no vacía, elimina el elemento del tope
	 *  Si la pila está vacía, lanza una excepción de tipo
	 * PilaException. 
	 */
    public void pop() throws PilaException {
		
        if (top!=null) {
        	top = top.getNext();
			numItems--; 
		}
		else {
			throw new 
			PilaException("PilaSobreListasEnlazadas.desapilar: pila vacia.");
		} 
		
    } 
	
	/** 
	 * retorna el elemento al tope de la pila. 
	 * @return el elemento al tope de la pila.
	 * pre.longitud()>0
	 * post. si la pila es no vacía, se retorna el item del tope.
	 *  Si la pila está vacía, se lanza una excepción de tipo
	 * PilaException. 
	 */
    public Integer top() throws PilaException {
		
        if (top!=null) {
	    	return top.getInfo();
		}
		else {
			throw new 
			PilaException("PilaSobreListasEnlazadas.tope: pila vacia.");
		} 
		
    } 
	
	/**
     * Indica si la representación de la pila es internamente consistente.  
     * @return true si y sólo si la representación de la pila es internamente 
     * consistente.
     * pre. true
     * post. retornar true si y sólo si la representación de la pila es internamente
	 * consistente. En este caso, la estructura es internamente consistente si y
	 * sólo si numItems es exactamente el número de nodos en la lista enlazada, desde 
	 * tope y además la estructura de datos (Lista simplemente encadenada 
	 * sobre la que se implementa)  es aciclica.
     */	
//    public boolean isConsistent(){
//    	//HACER
//        return true;
//
//    }

	private List<Integer> toList(){
		List<Integer> list = new ArrayList<Integer>();
		Entry nodo = top;
		while(nodo!=null){
			list.add(nodo.getInfo());
			nodo = nodo.getNext();
		}
		return list;
	}

	@Override
	public String toString(){
		return toList().toString();
	}

	@Override
	public boolean equals(Object o){
		// If the object is compared with itself then return true
		if (o == this) {
			return true;
		}

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
		if (!(o instanceof PilaSobreListasEnlazadas)) {
			return false;
		}

		// typecast o to Complex so that we can compare data members
		PilaSobreListasEnlazadas c = (PilaSobreListasEnlazadas) o;

		if(length()!=c.length()) return false;

		// Compare the data members and return accordingly
		Entry ttop = top;
		Entry ctop = c.top;

		while(ttop != null){
			if(!Objects.equals(ttop.getInfo(), ctop.getInfo())) return false;
			ttop = ttop.getNext();
			ctop = ctop.getNext();
		}
		return true;
	}

	@Override
	public int hashCode(){
		int hash = 0;
		for(Integer i : toList()){
			hash += Objects.hash(i) * 13;
		}
		return hash;
	}
} 
