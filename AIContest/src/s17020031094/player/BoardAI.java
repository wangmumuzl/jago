package s17020031094.player;

import java.util.ArrayList;

import core.board.Board;
import core.board.PieceColor;

public class BoardAI extends Board {

	public BoardAI() {
		// TODO Auto-generated constructor stub
		roadTable = new RoadTable();
		roadTable.clear();
	}

	public BoardAI(Board b) {
		super(b);
		// TODO Auto-generated constructor stub
	}
	
	public void setStone(int index){
		roadTable.changePlayerBoard(index, whoseMove());
	}
		
	public void unSetStone(int index){
		roadTable.backPlayerBoard(index, whoseMove());
	}
		
	public ArrayList<Integer> getTSSMove(int isMy){
		if (isMy == 1) {
			return roadTable.TSSMy(whoseMove());
		}else return roadTable.TSSEnemy(whoseMove());
	}
	
	public ArrayList<Integer> getPoentialMove(){
		return roadTable.getPoentialMove(whoseMove());
	}
	
	public int getValue(PieceColor whoseMove){
		return roadTable.getValue(whoseMove);
	}
	
	public boolean gameOver(){
		return roadTable.gameOver();
	}
	private RoadTable roadTable;
	public void printRoadTable(){
		roadTable.print();
	}
}


