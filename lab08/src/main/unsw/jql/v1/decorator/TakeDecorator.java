package unsw.jql.v1.decorator;

import java.util.NoSuchElementException;

import unsw.jql.v1.TableView;

public class TakeDecorator<E> extends OperationDecorator<E, E> {

    private int numberOfItems;

    /**
     * Grab a subset of the table view
     * @param numberOfItems The number of items to take, the rest are ignored
    */
    public TakeDecorator(TableView<E> inner, int numberOfItems) {
        super(inner);
        this.numberOfItems = numberOfItems;
    }

    @Override
    public boolean hasNext() {
        return numberOfItems > 0 && super.hasNext();
    }

    @Override
    public E next() {
        if (hasNext()) {
            numberOfItems--;
            return super.nextElement();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public int count() {
        return numberOfItems;
    }
}
