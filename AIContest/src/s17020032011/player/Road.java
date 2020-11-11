package s17020032011.player;

import static core.board.PieceColor.EMPTY;

public class Road {
	private int x;//∆Â≈ÃŒª÷√
	private int y;//∆Â≈Ã∑ΩœÚ
	private int BlackNum;
	private int WhiteNum;
	private boolean active;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getBlackNum() {
		return BlackNum;
	}

	public void setBlackNum(int blackNum) {
		BlackNum = blackNum;
	}

	public int getWhiteNum() {
		return WhiteNum;
	}

	public void setWhiteNum(int whiteNum) {
		WhiteNum = whiteNum;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	

	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Road) {
			Road road=(Road)obj;	
			if(this.x==road.getX()&&this.y==road.getY()) {
				return true;
			}
		}
		return false;
	}
	

	
	
	

	public Road(int x,int y,int BlackNum,int WhiteNum,boolean active ) {
		this.x=x;
		this.y=y;
		this.BlackNum=BlackNum;
		this.WhiteNum=WhiteNum;
		this.active=active;
	}
	
}
