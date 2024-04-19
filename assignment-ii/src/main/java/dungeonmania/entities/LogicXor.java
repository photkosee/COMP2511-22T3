package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LogicXor implements LogicBehavior {

    @Override
    public boolean activate(GameMap map, Entity entity) {
        List<Position> near = entity.getCardinallyAdjacentPositions();
        List<ConductorEntity> conductors = new ArrayList<>();
        for (Position pos : near) {
            conductors.addAll(map.getConductors(pos));
        }
        return conductors.stream().filter(e -> e.isActivated()).count() == 1;
    }

}
