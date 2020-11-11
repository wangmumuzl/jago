package baseline.player;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class RoadList extends ArrayList<Road> {
	
	//往RoadList中添加一条路，添加在Array的最后
	public void addRoad(Road road) {
		this.add(road);
		road.setIndex(size() - 1);
	}
	
	//从RoadList中删除某条路road，前提是该road在RoadList中
	public void removeRoad(Road road) {
		
		//如果当前List为空
		if (this.isEmpty()) return;
		
		//删除List中的最后一条路，并返回至lastRoad中
		Road lastRoad = this.remove(size() - 1);	
		
		//要删除的路在RoadList中的位置
		int index = road.getIndex();
		
		//如果要删除的路不是最后一条路
		if (index < size()) {
			//最后一条路的index设为被删除的路的index
			lastRoad.setIndex(index);
			//在列表中，用最后一条路替换掉index位置的路
			set(index, lastRoad);
		}
	}
}
