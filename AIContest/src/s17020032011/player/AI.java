package s17020032011.player;


import static core.board.PieceColor.*;
import static core.game.Move.*;

import java.util.ArrayList;
import java.util.Random;

import core.board.Board;
import core.game.Game;
import core.game.Move;
import core.player.Player;

/* A player who plays by throwing dice*/
public class AI extends core.player.AI {

//    /** A new AI for GAME that will play MYCOLOR. */
//	public Dicer(Game game, PieceColor myColor) {
//        super(game, myColor, false);
//    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    @Override
    public Move findMove(Move opponentMove) {
    	if (opponentMove == null) {
			Move move = firstMove();
			board.makeMove(move);
			board.getScore();
			return move;
		}
		else {
			board.makeMove(opponentMove);
			board.getScore();
		}

        	System.out.println("进入");
        	
        	Move mov = Find();

        	
        	System.out.println("退出");
        	board.makeMove(mov);
        	return mov;
    }
    

    
    
    
  public Move Find() {
	  Move m = null;
	  int a=Integer.MIN_VALUE;
	  int b=Integer.MAX_VALUE;
	  int max=a;
	  for(Move move : board.getMove()) {
		  board.makeMove(move);
		  int c=alphabeta(false, 1, board, a, b);
		  //System.out.println("score"+c);
		  if(c>max) {
			  max=c;
			  m=move;
		  }
		  board.undo();
		  board.changeRoads(move.index1());
		  board.changeRoads(move.index2());
	  }
	  return m;
}
    
    
	private int alphabeta(boolean flag, int depth, Board4AI board, int a,int b) {
		if(board.gameOver()||depth==0) {
			return board.getScore();
		}
		if(flag) {
			for(Move move : board.getMove()) {
				//System.out.println("长度"+board.getMove().size());
				board.makeMove(move);
				int tmp = alphabeta(false, depth - 1, board, a, b);
				board.undo();
				board.changeRoads(move.index1());
				board.changeRoads(move.index2());
				if (a< tmp) {
					a=tmp;
				}
				if (b<= a) {
					return a;
				}
			}
			return a;
		}else {
			for(Move move : board.getMove()) {
				//System.out.println("长度"+board.getMove().size());
				board.makeMove(move);
				int tmp = alphabeta(true, depth - 1, board, a, b);
				board.undo();
				board.changeRoads(move.index1());
				board.changeRoads(move.index2());
				if (b> tmp) {
					b=tmp;
				}
				if (b<= a) {
					return b;
				}
			}
			return b;
			
		}
	}

    
    
    
    
 


    
    
    

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "潘星宇";  //组编号-为自己的AI所取的名字
	}
	
	@Override
	public void playGame(Game game) {
		// TODO Auto-generated method stub
		super.playGame(game);
		board = new Board4AI();
	}
	
	Board4AI board = new Board4AI();
}