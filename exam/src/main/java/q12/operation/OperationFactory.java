package q12.operation;

import java.util.List;

public class OperationFactory {

    public static<T, R> Operation<List<T>, List<R>> all(List<Operation<T, R>> Operations) {
        return null;
    }

    public static<T> Operation<T, T> chain(List<Operation<T, T>> Operations) {
        return null;
    }
}