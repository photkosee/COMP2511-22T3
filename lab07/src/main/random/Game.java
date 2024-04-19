package random;

import java.util.Random;

/**
 * A simple game, where a hero engages in battles.
 * The hero has an equally likely chance of succeeding as of failing.
 * @author Nick Patrikeos + @your name
 */
public class Game {

    private Random random;

    public Game(long seed) {
        random = new Random(seed);
    }

    public Game() {
        this(System.currentTimeMillis());
    }

    public boolean battle() {
        return new Random().nextInt(2) == 0;
    }

    public static void main(String[] args) {
        Game g = new Game();
        int win = 0;
        int lose = 0;
        for (int i = 0; i < 100; i++) {
            if (g.battle()) {
                System.out.println("We won!! You are awesome!!");
                win++;
            } else {
                System.out.println("Lost :(");
                lose++;
            }
        }
        System.out.println("Win: " + win + " Lose: " + lose);
    }
}