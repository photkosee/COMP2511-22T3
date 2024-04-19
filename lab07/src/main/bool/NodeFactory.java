package bool;

import org.json.JSONObject;

public class NodeFactory {

    public BooleanNode createBooleanNode(JSONObject config) {
        JSONObject subnode1;
        JSONObject subnode2;
        switch (config.getString("node")) {
        case "and":
            subnode1 = config.getJSONObject("subnode1");
            subnode2 = config.getJSONObject("subnode2");
            return new And(
                createBooleanNode(subnode1),
                createBooleanNode(subnode2)
            );
        case "or":
            subnode1 = config.getJSONObject("subnode1");
            subnode2 = config.getJSONObject("subnode2");
            return new Or(
                createBooleanNode(subnode1),
                createBooleanNode(subnode2)
            );
        case "not":
            subnode1 = config.getJSONObject("subnode1");
            return new Not(
                createBooleanNode(subnode1)
            );
        case "value":
            boolean input = config.getBoolean("value");
            return new Boolean(input);
        default:
            return null;
        }
    }
}