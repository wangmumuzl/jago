package s17020032009.player;

import static core.board.PieceColor.*;
//import static core.game.Move.*;

//import java.util.Random;

import core.board.Board;
import core.game.Game;
import core.game.Move;
//import core.player.Player;

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
    		//System.out.println("here1");
			Move move = firstMove();
			//board.makeMove(move);
			roadtable.MakeMove(move);
			return move;
		}
		else {
			//board.makeMove(opponentMove);
			roadtable.MakeMove(opponentMove);
			//System.out.println("here2");
		}
    	//System.out.println("开始找点");
    	Move mov;
    	do {
    		mov = roadtable.findMove();
    		int i = mov.index1();
    		int j = mov.index2();
    		if(board.get(i) == EMPTY && board.get(j) == EMPTY)break;
    	}while(true);
    	
    	//board.makeMove(mov);
    	roadtable.MakeMove(mov);
    	//System.out.println("here3");
    	return mov;
    }
    
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "李超然";  //组编号-为自己的AI所取的名字
	}
	
	@Override
	public void playGame(Game game) {
		// TODO Auto-generated method stub
		super.playGame(game);
		board = new Board();
		roadtable = new RoadTable(board);
		//roadtable.newgame(board);
	}
	
	Road road = new Road();
	Board board = new Board();
	RoadTable roadtable = new RoadTable(board);
	//RoadTable roadtable = new RoadTable();
	int [][]d = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};//方向
}
