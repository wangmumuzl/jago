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
	
	//��ʼ��·��
	public void clear() {
		//�����ϵ�ÿ����
		int count=0;
		for(int v=0;v<7;v++) {
			for(int b=0;b<7;b++) {
				playerRoads[v][b].clear();;
			}
		}
		for(int i=0;i< SIDE * SIDE;i++) {
			//�ĸ�ǰ������
			for(int k=0;k<4;k++) {
				//��λ��iǰ�������֮���Ƿ��Ѿ�����
				int endPos = i+FORWARD[k][0]*5+ FORWARD[k][1]*5;
				//�ж��Ƿ��Ѿ�����
				boolean active = Move.validSquare(endPos);
				//���÷����·�������·��roads�С�����Ѿ����磬���·�Ͳ�����Ч·��
				roads[i][k] = new Road(i,k,0,0,active);

				//����Ч·����ڰ�·����
				if(active) {
					
					playerRoads[0][0].addRoad(roads[i][k]);
					
					
				}
			}
		}
//		System.out.println("count"+count);
		
	}
	//���ºڰ�·��
	public void refreshRoad(Road road) {
		for(int i=0;i<7;i++) {
			for(int j=0;j<7;j++) {
				playerRoads[i][j].removeRoad(road);
			}
		}
		addRoad(road);
	}
	

	
	
	
	
	//��·road����ӵ���Ӧ��·����
	public void addRoad(Road road) {
		int blackNum=road.getBlackNum();
		int whiteNum=road.getWhiteNum();
		playerRoads[blackNum][whiteNum].addRoad(road);
	}
	
	//���»���·��
	public void addRoads(Road road) {
		int blackNum=road.getBlackNum();
		int whiteNum=road.getWhiteNum();
		int x=road.getX();
		int y=road.getY();
		roads[x][y].setBlackNum(blackNum);
		roads[x][y].setWhiteNum(whiteNum);
	}
	
	//�������
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
	
	//����·����ÿ����Ϊ����·
	public Road[][] roads = new Road[SIDE*SIDE][4];
	
	//���ڰ��Ӹ������ֵ�·������0-6�����ӣ�0-6�����ӵ����е�·

	public RoadList[][] playerRoads = new RoadList[7][7];
	
	private int[] standard = {0, 17, 78, 141, 788, 1030, 100000};
	
	public static void main(String[] args) {
		RoadTable table = new RoadTable();
		table.clear();
	}
}
