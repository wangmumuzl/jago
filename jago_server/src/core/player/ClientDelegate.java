/**
 * 
 */
package core.player;

import java.io.BufferedReader;
import java.io.PrintWriter;

import core.board.Board;
import core.game.Move;

/**
 * @author 朱琳
 *
 */
public class ClientDelegate extends Player{
	private PrintWriter writer;
	private BufferedReader reader;
	private Board board=new Board();
	private String name ;
	private Move currentMove;
	
	private volatile String socketmove;
	
	public String getSocketmove() {
		return socketmove;
	}

	public void setSocketmove(String socketmove) {
		this.socketmove = socketmove;
	}

	public ClientDelegate(String name) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.socketmove=null;
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
	public Move findMove(Move opponentMove) {//发送playchess命令
		// TODO Auto-generated method stub
		//将当前move发送给服务端
		while(true){
			if(this.socketmove==null){
//				System.out.println(this.servermove+"getServermove");
			}else{
				break;
			}
		}
		currentMove=currentMove.parseMove(socketmove);
		this.socketmove=null;
		return currentMove;
	}
}
