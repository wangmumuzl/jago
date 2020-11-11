package core.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class Timer implements ActionListener,Serializable{
	
	private int countTime=0;
	javax.swing.Timer timer;
	public Timer() {
		timer=new javax.swing.Timer(1000,this);
		timer.stop();
	}
	
	public int getCountTime() {
		return countTime;
	}
	public void setCountTime(int countTime) {
		this.countTime = countTime;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(timer.isRunning()) {
			addTime();
		}
		
	}
	
	public int addTime(){
        countTime++;
        return countTime;
    }
	
	public void stopTime(){
        timer.stop();
    }
	
	public void restartTime() {
		timer.restart();
	}

}
