package baseline.player;

import static core.board.Board.FORWARD;
import static core.board.PieceColor.*;
import static core.game.Move.SIDE;

import java.util.ArrayList;

import core.board.PieceColor;
import core.game.Move;

public class RoadTable {
	
	//�ҷ�·�Ĺ�ֵ
	private static final int SCORES_ME[] = {
		0, 9, 520, 2070, 7890, 10020, 1000000
	};
	
	//�Է�·�Ĺ�ֵ
	private static final int SCORES_OPP[] = {
		0, 3, 480, 2670, 3887, 4900,  1000000
	};
	
//	//�ҷ�·�Ĺ�ֵ
//	private static final int SCORES_ME[] = {
//		0, 9, 520, 2070, 7890, 10020, 1000000
//	};
//	
//	//�Է�·�Ĺ�ֵ
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
	
	//��ȡ��startPosΪ���ģ�ǰ�������ϵ�4��·
	public Road[] getRoads(int startPos) {
		return roads[startPos];
	}

	//��ʼ��·��
	public void clear() {
		//�����ϵ�ÿ����
		for (int i = 0; i < SIDE * SIDE; i++){
			//4��ǰ������
			for (int k = 0; k < 4; k++) {
				char row = Move.row(i);
				char col = Move.col(i);
				//��λ��i�����ŷ���k��ǰ��5����֮��, 	�Ƿ��Ѿ�����
				col = (char)(col + FORWARD[k][0] * 5);
				row = (char)(row + FORWARD[k][1] * 5);
				//����÷����ϵ�·������
				if (Move.validSquare(col, row)) {
					//���÷����·�������·��roads�С�
					roads[i][k] = new Road(i, k, 0, 0, 0);
					//��·����ڰ�·����
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
		//��ö��ֵ�4·��5·
		RoadList oppFour = getActiveRoads(whoseMove.opposite(), 4);
		RoadList oppFive = getActiveRoads(whoseMove.opposite(), 5);

		return oppFour.size() + oppFive.size();
	}
	
	//��ȡ��������λ��posʱ���ܵ�Ӱ������е���Ч·
	public ArrayList<Road> getAffectedRoads(int pos){
		ArrayList<Road> affectedRoads = new ArrayList<>();
		//��ǰλ�õ�4��ǰ������
		for (int i = 0; i < 4; i++) {
			//���λ��pos�ط���i��·����null����Ϊ�����null��������·�ĳ���С��6��������Ч��·
			if (roads[pos][i] != null)
				affectedRoads.add(roads[pos][i]);
			
			char row = Move.row(pos);
			char col = Move.col(pos);
			//����ÿ��ǰ���������䷴���������˺󣬹�5����
			for (int j = 0; j < 5; j++) {				
				col = (char)(col - FORWARD[i][0]);
				row = (char)(row - FORWARD[i][1]);
				
				if (Move.validSquare(col, row)) {
					int startPos = Move.index(col, row);
					//���λ��startPos�ط���i��·����null
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
	
	//��ȡcolor������num���ӵ���Ч·
	public RoadList getActiveRoads(PieceColor color, int num) {
		assert color != EMPTY;
		
		if (color == BLACK) {
			return playerRoads[num][0];
		}
		return playerRoads[0][num];
	}
	
	//��·road���������ڵ�·����ɾ��
	public void removeRoad(Road road)
	{
		int blackNum = road.getBlackNum();
		int whiteNum = road.getWhiteNum();
		playerRoads[blackNum][whiteNum].removeRoad(road);
	}
	
	//��·road����ӵ���Ӧ��·����
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
	
	//����·�� ��ÿ����Ϊ����·(19 * 14 * 2 + 14 * 14 * 2��)
	private Road[][] roads = new Road[SIDE * SIDE][4]; 
	
	//���ڰ��Ӹ������ֵ�·������0����6������, 0����6�����ӵ�·�����Ϊ�ڰ�·��
	//���磺 playerRoads[3][2]��ʾ����3�����ӣ�2�����ӵ����е�·
	//      playerRoads[0][3]��ʾ����0�����ӣ�3�����ֵ����е�·
	private RoadList[][] playerRoads = new RoadList[7][7];
	
	public static void main(String[] args) {
		RoadTable rt = new RoadTable();
		rt.clear();
		ArrayList<Point> intStus = new ArrayList<>();
		
		intStus.addAll(null);
		
	}
}
