package core.game;

import java.util.Observer;

public interface GameUI extends Observer{
	//��UI�Ϸ��ÿ��ӻ��ļ�ʱ��
	public void setTimer(GameTimer bTimer, GameTimer wTimer);
}
