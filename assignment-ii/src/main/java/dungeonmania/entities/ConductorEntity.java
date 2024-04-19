package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class ConductorEntity extends Entity {

    private boolean activate = false;
    private boolean justActivate = false;

    public ConductorEntity(Position position) {
        super(position);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    protected void activated(GameMap map) {
        this.activate = true;
        this.justActivate = true;
        List<ConductorEntity> list = new ArrayList<>();
        for (Position pos : getCardinallyAdjacentPositions()) {
            list.addAll(map.getConductors(pos));
        }
        for (ConductorEntity conductor : list) {
            if (!conductor.isActivated() && !(conductor instanceof Switch)) {
                conductor.activated(map);
            }
        }
    }

    protected void deactivated(GameMap map) {
        this.activate = false;
        this.justActivate = false;
        List<ConductorEntity> list = new ArrayList<>();
        for (Position pos : getCardinallyAdjacentPositions()) {
            list.addAll(map.getConductors(pos));
        }
        for (ConductorEntity conductor : list) {
            if (conductor.isActivated() && !(conductor instanceof Switch)) {
                conductor.deactivated(map);
            }
        }
    }

    public void updateActivated() {
        this.justActivate = false;
    }

    public boolean isActivated() {
        return activate;
    }

    public boolean isJustActivated() {
        return justActivate;
    }
}
