package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {

    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 7.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private double failRate;

    public Assassin(Position position, double health, double attack, int bribeAmount,
                    int bribeRadius, double failRate, double alliedAttack, double alliedDefence) {
        super(position, health, attack, bribeAmount, bribeRadius, alliedAttack, alliedDefence);
        this.failRate = failRate;
    }

    @Override
    public void bribe(Game game, Player player) {
        super.bribe(game, player);
        if ((Math.random() < failRate) || failRate == 1) {
            setAllied(false);
        }
    }
}
