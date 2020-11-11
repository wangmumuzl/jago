package s17020032009.player;
import java.util.*;


public class Road {//�������·�Ľṹ��������·
	//private int[] point = new int[6];//���ڸ�·����Ϣ������Ϊ7������Ϊ1
	//private ArrayList<Point> points = new ArrayList<Point>();
	private HashMap<Integer, Point> p = new HashMap<Integer, Point>();
	private int start;//·����ʼ��
	private int d;//·���Ǹ�����������죬����涨0��1��2��3�ֱ�Ϊ�����������ϵ����£����ϵ�����
	private int roadcolor;//��·����ɫ1Ϊ��ɫ��7Ϊ��ɫ��0Ϊû����ɫ����·������������ɫ����
	private int num;//��¼����
	private int[][]direct = {{1, 0}, {0, 1}, {1, 1}, {-1, 1}};
	private int SIDE = 19;
	//private int sum;
	private int x = 0;//�����·����û���¹���
	
	
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
		Color(color);//��ʼ��road��ɫ
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
	
	public void Color(int[] color) {//�ж�·����ɫ
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
		//��point��color
		p.get(index).setColor(color);
	}
	
	public ArrayList<Integer> getIndexEmpty() {//��ÿյ�λ��
		ArrayList<Integer> Empty = new ArrayList<Integer>();
		for(int index : p.keySet()) {
			//System.out.println("index" + index);
			//System.out.println("p.get(index).getColor()" + p.get(index).getColor());
			if(p.get(index).getColor() == 0)
				Empty.add(index);
		}
		//System.out.println("ArrayList<Integer> getIndexEmpty() d=" + d);
		return Empty;//����һ����Ŵ�·�������λ�õ�ArrayList
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
