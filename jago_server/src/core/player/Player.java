package core.player;

import java.util.ArrayList;
import core.board.PieceColor;
import core.game.Game;
import core.game.GameResult;
import core.game.Move;
import core.game.timer.GameTimer;

/**
 * A generic Connect6 Player.
 * 
 * @author
 */
public abstract class Player implements Comparable<Player>, Cloneable {

	public abstract boolean isManual();

	public abstract String name();

	private ArrayList<GameResult> gameResults = new ArrayList<>();

	// ��ӵ�ǰ���ֲ������ֵĽ��
	public void addGameResult(GameResult result) {
		gameResults.add(result);
	}

	public ArrayList<GameResult> gameResults() {
		return gameResults;
	}

	// ���㵱ǰ����������ֵ��ܵ÷֣���ʤ��2�֣�ƽ�ֵ�1�֣���ܵ�0��
	public int scores() {
		int scores = 0;
		for (GameResult result : gameResults) {
			scores += result.score(this.name());
		}
		return scores;
	}

	public PieceColor getColor() {
		return _myColor;
	}

	/** A Player that will play MYCOLOR in GAME. */
	public void setColor(PieceColor myColor) {
		_myColor = myColor;
	}

	/** Return the game I am playing in. */
	public Game game() {
		return _game;
	}

	/** Join a game. */
	public void playGame(Game game) {
		_game = game;
		timer.addObserver(_game);
	}

	/**
	 * Return a legal move for me according to my opponent's move, and at that
	 * moment, I am facing a board after the opponent's move. Abstract method to be
	 * implemented by subclasses.
	 */
	public abstract Move findMove(Move opponentMove) throws Exception;

	/** The game I am playing in. */
	private Game _game;

	/** The color of my pieces. */
	private PieceColor _myColor;
	private GameTimer timer;	//���ֵļ�ʱ��
	@Override
	public int compareTo(Player arg0) {
		// TODO Auto-generated method stub
		return arg0.scores() - this.scores();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
    }

	public void setTimer(GameTimer timer) {
		// TODO Auto-generated method stub
		this.timer = timer;
	}
	
	public void stopTimer() {
		timer.stop();
	}
	public void startTimer() {
		timer.start();
	}
}
