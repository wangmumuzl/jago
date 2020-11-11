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

import core.game.Move;

/**
 * @author 朱琳
 *
 */
public class SocketSender implements Observer{
	PrintStream out = null;
	private boolean change = true;//true 为白色 ；false为黑色
	public SocketSender(Socket socket) {
		super();
		try {
			out = new PrintStream(socket.getOutputStream());
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMove(Move currentMove) {
		// TODO Auto-generated method stub
		if(change){
			out.println("ServerGameMove@white@" + currentMove);
		}else{
			out.println("ServerGameMove@black@" + currentMove);
		}
		out.flush();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		sendMove((Move)arg1);
		change = !change;
	}

}
