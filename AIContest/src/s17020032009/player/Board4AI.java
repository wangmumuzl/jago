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

	
	/*public void findThreat() {//���arraylistÿ��ֻ���һ�������
		//������������ɫ��
		Road b = new Road();
		Road w = new Road();
		b = r.getBlackRoads().get(0);//��ȡ�ڰ�·��ĵ�һ��
		w = r.getWhiteRoads().get(0);
		if(b.getNum() > 2 || w.getNum() > 2) {
			if(color == 1) {//����ҷ�Ϊ��ɫ
				if(b.getNum() >= w.getNum()) {//����ҷ������������ڶԷ�������ֱ��ѡȡ�ҷ�����
					if(b.getIndexEmpty().size() > 2)move = new Move(b.getIndexEmpty().get(0), b.getIndexEmpty().get(1));
					if(b.getNum() == 5) winkey = b.getIndexEmpty().get(0);//���˵ֻ��һ���Ӿ�Ӯ�˰�����ӵ�λ�ô�����
				}
				else {
					//������ĸ�����ô��˵��ֻ�������Ӯ��
					if(b.getNum() == 4) {
						move = new Move(b.getIndexEmpty().get(0), b.getIndexEmpty().get(1));
						x = 1;
					}
					//�����ȥ�¶������
					if(w.getNum() == 5) oppowinkey = w.getIndexEmpty().get(0);
					else {
						move = new Move(w.getIndexEmpty().get(0), w.getIndexEmpty().get(1));
					}
				}			
			}
			else {//�ҷ�Ϊ��ɫ
				if(w.getNum() >= b.getNum()) {//����ҷ������������ڶԷ�������ֱ��ѡȡ�ҷ�����
					if(w.getIndexEmpty().size() > 2)move = new Move(w.getIndexEmpty().get(0), w.getIndexEmpty().get(1));
					if(w.getNum() == 5) winkey = w.getIndexEmpty().get(0);//���˵ֻ��һ���Ӿ�Ӯ�˰�����ӵ�λ�ô�����
				}
				else {
					//������ĸ�����ô��˵��ֻ�������Ӯ��
					if(w.getNum() == 4) {
						move = new Move(w.getIndexEmpty().get(0), w.getIndexEmpty().get(1));
						x = 1;
					}
					//�����ȥ�¶������
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
	
	
	
	public void findthreatnew(RoadList rl, Board bd) {//��ȡ���ܵ��
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
		
		if(color == 1 && r.getBlackRoads().size() > 0) {//������������Ϊ��ɫ�Ļ�
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
