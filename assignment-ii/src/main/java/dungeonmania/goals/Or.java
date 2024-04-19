package dungeonmania.goals;

import dungeonmania.Game;

public class Or implements Goal {

    private Goal g1;
    private Goal g2;

    public Or(Goal g1, Goal g2) {
        this.g1 = g1;
        this.g2 = g2;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null) return false;
        return g1.achieved(game) || g2.achieved(game);
    }

    public String toString(Game game) {
        if (achieved(game)) return "";
        return "(" + g1.toString(game) + " OR " + g2.toString(game) + ")";
    }
}
