package s17020032009.player;
import java.util.*;


public class Road {//最基本的路的结构，就是有路
	//private int[] point = new int[6];//用于该路的信息，白子为7，黑子为1
	//private ArrayList<Point> points = new ArrayList<Point>();
	private HashMap<Integer, Point> p = new HashMap<Integer, Point>();
	private int start;//路的起始点
	private int d;//路向那个方向进行延伸，这里规定0，1，2，3分别为横向，纵向，左上到右下，右上到左下
	private int roadcolor;//该路的颜色1为黑色，7为白色，0为没有颜色即空路或者是两种颜色都有
	private int num;//记录数量
	private int[][]direct = {{1, 0}, {0, 1}, {1, 1}, {-1, 1}};
	private int SIDE = 19;
	//private int sum;
	private int x = 0;//看这个路里有没有下过棋
	
	
	public Road() {}
	
	public Road(int start, int d, int[] color) {
		this.start = start;
		this.d = d;
		int index;
		for(int i = 0; i < 6; i++) {
			index = start + i * direct[d][0] + i * SIDE * direct[d][1];
			p.put(index, new Point(index, color[i]));
			//points.add(new Point(start + i * direct[d][0] + i * SIDE * direct[d][1], color[i]));
		}
		Color(color);//初始化road颜色
		//System.out.println("Color" + roadcolor);
		//System.out.println("Num" + num);
	}
	
	public Road(Road r) {
		this.d = r.getD();
		this.num = r.getNum();
		this.roadcolor = r.getRoadColor();
		this.start = r.start;
		this.x = r.x;
		for(int index : r.p.keySet()) {
			this.p.put(index, new Point(r.p.get(index)));
		}
	}
	
	public void Color(int[] color) {//判断路的颜色
		int sum = 0;
		for(int i : color) {
			sum = sum + i;
		}
		//System.out.println("Sum:" + sum);
		if(sum < 7 && sum > 0) {
			roadcolor = 1;
			num = sum;
			x = 1;
		}
		if(sum > 7 && sum % 7 == 0) {
			roadcolor = 7;
			num = sum / 7;
			x = 1;
		}
		if(sum > 7 && sum % 7 != 0) {
			num = 0;
			roadcolor = 0;
			x = 1;
		}
	}
	
	public void Move(int index, int color) {
		if(roadcolor != 0) {
			if(color == roadcolor) {
				num++;
				//if(num > 5) num = 5;
			}				
			else {
				num = 0;
				roadcolor = 0;
			}
		}
		if(roadcolor == 0 && x == 0) {
			num = 1;
			roadcolor = color;
			x = 1;
		}
		//改point的color
		p.get(index).setColor(color);
	}
	
	public ArrayList<Integer> getIndexEmpty() {//获得空的位置
		ArrayList<Integer> Empty = new ArrayList<Integer>();
		for(int index : p.keySet()) {
			//System.out.println("index" + index);
			//System.out.println("p.get(index).getColor()" + p.get(index).getColor());
			if(p.get(index).getColor() == 0)
				Empty.add(index);
		}
		//System.out.println("ArrayList<Integer> getIndexEmpty() d=" + d);
		return Empty;//返回一个存放此路可下棋的位置的ArrayList
	}
	
	public int getNum() {
		return num;
	}
	
	public int getRoadColor(){
		return roadcolor;
	}
	
	public int getD() {
		return d;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getX() {
		return x;
	}
}
