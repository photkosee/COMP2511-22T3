package bool;

public class Not implements BooleanNode {

    private BooleanNode b1;

    public Not(BooleanNode b1) {
        this.b1 = b1;
    }

    @Override
    public boolean evaluate() {
        return !b1.evaluate();
    }

    @Override
    public String print() {
        return "(Not " + b1.print() + ")";
    }
}
