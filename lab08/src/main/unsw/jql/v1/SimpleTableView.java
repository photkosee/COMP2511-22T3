package unsw.jql.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleTableView<E> implements TableView<E> {
    private List<E> list;
    private Iterator<E> it;

    public SimpleTableView(List<E> list) {
        this.list = new ArrayList<E>(list);
        this.it = this.list.iterator();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public E next() {
        Iterator<E> newIter = list.iterator();
        newIter.next();
        List<E> newList = new ArrayList<E>();
        newIter.forEachRemaining(newList::add);
        this.list = newList;
        return it.next();
    }

    @Override
    public Table<E> toTable() {
        List<E> list = new ArrayList<E>();
        this.forEachRemaining(list::add);
        return new Table<E>(list);
    }

    @Override
    public Iterator<E> iterator() {
        // *technically* this is non standard
        // since this should reproduce a unique iterator each time
        // but for our sakes it's fine, since any operation on an
        // iterator will implicitly invalidate the inner iterators
        // invalidating its original context anyways.
        return new SimpleTableView<E>(list);
    }

    @Override
    public int count() {
        int count = 0;
        Iterator<E> newIter = list.iterator();
        while (newIter.hasNext()) {
            newIter.next();
            count++;
        }

        return count;
    }
}
