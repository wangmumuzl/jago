package g12;

import java.util.ArrayList;

import core.board.Board;
import core.board.PieceColor;

import static core.board.PieceColor.*;

public class Road {

    //某点四个前进方向的位移量；后退方向  = -前进方向
    //前进方向：下， 右，右下，右上；  {col增量，row增量}
    public static final int FORWARD[][] = {
            {0, 1}, {1, 0}, {1, 1}, {1, -1}
    };

    //某点直接影响到的范围，共8个方向，相对于该点的位移量
    public static final int DIRECTIONS[][] = {
            //下， 右，右下，右上
            {0, 1}, {1, 0}, {1, 1}, {1, -1},
            //上，左，左上， 左下
            {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}
    };

    public static final int score[][] = {{ 0, 9, 520, 2070, 7890, 10020, 1000000 },
            { 0, 3, 480, 2670, 3887, 4900, 1000000 }};

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public void setBlackNum(int blackNum) {
        this.blackNum = blackNum;
    }

    public void setWhiteNum(int whiteNum) {
        this.whiteNum = whiteNum;
    }

    public static final int cover[][]={
            { 1, 0 },
            { 0, 1 },
            { -1, 0 },
            { 0, -1 },
            { 1, -1 },
            { -1, 1 },
            { 1, 1 },
            { -1, -1 },
            { 2, 0 },
            { 0, 2 },
            { -2, 0 },
            { 0, -2 },
            { 2, -2 },
            { -2, 2 },
            { 2, 2 },
            { -2, -2 },
            { 3, 0 },
            { 0, 3 },
            { -3, 0 },
            { 0, -3 },
            { 3, -3 },
            { -3, 3 },
            { 3, 3 },
            { -3, -3 }
    };

    public Road() {
        // TODO Auto-generated constructor stub
    }

    public Road(int startPos, int dir, int blackNum, int whiteNum, int index,boolean effective) {
        super();
        this.startPos = startPos;
        this.dir = dir;
        this.blackNum = blackNum;
        this.whiteNum = whiteNum;
        this.index = index;
        this.effective = effective;
    }
    public Road(int row,int col,int dir, int blackNum,int whiteNum,int index,boolean effective){
        this.row = row;
        this.col = col;
        this.dir = dir;
        this.blackNum = blackNum;
        this.whiteNum = whiteNum;
        this.index = index;
        this.effective = effective;

    }

    //向该路中添加一个棋子
    public void addStone(PieceColor stone) {
        if (stone == BLACK) blackNum++;
        else if (stone == WHITE) whiteNum++;
    }
    //从该路中移除一个棋子
    public void removeStone(PieceColor stone) {
        if (stone == BLACK) blackNum--;
        else if (stone == WHITE) whiteNum--;
    }

    public int getBlackNum() {
        return blackNum;
    }
    public int getWhiteNum() {
        return whiteNum;
    }

    public boolean isEmpty() {
        return blackNum == 0 && whiteNum == 0;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getDir() {
        return dir;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    private int startPos; 	//该路的起点的编号 （0~360）
    private boolean effective;

    public boolean isEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    private int row;
    private int col;
    private	int dir;   	  	//该路的方向，起点的前进方向： 下，右，右下，右上

    private int blackNum; 	//该路中黑子的个数
    private int whiteNum; 	//该路中白子的个数

    private int index = 0; 		//该路在其相应的黑白路表中的下标; 在该路被添加到黑白路表中时确定


    /**
     * 从路中寻找空位。那些已经在其他路中遍历过的空位，不计算在内
     * @param b
     * @param vis
     * @return
     */
    public ArrayList<Point> findEmptyPos(Board b, boolean[] vis) {
        ArrayList<Point> points = new ArrayList<>();
        return points;
    }
}
