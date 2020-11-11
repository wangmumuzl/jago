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

	//����
	private Referee referee = null;
	
	//��ǰGame���߳�
	private Thread me;
	
	public Game(Player black, Player white, int timeLimit) {
		//����ļ�ʱ��
		GameTimer blackTimer = TimerFactory.getTimer("Console", timeLimit);		
		black.setTimer(blackTimer);
		
		//����ļ�ʱ��
		GameTimer whiteTimer = TimerFactory.getTimer("Console", timeLimit);	
		white.setTimer(whiteTimer);
		black.setColor(BLACK);
		white.setColor(WHITE);

		black.playGame(this);
		white.playGame(this);

		GameUI ui = UiFactory.getUi("GUI", black.name() + " vs " + white.name());
		//����Ǵ�GUI��Timer�����Խ�����ui��GoFrame����һ��
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
			// ��ǰ���֣��ֵ�˭����
			currPlayer = referee.whoseMove();

			// �����������ֳ�ʤ��
			if (referee.gameOver()) {
				referee.endingGame("F", currPlayer, currMove);
				break;
			}

			// �������80����û�зֳ�ʤ��������Ϊƽ��
			if (steps > 80) {
				referee.endingGame("M", currPlayer, currMove); // ��Ϊƽ��
				break;
			}

			// ��������
			Move move = null;
			//������ǰ���ֵļ�ʱ��
			currPlayer.startTimer();
			
			// ��ǰ��������
			try {
				move = currPlayer.findMove(currMove);
			} catch (Exception ex) {  // �����ǰ���ֳ����쳣	
				referee.endingGame("E", currPlayer, null); 			
				System.out.println(ex.getStackTrace());
				break;
			}
			//ֹͣ��ǰ���ֵļ�ʱ��
			currPlayer.stopTimer();
			
			//���Thread�ѱ��ж�
			if(Thread.interrupted()){  
				break;  
			}  
			
			// ����ǺϷ��߲�
			if (referee.legalMove(move)) {
				//֪ͨ��������
				setChanged();
				notifyObservers(move);
			} else { // ����ǷǷ��߲�������Ӧ�������ϣ�
				referee.endingGame("N", currPlayer, move); 
				break;
			}
			
			// ���м�¼�Ϸ��߲�
			referee.recordMove(move);			
			steps++;
			currMove = move;
		} // end of while	
	}
	
	//��������TimeWatcher���κ�һ����ʱ������ִ�����Update����
	@Override
	public void update(Observable arg0, Object arg1) {
		referee.endingGame("T", null, null);
		me.interrupt();
	}
}
