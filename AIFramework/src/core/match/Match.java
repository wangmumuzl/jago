package core.match;

import core.game.Game;
import core.game.timer.GameTimer;
import core.game.timer.TimerFactory;
import core.game.ui.GameUI;
import core.game.ui.UiFactory;
import core.player.Player;
import java.util.ArrayList;

public class Match {
	
	public Match(int gameNumbers, Player one, Player another) {
		super();
		this.gameNumbers = gameNumbers;
		this.one = one;
		this.another = another;
	}

	public void process() {
		Player black = one;
		Player white = another;		
		for (int i = 0; i < gameNumbers; i++) {
		
			int timeLimit = 300;		
			//����Game
			try {
				Player bClone = (Player) black.clone();				
				Player wClone = (Player) white.clone();
				
				Game game = new Game(bClone, wClone, timeLimit);
				games.add(game.start());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			//�����ڰ���
			Player temp;
			temp = black;
			black = white;
			white = temp;
		}
	}
	
	public ArrayList<Thread> getGames(){
		return games;
	}
	
	private int gameNumbers;	//һ�������ȼ���
	private ArrayList<Thread> games = new ArrayList<>();
	private Player one;			//����1
	private Player another;	    //����2
}
