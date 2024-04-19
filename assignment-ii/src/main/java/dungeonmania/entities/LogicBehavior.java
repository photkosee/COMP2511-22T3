package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface LogicBehavior {
    public boolean activate(GameMap map, Entity entity);
}
