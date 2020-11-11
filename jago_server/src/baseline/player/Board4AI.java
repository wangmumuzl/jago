package baseline.player;

import static core.board.PieceColor.*;
import static core.game.Move.SIDE;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import core.board.Board;
import core.board.PieceColor;
import core.game.Move;

public class Board4AI extends Board {

	public Board4AI() {
		// TODO Auto-generated constructor stub
		roadTable.clear();
		changeRoads(Move.index('J', 'J'), BLACK);
	}

	public Board4AI(Board b) {
		super(b);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void makeMove(Move mov) {
		super.makeMove(mov);
		changeRoads(mov);
	}

	@Override
	public void undo(Move mov) {
		super.undo(mov);
		unchangeRoads(mov);
	}

	public int staticScore() {
		return roadTable.getScore(whoseMove());
	}

	public ArrayList<Move> getMoves() {

		ArrayList<Move> moves = new ArrayList<>();

		// �Ƿ��л�ʤ�߲�
		Move move = getWinMove();
		if (move != null) {
			moves.add(move);
			return moves;
		}

		// ������в�ĸ���
		int threats = countAllThreats();

		if (threats == 1) {
			// ��סһ����в���߲�
			return getSingleBlocks();
		}
		if (threats >= 2) {
			// ��ס������в���߲�
			moves = getDoubleBlocks();
			// ����Ҳ�����ס������в���߲�
			if (moves.isEmpty()) {
				// Ͷ������
				moves.add(resign());
			}
			return moves;
		}
		// �ҵ��������������ϵ�topK
		return getFreeMoves();
	}

	// ��ĳ���������
	private void staticScore(Point p) {
		changeRoads(p.getPos(), whoseMove());
		p.setScore(this.staticScore());
		unchangeRoads(p.getPos(), whoseMove());
	}

	// ������ǰ���֣�whoseMove���ı�ʤ�ŷ�
	private Move getWinMove() {
		// ȡ����ǰ���ֵ�4·��5·������һ�����б�ʤ�ŷ�
		RoadList four = roadTable.getActiveRoads(whoseMove(), 4);
		RoadList five = roadTable.getActiveRoads(whoseMove(), 5);

		if (four.size() > 0) {
			// ȡ�����еĵ�һ�������ɱ�ʤ�ŷ�
			Road road = four.get(0);
			// ��4·�п϶��ܹ��ҵ�������λ
			ArrayList<Point> posList = road.findEmptyPos(this);

			return new Move(posList.get(0).getPos(), posList.get(1).getPos());
		}

		// ����и����ֵ�5·
		if (five.size() > 0) {
			// ȡ�����еĵ�һ�������ɱ�ʤ�ŷ�
			Road road = five.get(0);

			// ��5·���ҵ�1����λ
			ArrayList<Point> posList = road.findEmptyPos(this);
			int index1 = posList.get(0).getPos();

			int index2 = 0;
			// ��������λ�У�ѡ��һ������Ϊ�ڶ����ӵ�λ��
			for (; index2 < SIDE * SIDE; index2++) {
				if (get(index2) == EMPTY && index2 != index1)
					break;
			}

			return new Move(index1, index2);
		}

		return null;
	}

	// ��һ����������һ������ǰ���ǵ�ǰ������ֻ���ڶ��ֵ�һ����в
	private ArrayList<Move> getSingleBlocks() {
		// vis ���ĳ��λ���Ƿ��Ѿ����ʹ����ų��ظ�
		boolean vis[] = new boolean[SIDE * SIDE];
		for (int i = 0; i < SIDE * SIDE; i++) {
			vis[i] = false;
		}

		// ��ȡ�����ס�ĵ�ĺ�ѡ���ϣ����ֵ�4·��5·�еĿհ׵�
		ArrayList<Point> posList = getThreatPoints(vis);
		// �Կհ׵��������
		Iterator<Point> itr = posList.iterator();
		while (itr.hasNext()) {
			// �Ե�һ����ĺ�ѡ������ֵΪ���ֵ
			itr.next().setScore(Integer.MAX_VALUE);
			;
		}

		// �ڵ�һ����Ŀ�ѡ���ϵĻ����ϣ���ȡ�õ�������ϵĵڶ�����ļ��ϣ��ų��Ѿ����ʹ��ĵ㡣
		ArrayList<Point> secondPoints = getPotentialPoints(vis);
		posList.addAll(secondPoints);

		return getBlocks(posList);
	}

	// �����ܹ���ס�Է�������в�������߲�
	private ArrayList<Move> getDoubleBlocks() {

		// vis ���ĳ��λ���Ƿ��Ѿ����ʹ����ų��ظ�
		boolean vis[] = new boolean[SIDE * SIDE];
		for (int i = 0; i < SIDE * SIDE; i++) {
			vis[i] = false;
		}

		// ��ȡ�����ס�ĵ�ĺ�ѡ���ϣ����ֵ�4·��5·�еĿհ׵�
		ArrayList<Point> posList = getThreatPoints(vis);
		// �Կհ׵��������
		Iterator<Point> itr = posList.iterator();
		while (itr.hasNext()) {
			// ˫��вʱ��ÿ��������۾�Ĭ��Ϊ���ֵ������Ҫ����
			itr.next().setScore(Integer.MAX_VALUE);
			;
		}

		return getBlocks(posList);

	}

	// ��ȡ�����ס�ĵ�ĺ�ѡ���ϣ����ֵ�4·��5·�еĿհ׵�
	// ���ܺ�ѡ���п��ܴ��ڶ���㣬��ֻҪѡ��һ���������㣬���п��ܶ�ס������в��
	private ArrayList<Point> getThreatPoints(boolean[] vis) {
		// ���ֵ�4·��5·�ļ���
		ArrayList<RoadList> roads = new ArrayList<>();
		// ��ö��ֵ�4··��
		RoadList oppFour = roadTable.getActiveRoads(whoseMove().opposite(), 4);
		if (oppFour.size() != 0)
			roads.add(oppFour);

		// ��ö��ֵ�5··��
		RoadList oppFive = roadTable.getActiveRoads(whoseMove().opposite(), 5);
		if (oppFive.size() != 0)
			roads.add(oppFive);

		// ��������ڶ��ֵ�4·��5·�����ؿ�
		if (roads.isEmpty())
			return null;

		// ȡ�����ֵ�4·��5·�е����пհ׵㣬�ų��ظ���
		return getBlanks(roads, vis);
	}

	// �ڵ�ǰ�����£��ҳ��ܹ���ȫ�����Է���в���߲��ļ��ϣ�
	// ���������������в�����Ҳ����������߲�
	// posList�Ƕ��ֵ�4·��5·�еĿհ׵㣬��ȥ���ظ��ġ�
	// ֻ��1����вʱ��posList֮�У�ǰ��Ĺ�ֵΪMaxValue�ĵ������п��ܶ�ס�����в�ĵ㡣
	// �����ֵΪ0�ĵ㣬�Ƕ�ס��в�����п��ܵ�ѡ��
	private ArrayList<Move> getBlocks(ArrayList<Point> posList) {
		// �ܹ���ȫ�����Է���в���߲��ļ���
		ArrayList<Move4AI> moves = new ArrayList<>();

		// ���������㣬���е���������
		Point[] points = posList.toArray(new Point[posList.size()]);

		// ֻ��һ����вʱ��cntFirstΪ4·��5·�еĿհ׵�ĸ�����
		// ��������вʱ��cntFirst = posList.length
		int cntFirst = 0;
		for (int i = 0; i < points.length; i++) {
			if (points[i].getScore() == Integer.MAX_VALUE)
				cntFirst++;
		}

		// ֻ��һ����вʱ����֤��һ�����4·��5·�еĿհ׵���ȡ
		for (int i = 0; i < cntFirst; i++) {
			// �����Ƴ���в�ĵ�һ����
			int p1 = points[i].getPos();
			for (int j = i + 1; j < points.length; j++) {
				// �����Ƴ���в�ĵڶ�����
				int p2 = points[j].getPos();
				Move4AI mov = new Move4AI(p1, p2);
				changeRoads(p1, whoseMove());
				changeRoads(p2, whoseMove());
				// �������߲�����������в��������ѡ��
				if (roadTable.noThreats(whoseMove())) {
					mov.setScore(this.staticScore());
					moves.add(mov);
				}
				unchangeRoads(p2, whoseMove());
				unchangeRoads(p1, whoseMove());
			}
		}
		// ������Max�㻹��Min�㣬ѡ��ͬ���������
		moves.sort(Move4AI.scoreComparatorDesc);

		ArrayList<Move> results = new ArrayList<>();
		fillMoves(moves, results);

		return results;
	}

	// ��Move4AI���б����Ƶ�Move�б�
	private void fillMoves(ArrayList<Move4AI> src, ArrayList<Move> dest) {
		Iterator<Move4AI> itr = src.iterator();
		while (itr.hasNext()) {
			dest.add(itr.next().getMove());
		}
	}

	// ��ȡRoadArray�б��е�����·�Ŀհ׵㣬�ų��Ѿ����ʹ���
	private ArrayList<Point> getBlanks(ArrayList<RoadList> roads, boolean[] vis) {
		// ����������пո�ļ���
		ArrayList<Point> posList = new ArrayList<>();

		// ����roads�е�ÿһ��RoadArray
		Iterator<RoadList> itr = roads.iterator();
		while (itr.hasNext()) {
			RoadList roadArr = itr.next();
			// ����RoadArray�е�ÿһ��·
			for (int k = 0; k < roadArr.size(); k++) {
				// ȡ��roadArr�е�һ��·
				Road road = roadArr.get(k);
				// �ҵ���·�еĿո�
				ArrayList<Point> temp = road.findEmptyPos(this, vis);
				// �����·�д��ڿո�
				if (temp != null) {
					// ��·�еĿո���뵽�ո���
					posList.addAll(temp);
				}
			}
		}
		return posList;
	}

	// ��ȡ�ܹ����һ���߲������п��ܵĺõ㣬�ų��Ѿ����ʹ��ĵ㡣
	// �Ӷ��ֵ�2·��3·��ѡ�����Լ���1��2��3·��ѡ�հ׵�
	private ArrayList<Point> getPotentialPoints(boolean[] vis) {

		// ��ö��ֵ�2·��3·
		RoadList oppTwo = roadTable.getActiveRoads(whoseMove().opposite(), 2);
		RoadList oppThree = roadTable.getActiveRoads(whoseMove().opposite(), 3);
		ArrayList<RoadList> roads = new ArrayList<>();
		roads.add(oppTwo);
		roads.add(oppThree);

		// ����ҷ���1��2��3·
		RoadList meOne = roadTable.getActiveRoads(whoseMove(), 1);
		RoadList meTwo = roadTable.getActiveRoads(whoseMove(), 2);
		RoadList meThree = roadTable.getActiveRoads(whoseMove(), 3);
		roads.add(meOne);
		roads.add(meTwo);
		roads.add(meThree);

		// ��ȡ�հ׵�
		ArrayList<Point> posList = getBlanks(roads, vis);

		// �Կհ׵��������
		Iterator<Point> itr = posList.iterator();

		while (itr.hasNext()) {
			staticScore(itr.next());
		}

		return posList;
	}

	// ���ѡ�������㣬��Ϊһ���߲�
	public ArrayList<Move> findRadomMove() {
		Random rand = new Random();
		while (true) {
			int index1 = rand.nextInt(SIDE * SIDE);
			int index2 = rand.nextInt(SIDE * SIDE);

			if (index1 != index2 && get(index1) == EMPTY && get(index2) == EMPTY) {
				ArrayList<Move> result = new ArrayList<>();
				result.add(new Move(index1, index2));
				return result;
			}
		}
	}

	private ArrayList<Move> getFreeMoves() {

		// vis ���ĳ��λ���Ƿ��Ѿ����ʹ����ų��ظ�
		boolean vis[] = new boolean[SIDE * SIDE];
		for (int i = 0; i < SIDE * SIDE; i++) {
			vis[i] = false;
		}

		// ��ȡ���п��ܵ�Ǳ����
		ArrayList<Point> posList = getPotentialPoints(vis);

		// ��������ںõ�Ǳ����
		if (posList.isEmpty()) {
			// ���ѡ�������㣬��Ϊһ���߲�
			return findRadomMove();
		}

		// ��Ǳ�����������
		posList.sort(Point.scoreComparator);
		Point[] points = posList.toArray(new Point[posList.size()]);

		// �������е�Ĺ�ֵ��ƽ��ֵ
		int sum = 0, average = 0;
		// �м�м�Ԥ��Ϊ���鳤��
		int mid = points.length;
		// ���е�Ĺ�ֵ��ƽ��ֵ
		for (int i = 0; i < points.length; i++) {
			sum += points[i].getScore();
		}
		average = sum / points.length;
		// ����λ����λ��
		for (int i = 0; i < points.length; i++) {
			// �����еĵ�ĵ÷־���ͬʱ�������������������
			if (points[i].getScore() < average) {
				mid = i;
				break;
			}
		}

		// �����߲��ļ���
		ArrayList<Move4AI> moves = new ArrayList<>();
		// ���������㣬���е��������ϣ���һ���ӴӺ�ѡ����ȡ
		for (int i = 0; i < points.length; i++) {
			// �ڶ����ӴӴ�����λ���ĵ���ȡ
			for (int j = i + 1; j < mid; j++) {
				// ����������
				int p1 = points[i].getPos();
				int p2 = points[j].getPos();
				Move4AI mov = new Move4AI(p1, p2);

				// ģ�����壬�õ���ǰ�߲�������
				changeRoads(p1, whoseMove());
				changeRoads(p2, whoseMove());
				mov.setScore(this.staticScore());
				// undoģ������
				unchangeRoads(p2, whoseMove());
				unchangeRoads(p1, whoseMove());

				moves.add(mov);
			}
			mid--; // �Ż������ļм�
		}
		moves.sort(Move4AI.scoreComparatorDesc);

		// ���к��߲��ļ���
		ArrayList<Move> results = new ArrayList<>();
		fillMoves(moves, results);
		return results;
	}

	// Ͷ�����䣬�������һ���߲�
	private Move resign() {
		for (int i = 0; i < SIDE * SIDE - 1; i++) {
			int index1;
			if (get(i) == EMPTY) {
				index1 = i;
			} else {
				continue;
			}

			for (int j = i + 1; j < SIDE * SIDE; j++) {
				if (get(j) == EMPTY && index1 != j) {
					return new Move(index1, j);
				}
			}
		}
		return null;
	}

	// ������в�ĸ���
	private int countAllThreats() {

		if (roadTable.noThreats(whoseMove()))
			return 0;

		// vis ���ĳ��λ���Ƿ��Ѿ����ʹ����ų��ظ�
		boolean vis[] = new boolean[SIDE * SIDE];
		for (int i = 0; i < SIDE * SIDE; i++) {
			vis[i] = false;
		}

		// ȡ��4·��5·�е����еĿհ׵㣬�ų��ظ���
		ArrayList<Point> posList = getThreatPoints(vis);
		Point[] points = posList.toArray(new Point[posList.size()]);

		boolean flag = false;
		// �ж��Ƿ�ֻ��1����в
		for (int i = 0; i < points.length; i++) {
			int pos = points[i].getPos();
			// ��һ�ŵ�ǰ��ɫ������
			changeRoads(pos, whoseMove());
			// �������߲�����������в��������ѡ��
			if (roadTable.noThreats(whoseMove())) {
				flag = true;
			}
			unchangeRoads(pos, whoseMove());
			if (flag)
				return 1;
		}

		// �ж��Ƿ����������в
		for (int i = 0; i < points.length - 1; i++) {
			// �����Ƴ���в�ĵ�һ����
			int p1 = points[i].getPos();
			for (int j = i + 1; j < points.length; j++) {
				// �����Ƴ���в�ĵڶ�����
				int p2 = points[j].getPos();
				// �����ŵ�ǰ��ɫ������
				changeRoads(p1, whoseMove());
				changeRoads(p2, whoseMove());
				// �������߲�����������в��������ѡ��
				if (roadTable.noThreats(whoseMove())) {
					flag = true;
				}
				unchangeRoads(p2, whoseMove());
				unchangeRoads(p1, whoseMove());
				if (flag)
					return 2;
			}
		}

		// ��������������в
		return 3;
	}

	// �����߲�move���޸�·����Ϣ
	private void changeRoads(Move move) {
		changeRoads(move.index1());
		changeRoads(move.index2());
	}

	// ������·����Ϣ���޸�
	private void unchangeRoads(Move move) {
		unchangeRoads(move.index2());
		unchangeRoads(move.index1());
	}

	// ����ĳ�����ӵ㣬�޸�·����Ϣ���޸��ܵ�Ӱ�����Ч·����Ϣ��
	private void changeRoads(int pos, PieceColor whoseMove) {
		ArrayList<Road> affectedRoads = roadTable.getAffectedRoads(pos);
		if (affectedRoads.isEmpty())
			return;
		for (Road road : affectedRoads) {
			// ���ܵ�Ӱ���·����ԭ�ȵ�·����ɾ��
			roadTable.removeRoad(road);
			// ���·����ӱ�����������ɫ������
			road.addStone(whoseMove);
			// ���µ�·����ӵ���Ӧ��·����
			roadTable.addRoad(road);
		}
	}

	private void changeRoads(int pos) {
		changeRoads(pos, whoseMove().opposite());
	}

	// ������·����Ϣ���޸�
	private void unchangeRoads(int pos, PieceColor whoseMove) {
		ArrayList<Road> affectedRoads = roadTable.getAffectedRoads(pos);

		if (affectedRoads.isEmpty())
			return;

		for (Road road : affectedRoads) {
			// ���ܵ�Ӱ���·����ԭ�ȵ�·����ɾ��
			roadTable.removeRoad(road);
			// �Ӹ�·��ɾ��������������ɫ������
			road.removeStone(whoseMove);
			// ���µ�·����ӵ���Ӧ��·����
			roadTable.addRoad(road);
		}
	}

	private void unchangeRoads(int pos) {
		unchangeRoads(pos, whoseMove().opposite());
	}

	private RoadTable roadTable = new RoadTable();
}
