package q12.operation;

import java.util.function.Function;
import java.util.function.Supplier;

public class Operation<T, R> {

    public Operation() {
    }

    public Operation(Function<T, R> operation) {
    }

    public <V> Operation<T, V> then(Function<R, V> operation) {
        return null;
    }

    public Operation<T, R> except(Supplier<R> operation) {
        return null;
    }

    public boolean hasCachedResult(T forInput) {
        return false;
    }

    public R resolve(T input) {
        return null;
    }
}