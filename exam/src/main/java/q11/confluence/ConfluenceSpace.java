package q11.confluence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfluenceSpace implements Iterable<ConfluencePage>, ConfluenceNode {
    List<ConfluenceNode> confluenceNodes = new ArrayList<>();
    int iterator = 0;

    public void addSubnode(ConfluenceNode node) {
        this.confluenceNodes.add(node);
    }

    public int getNumPages() {
        return confluenceNodes.size();
    }

    public boolean hasnext() {
        return iterator < confluenceNodes.size();
    }

    @Override
    public Iterator<ConfluencePage> iterator() {
        return null;
    }
}