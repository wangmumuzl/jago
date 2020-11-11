package s17020032011.player;

import core.game.Move;

public class Move4AI extends Move {

	private int score;

	public Move4AI(char col0, char row0, char col1, char row1) {
		super(col0, row0, col1, row1);
		// TODO Auto-generated constructor stub
	}

	public Move4AI(int index0,int index1) {
		super(index0,index1);
	}
	
	public Move4AI(int index0,int index1,int score) {
		this(index0,index1);
		this.score=score;
	}
	
	public void draw() {
		System.out.println("index1:"+this.index1()+"index2:"+this.index2());
		System.out.println("score:"+this.getScore());
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Move4AI) {
			Move4AI mov=(Move4AI)obj;	
			if((this.index1()==mov.index1()&&this.index2()==mov.index2())||(this.index1()==mov.index2()&&this.index2()==mov.index1())) {
				return true;
			}
		}
		return false;
	}
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score=score;
	}
	
	public Move getMove() {
		return new Move(this.index1(),this.index2());
	}
	public Move4AI (Move move) {
		this(move.index1(),move.index2());
	}

	
	

	
}
