package s17020031094.player;

import static core.board.PieceColor.*;
import static core.game.Move.SIDE;
import static core.game.Move.validSquare;

import java.util.ArrayList;
import java.util.HashSet;

import core.board.PieceColor;
import s17020031094.player.Road;

public class RoadTable {
	
	public static final int FORWARD[][] = {
			{1,0},{0,1},{1,1},{-1,1}
	};
	
	public static final int FORWARDINDEX[][] = {
			{0,SIDE},{1,0},{1,SIDE},{1,-SIDE}	
	};
	
	private int[] scoreOfMyRoad = {9,520,2070,7890,10020,1000000};
	private int[] scoreOfEnemyRoad = {3,480,2670,3887,4900,1000000};
	
	public RoadTable() {
		// TODO Auto-generated constructor stub
		for (int i = 0; i < playerRoads.length; i++) {
			for (int j = 0; j < playerRoads[0].length; j++) {
				playerRoads[i][j] = new RoadList();
			}
		}
	}
	
	//初始化路表
	public void clear() {
		for(int i=0; i<SIDE; i++){
			for(int j=0; j<SIDE; j++){
				for(int k=0; k<4; k++){
					char row = (char) ('A' + i + FORWARD[k][0]*5);
					char col = (char) ('A' + j + FORWARD[k][1]*5);
					//System.out.println(row + " " + col);
					boolean active = validSquare(col, row);
					int pos = i*SIDE + j;
					roads[pos][k] = new Road(pos,k,0,0,active);
					if(active){
						playerRoads[0][0].addRoad(roads[pos][k]);
					}
				}
			}
		}
		changePlayerBoard(9*SIDE+9, BLACK);
	}
			
	//基本路表： 以每个点为起点的路
	private Road[][] roads = new Road[SIDE * SIDE][4]; 
	
	//按黑白子个数划分的路表：包含0个至6个黑子, 0个至6个白子的路；简称为黑白路表
	//例如： playerRoads[3][2]表示含有3个黑子，2个白子的路
	private RoadList[][] playerRoads = new RoadList[7][7];
	
	public void changePlayerBoard(int index,PieceColor color){
		//System.out.println("Move:"+index + " "+color);
		int row = index/SIDE;
		int col = index%SIDE;
		for(int i=0; i<6; i++){
			for(int j=0; j<4; j++){
				char nowPosRow = (char) ('A'+row - FORWARD[j][0]*i);
				char nowPosCol = (char) ('A'+col - FORWARD[j][1]*i);
				if(!validSquare(nowPosCol,nowPosRow))	continue;
				int nowPos = (nowPosRow-'A')*SIDE + nowPosCol-'A';
				int blackNum = roads[nowPos][j].getBlackNum();
				int whiteNum = roads[nowPos][j].getWhiteNum();
				boolean active = roads[nowPos][j].isActive();
				if(active)
					playerRoads[blackNum][whiteNum].removeRoad(roads[nowPos][j]);
				if(color == BLACK){
					if(blackNum == 0 && whiteNum!=0){
						active = false;
						roads[nowPos][j].changeRoad(1, whiteNum, active);
					}else{
						roads[nowPos][j].changeRoad(blackNum+1, whiteNum, active);
						if(active)
							playerRoads[blackNum+1][whiteNum].addRoad(roads[nowPos][j]);
					}
				}else{
					if(whiteNum==0 && blackNum!=0){
						active = false;
						roads[nowPos][j].changeRoad(blackNum, 1, active);
					}else{
						roads[nowPos][j].changeRoad(blackNum, whiteNum+1, active);
						if(active)
							playerRoads[blackNum][whiteNum+1].addRoad(roads[nowPos][j]);
					}
				}
				//System.out.println(nowPos+" "+j+"  " +roads[nowPos][j].getBlackNum() + " "+roads[nowPos][j].getWhiteNum()+" "+roads[nowPos][j].isActive());
			}
		}
	}
	public void backPlayerBoard(int index,PieceColor color){
		//System.out.println("unMove:"+index + " "+ color);
		int row = index/SIDE;
		int col = index%SIDE;
		for(int i=0; i<6; i++){
			for(int j=0; j<4; j++){
				char nowPosRow = (char) ('A'+row - FORWARD[j][0]*i);
				char nowPosCol = (char) ('A'+col - FORWARD[j][1]*i);
				if(!validSquare(nowPosCol,nowPosRow))	continue;
				int nowPos = (nowPosRow-'A')*SIDE + nowPosCol-'A';
				int blackNum = roads[nowPos][j].getBlackNum();
				int whiteNum = roads[nowPos][j].getWhiteNum();
				boolean active = roads[nowPos][j].isActive();
				if(active)
					playerRoads[blackNum][whiteNum].removeRoad(roads[nowPos][j]);
				if(color == BLACK){
					if(blackNum == 1){
						char endrow = (char) (nowPosRow + FORWARD[j][0]*5);
						char endcol = (char) (nowPosCol + FORWARD[j][1]*5);
						active = validSquare(endcol, endrow);
						roads[nowPos][j].changeRoad(0, whiteNum, active);
						if(active)
							playerRoads[0][whiteNum].addRoad(roads[nowPos][j]);
					}else{
						roads[nowPos][j].changeRoad(blackNum-1, whiteNum, active);
						if(active)
							playerRoads[blackNum-1][whiteNum].addRoad(roads[nowPos][j]);
					}
				}else{
					if(whiteNum==1){
						char endrow = (char) (nowPosRow + FORWARD[j][0]*5);
						char endcol = (char) (nowPosCol + FORWARD[j][1]*5);
						active = validSquare(endcol, endrow);
						roads[nowPos][j].changeRoad(blackNum, 0, active);
						if(active)
							playerRoads[blackNum][0].addRoad(roads[nowPos][j]);
					}else{
						roads[nowPos][j].changeRoad(blackNum, whiteNum-1, active);
						if(active)
							playerRoads[blackNum][whiteNum-1].addRoad(roads[nowPos][j]);
					}
				}
				//System.out.println(nowPos+" "+j+"  " +roads[nowPos][j].getBlackNum() + " "+roads[nowPos][j].getWhiteNum()+" "+roads[nowPos][j].isActive());
			}
		}
	} 
	
	public int getValue(PieceColor whoseMove){
		int score = 0;
		if(whoseMove == BLACK){
			for(int i=0; i<6; i++){
				score += playerRoads[i+1][0].size()*scoreOfMyRoad[i];
				score -= playerRoads[0][i+1].size()*scoreOfEnemyRoad[i];
			}
		}else{
			for(int i=0; i<6; i++){
				score += playerRoads[0][i+1].size()*scoreOfMyRoad[i];
				score -= playerRoads[i+1][0].size()*scoreOfEnemyRoad[i];
			}
		}
		return score;
	}
	
	public boolean gameOver(){
		return playerRoads[6][0].size()>0 || playerRoads[0][6].size()>0;
	}
	public ArrayList<Integer> TSSMy(PieceColor nowColor){
		ArrayList<Integer> ans = new ArrayList<>();
		HashSet<Integer> poentialMove = new HashSet<>();
		ArrayList<Road> roads = new ArrayList<>();
		if(nowColor == BLACK){
			for(Road road : playerRoads[5][0]){
				roads.add(road);
				break;
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[4][0]){
					roads.add(road);
					break;
				}
			}
		}else{
			for(Road road : playerRoads[0][5]){
				roads.add(road);
				break;
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[0][4]){
					roads.add(road);
					break;
				}
			}
		}
		for(Road i : roads){
			int pos = i.getStartPos();
			int dir = i.getDir();
			for(int k=0; k<6; k++){
				int nowPos = pos + FORWARDINDEX[dir][0]*k + FORWARDINDEX[dir][1]*k;
				poentialMove.add(nowPos);
			}
		}
		ans.addAll(poentialMove);
		return ans;
	}
	public ArrayList<Integer> TSSEnemy(PieceColor nowColor){
		ArrayList<Integer> ans = new ArrayList<>();
		HashSet<Integer> poentialMove = new HashSet<>();
		ArrayList<Road> roads = new ArrayList<>();
		if(nowColor == BLACK){
			for(Road road : playerRoads[0][5]){
				roads.add(road);
				if(roads.size() == 2)	break;
			}
			for(Road road : playerRoads[0][4]){
				roads.add(road);
				if(roads.size() == 2)	break;
			}
		}else{
			for(Road road : playerRoads[5][0]){
				roads.add(road);
				if(roads.size() == 2)	break;
			}
			for(Road road : playerRoads[4][0]){
				roads.add(road);
				if(roads.size() == 2)	break;
			}
		}
		for(Road i : roads){
			int pos = i.getStartPos();
			int dir = i.getDir();
			for(int k=0; k<6; k++){
				int nowPos = pos + FORWARDINDEX[dir][0]*k + FORWARDINDEX[dir][1]*k;
				poentialMove.add(nowPos);
			}
		}
		ans.addAll(poentialMove);
		return ans;
	}
	public ArrayList<Integer> getPoentialMove(PieceColor color){
		ArrayList<Integer> ans = new ArrayList<>();
		HashSet<Integer> poentialMove = new HashSet<>();
		ArrayList<Road> roads = new ArrayList<>();
		
		if(color == BLACK){
			for(Road road : playerRoads[3][0]){
				roads.add(road);
			}
			if(roads.size()<2){
				for(Road road : playerRoads[0][3]){
					roads.add(road);
				}
				for(Road road : playerRoads[2][0]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[1][0]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[0][0]){
					roads.add(road);
				}
			}
		}else{
			for(Road road : playerRoads[0][3]){
				roads.add(road);
			}
			if(roads.size()<2){
				for(Road road : playerRoads[3][0]){
					roads.add(road);
				}
				for(Road road : playerRoads[0][2]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[0][1]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[0][0]){
					roads.add(road);
				}
			}
		}
		for(Road i : roads){
			int pos = i.getStartPos();
			int dir = i.getDir();
			for(int k=0; k<6; k++){
				int nowPos = pos + FORWARDINDEX[dir][0]*k + FORWARDINDEX[dir][1]*k;
				poentialMove.add(nowPos);
			}
		}
		ans.addAll(poentialMove);
		return ans;
	}
	/*
	public ArrayList<Integer> getPoentialMove(PieceColor color){
		ArrayList<Integer> ans = new ArrayList<>();
		HashSet<Integer> poentialMove = new HashSet<>();
		ArrayList<Road> roads = new ArrayList<>();
		
		if(color == BLACK){
			for(Road road : playerRoads[3][0]){
				roads.add(road);
			}
			if(roads.size()<2 && playerRoads[0][3].size()>1){
				for(Road road : playerRoads[0][3]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[2][0]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[1][0]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[0][0]){
					roads.add(road);
				}
			}
		}else{
			for(Road road : playerRoads[0][3]){
				roads.add(road);
			}
			if(roads.size()<2 && playerRoads[3][0].size()>1){
				for(Road road : playerRoads[3][0]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[0][2]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[0][1]){
					roads.add(road);
				}
			}
			if(roads.isEmpty()){
				for(Road road : playerRoads[0][0]){
					roads.add(road);
				}
			}
		}
		for(Road i : roads){
			int pos = i.getStartPos();
			int dir = i.getDir();
			for(int k=0; k<6; k++){
				int nowPos = pos + FORWARDINDEX[dir][0]*k + FORWARDINDEX[dir][1]*k;
				poentialMove.add(nowPos);
			}
		}
		ans.addAll(poentialMove);
		return ans;
	}*/
	
	public void print(){
		System.out.println("RoadTable:");
		for(int i=0; i<7; i++){
			for(int j=0; j<7; j++){
				for(Road road : playerRoads[i][j]){
					System.out.println(road.getStartPos()+" "+road.getDir()+"  " +road.getBlackNum() + " "+road.getWhiteNum()+" "+road.isActive());
				}
			}
		}
		int[][] now = {{-1,0}};
		now[-1][0] = 0;
	}
	
	public static void main(String[] args) {
		RoadTable rt = new RoadTable();
		rt.clear();
		ArrayList<Point> intStus = new ArrayList<>();
		
		intStus.addAll(null);
		
	}
}
