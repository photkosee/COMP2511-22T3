package bool;

public class Or implements BooleanNode {

    private BooleanNode b1;
    private BooleanNode b2;


    public Or(BooleanNode b1, BooleanNode b2) {
        this.b1 = b1;
        this.b2 = b2;
    }

    @Override
    public boolean evaluate() {
        return b1.evaluate() || b2.evaluate();
    }

    @Override
    public String print() {
        return "(Or " + b1.print() + " " + b2.print() + ")";
    }
}
