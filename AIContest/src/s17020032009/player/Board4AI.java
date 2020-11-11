package s17020032009.player;

import core.game.Move;
import core.board.*;
import static core.board.PieceColor.*;
import java.util.*;


public class Board4AI {
	private Move move;
	//private int winkey;
	//private int oppowinkey;
	private RoadList r;
	private int color;
	//private int x = 0;
	private ArrayList<Integer> num = new ArrayList<Integer>();
	public Board4AI() {}
	public Board b;
	public Board4AI(int color) {
		this.color = color;
		//findThreat();
	}

	
	/*public void findThreat() {//这个arraylist每次只存放一个点而已
		//首先找自身颜色的
		Road b = new Road();
		Road w = new Road();
		b = r.getBlackRoads().get(0);//均取黑白路表的第一个
		w = r.getWhiteRoads().get(0);
		if(b.getNum() > 2 || w.getNum() > 2) {
			if(color == 1) {//如果我方为黑色
				if(b.getNum() >= w.getNum()) {//如果我方的棋子数大于对方棋子数直接选取我方的子
					if(b.getIndexEmpty().size() > 2)move = new Move(b.getIndexEmpty().get(0), b.getIndexEmpty().get(1));
					if(b.getNum() == 5) winkey = b.getIndexEmpty().get(0);//如果说只差一个子就赢了把这个子的位置存起来
				}
				else {
					//如果是四个点那么就说明只差两点就赢了
					if(b.getNum() == 4) {
						move = new Move(b.getIndexEmpty().get(0), b.getIndexEmpty().get(1));
						x = 1;
					}
					//否则就去堵对面的子
					if(w.getNum() == 5) oppowinkey = w.getIndexEmpty().get(0);
					else {
						move = new Move(w.getIndexEmpty().get(0), w.getIndexEmpty().get(1));
					}
				}			
			}
			else {//我方为白色
				if(w.getNum() >= b.getNum()) {//如果我方的棋子数大于对方棋子数直接选取我方的子
					if(w.getIndexEmpty().size() > 2)move = new Move(w.getIndexEmpty().get(0), w.getIndexEmpty().get(1));
					if(w.getNum() == 5) winkey = w.getIndexEmpty().get(0);//如果说只差一个子就赢了把这个子的位置存起来
				}
				else {
					//如果是四个点那么就说明只差两点就赢了
					if(w.getNum() == 4) {
						move = new Move(w.getIndexEmpty().get(0), w.getIndexEmpty().get(1));
						x = 1;
					}
					//否则就去堵对面的子
					if(b.getNum() == 5) oppowinkey = b.getIndexEmpty().get(0);
					else {
						move = new Move(b.getIndexEmpty().get(0), b.getIndexEmpty().get(1));
					}
				}
			}	
		}
	}*/	

	public boolean isthreatmove() {
		if(move != null) return true;
		return false;
	}
	
	public boolean isthreatarr() {
		if(num.size() >= 2)return true;
		return false;
	}
	
	
	
	public void findthreatnew(RoadList rl, Board bd) {//获取可能点的
//		Road b = new Road(); 
//		Road w = new Road();
		this.r = rl;
		this.b = bd;
		int n = 0;
		do {
			if(r.getBlackRoads().get(n).getNum() < 3)break;
			addtonum(r.getBlackRoads().get(n).getIndexEmpty());
			if(n == r.getBlackRoads().size() - 1)break;
			n++;
		}while(true);
		n = 0;
		do {
			if(n >= r.getWhiteRoads().size())break;
			if(r.getWhiteRoads().get(n).getNum() < 3 || r.getWhiteRoads().get(n) == null)break;
			addtonum(r.getWhiteRoads().get(n).getIndexEmpty());			
			n++;
		}while(true);
		
		if(num.size() >= 2) {
			findthreatmove(rl);
		}
		
		if(color == 1 && r.getBlackRoads().size() > 0) {//如果自身的棋子为黑色的话
			if(r.getBlackRoads().get(0).getNum() == 4) 
				move = new Move(r.getBlackRoads().get(0).getIndexEmpty().get(0), r.getBlackRoads().get(0).getIndexEmpty().get(1));
			if(r.getBlackRoads().get(0).getNum() == 5 && b.get(r.getBlackRoads().get(0).getIndexEmpty().get(0)) == EMPTY)
				move = new Move(r.getBlackRoads().get(0).getIndexEmpty().get(0), 0);
		}
		if(color == 7 && r.getWhiteRoads().size() > 0) {
			if(r.getWhiteRoads().get(0).getNum() == 4) 
				move = new Move(r.getWhiteRoads().get(0).getIndexEmpty().get(0), r.getWhiteRoads().get(0).getIndexEmpty().get(1));
			if(r.getWhiteRoads().get(0).getNum() == 5 && b.get(r.getWhiteRoads().get(0).getIndexEmpty().get(0)) == EMPTY)
				move = new Move(r.getWhiteRoads().get(0).getIndexEmpty().get(0), 0);
		}
	}
	
	public void findthreatmove(RoadList rl) {
		RoadList list = new RoadList();
		list.copy(rl);
		int x;
		int y = 0;
		int z = 0;
		if(color == 7)
			x = list.getbfandf();
		else
			x = list.getwfandf();		
		for(int i : num) {
			for(int j : num) {
				if(i == j)continue;
				
//				list.MakeMove(i, color);
//				list.MakeMove(j, color);
				list = new RoadList(rl, i, j, color);
				if(color == 7)
					y = x - list.getbfandf();
				else
					y = x - list.getwfandf();
				if(z < y) {
					z = y;
					move = new Move(i ,j);
				}
				//list.copy(rl);
			}
			//list.copy(rl);
		}
	}
	
	
	
	public void addtonum(ArrayList<Integer> a) {
		for(int i : a) {
			if(nohave(i) && b.get(i) == EMPTY)num.add(i);
		}
	}
	
	public Move getmov() {
		return move;
	}
	
	public boolean nohave(int i) {
		for(int j : num)
			if(j == i)return false;
		return true;
	}
	
	public ArrayList<Integer> getNum(){
		return num;
	}
}
