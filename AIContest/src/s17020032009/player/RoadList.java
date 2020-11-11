package s17020032009.player;
import core.board.Board;
import core.board.PieceColor;


import static core.board.PieceColor.BLACK;
import static core.board.PieceColor.WHITE;

import java.util.*;


public class RoadList {//要有黑表，白表和空表
	private ArrayList<Road> WhiteRoads = new ArrayList<Road>();
	private ArrayList<Road> BlackRoads = new ArrayList<Road>();
	private ArrayList<Road> EmptyRoads = new ArrayList<Road>();
	//private HashMap<Integer, Road> EmptyRoads = new HashMap<Integer, Road>();
	//private int[][]direct = {{0, 1}, {1, 0}, {1, 1}, {-1, 1}};
	private int SIDE = 19;
	private int[] blacknum = {0, 0, 0, 0, 0, 0};
	private int[] whitenum = {0, 0, 0, 0, 0, 0};
	//private int[] standard = {1, 3, 10, 40, 400, 10000};//重写一下标准
	private int[] standard = {17, 78, 141, 788, 1030, 100000};
	
	public RoadList() {}
	
	public RoadList(Board board) {//初始化先全部扫描一遍，然后存放到相应的表里
		analysisVertical(board);
		analysisHorizon(board);
		analysisLeft(board);
		analysisRight(board);
	}
	
	public RoadList(RoadList r, int i, int j, int color) {//初始化方法，相当于复制了上一层的
		ArrayList<Road> x = new ArrayList<Road>();
		x = r.getBlackRoads();
		for(Road road : x)
			this.BlackRoads.add(new Road(road));
		x = r.getWhiteRoads();
		for(Road road : x)
			this.WhiteRoads.add(new Road(road));
		x = r.getEmptyRoads();
		for(Road road : x)
			this.EmptyRoads.add(new Road(road));
		int[]w = r.getwhitenum();
		int[]b = r.getblacknum();
		for(int k = 0; k < 6; k++) {
			this.blacknum[k] = b[k];
			this.whitenum[k] = w[k];
		}
		this.MakeMove(i, color);
		this.MakeMove(j, color);
		//System.out.println("传入的");
		//r.showallroadsnum();
		//r.shownums();
		//System.out.println("现在的");
		//this.showallroadsnum();
		//this.shownums();
		//System.out.println("---------------------------------------");
	}
	
	public void copy(RoadList r) {
		ArrayList<Road> x = new ArrayList<Road>();
		x = r.getBlackRoads();
		for(Road road : x)
			this.BlackRoads.add(new Road(road));
		x = r.getWhiteRoads();
		for(Road road : x)
			this.WhiteRoads.add(new Road(road));
		x = r.getEmptyRoads();
		for(Road road : x)
			this.EmptyRoads.add(new Road(road));
		int[]w = r.getwhitenum();
		int[]b = r.getblacknum();
		for(int k = 0; k < 6; k++) {
			this.blacknum[k] = b[k];
			this.whitenum[k] = w[k];
		}
	}
	
	public int change(PieceColor pc) {//把棋子颜色转换为数字
		if(pc == BLACK) return 1;
		if(pc == WHITE) return 7;
		return 0;
	}
	//初始的四个方向的扫描
	public void analysisVertical(Board board) {//横向扫描
		int start = 0;
		int[] filter = new int[6];
		for(int i = 0; i < 6; i++)
		{
			filter[i] = change(board.get(i));
		}	
		Save(new Road(start, 0, filter));
		while(true) {
			if((start - 13) % 19 == 0)start += 6;
			else start++;
			if(start >= SIDE * SIDE)break;			
			filter[0] = change(board.get(start));
			for(int j = 1; j < 6; j++) {
				filter[j] = change(board.get(start + j));
			}
			/*if(start == 161) {
				for(int i : filter)System.out.println("analysisVertical(Board board)index=161..." + filter[i]);
			}*/
			Save(new Road(start, 0, filter));
		}
	}
	
	public void analysisHorizon(Board board) {//纵向扫描
		int start = 0;
		int[]filter = new int[6];
		while(true){
			filter[0] = change(board.get(start));
			for(int i = 1; i < 6; i++) {
				filter[i] = change(board.get(start + i * SIDE));
			}
			Save(new Road(start, 1, filter));
			start++;
			if(start > 265)break;
		}
	}
	
	public void analysisLeft(Board board) {//左向扫描,从左上向右下扫描
		int start = 0;
		int[] filter = new int[6];
		while(true) {
			filter[0] = change(board.get(start));
			for(int i = 1; i < 6; i++) {
				filter[i] = change(board.get(start + i + i * SIDE));
			}
			Save(new Road(start, 2, filter));
			if((start - 13) % 19 == 0)start += 6;
			else start++;
			if(start > 259)break;
		}
	}
	
	public void analysisRight(Board board) {//右向扫描
		int start = 5;
		int[] filter = new int[6];
		while(true) {
			filter[0] = change(board.get(start));
			for(int i = 1; i < 6; i++) {
				filter[i] = change(board.get(start + i * SIDE - i));
			}
			Save(new Road(start, 3, filter));
			if((start - 18) % 19 == 0) start += 6;
			else start++;
			if(start > 265)break;
		}
	}
	
	public void Save(Road r) {
		if(r.getRoadColor() == 1) {
			BlackRoads.add(r);
			//System.out.println(r.getNum());
			blacknum[r.getNum() - 1]++;
		}
		if(r.getRoadColor() == 7) {
			WhiteRoads.add(r);
			//System.out.println(r.getNum());
			whitenum[r.getNum() - 1]++;
		}
		if(r.getRoadColor() == 0)EmptyRoads.add(r);
	} 
 	
	public void MakeMove(int index, int color) {//一次更新一个点，更新路表，要通过index找到起始点
		 //System.out.println("color = " + color);
		 refreshVertical(index, color);
		 refreshHorizon(index, color);
		 refreshLeft(index, color);
		 refreshRight(index, color);
		 //更新完路表后对路表进行排序
		 BlackRoadsSort();
		 WhiteRoadsSort();
	}
	
	public void refreshVertical(int index, int color) {
		int start = index;
		int n = index % SIDE;
		if(n > 5)n = 5;//限制越界
		do {
			findRoad(start, index, color, 0);
			start--;
			n--;
		}while(n > 0);
	}
	
	public void refreshHorizon(int index, int color) {
		int start = index;
		int n = (index - index % SIDE)/SIDE;
		if(n > 5)n = 5;
		do {
			findRoad(start, index, color, 1);
			start = start - SIDE;
			n--;
		}while(n > 0);
	}
	
	public void refreshLeft(int index, int color) {
		int start = index;
		int n1 = (index - index % SIDE)/SIDE;
		int n = n1;
		int n2 = index % SIDE;
		if(n2 < n1) n = n2;
		if(n > 5)n = 5;
		do {
			findRoad(start, index, color, 2);
			start = start - SIDE - 1;
			n--;
		}while(n > 0);
	}
	
	public void refreshRight(int index, int color) {
		int start = index;
		int n1 = SIDE - (index - index % SIDE)/SIDE;
		int n = n1;
		int n2 = index % SIDE;
		if(n2 < n1) n = n2;
		if(n > 5)n = 5;
		do {
			findRoad(start, index, color, 3);
			start = start - SIDE + 1;
			n--;
		}while(n > 0);
	}
	
	public boolean findRoad(int start, int index, int color, int d) {//起始点下棋的点颜色，还有方向点更新路表
		for(int i = 0; i < WhiteRoads.size(); i++) {
			//白色路表
			if(WhiteRoads.get(i).getStart() == start && WhiteRoads.get(i).getD() == d) {
				int n = WhiteRoads.get(i).getNum();
				WhiteRoads.get(i).Move(index, color);
				//System.out.println("findRoad白色路表" + n);
				//移动之后，对路表进行更新
				//System.out.println("嘿嘿");
				if(WhiteRoads.get(i).getRoadColor() == 7) {
					whitenum[n - 1]--;
					whitenum[n]++;
					//System.out.println("whitenum[n]" + whitenum[n] + "d = " + d + "color = " + color);
					return true;
				}
				if(WhiteRoads.get(i).getRoadColor() == 0) {
					EmptyRoads.add(WhiteRoads.get(i));
					whitenum[n - 1]--;
					WhiteRoads.remove(i);
					return true;//完成操作就结束
				}
			}
		}
			//黑色路表
		for(int j = 0; j < BlackRoads.size(); j++) {
			/*if(BlackRoads.get(j).getRoadColor() != 1) {
				BlackRoads.remove(j);
				return true;
			}*/
			if(BlackRoads.get(j).getStart() == start && BlackRoads.get(j).getD() == d) {
				int n = BlackRoads.get(j).getNum();
				BlackRoads.get(j).Move(index, color);
				//int n = BlackRoads.get(j).getD();
				if(BlackRoads.get(j).getRoadColor() == 1) {
					//System.out.println("findRoad黑色路表" + n);
					blacknum[n - 1]--;
					blacknum[n]++;
					return true;
				}
				if(BlackRoads.get(j).getRoadColor() == 0) {
					EmptyRoads.add(BlackRoads.get(j));
					BlackRoads.remove(j);
					blacknum[n - 1]--;
					return true;
				}
			}
		}
		for(int k = 0; k < EmptyRoads.size(); k++) {
			//空白路表
			if(EmptyRoads.get(k).getStart() == start && EmptyRoads.get(k).getD() == d) {
				
				if(EmptyRoads.get(k).getX() == 0) {
					EmptyRoads.get(k).Move(index, color);
					if(color == 1) {
						BlackRoads.add(EmptyRoads.get(k));
						blacknum[0]++;
						EmptyRoads.remove(k);
						return true;
					}
					else {
						whitenum[0]++;
						WhiteRoads.add(EmptyRoads.get(k));
						EmptyRoads.remove(k);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void RoadsCheck() {
		for(int i = 0; i < BlackRoads.size(); i++) {
			if(BlackRoads.get(i).getRoadColor() != 1) {//如果原来的黑路不等于1了就一定变成空路了
				EmptyRoads.add(BlackRoads.get(i));
				BlackRoads.remove(i);
			}
		}	
		for(int i = 0; i < WhiteRoads.size(); i++) {
			if(WhiteRoads.get(i).getRoadColor() != 7) {
				EmptyRoads.add(WhiteRoads.get(i));
				WhiteRoads.remove(i);
			}
		}
		for(int i = 0; i < EmptyRoads.size(); i++) {
			if(EmptyRoads.get(i).getRoadColor() == 1) {
				BlackRoads.add(EmptyRoads.get(i));
				BlackRoads.remove(i);
			}
			if(EmptyRoads.get(i).getRoadColor() == 7) {
				WhiteRoads.add(EmptyRoads.get(i));
				EmptyRoads.remove(i);
			}
		}
		for(int i = 0; i < 6; i++) {
			blacknum[i] = 0;
			whitenum[i] = 0;
		}
		for(Road r : BlackRoads) {
			blacknum[r.getNum() - 1]++;
		}
		for(Road r : WhiteRoads) {
			whitenum[r.getNum() - 1]++;
		}
	}
	
	public void BlackRoadsSort() {//对路表进行排序重写了compare方法
		Collections.sort(BlackRoads, new Comparator<Road>() {

			@Override
			public int compare(Road o1, Road o2) {
				if(o1.getNum()>o2.getNum()) 
					return -1;
				if(o1.getNum()<o2.getNum())
					return 1;
				return 0;
			}		
		});
	}
	
	public void WhiteRoadsSort() {//对路表进行排序重写了compare方法
		Collections.sort(WhiteRoads, new Comparator<Road>() {

			@Override
			public int compare(Road o1, Road o2) {
				if(o1.getNum()>o2.getNum()) 
					return -1;
				if(o1.getNum()<o2.getNum())
					return 1;
				return 0;
			}		
		});
	}
	
	public int getSocre() {//评估函数
		int blackscore = 0;
		int whitescore = 0;
		for(int i = 0; i < 6; i++) {
			blackscore = blackscore + blacknum[i] * standard[i]; 
			whitescore = whitescore + whitenum[i] * standard[i];
		} 
		return - blackscore + whitescore;
	}
	
	public ArrayList<Integer> getfirstten() {//获取最有可能的前十个位置,不要要求前十个了因为一开始可能没有前十个
		ArrayList<Integer> a = new ArrayList<Integer>();
		//ArrayList<Integer> black = new ArrayList<Integer>();
		//ArrayList<Integer> white = new ArrayList<Integer>();
		//int size = BlackRoads.size();
		//if(WhiteRoads.size() == 0)return BlackRoads.get(0).getIndexEmpty();//如果说WhiteRoads的大小为零那就是第一步直接返回一个就好都一样
		//if(size > WhiteRoads.size())size = WhiteRoads.size();
		
		/*for(int i = 0; i < size; i++) {
			black = BlackRoads.get(i).getIndexEmpty();
			white = WhiteRoads.get(i).getIndexEmpty();
			for(int j : black) {
				if(ishave(a, j))a.add(j);
			}
			for(int k : white) {
				if(ishave(a, k))a.add(k);
			}
			if(a.size() > 20)return a;
		}*/
		int n = 0;
		while(true) {
			if(n < BlackRoads.size()) {
				for(int i : BlackRoads.get(n).getIndexEmpty())
					if(ishave(a, i))
						a.add(i);
			}
			if(n < WhiteRoads.size()) {
				for(int j : WhiteRoads.get(n).getIndexEmpty())
					if(ishave(a, j))
						a.add(j);
			}
			n++;
			if(a.size() > 20 || n > 50)break;
		}
		//System.out.println(a.size());
		return a;
	}
	
	public boolean ishave(ArrayList<Integer> a, int b) {
		for(int i : a) {
			if(i == b)return false;
		}
		return true;
	}
	
	public void shownums() {
		System.out.println("BlackNum");
		for(int i : blacknum) System.out.print("  " + i);
		System.out.println();
		System.out.println("WhiteNum");
		for(int j : whitenum) System.out.print("  " + j);
		System.out.println();
	}
	
	public void showallroadsnum() {
		System.out.println("Black");
		for(Road r : BlackRoads) {
			System.out.println("color = " + r.getRoadColor() + "num = " + r.getNum() + "color = " + r.getRoadColor());
		}
		System.out.println("White");
		for(Road k : WhiteRoads) {
			System.out.println("color = " + k.getRoadColor() + "num = " + k.getNum() + "color = " + k.getRoadColor());
		}
	}
	
	public ArrayList<Road> getWhiteRoads() {
		return WhiteRoads;
	}
	
	public ArrayList<Road> getBlackRoads() {
		return BlackRoads;
	}
	
	public ArrayList<Road> getEmptyRoads() {
		return EmptyRoads;
	}
	
	public int[] getblacknum() {
		return blacknum;
	}
	public int[] getwhitenum() {
		return whitenum;
	}
	
	public int getbfandf() {
		int x;
		x = blacknum[4] * 4 + blacknum[3] * 1;
		return x;
	}
	public int getwfandf() {
		int x;
		x = whitenum[4] * 5 + whitenum[3] * 1;
		return x;
	}
}


