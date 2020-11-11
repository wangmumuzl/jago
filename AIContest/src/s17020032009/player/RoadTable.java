package s17020032009.player;

import core.board.Board;
import static core.board.PieceColor.*;
import core.game.*;
import java.util.*;


public class RoadTable {//���������һϵ�еĲ�����������֦�������������λ��
	private RoadList roadlist = new RoadList();
	private Board board = new Board();
	//private ArrayList<RoadList> rls = new ArrayList<RoadList>();//���ڴ��״̬������·���״̬����
	private Board4AI r4 = new Board4AI();
	
	public RoadTable() {}
	
	public RoadTable(Board board) {
		//System.out.println("RoadTable(Board board)");
		this.board = board;
		roadlist = new RoadList(board);
		//roadlist.showallroadsnum();//��ʼ��һ�����ⶼû��
		//roadlist.shownums();
	}
	
	public void newgame(Board board) {
		this.board = board;
		roadlist = new RoadList(board);
		roadlist.shownums();
	}
	
	public void MakeMove(Move mov) {
		//board.makeMove(mov);���Ӧ�÷�������棬Ҫ��������whosemove�ͱ���
		int[] index = new int[2];
		index[0] = Move.index(mov.col0(), mov.row0());
		index[1] = Move.index(mov.col1(), mov.row1());
		int color;
		if(board.whoseMove() == BLACK)
			color = 1;
		else 
			color = 7;
		for(int i : index)
			roadlist.MakeMove(i, color);
		board.makeMove(mov);
		//roadlist.showallroadsnum();
	}
	
	private int deep = 4;//���
	
	public Move findMove() {
		//rls.add(roadlist);
		//alpha_beta();
		int color = color();
		int value = -10000;
		//�Ƚ���isthreat�жϣ�Ȼ���ٽ����֦
		ArrayList<Integer> move = new ArrayList<Integer>();
		r4 = new Board4AI(color);
		r4.findthreatnew(roadlist, board);
		if(r4.isthreatmove()) {
			return r4.getmov();
		}
		if(r4.isthreatarr()) {
			move = r4.getNum();
		}
		else
			move = roadlist.getfirstten();
		//ArrayList<Integer> move = r4.getNum();
		//ArrayList<Integer> move = roadlist.getfirstten();
		//System.out.println(move.size());
		
		Move mov = randMove(move);//�����Ը�ֵ
		//roadlist.showallroadsnum();
		//System.out.println("before");
		//roadlist.shownums();//��ʾroadlist�����е�·�ĸ��� 
		int v;
		
		for(int i : move) {
			if(board.get(i) != EMPTY)continue;
			for(int j : move) {
				if(i == j || board.get(j) != EMPTY)continue;//�����������ֱͬ�ӽ�����һ��ѭ��
				//roadlist.MakeMove(i, color());
				//roadlist.MakeMove(j, color());
				
				board.makeMove(new Move(i, j));
				v = alpha_beta(new RoadList(roadlist, i, j, color), roadlist.getSocre(), roadlist.getSocre(), 0);
				if(value < v) {
					value = v; 
					mov = new Move(i, j);
				}				
				board.undo();
			}
		}
		//System.out.println("value = " + value);
		//roadlist.shownums();
		return mov;
	}
	
	public Move randMove(ArrayList<Integer> a) {
		Random rand = new Random();
		int index1;
		int index2;
		do {
			index1 = a.get(rand.nextInt(a.size()));
			if(board.get(index1)==EMPTY)break;
		}while(true);
		do {
			index2 = a.get(rand.nextInt(a.size()));
			if(board.get(index2)==EMPTY && index2 != index1)break;
		}while(true);
		return new Move(index1, index2);
	}
	
	public int color() {
		if(board.whoseMove() == BLACK)return 1;
		else return 7;
	}
	
	//��������r��Ҫ��ģ���һ����֮��ģ�����a�Ǿ�����С��ֵ��b�Ǿ����ܴ��ֵ
	public int alpha_beta(RoadList r, int a, int b, int n) {//nΪ��������ʼֵΪ0
		int value;
		if(n == deep) {
			return r.getSocre();
		}
		//ArrayList<Integer> move = r.getfirstten();
		//rls.add(r);//���һ�µ�ǰ��
		//roadlist.showallroadsnum();//color = 0��black����white�д�����Գ��� 
		int color = color();
		ArrayList<Integer> move = new ArrayList<Integer>();
		r4 = new Board4AI(color);
		r4.findthreatnew(roadlist, board);
		if(r4.isthreatarr()) {
			move = r4.getNum();
		}
		else 
			move = r.getfirstten();
		for(int i : move) {
			if(board.get(i) != EMPTY)break;
			for(int j : move) {
				if(i == j || board.get(j) != EMPTY)continue;//�����������ֱͬ�ӽ�����һ��ѭ��
				//r.MakeMove(i, color());
				//r.MakeMove(j, color());
				board.makeMove(new Move(i, j));
				value = alpha_beta(new RoadList(r, i, j, color), a, b, n + 1);
				board.undo();
				//r = rls.get(n + 1);
				if(n % 2 == 0) {//ż����ȡ��С
					if(value < b)
						b = value;				
					else {
						//rls.remove(n + 1);
						return b;
					}
						
				}
				else {//������ȡ���
					if(value > a)
						a = value;
					else {
						//rls.remove(n + 1);
						return a;
					}
				}
			}
		}
		//rls.remove(n + 1);
		return b;//������return���Բ����ߵ���һ��  
	}
}
