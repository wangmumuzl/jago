
package core.game;

public class GameResult {
	private String black;	//黑棋名字
	private String white;	//白棋名字
	private String winner;	//获胜者名字, 如果是平局，"NONE"
	private int steps;		//总共下了多少步

	private String endReason;  //棋局结束的原因

	public GameResult(String black, String white, String winner, int steps, String endReason) {
		super();
		this.black = black;
		this.white = white;
		this.winner = winner;
		this.steps = steps;
		this.endReason = endReason;
	}
	
	//计算名字为name的棋手在GameResult中的得分
	//获胜得2分，平局得1分，落败得0分
	public int score(String name) {
		if ("NONE".equals(winner)) 
			return 1;
		else if (name.equals(winner)) 
			return 2;
		else
			return 0;
	}
	
	public String toString() {
		return "\t黑棋：" + black + "\n" +
	           "\t白棋：" + white + "\n" +
			   "\t胜方：" + winner + "\n" +
	           "\t步数：" + steps + "\n" +
			   "\t结束原因：" + endReason + "\n";
	}
} 
