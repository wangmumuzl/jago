/**
 * 
 */
package core.player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import core.game.Move;

/**
 * @author ����
 *
 */
public class SocketPlayer extends Player implements Observer{
	Player player;
	private final String name;
	PrintStream out = null;
	private volatile boolean continueMove = false;
	private String color;//ʶ��������
	
	public SocketPlayer(String name,Socket socket,Player player,String color) {
		super();
		this.player = player;
		this.name = name;
		this.color = color;
		try {
			out = new PrintStream(socket.getOutputStream());
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see core.player.Player#isManual()
	 */
	@Override
	public boolean isManual() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see core.player.Player#name()
	 */
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return name;
	}

	/* (non-Javadoc)
	 * @see core.player.Player#findMove(core.game.Move)
	 */
	@Override
	public Move findMove(Move opponentMove) throws Exception {
		// TODO Auto-generated method stub
		Move move = player.findMove(opponentMove);
		sendMove(move);
		//�ȷ��͸��ͻ��ˣ�����˲��ܼ�������
		while(!continueMove){
			
		}
		this.continueMove = false;
		return move;
	}
	
	public void sendMove(Move currentMove) {
		// TODO Auto-generated method stub
		out.println("ServerGameMove@"+this.color+ "@" + currentMove);
		out.flush();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		StringTokenizer stringTokenizer = new StringTokenizer((String)arg, "@");
		String flag = stringTokenizer.nextToken(); // ��ʶ��
		if(flag.equals("ServerGameMove")){
			String colorStr = stringTokenizer.nextToken();
			if(colorStr.equals(this.color)){
				this.continueMove = true;
			}
		}
	}
}
