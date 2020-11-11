package s17020031094.player;

public class Road {

	
	public Road() {
		// TODO Auto-generated constructor stub
	}

	public Road(int startPos, int dir, int blackNum, int whiteNum, boolean active) {
		super();
		this.startPos = startPos;
		this.dir = dir;
		this.blackNum = blackNum;
		this.whiteNum = whiteNum;
		this.active = active;
	}
	
	public void changeRoad(int blackNum,int whiteNum,boolean active){
		this.blackNum = blackNum;
		this.whiteNum = whiteNum;
		this.active = active;
	}
	public int getBlackNum() {
		return blackNum;
	}
	public int getWhiteNum() {
		return whiteNum;
	}
	
	public boolean isEmpty() {
		return blackNum == 0 && whiteNum == 0;
	}
	
	public int getStartPos() {
		return startPos;
	}

	public int getDir() {
		return dir;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + blackNum;
		result = prime * result + dir;
		result = prime * result + startPos;
		result = prime * result + whiteNum;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Road other = (Road) obj;
		if (active != other.active)
			return false;
		if (blackNum != other.blackNum)
			return false;
		if (dir != other.dir)
			return false;
		if (startPos != other.startPos)
			return false;
		if (whiteNum != other.whiteNum)
			return false;
		return true;
	}

	private int startPos; 	//该路的起点的编号 （0~360）
	private	int dir;   	  	//该路的方向，起点的前进方向： 下，右，右下，右上

	private int blackNum; 	//该路中黑子的个数
	private int whiteNum; 	//该路中白子的个数
	
	private boolean active; 		

}
