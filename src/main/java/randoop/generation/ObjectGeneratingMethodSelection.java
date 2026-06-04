package randoop.generation;

import randoop.operation.TypedOperation;
import randoop.sequence.Sequence;
import randoop.types.ClassOrInterfaceType;
import randoop.types.Type;
import randoop.util.Randomness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectGeneratingMethodSelection implements TypedOperationSelector {

    private final List<TypedOperation> objectGeneratingOperations;

    private final List<TypedOperation> noObjectGeneratingOperations;

    public ObjectGeneratingMethodSelection(List<TypedOperation> operations, Set<ClassOrInterfaceType> classesUnderTest) {
        Set<ClassOrInterfaceType> cuts = new HashSet<>(classesUnderTest);
        cuts.remove(ClassOrInterfaceType.forClass(Object.class));
        objectGeneratingOperations = new ArrayList<>();
        noObjectGeneratingOperations = new ArrayList<>();
        for (TypedOperation operation : operations) {
            Type t = operation.getOutputType();
            if (t.isClassOrInterfaceType() && cuts.contains(ClassOrInterfaceType.forClass(t.getRuntimeClass()))) {
                objectGeneratingOperations.add(operation);
            } else {
                noObjectGeneratingOperations.add(operation);
            }
        }
    }

    @Override
    public TypedOperation selectOperation() {
        if (Randomness.weightedCoinFlip(0.5f)) {
            return Randomness.randomMember(objectGeneratingOperations);
        } else {
            return Randomness.randomMember(noObjectGeneratingOperations);
        }
    }

    @Override
    public void newRegressionTestHook(Sequence sequence) {
    }

}
