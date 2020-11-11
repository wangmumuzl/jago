package g12;

import java.util.ArrayList;

import static core.board.PieceColor.EMPTY;
import static core.game.Move.SIDE;
import static g12.Board.judge;

public class GenerateMove {
    ArrayList<MyMove> moves = new ArrayList<>();
    ArrayList<Point>points = new ArrayList<>();
    boolean [][]isempty = new boolean[SIDE][SIDE];

    public void createmove(Board board,int who){
        moves.clear();
        points.clear();
        getwinmove(board,who);
        if(moves.size() != 0)
            return;
        int numberofthreat = board.getAllthreats(who);
        if(numberofthreat == 1)
            disableonethreat(board,who);
        else if(numberofthreat >= 2)
            disabletwothreat(board,who);
        else
            isbestpoint(board,who);
        sortmoves();



    }

    public void createthreat(Board board,int who){
        moves.clear();
        points.clear();
        getwinmove(board,who);
        if(moves.size() != 0)
            return;
        int numofthreat = board.getffthreat(who);
        if (numofthreat == 0)
            getwothreat(board,who);
        else
            disabletwothreat(board,who);

    }




    public void getwinmove(Board board,int who) {
        RoadList liansi = new RoadList();
        RoadList lianwu = new RoadList();
        if (who == 0){
            liansi = board.getRoadTable().getPlayerRoads()[4][0];
            lianwu = board.getRoadTable().getPlayerRoads()[5][0];
        }
        else {
            liansi = board.getRoadTable().getPlayerRoads()[0][4];
            lianwu = board.getRoadTable().getPlayerRoads()[0][5];
        }
        MyMove move = new MyMove();
        if(liansi.size() != 0){
            for(int i = 0, j = 0; i < 6; i ++){
                int x = liansi.get(0).getRow() + i * Road.FORWARD[liansi.get(0).getDir()][0];
                int y = liansi.get(0).getCol() + i * Road.FORWARD[liansi.get(0).getDir()][1];
                if(!judge(x,y))
                    continue;
                if(board.getchess(x,y) != EMPTY)
                    continue;
                if(j == 0){
                    move.setRow0(x);
                    move.setCol0(y);
                    j ++;
                }
                else {
                    move.setRow1(x);
                    move.setCol1(y);
                }
            }

            board.fairmove(move.getRow0(),move.getCol0(),who);
            board.fairmove(move.getRow1(),move.getCol1(),who);
            move.setScore(board.getestimate(who));
            board.undofairmove(move.getRow1(),move.getCol1(),who);
            board.undofairmove(move.getRow0(),move.getCol0(),who);
            moves.add(move);


        }


        if(lianwu.size() != 0 && moves.size() == 0){
            move = board.getbestmove(who);
            for(int i = 0; i < 6; i ++){
                int x = lianwu.get(0).getRow() + i * Road.FORWARD[lianwu.get(0).getDir()][0];
                int y = lianwu.get(0).getCol() + i * Road.FORWARD[lianwu.get(0).getDir()][1];
                if(!judge(x,y))
                    continue;
                if(board.getchess(x,y) != EMPTY)
                    continue;
                move.setRow0(x);
                move.setCol0(y);
            }
            board.fairmove(move.getRow0(),move.getCol0(),who);
            board.fairmove(move.getRow1(),move.getCol1(),who);
            move.setScore(board.getestimate(who));
            board.undofairmove(move.getRow1(),move.getCol1(),who);
            board.undofairmove(move.getRow0(),move.getCol0(),who);
            moves.add(move);

        }


    }


   public void sortmoves(){
        moves.sort(MyMove.scoreComparatorDesc);
   }

   public void sortpoints(){
        points.sort(Point.scoreComparator);
   }

   public void findEmpty(Road road, Board board){
        for(int i = 0; i < 6; i ++){
            int x = road.getRow() + i * Road.FORWARD[road.getDir()][0];
            int y = road.getCol() + i * Road.FORWARD[road.getDir()][1];
            if(!judge(x,y))
                continue;
            if(board.getchess(x,y) != EMPTY)
                continue;
            if(!isempty[x][y]){
                Point point = new Point(x,y,0);
                points.add(point);
                isempty[x][y] = true;
            }
        }
   }


   public void getcanthreat(Board board,int who){
        RoadList lianer = new RoadList();
        RoadList liansan = new RoadList();
        if(who == 0){
            lianer = board.getRoadTable().getPlayerRoads()[2][0];
            liansan = board.getRoadTable().getPlayerRoads()[3][0];
        }
        else {
            lianer = board.getRoadTable().getPlayerRoads()[0][2];
            liansan = board.getRoadTable().getPlayerRoads()[0][3];
        }
        for(int i = 0; i < lianer.size(); i ++)
            findEmpty(lianer.get(i),board);
        for(int i =0; i < liansan.size(); i ++)
            findEmpty(liansan.get(i),board);
   }


   public void disabletwothreat(Board board, int who){
       RoadList liansi = new RoadList();
       RoadList lianwu = new RoadList();
       if (who == 0){
           liansi = board.getRoadTable().getPlayerRoads()[0][4];
           lianwu = board.getRoadTable().getPlayerRoads()[0][5];
       }
       else {

           liansi = board.getRoadTable().getPlayerRoads()[4][0];
           lianwu = board.getRoadTable().getPlayerRoads()[5][0];
       }
       points.clear();
       for(int i = 0; i < SIDE; i ++)
           for(int j = 0; j < SIDE; j ++)
               isempty[i][j] = false;

       for(int i = 0; i < liansi.size(); i ++)
           findEmpty(liansi.get(i),board);

       for(int i = 0; i < lianwu.size(); i ++)
           findEmpty(lianwu.get(i),board);
       int pointsize = points.size();
       for(int i = 0; i < pointsize; i ++){
           for(int j = i + 1; j < pointsize; j ++){
               board.fairmove(points.get(i).getRow(),points.get(i).getCol(),who);
               board.fairmove(points.get(j).getRow(),points.get(j).getCol(),who);
               if(liansi.size() + lianwu.size() == 0){
                   MyMove move = new MyMove(points.get(i).getRow(),points.get(i).getCol(),points.get(j).getRow(),points.get(j).getCol());
                   move.setScore(board.getestimate(who));
                   moves.add(move);
               }
               board.undofairmove(points.get(j).getRow(),points.get(j).getCol(),who);
               board.undofairmove(points.get(i).getRow(),points.get(i).getCol(),who);

           }
       }
       if(moves.size() == 0){
           MyMove move = board.getbestmove(who);
           board.fairmove(move.getRow0(),move.getCol0(),who);
           board.fairmove(move.getRow1(),move.getCol1(),who);
           move.setScore(board.getestimate(who));
           board.undofairmove(move.getRow1(),move.getCol1(),who);
           board.undofairmove(move.getRow0(),move.getCol0(),who);
           moves.add(move);

       }

   }
   public void disableonethreat(Board board, int who){
       RoadList liansi = new RoadList();
       RoadList lianwu = new RoadList();
       if (who == 0){
           liansi = board.getRoadTable().getPlayerRoads()[0][4];
           lianwu = board.getRoadTable().getPlayerRoads()[0][5];
       }
       else {

           liansi = board.getRoadTable().getPlayerRoads()[4][0];
           lianwu = board.getRoadTable().getPlayerRoads()[5][0];
       }
       points.clear();
       for(int i = 0; i < SIDE; i ++)
           for(int j = 0; j < SIDE; j ++)
               isempty[i][j] = false;

       for(int i = 0; i < liansi.size(); i ++)
           findEmpty(liansi.get(i),board);

       for(int i = 0; i < lianwu.size(); i ++)
           findEmpty(lianwu.get(i),board);
       int pointsize = points.size();
       isgoodpoints(board,who);
       int tps = pointsize + (points.size() - pointsize);
       for(int i = 0; i < pointsize; i ++){
           board.fairmove(points.get(i).getRow(),points.get(i).getCol(),who);
           if(liansi.size() + lianwu.size() == 0){
               for(int j = i + 1; j < tps; j ++){
                   board.fairmove(points.get(j).getRow(),points.get(j).getCol(),who);
                   MyMove move = new MyMove(points.get(i).getRow(),points.get(i).getCol(),points.get(j).getRow(),points.get(j).getCol());
                   move.setScore(board.getestimate(who));
                   moves.add(move);
                   board.undofairmove(points.get(j).getRow(),points.get(j).getCol(),who);

               }
           }
           board.undofairmove(points.get(i).getRow(),points.get(i).getCol(),who);
       }

   }

   public void isgoodpoints(Board board,int who){
        RoadList lianer = new RoadList();
        RoadList liansan = new RoadList();
        RoadList wolianyi = new RoadList();
       RoadList wolianer = new RoadList();
       RoadList woliansan = new RoadList();
       if(who == 0){
           lianer = board.getRoadTable().getPlayerRoads()[0][2];
           liansan = board.getRoadTable().getPlayerRoads()[0][3];
           wolianyi = board.getRoadTable().getPlayerRoads()[1][0];
           wolianer = board.getRoadTable().getPlayerRoads()[2][0];
           woliansan = board.getRoadTable().getPlayerRoads()[3][0];
       }
       else {
           lianer = board.getRoadTable().getPlayerRoads()[2][0];
           liansan = board.getRoadTable().getPlayerRoads()[3][0];
           wolianyi = board.getRoadTable().getPlayerRoads()[0][1];
           wolianer = board.getRoadTable().getPlayerRoads()[0][2];
           woliansan = board.getRoadTable().getPlayerRoads()[0][3];
       }
       for(int i = 0; i < lianer.size(); i ++)
           findEmpty(lianer.get(i),board);

       for(int i = 0; i < liansan.size(); i ++)
           findEmpty(liansan.get(i),board);

       for(int i = 0; i < wolianyi.size(); i ++)
           findEmpty(wolianyi.get(i),board);

       for(int i = 0; i < wolianer.size(); i ++)
           findEmpty(wolianer.get(i),board);

       for(int i = 0; i < woliansan.size(); i ++)
           findEmpty(woliansan.get(i),board);

       int ps = points.size();
       for(int i = 0; i < ps; i ++){
           board.fairmove(points.get(i).getRow(),points.get(i).getCol(),who);
           points.get(i).setScore(board.getestimate(who));
           board.undofairmove(points.get(i).getRow(),points.get(i).getCol(),who);

       }



   }

   public void isbestpoint(Board board, int who){
        points.clear();
        for(int i = 0; i < SIDE; i ++)
           for(int j = 0; j < SIDE; j ++)
               isempty[i][j] = false;
       isgoodpoints(board,who);
       sortpoints();
       int ps = points.size();
       int count = 0;
       int tps = ps;
       for(int i = 0;  i< ps; i ++)
           count += points.get(i).getScore();
       count = count / ps;
       for(int i = 0; i < ps; i ++)
           if(points.get(i).getScore() < count){
               tps = i;
               break;
           }
       for(int i = 0; i < ps; i ++){
           for(int j = i + 1; j < tps; j ++){
               board.fairmove(points.get(i).getRow(),points.get(i).getCol(),who);
               board.fairmove(points.get(j).getRow(),points.get(j).getCol(),who);
               MyMove move  = new MyMove(points.get(i).getRow(),points.get(i).getCol(),points.get(j).getRow(),points.get(j).getCol());
               moves.add(move);
               board.undofairmove(points.get(j).getRow(),points.get(j).getCol(),who);
               board.undofairmove(points.get(i).getRow(),points.get(i).getCol(),who);

           }
           tps--;
       }

   }

   public void getwothreat(Board board,int who){
        int opposent = who ^ 1;
        points.clear();
       for(int i = 0; i < SIDE; i ++)
           for(int j = 0; j < SIDE; j ++)
               isempty[i][j] = false;
       getcanthreat(board,who);
       int ps = points.size();
       for(int i = 0; i < ps; i ++)
           for(int j = i + 1;  j< ps; j ++){
               board.fairmove(points.get(i).getRow(),points.get(i).getCol(),who);
               board.fairmove(points.get(j).getRow(),points.get(j).getCol(),who);
               if(board.getffthreat(opposent) >= 2){
                   MyMove move = new MyMove(points.get(i).getRow(),points.get(i).getCol(),points.get(j).getRow(),points.get(j).getCol());
                   moves.add(move);
               }
               board.undofairmove(points.get(i).getRow(),points.get(i).getCol(),who);
               board.undofairmove(points.get(j).getRow(),points.get(j).getCol(),who);
           }
   }



}
