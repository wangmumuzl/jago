package baseline.player;

import java.util.ArrayList;

import core.board.Board;
import core.board.PieceColor;

import static core.board.PieceColor.*;
import static core.game.Move.*;
import static core.board.Board.FORWARD;
public class Road {
	
	//某点直接影响到的范围，共8个方向，相对于该点的位移量
	public static final int DIRECTIONS[][] = {
		//下， 右，右下，右上
		{0, 1}, {1, 0}, {1, 1}, {1, -1},
		//上，左，左上， 左下
		{0, -1}, {-1, 0}, {-1, -1}, {-1, 1} 
	};
	
	
	public Road() {
		// TODO Auto-generated constructor stub
	}

	public Road(int startPos, int dir, int blackNum, int whiteNum, int index) {
		super();
		this.startPos = startPos;
		this.dir = dir;
		this.blackNum = blackNum;
		this.whiteNum = whiteNum;
		this.index = index;
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

	private int startPos; 	//该路的起点的编号 （0~360）
	private	int dir;   	  	//该路的方向，起点的前进方向： 下，右，右下，右上

	private int blackNum; 	//该路中黑子的个数
	private int whiteNum; 	//该路中白子的个数
	
	private int index; 		//该路在其相应的黑白路表中的下标; 在该路被添加到黑白路表中时确定

	/**
	 * 当前路必须是白方或黑方的4路或者5路，从中寻找空位，形成胜着.
	 * 4路有两个空位，5路有一个空位
	 * @param b
	 * @return
	 */
	public ArrayList<Point> findEmptyPos(Board b) {
		ArrayList<Point> points = new ArrayList<>();
		//从当前位置开始，沿dir方向，一次前进1个位置，寻找空位
		for (int i = 0; i < 6; i++){
			//从当前路的开始位置，沿dir方向，向前移动i个点
			int pos = startPos + i * (FORWARD[dir][0] + FORWARD[dir][1] * SIDE);
			
			//移动到的位置是否为空
			if (b.get(pos) == EMPTY) {
				points.add(new Point(pos, 0));				
			}
		}
		return points;
	}
	
	/**
	 * 从路中寻找空位。那些已经在其他路中遍历过的空位，不计算在内
	 * @param b
	 * @param vis
	 * @return
	 */
	public ArrayList<Point> findEmptyPos(Board b, boolean[] vis) {
		ArrayList<Point> points = new ArrayList<>();
		//从当前位置开始，沿dir方向，一次前进1个位置，寻找空位
		for (int i = 0; i < 6; i++){
			//从当前路的开始位置，沿dir方向，向前移动i个点
			int pos = startPos + i * (FORWARD[dir][0] + FORWARD[dir][1] * SIDE);
			
			//如果移动到的位置是空位，而且还没有查看过
			if (b.get(pos) == EMPTY && !vis[pos]) {
				//将该位置添加到点集合中
				points.add(new Point(pos, 0));
				//设置该位置已被访问过
				vis[pos] = true;
			}
		}
		return points;
	}
}
