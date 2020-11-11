package g12;

import core.board.PieceColor;

import java.util.ArrayList;

import static core.game.Move.SIDE;

public class RoadTable {
	

	public RoadTable() {
		// TODO Auto-generated constructor stub
		for (int i = 0; i < playerRoads.length; i++) {
			for (int j = 0; j < playerRoads[0].length; j++) {
				playerRoads[i][j] = new RoadList();
				playerRoads[i][j].clear();
			}
		}
	}

	//��ȡrow,colΪ���ǰ�������ϵ�4��·
	public Road[] getRoads(int row,int col) {
		return roads[row][col];
	}

	//��ʼ��·��
	public void clear() {
		for(int row = 0; row < SIDE; row ++){
			for(int col = 0; col < SIDE; col ++){
				for(int dir= 0; dir < 4; dir ++){
					Road road = new Road(row,col,dir,0,0,0,false);
					roads[row][col][dir] = road;
					if(Board.judge(row + 5*Road.FORWARD[dir][0],col + 5*Road.FORWARD[dir][1])){
						road.setEffective(true);
						addRoad(road);
					}

				}
			}
		}

	}

	public boolean noThreats(PieceColor whoseMove) {
		return true;
	}

	private int getPotentialThreats(PieceColor whoseMove) {
		return 0;
	}

	//��ȡ��������λ��posʱ���ܵ�Ӱ������е���Ч·
	public ArrayList<Road> getAffectedRoads(int pos){
		ArrayList<Road> affectedRoads = new ArrayList<>();


		return affectedRoads;
	}

	public ArrayList<Road> getAffectedRoads(int row,int col){
		ArrayList<Road> affectedRoads = new ArrayList<>();


		return affectedRoads;
	}

	public Road[][][] getRoads() {
		return roads;
	}

	public void setRoads(Road[][][] roads) {
		this.roads = roads;
	}

	public RoadList[][] getPlayerRoads() {
		return playerRoads;
	}

	public void setPlayerRoads(RoadList[][] playerRoads) {
		this.playerRoads = playerRoads;
	}

	//��·road���������ڵ�·����ɾ��
	public void removeRoad(Road road)
	{
		int blacknums  = road.getBlackNum();
		int whitenums = road.getWhiteNum();
		int index = road.getIndex();
		int rindex = playerRoads[blacknums][whitenums].size() - 1;
		//System.out.println(blacknums + "   " + whitenums);

		playerRoads[blacknums][whitenums].get(rindex).setIndex(index);
		playerRoads[blacknums][whitenums].set(index,playerRoads[blacknums][whitenums].get(rindex));
		playerRoads[blacknums][whitenums].remove(rindex);
//		if(blacknums==0&&whitenums==1)
//			System.out.println(blacknums + "   " + whitenums+" removeindex:"+index + "  lastindex  " + rindex);
//		for (int i = 0; i < playerRoads[blacknums][whitenums].size()&&(blacknums==0&&whitenums==1); i ++){
//			System.out.print(playerRoads[blacknums][whitenums].get(i).getIndex() + " ");
//		}
//		System.out.println();

	}

	//��·road����ӵ���Ӧ��·����
	public void addRoad(Road road)
	{
		int blacknums  = road.getBlackNum();
		int whitenums = road.getWhiteNum();
		int index = playerRoads[blacknums][whitenums].size();
//		if(blacknums==0&&whitenums==1)
//			System.out.println(blacknums + "   " + whitenums+" add index:" + index );
		road.setIndex(index);
		playerRoads[blacknums][whitenums].add(road);
//		for (int i = 0; i < playerRoads[blacknums][whitenums].size()&&(blacknums==0&&whitenums==1); i ++){
//			System.out.print(playerRoads[blacknums][whitenums].get(i).getIndex() + " ");
//		}
//		System.out.println();
	}

	//����·�� ��ÿ����Ϊ����·
	private Road[][][] roads = new Road[SIDE][SIDE][4];

	//���ڰ��Ӹ������ֵ�·������0����6������, 0����6�����ӵ�·�����Ϊ�ڰ�·��
	//���磺 playerRoads[3][2]��ʾ����3�����ӣ�2�����ӵ�·
	private RoadList[][] playerRoads = new RoadList[7][7];
	

}
