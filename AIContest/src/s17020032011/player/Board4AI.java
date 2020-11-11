package s17020032011.player;

import core.board.Board;
import core.board.PieceColor;
import core.game.Move;

import static core.board.PieceColor.*;
import static core.game.Move.SIDE;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Board4AI extends Board {
	
	static int [][]B=new int [SIDE][SIDE];
	static {
		for(int i=0;i<SIDE;i++) {
			for(int j=0;j<SIDE;j++) {
				B[i][j]=i*SIDE+j;
			}
		}
	}
	
	public Board4AI() {
		roadTable.clear();
		changeRoads(Move.index('J', 'J'));
		

	}
	public Board4AI(Board b) {
		super(b);
	}
	
	
	//根据某个点的位置更改RoadTable
	void changeRoads(int x) {
		int t=0;
		int a=0;
		int b=0;
		//System.out.println("x:"+x);
    	for(int i=0;i<4;i++) {
    		
    		a=x/SIDE;
    		b=x%SIDE;
//			System.out.println("a"+a);
//			System.out.println("b"+b);
    		t=x;//落子位置

			//System.out.println("a"+a);
			//System.out.println("b"+a);
    		for(int j=0;j<=5;j++) {
    			if(j!=0) {
        			a=a-FORWARD[i][0];
        			b=b-FORWARD[i][1];
    			}
    			if((a<0||b<0)||(a>SIDE-1||b>SIDE-1)||(a+5*FORWARD[i][0]>SIDE-1||b+5*FORWARD[i][1]>SIDE-1)||(a+5*FORWARD[i][0]<0||b+5*FORWARD[i][1]<0)) {
    				continue;
    			}
    			int n=a;
    			int m=b;
				int CB=0;
				int CW=0;
				
    			for(int u=0;u<=5;u++) {
        			if(u!=0) {
        				n=n+FORWARD[i][0];
        				m=m+FORWARD[i][1];
        			}

    				if(get(B[n][m])==WHITE) {
    					CW=CW+1;
    				}
    				if(get(B[n][m])==BLACK) {
    					CB=CB+1;
    				}
    			}
    			roadTable.addRoads(new Road(a*SIDE+b,i,CB,CW,true));
    			roadTable.refreshRoad(new Road(a*SIDE+b,i,CB,CW,true));	
    		}
 		
    	}
    	
//    		System.out.println(B[a][b]);
//    		System.out.println(B[SIDE-1][SIDE-1]);
    	
	}
	
	
	
	
	
	

    public void undo() {
    	super.undo();
    }


	
	
   public void makeMove(Move mov) {
	   super.makeMove(mov);
	   changeRoads(mov.index1());
	   changeRoads(mov.index2());
    }

	
	public int getScore() {
		return roadTable.getScore(whoseMove());
	}
	
	public ArrayList<Integer> getTSS() {

		//我方威胁
		ArrayList list = new ArrayList();
		list=getpoint(whoseMove(),5);
		if(list.size()==0) {
			list=getpoint(whoseMove(),4);
		}
		ArrayList pointlist=new ArrayList();
		//敌方威胁
		if(list.size()==0) {
			for(int s=5;s>=4;s--) {
				pointlist=getpoint(whoseMove().opposite(),s);
				for(int i=0;i<pointlist.size();i++) {
					if(!list.contains(pointlist.get(i))) {
						set((int) pointlist.get(i),whoseMove());
						this.changeRoads((int) pointlist.get(i));
						if(!hasThreat(whoseMove().opposite())) {
							list.add(pointlist.get(i));
						}
						set((int) pointlist.get(i),EMPTY);
						this.changeRoads((int) pointlist.get(i));
					
					}
				}
			}
		}
		
		
		if(list.size()==0) {
			for(int s=5;s>=4;s--) {
				pointlist=getpoint(whoseMove().opposite(),s);
				for(int i=0;i<pointlist.size();i++) {
					if(!list.contains(pointlist.get(i))) {
						list.add(pointlist.get(i));
					}
				}
			}
		}
		
		pointlist.clear();
		if(list.size()==0) {
			pointlist=getpoint(whoseMove(),3);
			for(int i=0;i<pointlist.size();i++) {
				set((int) pointlist.get(i),whoseMove());
				this.changeRoads((int) pointlist.get(i));
				if(!canSolve(whoseMove())) {
					list.add(pointlist.get(i));
				}
				set((int) pointlist.get(i),EMPTY);
				this.changeRoads((int) pointlist.get(i));
			}	
		}
		
		pointlist.clear();
		if(list.size()==0) {
			pointlist=getpoint(whoseMove().opposite(),3);
			for(int i=0;i<pointlist.size();i++) {
				set((int) pointlist.get(i),whoseMove().opposite());
				this.changeRoads((int) pointlist.get(i));
				if(!canSolve(whoseMove().opposite())) {
					list.add(pointlist.get(i));
				}
				set((int) pointlist.get(i),EMPTY);
				this.changeRoads((int) pointlist.get(i));
			}	
		}
		

		
		
		//创造威胁
		
//		pointlist.clear();
//		if(list.size()==0) {
//			pointlist=getpoint(whoseMove(),3);
//			for(int i=0;i<pointlist.size();i++) {
//				list.add(pointlist.get(i));
//			}
//			
//		}


		return list;	
	}
	
	

	
	
	
	
	public ArrayList getpoint(PieceColor color,int leng) {
		int W=0,B=0;
		if(color==WHITE) {
			W=1;
		}else {
			B=1;
		}
		int a=this.roadTable.playerRoads[B*leng][W*leng].size();
		ArrayList pointlist=new ArrayList();
		for(int j=0;j<a;j++) {
			Road road=this.roadTable.playerRoads[B*leng][W*leng].get(j);
			ArrayList list1=this.getEMPTY(road);
			for(int i=0;i<list1.size();i++) {
				if(!pointlist.contains(list1.get(i))) {
					pointlist.add(list1.get(i));
				}
			}
		}
		return pointlist;
	}
	/**
	 * 是否有color颜色形成的威胁
	 * @param color
	 * @return
	 */
	public boolean hasThreat(PieceColor color) {
		int W=0,B=0;
		if(color==WHITE) {
			W=1;
		}else {
			B=1;
		}
		if(this.roadTable.playerRoads[B*5][W*5].size()!=0||this.roadTable.playerRoads[B*4][W*4].size()!=0) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean hasFour(PieceColor color) {
		int W=0,B=0;
		if(color==WHITE) {
			W=1;
		}else {
			B=1;
		}
		if(this.roadTable.playerRoads[B*4][W*4].size()!=0) {
			return true;
		}else {
			return false;
		}
	}
	
		

	/**
	 * 输入一个路，输出该路的空位置
	 * @param road
	 * @return
	 */
	public ArrayList getEMPTY(Road road){
		ArrayList list = new ArrayList();
		int x=road.getX();
		int y=road.getY();
		int temp=x;
		for(int i=0;i<=5;i++) {
			if(get(temp)==EMPTY) {
				list.add(temp);
			}
			temp=temp+FORWARD[y][0]*SIDE+FORWARD[y][1];
			
		}
		return list;
	}
	
	@Override
	public boolean gameOver() {
		if(this.roadTable.playerRoads[0][6].size()>0||this.roadTable.playerRoads[6][0].size()>0) {
			return true;
		}else {
			return false;
		}

    }
	
	
	private RoadTable roadTable=new RoadTable() ;
	
	

	 public static final int W=5;
	public ArrayList<Move> getMove() {
		// TODO Auto-generated method stub
		
		ArrayList<Move4AI> list=new ArrayList<Move4AI>();
		ArrayList list1=this.getTSS();
		if(list1.isEmpty()) {
			list1 = this.getMovePoint();
		}
		int x1,x2;
		for(int i=0;i<list1.size();i++) {
			x1=(int) list1.get(i);
			if(get(x1)!=EMPTY) {
				continue;
			}
			set(x1,whoseMove());
			this.changeRoads(x1);
			ArrayList list2=this.getTSS();
			if(list2.isEmpty()) {
				list2 = this.getMovePoint();
			}
			for(int j=0;j<list2.size();j++) {
				x2=(int) list2.get(j);
				
				Move4AI move=new Move4AI(x1,x2);
				if(!list.contains(move)) {
					list.add(move);
				}
				
			}
			set(x1,EMPTY);
			this.changeRoads(x1);
			
			
		}
//		int w=0;
//		w= (list.size()<9)? list.size():9;
		ArrayList<Move> list3=new ArrayList<Move>();
		for(int i=0;i<list.size();i++) {
			list3.add(list.get(i).getMove());
		}
		
		
		return list3;
		
	}
	
	public ArrayList getMovePoint() {
		// TODO Auto-generated method stub
		
		ArrayList list=new ArrayList();
		list.clear();
		L.clear();
//		for(int i=0;i<=SIDE*SIDE-1;i++) {
//			for(int j=0;j<=SIDE*SIDE-1;j++) 
//			if(this.get(i)==EMPTY&&this.get(j)==EMPTY&&i!=j) {
//				list.add(new Move(i,j));
//			}
//		}
//		return list;
		
		boolean flag=false;
		
	
		
		for(int i=1;i<=3;i++) {
			list=getpoint(whoseMove(),i);
			for(int j=0;j<list.size();j++) {
				flag=false;
				if(get((int) list.get(j))==EMPTY) {
					flag=true;
					set((int) list.get(j),whoseMove());
					
					this.changeRoads((int) list.get(j));
					//System.out.println("score"+this.getScore());
					Point po =new Point((int) list.get(j),this.getScore());
					if(!L.contains(po)) {
						L.add(po);
					}
				}
				if(flag==true) {
					set((int) list.get(j),EMPTY);
					
					this.changeRoads((int) list.get(j));
				}
			}

		}
		int w=0;
		list.clear();
		w= (L.size()<6)? L.size():6;
		for(int i=0;i<w;i++) {
			int x=L.poll().getIndex();
			if(!list.contains(x));
			list.add(x);
		}
		L.clear();
		
		
		
		
		return list;

	}
	
	
	
    private Comparator<Point> comparator1 = new Comparator<Point>(){

        @Override

        public int compare(Point s1, Point s2) {

            return s2.getScore()-s1.getScore();

        }

    };
    private Comparator<Move4AI> comparator2 = new Comparator<Move4AI>(){

        @Override

        public int compare(Move4AI s1, Move4AI s2) {

            return s2.getScore()-s1.getScore();

        }

    };
    PriorityQueue<Move4AI> Lc = new PriorityQueue<Move4AI>(comparator2);
	PriorityQueue<Point> L = new PriorityQueue<Point>(comparator1);
	PriorityQueue<Point> LI = new PriorityQueue<Point>(comparator1);
	
	
	
	  public Move Find() {
		  Move m = null;
		  int a=Integer.MIN_VALUE;
		  int b=Integer.MAX_VALUE;
		  int n=a;
		  for(Move move : this.getMove()) {
			  this.makeMove(move);
			  int c=alphabeta(false, 2, this, a, b);
			  //System.out.println("score"+c);
			  if(c>n) {
				  n=c;
				  m=move;
			  }
			  this.undo();
		  }
		  return m;
	}
	    
	    
		private int alphabeta(boolean flag, int depth, Board4AI board, int a,int b) {
			if(board.gameOver()||depth==0) {
				return board.getScore();
			}
			if(flag) {
				for(Move move : board.getMove()) {
					//System.out.println("长度"+board.getMove().size());
					board.makeMove(move);
					int tmp = alphabeta(false, depth - 1, board, a, b);
					board.undo();
					if (a< tmp) {
						a=tmp;
					}
					if (b<= a) {
						return a;
					}
				}
				return a;
			}else {
				for(Move move : board.getMove()) {
					//System.out.println("长度"+board.getMove().size());
					board.makeMove(move);
					int tmp = alphabeta(true, depth - 1, board, a, b);
					board.undo();
					if (b> tmp) {
						b=tmp;
					}
					if (b<= a) {
						return b;
					}
				}
				return b;
				
			}
		}
		
		

		
		boolean canSolve(PieceColor color) {
			ArrayList pointlist=new ArrayList();
			ArrayList list = new ArrayList();
			for(int s=5;s>=4;s--) {
				pointlist=getpoint(color,s);
				for(int i=0;i<pointlist.size();i++) {
					if(!list.contains(pointlist.get(i))) {
						list.add(pointlist.get(i));
					}
				}
			}
			boolean flag=false;
			if(!hasThreat(color)) {
				flag=true;
				return flag;
			}
			int sizepoint=list.size();
			if(sizepoint>0) {
				for(int i=0;i<sizepoint;i++) {
					for(int j=i+1;j<sizepoint;j++) {
						set((int) pointlist.get(i),color.opposite());
						set((int) pointlist.get(j),color.opposite());
						
						this.changeRoads((int) pointlist.get(i));
						this.changeRoads((int) pointlist.get(j));
						if(!hasThreat(color)) {
							flag=true;
						}
						set((int) pointlist.get(i),EMPTY);
						set((int) pointlist.get(j),EMPTY);
						
						this.changeRoads((int) pointlist.get(i));
						this.changeRoads((int) pointlist.get(j));
						if(flag==true) {
							return flag;
						}
					}
				}
			}
			return flag;
			
			
			
		}
		
		
		
		
	
	
	 public static void main(String[] args) {
		 Board4AI B=new Board4AI();

		 B.makeMove(new Move(5,6));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 
		 B.makeMove(new Move(12,19));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 B.makeMove(new Move(1,60));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 B.makeMove(new Move(15,70));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 
		 B.makeMove(new Move(44,85));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 B.makeMove(new Move(92,84));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 B.makeMove(new Move(75,37));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 B.makeMove(new Move(20,21));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 B.makeMove(new Move(300,235));
		 B.draw();
		 B.makeMove(new Move(23,24));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 B.makeMove(new Move(25,333));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 B.makeMove(new Move(253,190));
		 B.draw();
		 System.out.println("score"+(-B.getScore()));
		 System.out.println("score"+(-B.getScore()));
		 
		 
		 
//		 ArrayList list=B.getMovePoint();
//		 for(int i=0;i<list.size();i++) {
//			 System.out.println("pointx1:"+(int)list.get(i));
//			 
//		 }
		 
		 
//		 if(list.size()==0) {
//			 list=B.getMovePoint();
//		 }
//		 System.out.println("size:"+list.size());
//		 for(int i=0;i<list.size();i++) {
//			 System.out.println("point1:"+(int)list.get(i));
//		 }
		 ArrayList<Move> list2=B.getMove();
		 for(int i=0;i<list2.size();i++) {
			 System.out.println("pointx1:"+(int)list2.get(i).index1());
			 System.out.println("pointx2:"+(int)list2.get(i).index2());
		 }
		 //Move mo=B.Find();
		// System.out.println("pointxxxxx1:"+mo.index1());
		 //System.out.println("pointxxxxx2:"+mo.index2());
		 
		 
		
		 
//		 B.getMove();
//		 B.getMove();
	 }

}
