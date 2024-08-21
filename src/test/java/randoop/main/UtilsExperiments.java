package randoop.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class UtilsExperiments {

    Set<String> stringSet = new HashSet<>(Arrays.asList(
            "8080", "1234", "hi!", "hello", "bye", "randoop", "metamorphic relation", "spec", "property",
            "oracle", "apple", "banana", "cherry", "date", "grape", "kiwi"));

    public static boolean isSorted(Object l){
        ArrayList<Integer> list = (ArrayList<Integer>) l;
        if(list.size() < 3) return false;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void sortedGeneration(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(ArrayList.class, Integer.class,231);
        rog.setAssume(UtilsExperiments::isSorted);
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        //TODO: hacer una prueba sin los addall
        rog.setOmitMethods("clear|clone|contains|ensureCapacity|get|indexOf|isEmpty|iterator|lastIndexOf|remove|size|toArray|listIterator|removeAll|removeIf|removeRange|replaceAll|retainAll|subList|spliterator|trimToSize");
        for (int i = 0; i < 10; i++) {//5 minutos
            ArrayList<Integer> list = (ArrayList<Integer>) rog.generate();
            System.out.println("----------\n" + list + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }

    public static boolean isKeyPresent(Object o){
        HashMap<Integer, String> map = (HashMap<Integer, String>) o;
        return map.containsKey(115) && map.size() > 3;
    }

    @Test
    public void containsKey(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(HashMap.class, List.of(Integer.class, String.class),231);
        rog.setAssume(UtilsExperiments::isKeyPresent);
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setCustomStrings(stringSet);
        rog.setOmitMethods("clear|clone|containsKey|containsValue|compute|computeIfAbsent|computeIfPresent|entrySet|forEach|get|getOrDefault|isEmpty|keySet|" +
                "merge|remove|replaceAll|replace|size|values");

        for (int i = 0; i < 10; i++) {// 1 minuto
            HashMap<Integer, String> map = (HashMap<Integer, String>) rog.generate();
            System.out.println("----------\n" + map + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }

    /*
     * Constraint: Ensure that the map contains at least one key-value pair where the key and value are the same.
     */
    public static boolean containsSelfMapping(Object o) {
        HashMap<Integer, Integer> map = (HashMap<Integer, Integer>) o;

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void containsSelfMapping(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(HashMap.class, List.of(Integer.class, Integer.class),231);
        rog.setAssume(UtilsExperiments::containsSelfMapping);
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setOmitMethods("clear|clone|containsKey|containsValue|compute|computeIfAbsent|computeIfPresent|entrySet|forEach|get|getOrDefault|isEmpty|keySet|" +
                "merge|remove|replaceAll|replace|size|values");
        for (int i = 0; i < 10; i++) {//45 seg
            HashMap<Integer, Integer> map = (HashMap<Integer, Integer>) rog.generate();
            System.out.println("----------\n" + map + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }


    /*
     * Constraint: Ensure the map contains the key 115
     */
    public static boolean containsKey(Object o) {
        HashMap<Integer, Integer> map = (HashMap<Integer, Integer>) o;

        return map.containsKey(115) && map.size() > 3;
    }

    @Test
    public void mapContainsKey(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(HashMap.class, List.of(Integer.class, Integer.class),231);
        rog.setAssume(UtilsExperiments::containsKey);
        Set<Integer> integers = IntStream.range(100, 200).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setOmitMethods("clear|clone|containsKey|containsValue|compute|computeIfAbsent|computeIfPresent|entrySet|forEach|get|getOrDefault|isEmpty|keySet|" +
                "merge|remove|replaceAll|replace|size|values");
        for (int i = 0; i < 10; i++) {
            HashMap<Integer, String> map = (HashMap<Integer, String>) rog.generate();
            System.out.println("----------\n" + map + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }


    /*
    Constraint: Ensure that the set contains at least three unique elements.
     */
    public static boolean hasMinimumSize(Object o) {
        HashSet<Integer> set = (HashSet<Integer>) o;
        return set.size() >= 3;
    }

    @Test
    public void minimumSetSize(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(HashSet.class, Integer.class,231);
        rog.setAssume(UtilsExperiments::hasMinimumSize);
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setOmitMethods("clear|clone|contains|isEmpty|iterator|remove|size|spliterator");
        for (int i = 0; i < 10; i++) {
            HashSet<Integer> set = (HashSet<Integer>) rog.generate();
            System.out.println("----------\n" + set + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }

    /*
    Constraint: All odds elements
    */
    public static boolean allOddElemets(Object o) {
        HashSet<Integer> set = (HashSet<Integer>) o;
        return set.size() >= 2 && set.stream().allMatch(i -> i % 2 != 0);
    }

    @Test
    public void oddSet(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(HashSet.class, Integer.class,231);
        rog.setAssume(UtilsExperiments::allOddElemets);
        Set<Integer> integers = IntStream.range(110, 130).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setOmitMethods("clear|clone|contains|isEmpty|iterator|remove|size|spliterator");
        for (int i = 0; i < 10; i++) {//7 minutos
            HashSet<Integer> set = (HashSet<Integer>) rog.generate();
            System.out.println("----------\n" + set + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }

    /*
    Constraint: Ensure that the list contains an odd number of elements.
     */
    public static boolean hasOddSize(Object o) {
        LinkedList<Integer> list = (LinkedList<Integer>) o;
        return list.size() % 2 != 0;
    }

    @Test
    public void linkedListHasOddSize(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(LinkedList.class, Integer.class,231);
        rog.setAssume(UtilsExperiments::hasOddSize);
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setOmitMethods("clear|clone|contains|descendingIterator|element|get|getFirst|getLast|indexOf|lastIndexOf|listIterator|" +
                "peek|peekFirst|peekLast|poll|pollFirst|pollLast|pop|remove|removeFirst|removeFirstOccurrence|removeLast|removeLastOccurrence" +
                "|size|spliterator|toArray");
        for (int i = 0; i < 10; i++) {
            LinkedList<Integer> list = (LinkedList<Integer>) rog.generate();
            System.out.println("----------\n" + list + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }

    /*
     Constraint: The LinkedList should have more than five elements, and should have the same first and last element.
     */
    public static boolean isCircularLinkedList(Object o) {
        LinkedList<Integer> list = (LinkedList<Integer>) o;
        return list.size() > 5 &&
                list.getFirst().equals(list.getLast()) &&
                list.stream().noneMatch(Objects::isNull);
    }

    @Test
    public void circularLinkedList(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(LinkedList.class, Integer.class,231);
        rog.setAssume(UtilsExperiments::isCircularLinkedList);
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setOmitMethods("clear|clone|contains|descendingIterator|element|get|getFirst|getLast|indexOf|lastIndexOf|listIterator|" +
                "peek|peekFirst|peekLast|poll|pollFirst|pollLast|pop|remove|removeFirst|removeFirstOccurrence|removeLast|removeLastOccurrence" +
                "|size|spliterator|toArray");
        for (int i = 0; i < 10; i++) {
            LinkedList<Integer> list = (LinkedList<Integer>) rog.generate();
            System.out.println("----------\n" + list + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }

    /*
    Constraint: Ensure that the queue's first element (head) is the smallest element.
     */
    public static boolean isHeadSmallest(Object o) {
        PriorityQueue<Integer> queue = (PriorityQueue<Integer>) o;
        return !queue.isEmpty() && queue.peek().equals(Collections.min(queue));
    }

    @Test
    public void headSmallestPriorityQueue(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(PriorityQueue.class, Integer.class,231);
        rog.setAssume(UtilsExperiments::isHeadSmallest);
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setOmitMethods("clear|comparator|contains|iterator|" +
                "peek|poll|remove|size|spliterator|toArray");
        for (int i = 0; i < 10; i++) {//20 sec
            PriorityQueue<Integer> list = (PriorityQueue<Integer>) rog.generate();
            System.out.println("----------\n" + list + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }

    /*
    Constraint: The PriorityQueue should contain odd numbers and should have a minimum size of five elements.
                Additionally, the queue should maintain a max heap property.
     */
    public static boolean isValidPriorityQueue(Object o) {
        PriorityQueue<Integer> queue = (PriorityQueue<Integer>) o;
        return queue.size() >= 3 &&
                queue.stream().allMatch(i -> i % 2 != 0) &&
                isMaxHeap(queue);
    }

    /*
    maintaining the max heap property means that the largest element is always at the root (or top) of the heap,
    and each parent node is greater than or equal to its children.
     */
    private static boolean isMaxHeap(PriorityQueue<Integer> queue) {
        Integer[] arr = queue.toArray(new Integer[0]);
        for (int i = 0; i < arr.length / 2; i++) {
            if (2 * i + 1 < arr.length && arr[i] < arr[2 * i + 1]) return false;
            if (2 * i + 2 < arr.length && arr[i] < arr[2 * i + 2]) return false;
        }
        return true;
    }

    @Test
    public void validPriorityQueue(){
        RandoopObjectGenerator rog = new RandoopObjectGenerator(PriorityQueue.class, Integer.class,231);
        rog.setAssume(UtilsExperiments::isValidPriorityQueue);
        Set<Integer> integers = IntStream.range(110, 130).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setOmitMethods("clear|comparator|contains|iterator|" +
                "peek|poll|remove|size|spliterator|toArray");
        for (int i = 0; i < 10; i++) {
            PriorityQueue<Integer> list = (PriorityQueue<Integer>) rog.generate();
            System.out.println("----------\n" + list + "\n"
                    + rog.getLastSequence() + "\n----------\n");
        }
    }

    public static Stream<Arguments> generationSource() {
        return Stream.of(
                Arguments.of(ArrayList.class, List.of(Integer.class),
                        (Function<Object, Boolean>) UtilsExperiments::isSorted, "clear|clone|contains|ensureCapacity|get|indexOf|isEmpty|iterator|lastIndexOf|remove|size|toArray|listIterator|removeAll|removeIf|removeRange|replaceAll|retainAll|subList|spliterator|trimToSize"),
                Arguments.of(HashMap.class, List.of(Integer.class, String.class),
                        (Function<Object, Boolean>) UtilsExperiments::isKeyPresent, "clear|clone|containsKey|containsValue|compute|computeIfAbsent|computeIfPresent|entrySet|forEach|get|getOrDefault|isEmpty|keySet|merge|remove|replaceAll|replace|size|values"),
                Arguments.of(HashMap.class, List.of(Integer.class, Integer.class),
                        (Function<Object, Boolean>) UtilsExperiments::containsSelfMapping, "clear|clone|containsKey|containsValue|compute|computeIfAbsent|computeIfPresent|entrySet|forEach|get|getOrDefault|isEmpty|keySet|merge|remove|replaceAll|replace|size|values"),
                Arguments.of(HashSet.class, List.of(Integer.class),
                        (Function<Object, Boolean>) UtilsExperiments::hasMinimumSize, "clear|clone|contains|isEmpty|iterator|remove|size|spliterator"),
                Arguments.of(HashSet.class, List.of(Integer.class),
                        (Function<Object, Boolean>) UtilsExperiments::allOddElemets, "clear|clone|contains|isEmpty|iterator|remove|size|spliterator"),
                Arguments.of(LinkedList.class, List.of(Integer.class),
                        (Function<Object, Boolean>) UtilsExperiments::hasOddSize, "clear|clone|contains|descendingIterator|element|get|getFirst|getLast|indexOf|lastIndexOf|listIterator|" +
                                "peek|peekFirst|peekLast|poll|pollFirst|pollLast|pop|remove|removeFirst|removeFirstOccurrence|removeLast|removeLastOccurrence" +
                                "|size|spliterator|toArray"),
                Arguments.of(LinkedList.class, List.of(Integer.class),
                        (Function<Object, Boolean>) UtilsExperiments::isCircularLinkedList, "clear|clone|contains|descendingIterator|element|get|getFirst|getLast|indexOf|lastIndexOf|listIterator|" +
                                "peek|peekFirst|peekLast|poll|pollFirst|pollLast|pop|remove|removeFirst|removeFirstOccurrence|removeLast|removeLastOccurrence" +
                                "|size|spliterator|toArray"),
                Arguments.of(PriorityQueue.class, List.of(Integer.class),
                        (Function<Object, Boolean>) UtilsExperiments::isCircularLinkedList, "clear|comparator|contains|iterator|peek|poll|remove|size|spliterator|toArray")
//                Arguments.of(PriorityQueue.class, List.of(Integer.class),
//                        (Function<Object, Boolean>) UtilsExperiments::isValidPriorityQueue, "clear|comparator|contains|iterator|peek|poll|remove|size|spliterator|toArray")
                );
    }

    @ParameterizedTest
    @MethodSource("generationSource")
    public void experiments(Class<?> clazz, List<Class<?>> types, Function<Object, Boolean> assume, String ommitMethods) {
        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, types, 231);
        rog.setAssume(assume);
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
        rog.setCustomIntegers(integers);
        rog.setCustomStrings(stringSet);
        rog.setOmitMethods(ommitMethods);

        long totalStartTime = System.nanoTime();  // Medir tiempo total
        long firstStartTime = System.nanoTime();  // Tiempo antes del primer objeto
        rog.generate();
        long firstEndTime = System.nanoTime();  // Tiempo después del primer objeto
        long firstObjectTime = firstEndTime - firstStartTime;  // Tiempo que tarda el primer objeto

        long intermediateStartTime = System.nanoTime();  // Tiempo antes del segundo objeto
        for (int i = 1; i < 10; i++) {  // Bucle para los objetos restantes
            rog.generate();
        }
        long lastEndTime = System.nanoTime();  // Tiempo después del último objeto

        long totalTime = lastEndTime - totalStartTime;  // Tiempo total
        long intermediateTime = lastEndTime - intermediateStartTime;  // Tiempo entre el segundo y el último objeto
        double averageTimePerObject = (double) totalTime / 10;  // Tiempo promedio por objeto
        System.out.println(assume + " " + TimeUnit.SECONDS.convert(firstObjectTime, TimeUnit.NANOSECONDS) +
                " " + TimeUnit.SECONDS.convert(intermediateTime, TimeUnit.NANOSECONDS) + " "
                + TimeUnit.SECONDS.convert((long) averageTimePerObject, TimeUnit.NANOSECONDS) + " "
                + TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS));
    }
}
