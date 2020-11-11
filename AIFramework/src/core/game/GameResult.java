
package core.game;

public class GameResult {
	private String black;	//��������
	private String white;	//��������
	private String winner;	//��ʤ������, �����ƽ�֣�"NONE"
	private int steps;		//�ܹ����˶��ٲ�

	private String endReason;  //��ֽ�����ԭ��

	public GameResult(String black, String white, String winner, int steps, String endReason) {
		super();
		this.black = black;
		this.white = white;
		this.winner = winner;
		this.steps = steps;
		this.endReason = endReason;
	}
	
	//��������Ϊname��������GameResult�еĵ÷�
	//��ʤ��2�֣�ƽ�ֵ�1�֣���ܵ�0��
	public int score(String name) {
		if ("NONE".equals(winner)) 
			return 1;
		else if (name.equals(winner)) 
			return 2;
		else
			return 0;
	}
	
	public String toString() {
		return "\t���壺" + black + "\n" +
	           "\t���壺" + white + "\n" +
			   "\tʤ����" + winner + "\n" +
	           "\t������" + steps + "\n" +
			   "\t����ԭ��" + endReason + "\n";
	}
} 
