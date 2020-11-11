package baseline.player;

import static core.board.Board.FORWARD;
import static core.board.PieceColor.*;
import static core.game.Move.SIDE;

import java.util.ArrayList;

import core.board.PieceColor;
import core.game.Move;

public class RoadTable {
	
	//我方路的估值
	private static final int SCORES_ME[] = {
		0, 9, 520, 2070, 7890, 10020, 1000000
	};
	
	//对方路的估值
	private static final int SCORES_OPP[] = {
		0, 3, 480, 2670, 3887, 4900,  1000000
	};
	
//	//我方路的估值
//	private static final int SCORES_ME[] = {
//		0, 9, 520, 2070, 7890, 10020, 1000000
//	};
//	
//	//对方路的估值
//	private static final int SCORES_OPP[] = {
//		0, 9, 520, 2070, 7890, 10020, 1000000
//	};

	public RoadTable() {
		// TODO Auto-generated constructor stub
		for (int i = 0; i < playerRoads.length; i++) {
			for (int j = 0; j < playerRoads[0].length; j++) {
				playerRoads[i][j] = new RoadList();
			}
		}
	}
	
	//获取以startPos为起点的，前进方向上的4条路
	public Road[] getRoads(int startPos) {
		return roads[startPos];
	}

	//初始化路表
	public void clear() {
		//棋盘上的每个点
		for (int i = 0; i < SIDE * SIDE; i++){
			//4个前进方向
			for (int k = 0; k < 4; k++) {
				char row = Move.row(i);
				char col = Move.col(i);
				//从位置i，沿着方向k，前进5个点之后, 	是否已经出界
				col = (char)(col + FORWARD[k][0] * 5);
				row = (char)(row + FORWARD[k][1] * 5);
				//如果该方向上的路够长，
				if (Move.validSquare(col, row)) {
					//将该方向的路放入基本路表roads中。
					roads[i][k] = new Road(i, k, 0, 0, 0);
					//将路放入黑白路表中
					playerRoads[0][0].addRoad(roads[i][k]);
				}
				else {
					roads[i][k] = null;
				}
			}
		}
	}
	
	public boolean noThreats(PieceColor whoseMove) {
		return getPotentialThreats(whoseMove) == 0;
	}
	
	private int getPotentialThreats(PieceColor whoseMove) {
		//获得对手的4路和5路
		RoadList oppFour = getActiveRoads(whoseMove.opposite(), 4);
		RoadList oppFive = getActiveRoads(whoseMove.opposite(), 5);

		return oppFour.size() + oppFive.size();
	}
	
	//获取将子下在位置pos时，受到影响的所有的有效路
	public ArrayList<Road> getAffectedRoads(int pos){
		ArrayList<Road> affectedRoads = new ArrayList<>();
		//当前位置的4个前进方向
		for (int i = 0; i < 4; i++) {
			//如果位置pos沿方向i的路不是null。因为如果是null，则这条路的长度小于6，不是有效的路
			if (roads[pos][i] != null)
				affectedRoads.add(roads[pos][i]);
			
			char row = Move.row(pos);
			char col = Move.col(pos);
			//对于每个前进方向，沿其反方向依次退后，共5个点
			for (int j = 0; j < 5; j++) {				
				col = (char)(col - FORWARD[i][0]);
				row = (char)(row - FORWARD[i][1]);
				
				if (Move.validSquare(col, row)) {
					int startPos = Move.index(col, row);
					//如果位置startPos沿方向i的路不是null
					if (roads[startPos][i] != null)
						affectedRoads.add(roads[startPos][i]);
				}
				else {
					break;
				}
			}
		}
		return affectedRoads;
	}
	
	//获取color方的有num个子的有效路
	public RoadList getActiveRoads(PieceColor color, int num) {
		assert color != EMPTY;
		
		if (color == BLACK) {
			return playerRoads[num][0];
		}
		return playerRoads[0][num];
	}
	
	//将路road，从其所在的路表中删除
	public void removeRoad(Road road)
	{
		int blackNum = road.getBlackNum();
		int whiteNum = road.getWhiteNum();
		playerRoads[blackNum][whiteNum].removeRoad(road);
	}
	
	//将路road，添加到相应的路表中
	public void addRoad(Road road)
	{
		int blackNum = road.getBlackNum();
		int whiteNum = road.getWhiteNum();
		playerRoads[blackNum][whiteNum].addRoad(road);
	}
	
	public int getScore(PieceColor whoseMove) {
		
		int res = 0;
		
		for (int i = 1; i <= 6; i++){
			int sizeMe = (whoseMove == BLACK ? playerRoads[i][0].size() : playerRoads[0][i].size());
			int sizeOpp = (whoseMove == BLACK ? playerRoads[0][i].size() : playerRoads[i][0].size());
			res += sizeMe * SCORES_ME[i];
			res -= sizeOpp * SCORES_OPP[i];
		}
		
		return res;
	}
	
	//基本路表： 以每个点为起点的路(19 * 14 * 2 + 14 * 14 * 2条)
	private Road[][] roads = new Road[SIDE * SIDE][4]; 
	
	//按黑白子个数划分的路表：包含0个至6个黑子, 0个至6个白子的路；简称为黑白路表
	//例如： playerRoads[3][2]表示含有3个黑子，2个白子的所有的路
	//      playerRoads[0][3]表示含有0各黑子，3个白字的所有的路
	private RoadList[][] playerRoads = new RoadList[7][7];
	
	public static void main(String[] args) {
		RoadTable rt = new RoadTable();
		rt.clear();
		ArrayList<Point> intStus = new ArrayList<>();
		
		intStus.addAll(null);
		
	}
}
