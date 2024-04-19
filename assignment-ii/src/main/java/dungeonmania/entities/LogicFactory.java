package dungeonmania.entities;

public class LogicFactory {
    public static LogicBehavior createLogic(String logic) {
        switch (logic) {
        case "and":
            return new LogicAnd();
        case "or":
            return new LogicOr();
        case "xor":
            return new LogicXor();
        case "co_and":
            return new LogicCoAnd();
        default:
            return null;
        }
    }
}
