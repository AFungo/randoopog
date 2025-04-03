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
        runExperiment(ArrayList.class, List.of(Integer.class), (Function<Object, Boolean>) UtilsExperiments::isSorted,"clear|clone|contains|ensureCapacity|get|indexOf|isEmpty|iterator|lastIndexOf|remove|size|toArray|listIterator|removeAll|removeIf|removeRange|replaceAll|retainAll|subList|spliterator|trimToSize|addAll", 100);
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
        runExperiment(HashMap.class, List.of(Integer.class, Integer.class), (Function<Object, Boolean>) UtilsExperiments::containsSelfMapping,"clear|clone|containsKey|containsValue|compute|computeIfAbsent|computeIfPresent|entrySet|forEach|get|getOrDefault|isEmpty|keySet|" +
                "merge|remove|replaceAll|replace|size|values", 100);
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
        runExperiment(HashMap.class, List.of(Integer.class, Integer.class), (Function<Object, Boolean>) UtilsExperiments::containsKey,"clear|clone|containsKey|containsValue|compute|computeIfAbsent|computeIfPresent|entrySet|forEach|get|getOrDefault|isEmpty|keySet|" +
                "merge|remove|replaceAll|replace|size|values", 100);
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
        runExperiment(HashSet.class, List.of(Integer.class), (Function<Object, Boolean>) UtilsExperiments::hasMinimumSize,"clear|clone|contains|isEmpty|iterator|remove|size|spliterator", 100);
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
        runExperiment(HashSet.class, List.of(Integer.class), (Function<Object, Boolean>) UtilsExperiments::allOddElemets,"clear|clone|contains|isEmpty|iterator|remove|size|spliterator", 100);
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
        runExperiment(LinkedList.class, List.of(Integer.class), (Function<Object, Boolean>) UtilsExperiments::hasOddSize, "clear|clone|contains|descendingIterator|element|get|getFirst|getLast|indexOf|lastIndexOf|listIterator|" +
                "peek|peekFirst|peekLast|poll|pollFirst|pollLast|pop|remove|removeFirst|removeFirstOccurrence|removeLast|removeLastOccurrence" +
                "|size|spliterator|toArray", 100);
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
        runExperiment(LinkedList.class, List.of(Integer.class), (Function<Object, Boolean>) UtilsExperiments::isCircularLinkedList, "clear|clone|contains|descendingIterator|element|get|getFirst|getLast|indexOf|lastIndexOf|listIterator|" +
                "peek|peekFirst|peekLast|poll|pollFirst|pollLast|pop|remove|removeFirst|removeFirstOccurrence|removeLast|removeLastOccurrence" +
                "|size|spliterator|toArray", 100);
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
        runExperiment(PriorityQueue.class, Collections.singletonList(Integer.class), (Function<Object, Boolean>) UtilsExperiments::isHeadSmallest, "clear|comparator|contains|iterator|peek|poll|remove|size|spliterator|toArray", 100);
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

//    @Test
//    public void validPriorityQueue(){
//        runExperiment(PriorityQueue.class, Collections.singletonList(Integer.class), (Function<Object, Boolean>) UtilsExperiments::isValidPriorityQueue, "clear|comparator|contains|iterator|peek|poll|remove|size|spliterator|toArray", 100);
//    }

    public void runExperiment(Class<?> clazz, List<Class<?>> types, Function<Object, Boolean> assume, String ommitMethods, int amount) {
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
        for (int i = 1; i < amount; i++) {  // Bucle para los objetos restantes
            rog.generate();
        }
        long lastEndTime = System.nanoTime();  // Tiempo después del último objeto

        long totalTime = lastEndTime - totalStartTime;  // Tiempo total
        long intermediateTime = lastEndTime - intermediateStartTime;  // Tiempo entre el segundo y el último objeto
        double averageTimePerObject = (double) totalTime / 10;  // Tiempo promedio por objeto
        System.out.println("firstObjectTime: " + TimeUnit.SECONDS.convert(firstObjectTime, TimeUnit.NANOSECONDS) +
                " secondToLast: " + TimeUnit.SECONDS.convert(intermediateTime, TimeUnit.NANOSECONDS) + " average: "
                + TimeUnit.SECONDS.convert((long) averageTimePerObject, TimeUnit.NANOSECONDS) + " total: "
                + TimeUnit.SECONDS.convert(totalTime, TimeUnit.NANOSECONDS));
    }
}
