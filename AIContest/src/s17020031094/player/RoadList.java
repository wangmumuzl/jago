package s17020031094.player;

import java.util.HashSet;

import s17020031094.player.Road;

@SuppressWarnings("serial")
public class RoadList extends HashSet<Road> {

	public boolean addRoad(Road road) {
		return add(road);
	}
	
	public boolean removeRoad(Road road){
		return remove(road);
	}
	
}
