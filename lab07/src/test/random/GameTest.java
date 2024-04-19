package random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTest {

    @Test
    public void Test() {
        Game game = new Game();
        int win = 0;
        int lose = 0;
        for (int i = 0; i < 10000000; i++) {
            if (game.battle()) {
                win++;
            } else {
                lose++;
            }
        }
        assertTrue((10000000 / 2) * 0.1 >= Math.abs(10000000 / 2 - win));
        assertTrue((10000000 / 2) * 0.1 >= Math.abs(10000000 / 2 - lose));

        boolean w = false;
        boolean l = false;
        for (int i = 0; i < 10; i++) {
            if (game.battle()) {
                w = true;
            } else {
                l = true;
            }
        }
        assertTrue(w);
        assertTrue(l);
    }
}