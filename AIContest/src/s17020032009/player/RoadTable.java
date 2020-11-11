package s17020032009.player;

import core.board.Board;
import static core.board.PieceColor.*;
import core.game.*;
import java.util.*;


public class RoadTable {//在这里进行一系列的操作，包括剪枝，返回最佳下棋位置
	private RoadList roadlist = new RoadList();
	private Board board = new Board();
	//private ArrayList<RoadList> rls = new ArrayList<RoadList>();//用于存放状态，方便路表的状态返回
	private Board4AI r4 = new Board4AI();
	
	public RoadTable() {}
	
	public RoadTable(Board board) {
		//System.out.println("RoadTable(Board board)");
		this.board = board;
		roadlist = new RoadList(board);
		//roadlist.showallroadsnum();//初始化一点问题都没有
		//roadlist.shownums();
	}
	
	public void newgame(Board board) {
		this.board = board;
		roadlist = new RoadList(board);
		roadlist.shownums();
	}
	
	public void MakeMove(Move mov) {
		//board.makeMove(mov);这个应该放在最后面，要是先下了whosemove就变了
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
	
	private int deep = 4;//深度
	
	public Move findMove() {
		//rls.add(roadlist);
		//alpha_beta();
		int color = color();
		int value = -10000;
		//先进行isthreat判断，然后再进入剪枝
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
		
		Move mov = randMove(move);//象征性赋值
		//roadlist.showallroadsnum();
		//System.out.println("before");
		//roadlist.shownums();//显示roadlist中所有的路的个数 
		int v;
		
		for(int i : move) {
			if(board.get(i) != EMPTY)continue;
			for(int j : move) {
				if(i == j || board.get(j) != EMPTY)continue;//如果两个点相同直接进行下一个循环
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
	
	//这个传入的r是要有模拟第一步棋之后的，输入a是尽可能小的值，b是尽可能大的值
	public int alpha_beta(RoadList r, int a, int b, int n) {//n为阶数，初始值为0
		int value;
		if(n == deep) {
			return r.getSocre();
		}
		//ArrayList<Integer> move = r.getfirstten();
		//rls.add(r);//存放一下当前表
		//roadlist.showallroadsnum();//color = 0在black或者white中存放所以出错 
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
				if(i == j || board.get(j) != EMPTY)continue;//如果两个点相同直接进行下一个循环
				//r.MakeMove(i, color());
				//r.MakeMove(j, color());
				board.makeMove(new Move(i, j));
				value = alpha_beta(new RoadList(r, i, j, color), a, b, n + 1);
				board.undo();
				//r = rls.get(n + 1);
				if(n % 2 == 0) {//偶数层取最小
					if(value < b)
						b = value;				
					else {
						//rls.remove(n + 1);
						return b;
					}
						
				}
				else {//奇数层取最大
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
		return b;//象征性return绝对不会走到这一步  
	}
}
