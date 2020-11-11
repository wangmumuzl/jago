/**
 * 
 */
package core.game;

import core.player.Player;

/**
 * @author ÷Ï¡’
 *
 */
public class Result {
	private String result;
	private Player currPlayer;
	public Result(){
		
	}
	
	public Result(String result, Player currPlayer) {
		super();
		this.result = result;
		this.currPlayer = currPlayer;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Player getCurrPlayer() {
		return currPlayer;
	}

	public void setCurrPlayer(Player currPlayer) {
		this.currPlayer = currPlayer;
	}
	
	
}
