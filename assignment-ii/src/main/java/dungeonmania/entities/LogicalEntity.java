package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class LogicalEntity extends Entity {

    private LogicBehavior logic;
    private boolean activate = false;

    public LogicalEntity(Position position, LogicBehavior logic) {
        super(position);
        this.logic = logic;
    }

    public void activate(GameMap map) {
        this.activate = logic.activate(map, this);
    }

    public boolean isActivated() {
        return activate;
    }
}
