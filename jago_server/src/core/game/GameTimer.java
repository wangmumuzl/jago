package core.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

public class GameTimer extends Observable implements ActionListener{
	
	private int timeLimit = 0;
	javax.swing.Timer timer;
	public GameTimer(int timeLimit) {
		this.timeLimit = timeLimit;
		timer=new javax.swing.Timer(1000, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		timeLimit--;
		//System.out.println("TimeLeft: " + timeLimit);
		
		//�����ʱ��
		if (timeLimit <= 0) {
			//֪ͨ������
			setChanged();
			notifyObservers("T");
		}
	}

	
	public void stop(){
        timer.stop();
    }
	
	public void start() {
		timer.start();
	}

}
