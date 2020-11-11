package g12;

import core.board.PieceColor;

import java.util.ArrayList;

import static core.board.PieceColor.*;
import static core.game.Move.SIDE;

public class Board extends core.board.Board {


    public Board() {
        // TODO Auto-generated constructor stub
        roadTable.clear();
        for(int row =0; row < SIDE; row ++)
            for(int col = 0; col < SIDE; col ++){
                range[row][col] = 0;
                chessboard[row][col] = EMPTY;
            }
    }

    public Board(Board b) {
        super(b);
        // TODO Auto-generated constructor stub
    }

    public static boolean judge(int row, int col){
        if(row >=0 && row <=18 && col >= 0 && col <= 18)
            return true;
        else
            return false;
    }

    public int getestimate(int who){
        int value = 0;
        for(int i = 1; i < 7; i ++)
            value += roadTable.getPlayerRoads()[i][0].size()*Road.score[who][i] - roadTable.getPlayerRoads()[0][i].size() * Road.score[who^1][i];
        if(who == 0)
            return value;
        else
            return -value;
    }

    public void domove(int row, int col, int who){
        int x,y;
        if(who == 0)
            chessboard[row][col] = BLACK;
        else
            chessboard[row][col] = WHITE;
        range[row][col]++;
        for(int i = 0; i <24; i ++){
            x = row + Road.cover[i][0];
            y = col + Road.cover[i][1];
            if(judge(x,y))
                range[x][y]++;
        }

        for(int i = 0; i < 4; i ++){
            x = row;
            y = col;
            if(roadTable.getRoads()[x][y][i].isEffective()){
                Road road = roadTable.getRoads()[x][y][i];
                roadTable.removeRoad(road);
                if(who == 0)
                    road.setBlackNum(road.getBlackNum()+1);
                else
                    road.setWhiteNum(road.getWhiteNum()+1);
                roadTable.addRoad(road);

            }

            for(int j =0; j < 5; j ++){
                x = x - Road.FORWARD[i][0];
                y = y - Road.FORWARD[i][1];
                if(judge(x,y)){
                    if(roadTable.getRoads()[x][y][i].isEffective()){
                        Road road = roadTable.getRoads()[x][y][i];
                        roadTable.removeRoad(road);
                        if(who == 0)
                            road.setBlackNum(1+ road.getBlackNum());
                        else
                            road.setWhiteNum(road.getWhiteNum()+1);
                        roadTable.addRoad(road);

                    }
                }
                else
                    break;
            }
        }

    }

    public void undomove(int row,int col , int who){
        int x,y;
        chessboard[row][col] = EMPTY;
        range[row][col] --;
        for(int i = 0; i <24; i ++){
            x = row + Road.cover[i][0];
            y = col + Road.cover[i][1];
            if(judge(x,y))
                range[x][y]--;
        }

        for(int i = 0; i < 4; i ++){
            x = row;
            y = col;
            if(roadTable.getRoads()[x][y][i].isEffective()){
                Road road = roadTable.getRoads()[x][y][i];
                roadTable.removeRoad(road);
                if(who == 0)
                    road.setBlackNum(road.getBlackNum()-1);
                else
                    road.setWhiteNum(road.getWhiteNum()-1);
                roadTable.addRoad(road);

            }

            for(int j =0; j < 5; j ++){
                x = x - Road.FORWARD[i][0];
                y = y - Road.FORWARD[i][1];
                if(judge(x,y)){
                    if(roadTable.getRoads()[x][y][i].isEffective()){
                        Road road = roadTable.getRoads()[x][y][i];
                        roadTable.removeRoad(road);
                        if(who == 0)
                            road.setBlackNum(road.getBlackNum() - 1);
                        else
                            road.setWhiteNum(road.getWhiteNum() - 1);
                        roadTable.addRoad(road);

                    }
                }
                else
                    break;
            }
        }




    }

    PieceColor getchess(int row,int col){
        return chessboard[row][col];
    }


    public void fairmove(int row,int col,int who){
        int x,y;
        if(who == 0)
            chessboard[row][col] = BLACK;
        else
            chessboard[row][col] = WHITE;

        for(int i = 0; i < 4; i ++){
            x = row;
            y = col;
            if(roadTable.getRoads()[x][y][i].isEffective()){
                Road road = roadTable.getRoads()[x][y][i];
                roadTable.removeRoad(road);
                if(who == 0)
                    road.setBlackNum(road.getBlackNum()+1);
                else
                    road.setWhiteNum(road.getWhiteNum()+1);
                roadTable.addRoad(road);

            }

            for(int j =0; j < 5; j ++){
                x = x - Road.FORWARD[i][0];
                y = y - Road.FORWARD[i][1];
                if(judge(x,y)){
                    if(roadTable.getRoads()[x][y][i].isEffective()){
                        Road road = roadTable.getRoads()[x][y][i];
                        roadTable.removeRoad(road);
                        if(who == 0)
                            road.setBlackNum(1+ road.getBlackNum());
                        else
                            road.setWhiteNum(road.getWhiteNum()+1);
                        roadTable.addRoad(road);

                    }
                }
                else
                    break;
            }
        }
        

    }

    public void undofairmove(int row,int col,int who){
        int x,y;
        chessboard[row][col] = EMPTY;

        for(int i = 0; i < 4; i ++){
            x = row;
            y = col;
            if(roadTable.getRoads()[x][y][i].isEffective()){
                Road road = roadTable.getRoads()[x][y][i];
                roadTable.removeRoad(road);
                if(who == 0)
                    road.setBlackNum(road.getBlackNum()-1);
                else
                    road.setWhiteNum(road.getWhiteNum()-1);
                roadTable.addRoad(road);

            }

            for(int j =0; j < 5; j ++){
                x = x - Road.FORWARD[i][0];
                y = y - Road.FORWARD[i][1];
                if(judge(x,y)){
                    if(roadTable.getRoads()[x][y][i].isEffective()){
                        Road road = roadTable.getRoads()[x][y][i];
                        roadTable.removeRoad(road);
                        if(who == 0)
                            road.setBlackNum(road.getBlackNum() - 1);
                        else
                            road.setWhiteNum(road.getWhiteNum() - 1);
                        roadTable.addRoad(road);

                    }
                }
                else
                    break;
            }
        }

    }

    //更新黑白路表
    public void updateroad(){

        for(int row = 0; row < SIDE; row ++)
            for(int col = 0; col < SIDE; col ++)
                for(int dir = 0; dir < 4; dir ++)
                    if(roadTable.getRoads()[row][col][dir].getBlackNum() > 0&& roadTable.getRoads()[row][col][dir].getWhiteNum() > 0)
                        roadTable.getRoads()[row][col][dir].setEffective(false);

    }

    public int getffthreat(int who){
        RoadList liansi = new RoadList();
        RoadList lianwu = new RoadList();
        RoadList threatlist = new RoadList();
        if(who == 0){
            liansi = roadTable.getPlayerRoads()[0][4];
            lianwu = roadTable.getPlayerRoads()[0][5];
        }
        else {
            liansi = roadTable.getPlayerRoads()[4][0];
            lianwu = roadTable.getPlayerRoads()[5][0];
        }
        if(liansi.size() + lianwu.size() == 0)
            return 0;
        if(liansi.size() == 0)
            threatlist = lianwu;
        else
            threatlist = liansi;
        for(int i = 0; i < 6; i ++){
            int x = threatlist.get(0).getRow() + i * Road.FORWARD[threatlist.get(0).getDir()][0];
            int y = threatlist.get(0).getCol() + i * Road.FORWARD[threatlist.get(0).getDir()][1];
            if(!judge(x,y))
                continue;
            if(getchess(x,y) != EMPTY)
                continue;
            fairmove(x,y,who);
            int number = liansi.size() + lianwu.size();
            undofairmove(x,y,who);
            if(number == 0)
                return 1;
        }
        return 2;

    }


    public int getAllthreats(int who){
        for(int i = 0; i < SIDE; i ++)
            for(int j = 0; j < SIDE; j ++)
                isempty[i][j] = false;

        points.clear();
        RoadList liansi = new RoadList();
        RoadList lianwu = new RoadList();
        RoadList threatlist = new RoadList();
        if(who == 0){
            liansi = roadTable.getPlayerRoads()[0][4];
            lianwu = roadTable.getPlayerRoads()[0][5];
        }
        else {
            liansi = roadTable.getPlayerRoads()[4][0];
            lianwu = roadTable.getPlayerRoads()[5][0];
        }
        if(liansi.size() + lianwu.size() == 0)
            return 0;
        if(liansi.size() == 0)
            threatlist = lianwu;
        else
            threatlist = liansi;
        for(int i = 0; i < 6; i ++){
            int x = threatlist.get(0).getRow() + i * Road.FORWARD[threatlist.get(0).getDir()][0];
            int y = threatlist.get(0).getCol() + i * Road.FORWARD[threatlist.get(0).getDir()][1];
            if(!judge(x,y))
                continue;
            if(getchess(x,y) != EMPTY)
                continue;
            fairmove(x,y,who);
            int number = liansi.size() + lianwu.size();
            undofairmove(x,y,who);
            if(number == 0)
                return 1;
        }
        for (int i = 0; i < lianwu.size(); i ++){
            for(int j = 0;  j < 6; j ++){
                int x = lianwu.get(i).getRow() + j *Road.FORWARD[lianwu.get(i).getDir()][0];
                int y = lianwu.get(i).getRow() + j * Road.FORWARD[lianwu.get(i).getDir()][1];
                if(!judge(x,y))
                    continue;
                if(getchess(x,y) != EMPTY)
                    continue;
                Point point = new Point(x,y,0);
                if(!isempty[x][y]){
                    isempty[x][y] = true;
                    points.add(point);
                }


            }
        }
        for (int i = 0; i < liansi.size(); i ++){
            for(int j = 0;  j < 6; j ++){
                int x = liansi.get(i).getRow() + j *Road.FORWARD[liansi.get(i).getDir()][0];
                int y = liansi.get(i).getRow() + j * Road.FORWARD[liansi.get(i).getDir()][1];
               if(!judge(x,y))
                   continue;
                if(getchess(x,y) != EMPTY)
                    continue;
                Point point = new Point(x,y,0);
                if(!isempty[x][y]){
                    isempty[x][y] = true;
                    points.add(point);
                }


            }
        }

        boolean flag = false;
        int count = points.size();
        for(int i = 0; i < count && !flag; i ++){
            for(int  j = i + 1; j < count&&!flag; j ++){
                fairmove(points.get(i).getRow(),points.get(i).getCol(),who);
                fairmove(points.get(j).getRow(),points.get(j).getCol(),who);
                if(liansi.size() + lianwu.size() == 0)
                    flag = true;
                undofairmove(points.get(i).getRow(),points.get(i).getCol(),who);
                undofairmove(points.get(j).getRow(),points.get(j).getCol(),who);
            }
        }

        if(flag)
            return 2;
        else
            return 3;


    }


    public Point getbestpoint(int who){
        Point bestpoint = new Point();
        Point point = new Point();
        bestpoint.setScore(Integer.MIN_VALUE);
        for(int row = 0; row < SIDE; row ++)
            for(int col = 0; col < SIDE; col ++){
                if(range[row][col] > 0&& chessboard[row][col] == EMPTY) {
                    point = new Point(row, col, 0);
                    fairmove(row, col, who);
                    point.setScore(getestimate(who));
                    if (point.getScore() > bestpoint.getScore())
                        bestpoint = point;
                    undofairmove(row, col, who);
                }
            }
        return bestpoint;
    }


    public int[][] getRange() {
        return range;
    }

    public void setRange(int[][] range) {
        this.range = range;
    }

    public boolean[][] getIsempty() {
        return isempty;
    }

    public void setIsempty(boolean[][] isempty) {
        this.isempty = isempty;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public PieceColor[][] getChessboard() {
        return chessboard;
    }

    public void setChessboard(PieceColor[][] chessboard) {
        this.chessboard = chessboard;
    }

    public RoadTable getRoadTable() {
        return roadTable;
    }

    public void setRoadTable(RoadTable roadTable) {
        this.roadTable = roadTable;
    }

    public MyMove getbestmove(int who){
        Point firstpoint = getbestpoint(who);
        fairmove(firstpoint.getRow(),firstpoint.getCol(),who);
        //int index1 = firstpoint.getCol() * SIDE + firstpoint.getRow();
        Point secondpoint = getbestpoint(who);
        fairmove(secondpoint.getRow(),secondpoint.getCol(),who );
       // int index2 = secondpoint.getCol() * SIDE + secondpoint.getRow();
        MyMove bestmove = new MyMove(firstpoint.getRow(),firstpoint.getCol(),secondpoint.getRow(),secondpoint.getCol());
        undofairmove(secondpoint.getRow(),secondpoint.getCol(),who);
        undofairmove(firstpoint.getRow(),firstpoint.getCol(),who);
        return bestmove;
    }


    private int [][]range = new int[SIDE][SIDE];
    private boolean [][]isempty = new boolean[SIDE][SIDE];
    private ArrayList<Point> points = new ArrayList<>();
    private PieceColor [][] chessboard = new PieceColor[SIDE][SIDE];


    private RoadTable roadTable = new RoadTable();

}
