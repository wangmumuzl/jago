package baseline.player;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class RoadList extends ArrayList<Road> {
	
	//��RoadList�����һ��·�������Array�����
	public void addRoad(Road road) {
		this.add(road);
		road.setIndex(size() - 1);
	}
	
	//��RoadList��ɾ��ĳ��·road��ǰ���Ǹ�road��RoadList��
	public void removeRoad(Road road) {
		
		//�����ǰListΪ��
		if (this.isEmpty()) return;
		
		//ɾ��List�е����һ��·����������lastRoad��
		Road lastRoad = this.remove(size() - 1);	
		
		//Ҫɾ����·��RoadList�е�λ��
		int index = road.getIndex();
		
		//���Ҫɾ����·�������һ��·
		if (index < size()) {
			//���һ��·��index��Ϊ��ɾ����·��index
			lastRoad.setIndex(index);
			//���б��У������һ��·�滻��indexλ�õ�·
			set(index, lastRoad);
		}
	}
}
