package s17020032011.player;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class RoadList extends ArrayList<Road> {
	public RoadList() {
		super();
	}
	
	//��RoadList�����һ��·�������ArrayList�����
	public void addRoad(Road road) {
		add(road);
	}
	public void removeRoad(Road road) {
		if(contains(road)) {
			remove(indexOf(road));
		}
	}
}
