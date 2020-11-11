package s17020032011.player;



public class Point {
	
	int index;
	int score;
	public Point(int i, int score) {
		// TODO Auto-generated constructor stub
		this.index=i;
		this.score=score;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public void draw() {
		System.out.println("index:"+this.getIndex());
		System.out.println("score:"+this.getScore());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + score;
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
		Point other = (Point) obj;
		if (index != other.index)
			return false;
		if (score != other.score)
			return false;
		return true;
	}
		
}
