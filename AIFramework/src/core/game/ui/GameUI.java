package core.game.ui;

import java.util.Observer;

import core.game.timer.GameTimer;

public interface GameUI extends Observer{
	//��UI�Ϸ��ÿ��ӻ��ļ�ʱ��
	public void setTimer(GameTimer bTimer, GameTimer wTimer);
}
