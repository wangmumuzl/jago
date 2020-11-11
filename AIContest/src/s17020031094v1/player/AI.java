package s17020031094v1.player;

import core.game.Game;
import core.game.Move;
import static core.board.PieceColor.*;
import static core.game.Move.SIDE;
import java.util.ArrayList;

public class AI extends core.player.AI {

	/** Maximum minimax search depth before going to static evaluation. */
	private static final int MAX_DEPTH = 4;

	public AI() {
		// TODO Auto-generated constructor stub
	}
	
	@Override	
    public Move findMove(Move opponentMove) {	
		if (opponentMove == null) {
			Move move = firstMove();
			board.setStone(move.index1());
			if(move.index1() != move.index2())
				board.setStone(move.index2());
			board.makeMove(move);
			return move;
		}
		else {
			board.setStone(opponentMove.index1());
			if(opponentMove.index1() != opponentMove.index2())
				board.setStone(opponentMove.index2());
			board.makeMove(opponentMove);
		}
		Move bestMove = findBestMove();
		board.setStone(bestMove.index1());
		board.setStone(bestMove.index2());
		board.makeMove(bestMove);
		return bestMove;
    }
	
	private Move findBestMove(){		
		Move bestMove = null;
		ArrayList<Integer> winMove = board.getTSSMove(1);
		int index1=-1,index2=-1;
		if(!winMove.isEmpty()){
			winMove = getEmptyPoentialMove(winMove);
			index1 = winMove.get(0);
			index2 = winMove.get(1);
			bestMove = new Move(index1, index2);
			return bestMove;
		}
		ArrayList<Integer> poential1Move = board.getTSSMove(0);
		if(poential1Move.isEmpty())
			poential1Move = board.getPoentialMove();
		boolean first = true;
		int maxScore=0;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int[] visit = new int[SIDE*SIDE];
		for(int i=0; i<poential1Move.size(); i++){
			index1 = poential1Move.get(i);
			if(board.get(index1) != EMPTY)	continue;
			visit[index1] = 1;
			board.setStone(index1);
			ArrayList<Integer> poential2Move = board.getTSSMove(0);
			if(poential2Move.isEmpty())
				poential2Move = board.getPoentialMove();
			for(int j=0; j<poential2Move.size(); j++){
				index2 = poential2Move.get(j);
				if(visit[index2]==1 || board.get(index2)!=EMPTY)	continue;
				board.setStone(index2);
				Move moves = new Move(index1, index2);
				board.makeMove(moves);
				int now = findMove(MAX_DEPTH,0,alpha, beta);
				alpha = Math.max(alpha, now);
				if(first || now > maxScore){
					first = false;
					maxScore = now;
					bestMove = new Move(index1, index2);
				}
				board.undo();
				board.unSetStone(index2);
			}
			board.unSetStone(index1);
		}
		return bestMove;
	}
	private int findMove(int depth, int sense, int alpha, int beta) {		
		if(depth==0 || board.gameOver())	return board.getValue(getColor());
		int v;
		if(sense == 1){		//閺嬩礁銇囬崐鑹板Ν閻愮櫢绱漚閸擃亝鐏婇敍灞藉絿鐎涙劘濡悙鍦畱閺堬拷婢堆冿拷锟�
			ArrayList<Integer> winMove = board.getTSSMove(1);
			int index1=-1,index2=-1;
			v = Integer.MIN_VALUE;
			if(!winMove.isEmpty()){
				winMove = getEmptyPoentialMove(winMove);
				index1 = winMove.get(0);
				index2 = winMove.get(1);
				board.setStone(index1);
				board.setStone(index2);
				Move move = new Move(index1, index2);
				board.makeMove(move);
				v = Math.max(v, findMove(depth-1,0, alpha, beta));
				alpha = Math.max(alpha, v);
				board.undo();
				board.unSetStone(index2);
				board.unSetStone(index1);
				return v;
			}
			int[] visit = new int[SIDE*SIDE];
			ArrayList<Integer> poential1Move = board.getTSSMove(0);
			if(poential1Move.isEmpty())
				poential1Move = board.getPoentialMove();
			for(int i=0; i<poential1Move.size(); i++){
				index1 = poential1Move.get(i);
				if(board.get(index1) != EMPTY)	continue;
				visit[index1] = 1;
				board.setStone(index1);
				ArrayList<Integer> poential2Move = board.getTSSMove(0);
				if(poential2Move.isEmpty())
					poential2Move = board.getPoentialMove();
				for(int j=0; j<poential2Move.size(); j++){
					index2 = poential2Move.get(j);
					if(visit[index2]==1 || board.get(index2)!=EMPTY)	continue;
					board.setStone(index2);
					Move moves = new Move(index1, index2);
					board.makeMove(moves);
					v = Math.max(v, findMove(depth-1,0, alpha, beta));
					alpha = Math.max(alpha, v);
					board.undo();
					board.unSetStone(index2);
					if(beta <= alpha){
						break;
					}
				}
				board.unSetStone(index1);
				if(beta <= alpha){
					break;
				}
				
			}
				
		}else{				//閺嬩礁鐨崐鑹板Ν閻愮櫢绱漛閸擃亝鐏婇敍灞藉絿鐎涙劘濡悙鍦畱閺堬拷鐏忓繐锟斤拷
			ArrayList<Integer> winMove = board.getTSSMove(1);
			int index1=-1,index2=-1;
			v = Integer.MAX_VALUE;
			if(!winMove.isEmpty()){
				winMove = getEmptyPoentialMove(winMove);
				index1 = winMove.get(0);
				index2 = winMove.get(1);
				board.setStone(index1);
				board.setStone(index2);
				Move move = new Move(index1, index2);
				board.makeMove(move);
				v = Math.min(v, findMove(depth-1,1, alpha, beta));
				beta = Math.min(beta, v);
				board.undo();
				board.unSetStone(index2);
				board.unSetStone(index1);
				return v;
			}
			int[] visit = new int[SIDE*SIDE];
			v = Integer.MAX_VALUE;
			ArrayList<Integer> poential1Move = board.getTSSMove(0);
			if(poential1Move.isEmpty())
				poential1Move = board.getPoentialMove();
			for(int i=0; i<poential1Move.size(); i++){
				index1 = poential1Move.get(i);
				if(board.get(index1) != EMPTY)	continue;
				visit[index1] = 1;
				board.setStone(index1);
				ArrayList<Integer> poential2Move = board.getTSSMove(0);
				if(poential2Move.isEmpty())
					poential2Move = board.getPoentialMove();
				for(int j=0; j<poential2Move.size(); j++){
					index2 = poential2Move.get(j);
					if(visit[index2]==1 || board.get(index2)!=EMPTY)	continue;
					board.setStone(index2);
					Move moves = new Move(index1, index2);
					board.makeMove(moves);
					v = Math.min(v, findMove(depth-1,1, alpha, beta));
					beta = Math.min(beta, v);
					board.undo();
					board.unSetStone(index2);
					if(beta <= alpha){
						break;
					}
				
				}
				board.unSetStone(index1);
				if(beta <= alpha){
					break;
				}
			}
		}
		return v;
	}

	ArrayList<Integer> getEmptyPoentialMove(ArrayList<Integer> poentialMove){
		ArrayList<Integer>	moves = new ArrayList<>();
 		for(int index : poentialMove){
			if(board.get(index) == EMPTY){
				moves.add(index);
			}
		}
		if(moves.size() < 2){
			for(int i=0; i<SIDE*SIDE; i++){
				if(board.get(i) == EMPTY){
					moves.add(i);
					if(moves.size() == 2)	break;
				}
			}
		}
		return moves;
	}
	 
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "岳志浩V1";
	}
	
	@Override
	public void playGame(Game game) {
		super.playGame(game);
		board = new BoardAI(); 
	}

	//閼奉亜绻佹穱婵囨箒閻ㄥ嫭顥愰惄锟�
	private BoardAI board = null;
}
