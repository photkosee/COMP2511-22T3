package dungeonmania.entities;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity implements Overlapable {

    private int duration;

    public SwampTile(Position position, int duration) {
        super(position);
        this.duration = duration;
    }

    public int getStickDuration() {
        return duration;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Enemy && !((Enemy) entity).isStuck()) {
            ((Enemy) entity).getStuck(duration, map.getGame());
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }
}
