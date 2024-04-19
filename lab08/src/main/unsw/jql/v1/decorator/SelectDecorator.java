package unsw.jql.v1.decorator;

import java.util.NoSuchElementException;
import java.util.function.Function;

import unsw.jql.v1.TableView;

public class SelectDecorator<E, R> extends OperationDecorator<E, R> {

    private Function<E, R> selector;
    /**
     * Map a table view to another table view.
     * 
     * Each item/record is mapped through the provided selector.
     * 
     * An example would be `new SelectDecorator(view, (fruit) -> fruit.age()))`
    */
    public SelectDecorator(TableView<E> inner, Function<E, R> selector) {
        super(inner);
        this.selector = selector;
    }

    @Override
    public R next() {
        if (hasNext()) {
            return selector.apply(super.nextElement());
        } else {
            throw new NoSuchElementException();
        }
    }
}
