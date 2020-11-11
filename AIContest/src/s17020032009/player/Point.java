package s17020032009.player;

public class Point {
	int color;//0表示没有颜色1为黑色7为白色
	int index;//记录位置
	
	public Point() {}
	
	public Point(int index, int color) {
		this.color = color;
		this.index = index;
	}
	
	public Point(Point p) {
		this.color = p.color;
		this.index = p.index;
	}
	
	public void setColor(int color) {//颜色全部统一
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getIndex() {
		return index;
	}
}
