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

	//获取row,col为起点前进方向上的4条路
	public Road[] getRoads(int row,int col) {
		return roads[row][col];
	}

	//初始化路表
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

	//获取将子下在位置pos时，受到影响的所有的有效路
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

	//将路road，从其所在的路表中删除
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

	//将路road，添加到相应的路表中
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

	//基本路表： 以每个点为起点的路
	private Road[][][] roads = new Road[SIDE][SIDE][4];

	//按黑白子个数划分的路表：包含0个至6个黑子, 0个至6个白子的路；简称为黑白路表
	//例如： playerRoads[3][2]表示含有3个黑子，2个白子的路
	private RoadList[][] playerRoads = new RoadList[7][7];
	

}
