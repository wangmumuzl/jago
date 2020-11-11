package g12;

import java.util.ArrayList;

public class ThreatSearch {
    private int copposent;
    private int score;
    private boolean found;
    ArrayList<MyMove> moves = new ArrayList<>();
    ArrayList<MyMove> stepmoves = new ArrayList<>();
    MyMove bestmove = new MyMove();

    public ThreatSearch(){
        score = 2;
        found = false;

    }

    public MyMove findbestmove(int who,Board board){
        copposent = who;
        for(int i = 1; i <= 13; i = i + 2){
            bestmove = null;
            if(Threatsearch(who,board,i))
                return bestmove;
        }

        stepmoves.clear();
        copposent = copposent ^ 1;
        found = false;
        int value = alphaBeta(6-score,Integer.MIN_VALUE,Integer.MAX_VALUE,who,board);
        if(!found)
            bestmove = stepmoves.get(0);
        return bestmove;


    }


    public int alphaBeta(int depth,int alpha,int beta,int who,Board board){
        if(depth == 0)
            return board.getestimate(who);
        int maxandmin = Integer.MIN_VALUE;
        boolean flag = false;
        GenerateMove move = new GenerateMove();
        move.createmove(board,who);
        int movesize = move.moves.size();
        movesize = Math.min(movesize, 100 + score * 6);
        if(depth == 6 - score)
            for(int i =0; i < movesize; i++)
                stepmoves.add(move.moves.get(i));
        for(int i = 0; i < movesize; i ++){
            board.domove(move.moves.get(i).getRow0(),move.moves.get(i).getCol0(),who);
            board.domove(move.moves.get(i).getRow1(),move.moves.get(i).getCol1(),who);
            if(flag){
                maxandmin = -alphaBeta(depth -1,-alpha-1,-alpha, who^1,board);
                if(maxandmin > alpha && maxandmin < beta)
                    maxandmin = -alphaBeta(depth -1,-beta,-alpha,who^1,board);
            }
            else
                maxandmin = -alphaBeta(depth -1,-beta,-alpha,who ^1,board);
            board.undomove(move.moves.get(i).getRow0(),move.moves.get(i).getCol0(),who);
            board.undomove(move.moves.get(i).getRow1(),move.moves.get(i).getCol1(),who);

            if(maxandmin >= beta)
                return beta;
            if(maxandmin > alpha){
                alpha = maxandmin;
                flag =true;
                if(depth == 6 - score){
                    board.domove(move.moves.get(i).getRow0(),move.moves.get(i).getCol0(),who);
                    board.domove(move.moves.get(i).getRow1(),move.moves.get(i).getCol1(),who);

                    if(!Threatsearch(who^1,board,15)){
                        bestmove = move.moves.get(i);
                        found  = true;
                    }
                    board.undomove(move.moves.get(i).getRow0(),move.moves.get(i).getCol0(),who);
                    board.undomove(move.moves.get(i).getRow1(),move.moves.get(i).getCol1(),who);

                }
            }


        }
        return alpha;

    }
    public boolean Threatsearch(int who,Board board,int depth){
        int opponent = who ^ 1;
        int result;
        if(copposent == 0)
            result = board.getRoadTable().getPlayerRoads()[6][0].size();
        else
            result = board.getRoadTable().getPlayerRoads()[0][6].size();
        if(result > 0){
           // System.out.println(moves.size());
            bestmove = moves.get(0);
            return true;
        }
        if(who == copposent && board.getAllthreats(who) > 0&&board.getAllthreats(opponent) == 0)
            return false;
        if(who == (copposent ^1) && board.getAllthreats(who) >= 3){
            bestmove = moves.get(0);
            return true;
        }
        if(depth == 0)
            return false;
        GenerateMove mymove = new GenerateMove();
        mymove.createthreat(board,who);
        int count = mymove.moves.size();
        for(int i = 0; i < count; i ++){
            board.domove(mymove.moves.get(i).getRow0(),mymove.moves.get(i).getCol0(),who);
            board.domove(mymove.moves.get(i).getRow1(),mymove.moves.get(i).getCol1(),who);
            moves.add(mymove.moves.get(i));
            boolean flag = Threatsearch(opponent,board,depth - 1);
            moves.remove(moves.size() - 1);
            board.undomove(mymove.moves.get(i).getRow1(),mymove.moves.get(i).getCol1(),who);
            board.undomove(mymove.moves.get(i).getRow0(),mymove.moves.get(i).getCol0(),who);
            if(who == copposent){
                flag = false || flag;
                if(flag)
                    return true;

            }
            else {
                flag = true && flag;
                if(!flag)
                    return false;
            }




        }
        return false;

    }

}
