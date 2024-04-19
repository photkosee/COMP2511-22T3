package bool;

public class Boolean implements BooleanNode {

    private boolean bool;

    public Boolean(boolean bool) {
        this.bool = bool;
    }

    @Override
    public boolean evaluate() {
        return bool;
    }

    @Override
    public String print() {
        if (bool) {
            return "true";
        } else {
            return "false";
        }
    }
    
}
