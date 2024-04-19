package bool;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONObject;

public class BooleanEvaluator {

    public static boolean evaluate(BooleanNode expression) {
        return expression.evaluate();
    }

    public static String prettyPrint(BooleanNode expression) {
        return expression.print();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        BooleanNode T = new Boolean(true);
        BooleanNode F = new Boolean(false);
        BooleanNode and = new And(T, F);
        assertFalse(evaluate(and));
        System.out.println(prettyPrint(and));

        BooleanNode notF = new Not(F);
        BooleanNode orFalseNot = new Or(F, notF);
        assertTrue(evaluate(orFalseNot));
        System.out.println(prettyPrint(orFalseNot));

        BooleanNode orTf = new Or(T, F);
        BooleanNode andForTf = new And(F, orTf);
        BooleanNode notAndForTf = new Not(andForTf);
        BooleanNode result = new Or(T, notAndForTf);
        assertTrue(evaluate(result));
        System.out.println(prettyPrint(result));

        NodeFactory factory = new NodeFactory();

        String jsonText;
        jsonText = Files.readString(Path.of("src/main/bool/BooleanTest.json"));
        JSONObject json = new JSONObject(jsonText);
        System.out.println(prettyPrint(factory.createBooleanNode(json)));
    }
}