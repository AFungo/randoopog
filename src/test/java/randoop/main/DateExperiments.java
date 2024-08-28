package randoop.main;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateExperiments {

    Set<String> stringSet = new HashSet<>(Arrays.asList(
            "8080", "1234", "hi!", "hello", "bye", "randoop", "metamorphic relation", "spec", "property",
            "oracle", "apple", "banana", "cherry", "date", "grape", "kiwi"));

    public static boolean isLeapDate(Object d){
        Date date = (Date) d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    @Test
    public void leapDateGeneration() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> runExperiment(Date.class, DateExperiments::isLeapDate,
                "clear|clone|after|before|getTime|toInstant", 100));
        try {
            future.get(3, TimeUnit.MINUTES);
        } catch(TimeoutException t){
            System.out.println("Time limit exceeded. Stopping the experiment.");
            future.cancel(true); // Interrumpe la ejecución si excede el tiempo
        }
    }

    public static boolean isJune(Object d){
        Date date = (Date) d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) == Calendar.JUNE;
    }

    @Test
    public void juneDateGeneration() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> runExperiment(Date.class, DateExperiments::isJune,
                "clear|clone|after|before|getTime|toInstant", 100));
        try {
            future.get(3, TimeUnit.MINUTES);
        } catch(TimeoutException t){
            System.out.println("Time limit exceeded. Stopping the experiment.");
            future.cancel(true); // Interrumpe la ejecución si excede el tiempo
        }
    }

    public static boolean isEndOfYear(Object d) {
        Date date = (Date) d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DAY_OF_MONTH) == 31;
    }

    @Test
    public void endOfYearGeneration() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> runExperiment(Date.class, DateExperiments::isEndOfYear,
                "clear|clone|after|before|getTime|toInstant", 100));
        try {
            future.get(3, TimeUnit.MINUTES);
        } catch(TimeoutException t){
            System.out.println("Time limit exceeded. Stopping the experiment.");
            future.cancel(true); // Interrumpe la ejecución si excede el tiempo
        }
    }

    public static boolean isWeekend(Object d) {
        Date date = (Date) d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    @Test
    public void weekendDateGeneration() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> runExperiment(Date.class, DateExperiments::isWeekend,
                "clear|clone|after|before|getTime|toInstant", 100));
        try {
            future.get(3, TimeUnit.MINUTES);
        } catch(TimeoutException t){
            System.out.println("Time limit exceeded. Stopping the experiment.");
            future.cancel(true); // Interrumpe la ejecución si excede el tiempo
        }
    }

    public static boolean isPayDayOfMonth(Object d) {
        Date date = (Date) d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) >= 1 && calendar.get(Calendar.DAY_OF_MONTH) < 10;
    }

    @Test
    public void payDayGeneration() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> runExperiment(Date.class, DateExperiments::isPayDayOfMonth,
                "clear|clone|after|before|getTime|toInstant", 100));
        try {
            future.get(3, TimeUnit.MINUTES);
        } catch(TimeoutException t){
            System.out.println("Time limit exceeded. Stopping the experiment.");
            future.cancel(true); // Interrumpe la ejecución si excede el tiempo
        }
    }

    public static boolean isInYear2000(Object d) {
        Date date = (Date) d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) == 2000;
    }

    @Test
    public void year2000DateGeneration() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> runExperiment(Date.class, DateExperiments::isInYear2000,
                "clear|clone|after|before|getTime|toInstant", 100));
        try {
            future.get(3, TimeUnit.MINUTES);
        } catch(TimeoutException t){
            System.out.println("Time limit exceeded. Stopping the experiment.");
            future.cancel(true); // Interrumpe la ejecución si excede el tiempo
        }
    }

    public static boolean isWorkingDay(Object d) {
        Date date = (Date) d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }

    @Test
    public void workingDayDateGeneration() throws InterruptedException, ExecutionException{
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> runExperiment(Date.class, DateExperiments::isWorkingDay,
                "clear|clone|after|before|getTime|toInstant", 100));
        try {
            future.get(3, TimeUnit.MINUTES);
        } catch(TimeoutException t){
            System.out.println("Time limit exceeded. Stopping the experiment.");
            future.cancel(true); // Interrumpe la ejecución si excede el tiempo
        }
    }

    public void runExperiment(Class<?> clazz, Function<Object, Boolean> assume, String ommitMethods, int amount) {
        RandoopObjectGenerator rog = new RandoopObjectGenerator(clazz, 231);
        rog.setAssume(assume);
        Set<Integer> integers = IntStream.range(110, 120).boxed().collect(Collectors.toSet());
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
