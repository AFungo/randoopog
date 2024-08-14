package randoop.main;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UtilsExperiments {

    public static boolean isSorted(Object l){
        ArrayList<Integer> list = (ArrayList<Integer>) l;
        if(list.size() < 3) return false;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) < list.get(i + 1)) {
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
        rog.setOmitMethods("clear|clone|contains|ensureCapacity|get|indexOf|isEmpty|iterator|lastIndexOf|remove|size|toArray|listIterator|removeAll|removeIf|removeRange|replaceAll|retainAll|subList|spliterator|trimToSize");
        for (int i = 0; i < 10; i++) {
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
        rog.setOmitMethods("clear|clone|containsKey|containsValue|compute|computeIfAbsent|computeIfPresent|entrySet|forEach|get|getOrDefault|isEmpty|keySet|" +
                "merge|remove|replaceAll|replace|size|values");
        for (int i = 0; i < 10; i++) {
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
        return set.size() >= 3 && set.stream().anyMatch(e -> e.hashCode() % 5 == 0);
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
        for (int i = 0; i < 10; i++) {
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
     Constraint: The LinkedList should have more than five elements, should not contain null values, and should have the same first and last element.
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
        for (int i = 0; i < 10; i++) {
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
}
