package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.inventory.Inventory;

public class MidnightArmour extends Buildable {

    private double attack;
    private double defence;

    public MidnightArmour(double attack, double defence) {
        super(null);
        this.attack = attack;
        this.defence = defence;
    }

    @Override
    public BattleStatistics applyBuff(Game game, BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            attack,
            defence,
            1,
            1));
    }

    public static boolean isBuildable(Inventory inventory, Game game) {
        int swords = inventory.count(Sword.class);
        int sunStone = inventory.count(SunStone.class);
        return swords >= 1 && sunStone >= 1 && (game.getEntitiesCount(ZombieToast.class) == 0);
    }

    public static void build(Inventory inventory) {
        List<SunStone> sunStone = inventory.getEntities(SunStone.class);
        List<Sword> swords = inventory.getEntities(Sword.class);
        inventory.remove(swords.get(0));
        inventory.remove(sunStone.get(0));
    }
}
