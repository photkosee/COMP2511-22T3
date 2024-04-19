package dungeonmania.goals;

import dungeonmania.Game;

public class And implements Goal {

    private Goal g1;
    private Goal g2;

    public And(Goal g1, Goal g2) {
        this.g1 = g1;
        this.g2 = g2;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        return g1.achieved(game) && g2.achieved(game);
    }

    public String toString(Game game) {
        if (achieved(game)) return "";
        return "(" + g1.toString(game) + " AND " + g2.toString(game) + ")";
    }
}
