package s17020032011.player;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class RoadList extends ArrayList<Road> {
	public RoadList() {
		super();
	}
	
	//往RoadList中添加一条路，添加在ArrayList的最后
	public void addRoad(Road road) {
		add(road);
	}
	public void removeRoad(Road road) {
		if(contains(road)) {
			remove(indexOf(road));
		}
	}
}
