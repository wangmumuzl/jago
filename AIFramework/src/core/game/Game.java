package core.game;

import static core.board.PieceColor.*;

import java.util.Observable;
import java.util.Observer;

import core.game.timer.GameTimer;
import core.game.timer.TimerFactory;
import core.game.ui.GameUI;
import core.game.ui.UiFactory;
import core.player.Player;

/**
 * Controls the play of the game.
 * 
 * @author
 */
public class Game extends Observable implements Observer, Runnable {

	//裁判
	private Referee referee = null;
	
	//当前Game的线程
	private Thread me;
	
	public Game(Player black, Player white, int timeLimit) {
		//黑棋的计时器
		GameTimer blackTimer = TimerFactory.getTimer("Console", timeLimit);		
		black.setTimer(blackTimer);
		
		//白棋的计时器
		GameTimer whiteTimer = TimerFactory.getTimer("Console", timeLimit);	
		white.setTimer(whiteTimer);
		black.setColor(BLACK);
		white.setColor(WHITE);

		black.playGame(this);
		white.playGame(this);

		GameUI ui = UiFactory.getUi("GUI", black.name() + " vs " + white.name());
		//如果是带GUI的Timer，可以将其与ui的GoFrame绑在一起
		//ui.setTimer(blackTimer, whiteTimer)
		
		this.addObserver(ui);
		
		
		
		referee = new Referee(black, white);
		
	}
	
	public Thread start() {		
		me = new Thread(this);
		me.start();
		return me;
	}
	
	@Override
	public void run() {
		Move currMove = null;
		Player currPlayer = null;
		int steps = 1;

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 当前棋手：轮到谁下棋
			currPlayer = referee.whoseMove();

			// 正常结束，分出胜负
			if (referee.gameOver()) {
				referee.endingGame("F", currPlayer, currMove);
				break;
			}

			// 如果超过80步还没有分出胜负，则判为平局
			if (steps > 80) {
				referee.endingGame("M", currPlayer, currMove); // 判为平局
				break;
			}

			// 继续下棋
			Move move = null;
			//启动当前棋手的计时器
			currPlayer.startTimer();
			
			// 当前棋手下棋
			try {
				move = currPlayer.findMove(currMove);
			} catch (Exception ex) {  // 如果当前棋手出现异常	
				referee.endingGame("E", currPlayer, null); 			
				System.out.println(ex.getStackTrace());
				break;
			}
			//停止当前棋手的计时器
			currPlayer.stopTimer();
			
			//如果Thread已被中断
			if(Thread.interrupted()){  
				break;  
			}  
			
			// 如果是合法走步
			if (referee.legalMove(move)) {
				//通知更新棋盘
				setChanged();
				notifyObservers(move);
			} else { // 如果是非法走步（不反应到棋盘上）
				referee.endingGame("N", currPlayer, move); 
				break;
			}
			
			// 裁判记录合法走步
			referee.recordMove(move);			
			steps++;
			currMove = move;
		} // end of while	
	}
	
	//监听两个TimeWatcher，任何一个超时，都会执行这个Update函数
	@Override
	public void update(Observable arg0, Object arg1) {
		referee.endingGame("T", null, null);
		me.interrupt();
	}
}
