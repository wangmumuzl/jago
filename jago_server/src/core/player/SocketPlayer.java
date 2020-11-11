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
 * @author 朱琳
 *
 */
public class SocketPlayer extends Player implements Observer{
	Player player;
	private final String name;
	PrintStream out = null;
	private volatile boolean continueMove = false;
	private String color;//识别黑棋白棋
	
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
		//先发送给客户端，服务端才能继续进行
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
		String flag = stringTokenizer.nextToken(); // 标识符
		if(flag.equals("ServerGameMove")){
			String colorStr = stringTokenizer.nextToken();
			if(colorStr.equals(this.color)){
				this.continueMove = true;
			}
		}
	}
}
