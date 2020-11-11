package core.client;

import core.game.Game;
import core.player.*;

public class SimpleClient3 {

    public static void main(String[] args) {
		
		Player black = new baseline.player.AI();
		Player white = new s17020031086.player.AI();
		
		int timeLimit = 30;

		Game game = new Game(black, white, timeLimit);
						
		game.start();
    }
}
