package randoop.main.randoopflags;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class TestClassListFlag extends RandoopFlag {

    private List<Class<?>> testClasses;//maybe this can be Class<Object>, but i don't know how to include class package,
                    // can i put another attribute for package? I don't look it well
    private Path classList;
    public TestClassListFlag(List<Class<?>> testClasses){
        this.flagName = "classlist";
        new File("classList.txt");
        this.classList = Paths.get("classList.txt");
        System.out.println(classList);
        for(Class<?> testClass : testClasses){
            try {
                Files.write(this.classList, testClass.getName().getBytes(), StandardOpenOption.CREATE);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected String flagParameterToString() {
        return this.classList.toString();
    }
}
