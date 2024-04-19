package dungeonmania.entities.buildables;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;

public class Sceptre extends Buildable {

    private int duration;

    public Sceptre(int duration) {
        super(null);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public BattleStatistics applyBuff(Game game, BattleStatistics origin) {
        return origin;
    }

    public static boolean isBuildable(Inventory inventory, Game game) {
        int wood = inventory.count(Wood.class);
        int arrows = inventory.count(Arrow.class);
        int treasure = inventory.count(Treasure.class);
        int keys = inventory.count(Key.class);
        int sunStone = inventory.count(SunStone.class);
        return (wood >= 1 || arrows >= 2) && (keys >= 1 || treasure >= 1 || sunStone >= 2) && sunStone >= 1;
    }

    public static void build(Inventory inventory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Arrow> arrows = inventory.getEntities(Arrow.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);
        List<SunStone> sunStone = inventory.getEntities(SunStone.class);
        if (wood.size() >= 1) {
            inventory.remove(wood.get(0));
        } else {
            inventory.remove(arrows.get(0));
            inventory.remove(arrows.get(1));
        }
        if (keys.size() >= 1 && sunStone.size() < 2) {
            inventory.remove(keys.get(0));
        } else if (treasure.size() >= 1 && sunStone.size() < 2) {
            inventory.remove(treasure.get(0));
        }
        inventory.remove(sunStone.get(0));
    }
}
