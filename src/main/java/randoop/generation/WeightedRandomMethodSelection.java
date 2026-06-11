package randoop.generation;

import randoop.operation.TypedOperation;
import randoop.sequence.Sequence;
import randoop.util.Randomness;
import randoop.util.SimpleArrayList;
import randoop.util.SimpleList;

import java.util.List;
import java.util.Map;

public class WeightedRandomMethodSelection implements TypedOperationSelector {

    private final SimpleList<TypedOperation> operations;
    private final Map<TypedOperation, Double> weights;

    public WeightedRandomMethodSelection(List<TypedOperation> operations, Map<TypedOperation, Double> weights) {
        this.weights = weights;
        this.operations = new SimpleArrayList<>(operations);
    }

    @Override
    public TypedOperation selectOperation() {
        return Randomness.randomMemberWeighted(operations, weights);
    }

    @Override
    public void newRegressionTestHook(Sequence sequence) { }
}
