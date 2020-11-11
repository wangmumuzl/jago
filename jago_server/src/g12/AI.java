package g12;

import core.board.PieceColor;
import core.game.Game;
import core.game.Move;

public class AI extends core.player.AI{
    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 2;
    /**
     * A position magnitude indicating a win (for white if positive, black if
     * negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;



    @Override
    public Move findMove(Move opponentMove) {
        int row0,col0,row1,col1,index1,index2;
        PieceColor mycolor = getColor();
        int myid;
        if(mycolor == PieceColor.BLACK)
            myid =0;
        else
            myid = 1;
        int opid = myid ^ 1;

        if (opponentMove == null) {
            Move move = firstMove();
            board.makeMove(move);
            row0 = move.index1()/19;
            col0 = move.index1()%19;
            row1 = move.index2()/19;
            col1 = move.index2()%19;
            board.domove(row0,col0,myid);
            board.domove(row1,col1,myid);
            board.updateroad();
            return move;
        }
        else {
            board.makeMove(opponentMove);
            row0 = opponentMove.index1()/19;
            col0 = opponentMove.index1()%19;
            row1 = opponentMove.index2()/19;
            col1 = opponentMove.index2()%19;
            board.domove(row0,col0,opid);
            board.domove(row1,col1,opid);
            board.updateroad();
        }




        MyMove mymove = ts.findbestmove(myid,board);
        if(mymove != null){
            index1 = mymove.getRow0() * 19 + mymove.getCol0();
            index2 =mymove.getRow1() * 19 + mymove.getCol1();
            Move move = new Move(index1,index2);
            board.makeMove(move);
            board.domove(mymove.getRow0(),mymove.getCol0(),myid);
            board.domove(mymove.getRow1(),mymove.getCol1(),myid);
            board.updateroad();
            return move;
        }
        else {
            mymove = board.getbestmove(myid);
            index1 = mymove.getRow0() * 19 + mymove.getCol0();
            index2 =mymove.getRow1() * 19 + mymove.getCol1();
            Move move = new Move(index1,index2);
            board.makeMove(move);
            board.domove(mymove.getRow0(),mymove.getCol0(),myid);
            board.domove(mymove.getRow1(),mymove.getCol1(),myid);
            board.updateroad();
            return move;
        }

    }

    /**
     * The move found by the last call to one of the ...FindMove methods below.
     */
    public AI(){

    }
    private Move _lastFoundMove;

    private int findMove(int depth, int sense, int alpha, int beta) {
        return 0;
    }

    @Override
    public String name() {
        return "G12";
    }

    @Override
    public void playGame(Game game) {
        super.playGame(game);
        board = new Board();
        board.getRoadTable().clear();
        board.domove(9,9,0);
        board.updateroad();

    }

    //自己保有的棋盘
    private Board board = null;
    private ThreatSearch ts = new ThreatSearch();
}
