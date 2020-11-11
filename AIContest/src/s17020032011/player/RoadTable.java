package s17020032011.player;
import static core.game.Move.*;

import java.util.ArrayList;

import core.board.PieceColor;
import static core.board.PieceColor.*;
import static core.board.Board.*;
import core.game.Move;


public class RoadTable {
	
	
	public RoadTable() {
		for(int i=0;i<7;i++) {
			for(int j=0;j<7;j++) {
				playerRoads[i][j]=new RoadList();
			}
		}
	}
	
	//初始化路表
	public void clear() {
		//棋盘上的每个点
		int count=0;
		for(int v=0;v<7;v++) {
			for(int b=0;b<7;b++) {
				playerRoads[v][b].clear();;
			}
		}
		for(int i=0;i< SIDE * SIDE;i++) {
			//四个前进方向
			for(int k=0;k<4;k++) {
				//从位置i前进五个点之后，是否已经出界
				int endPos = i+FORWARD[k][0]*5+ FORWARD[k][1]*5;
				//判断是否已经出界
				boolean active = Move.validSquare(endPos);
				//将该方向的路放入基本路表roads中。如果已经出界，则该路就不是有效路。
				roads[i][k] = new Road(i,k,0,0,active);

				//将有效路放入黑白路表中
				if(active) {
					
					playerRoads[0][0].addRoad(roads[i][k]);
					
					
				}
			}
		}
//		System.out.println("count"+count);
		
	}
	//更新黑白路表
	public void refreshRoad(Road road) {
		for(int i=0;i<7;i++) {
			for(int j=0;j<7;j++) {
				playerRoads[i][j].removeRoad(road);
			}
		}
		addRoad(road);
	}
	

	
	
	
	
	//将路road，添加到相应的路表中
	public void addRoad(Road road) {
		int blackNum=road.getBlackNum();
		int whiteNum=road.getWhiteNum();
		playerRoads[blackNum][whiteNum].addRoad(road);
	}
	
	//更新基本路表
	public void addRoads(Road road) {
		int blackNum=road.getBlackNum();
		int whiteNum=road.getWhiteNum();
		int x=road.getX();
		int y=road.getY();
		roads[x][y].setBlackNum(blackNum);
		roads[x][y].setWhiteNum(whiteNum);
	}
	
	//计算分数
	public int getScore(PieceColor whoseMove) {
		int Score;
		int WS=0;
		int BS=0;
		for(int i=0;i<=6;i++) {
//			System.out.println("bs"+i+":"+playerRoads[i][0].size());
//			System.out.println("ws"+i+":"+playerRoads[0][i].size());
			
			BS=BS+playerRoads[i][0].size()*standard[i];
			WS=WS+playerRoads[0][i].size()*standard[i];
		}
//		System.out.println("bs"+BS);
//		System.out.println("ws"+WS);
		if(whoseMove==WHITE) {
			return WS-BS;
//			return WS;
		}else {
			return BS-WS;
//			return BS;
		}
		
		
	}
	
	//基本路表：以每个点为起点的路
	public Road[][] roads = new Road[SIDE*SIDE][4];
	
	//按黑白子个数划分的路表：包含0-6个黑子，0-6个白子的所有的路

	public RoadList[][] playerRoads = new RoadList[7][7];
	
	private int[] standard = {0, 17, 78, 141, 788, 1030, 100000};
	
	public static void main(String[] args) {
		RoadTable table = new RoadTable();
		table.clear();
	}
}
