package g12;

import java.util.ArrayList;

import core.board.Board;
import core.board.PieceColor;

import static core.board.PieceColor.*;

public class Road {

    //ĳ���ĸ�ǰ�������λ���������˷���  = -ǰ������
    //ǰ�������£� �ң����£����ϣ�  {col������row����}
    public static final int FORWARD[][] = {
            {0, 1}, {1, 0}, {1, 1}, {1, -1}
    };

    //ĳ��ֱ��Ӱ�쵽�ķ�Χ����8����������ڸõ��λ����
    public static final int DIRECTIONS[][] = {
            //�£� �ң����£�����
            {0, 1}, {1, 0}, {1, 1}, {1, -1},
            //�ϣ������ϣ� ����
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

    //���·�����һ������
    public void addStone(PieceColor stone) {
        if (stone == BLACK) blackNum++;
        else if (stone == WHITE) whiteNum++;
    }
    //�Ӹ�·���Ƴ�һ������
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

    private int startPos; 	//��·�����ı�� ��0~360��
    private boolean effective;

    public boolean isEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    private int row;
    private int col;
    private	int dir;   	  	//��·�ķ�������ǰ������ �£��ң����£�����

    private int blackNum; 	//��·�к��ӵĸ���
    private int whiteNum; 	//��·�а��ӵĸ���

    private int index = 0; 		//��·������Ӧ�ĺڰ�·���е��±�; �ڸ�·����ӵ��ڰ�·����ʱȷ��


    /**
     * ��·��Ѱ�ҿ�λ����Щ�Ѿ�������·�б������Ŀ�λ������������
     * @param b
     * @param vis
     * @return
     */
    public ArrayList<Point> findEmptyPos(Board b, boolean[] vis) {
        ArrayList<Point> points = new ArrayList<>();
        return points;
    }
}
