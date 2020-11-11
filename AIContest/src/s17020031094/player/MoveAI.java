package s17020031094.player;

import java.util.Comparator;

import core.game.Move;

public class MoveAI extends Move implements Comparable<MoveAI>{

	public MoveAI(char col0, char row0, char col1, char row1) {
		super(col0, row0, col1, row1);
		// TODO Auto-generated constructor stub
	}

	public MoveAI(int index0, int index1) {
		super(index0, index1);
		// TODO Auto-generated constructor stub
	}
	public MoveAI(int index0, int index1, int score) {
		this(index0, index1);
		this.score = score;
		// TODO Auto-generated constructor stub
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public Move getMove() {
		return new Move(this.index1(), this.index2());
	}

	@Override
	public int compareTo(MoveAI mov) {
		// TODO Auto-generated method stub
		return this.score - mov.score;
	}
	
	public static Comparator<MoveAI> scoreComparatorDesc = new Comparator<MoveAI>(){
	       public int compare(MoveAI m1, MoveAI m2) {
	           return m2.score - m1.score;
	        }
	};
	
	public static Comparator<MoveAI> scoreComparatorAsc = new Comparator<MoveAI>(){
	       public int compare(MoveAI m1, MoveAI m2) {
	           return m1.score - m2.score;
	        }
	};
	
	private int score;
}
