package s17020032009.player;

public class Point {
	int color;//0��ʾû����ɫ1Ϊ��ɫ7Ϊ��ɫ
	int index;//��¼λ��
	
	public Point() {}
	
	public Point(int index, int color) {
		this.color = color;
		this.index = index;
	}
	
	public Point(Point p) {
		this.color = p.color;
		this.index = p.index;
	}
	
	public void setColor(int color) {//��ɫȫ��ͳһ
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getIndex() {
		return index;
	}
}
