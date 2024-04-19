package q11.confluence;

public interface ConfluenceNode {
    public int getNumPages();
    public void addSubnode(ConfluenceNode node);
    public boolean hasnext();
}
