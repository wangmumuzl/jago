package baseline.player;

import java.util.ArrayList;

import core.board.Board;
import core.board.PieceColor;

import static core.board.PieceColor.*;
import static core.game.Move.*;
import static core.board.Board.FORWARD;
public class Road {
	
	//ĳ��ֱ��Ӱ�쵽�ķ�Χ����8����������ڸõ��λ����
	public static final int DIRECTIONS[][] = {
		//�£� �ң����£�����
		{0, 1}, {1, 0}, {1, 1}, {1, -1},
		//�ϣ������ϣ� ����
		{0, -1}, {-1, 0}, {-1, -1}, {-1, 1} 
	};
	
	
	public Road() {
		// TODO Auto-generated constructor stub
	}

	public Road(int startPos, int dir, int blackNum, int whiteNum, int index) {
		super();
		this.startPos = startPos;
		this.dir = dir;
		this.blackNum = blackNum;
		this.whiteNum = whiteNum;
		this.index = index;
	}
	
	//���·�����һ������
	public void addStone(PieceColor stone) {
		if (stone == BLACK) blackNum++;
		else if (stone == WHITE) whiteNum++;
	}
	//�Ӹ�·���Ƴ�һ������
	public void removeStone(PieceColor stone) {
		if (stone == BLACK) blackNum--;
		else if (stone == WHITE) whiteNum--;
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
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private int startPos; 	//��·�����ı�� ��0~360��
	private	int dir;   	  	//��·�ķ�������ǰ������ �£��ң����£�����

	private int blackNum; 	//��·�к��ӵĸ���
	private int whiteNum; 	//��·�а��ӵĸ���
	
	private int index; 		//��·������Ӧ�ĺڰ�·���е��±�; �ڸ�·����ӵ��ڰ�·����ʱȷ��

	/**
	 * ��ǰ·�����ǰ׷���ڷ���4·����5·������Ѱ�ҿ�λ���γ�ʤ��.
	 * 4·��������λ��5·��һ����λ
	 * @param b
	 * @return
	 */
	public ArrayList<Point> findEmptyPos(Board b) {
		ArrayList<Point> points = new ArrayList<>();
		//�ӵ�ǰλ�ÿ�ʼ����dir����һ��ǰ��1��λ�ã�Ѱ�ҿ�λ
		for (int i = 0; i < 6; i++){
			//�ӵ�ǰ·�Ŀ�ʼλ�ã���dir������ǰ�ƶ�i����
			int pos = startPos + i * (FORWARD[dir][0] + FORWARD[dir][1] * SIDE);
			
			//�ƶ�����λ���Ƿ�Ϊ��
			if (b.get(pos) == EMPTY) {
				points.add(new Point(pos, 0));				
			}
		}
		return points;
	}
	
	/**
	 * ��·��Ѱ�ҿ�λ����Щ�Ѿ�������·�б������Ŀ�λ������������
	 * @param b
	 * @param vis
	 * @return
	 */
	public ArrayList<Point> findEmptyPos(Board b, boolean[] vis) {
		ArrayList<Point> points = new ArrayList<>();
		//�ӵ�ǰλ�ÿ�ʼ����dir����һ��ǰ��1��λ�ã�Ѱ�ҿ�λ
		for (int i = 0; i < 6; i++){
			//�ӵ�ǰ·�Ŀ�ʼλ�ã���dir������ǰ�ƶ�i����
			int pos = startPos + i * (FORWARD[dir][0] + FORWARD[dir][1] * SIDE);
			
			//����ƶ�����λ���ǿ�λ�����һ�û�в鿴��
			if (b.get(pos) == EMPTY && !vis[pos]) {
				//����λ����ӵ��㼯����
				points.add(new Point(pos, 0));
				//���ø�λ���ѱ����ʹ�
				vis[pos] = true;
			}
		}
		return points;
	}
}
