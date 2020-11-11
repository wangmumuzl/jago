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

		// 是否有获胜走步
		Move move = getWinMove();
		if (move != null) {
			moves.add(move);
			return moves;
		}

		// 计算威胁的个数
		int threats = countAllThreats();

		if (threats == 1) {
			// 堵住一个威胁的走步
			return getSingleBlocks();
		}
		if (threats >= 2) {
			// 堵住两个威胁的走步
			moves = getDoubleBlocks();
			// 如果找不到堵住两个威胁的走步
			if (moves.isEmpty()) {
				// 投子认输
				moves.add(resign());
			}
			return moves;
		}
		// 找到任意两个点的组合的topK
		return getFreeMoves();
	}

	// 对某个点的评价
	private void staticScore(Point p) {
		changeRoads(p.getPos(), whoseMove());
		p.setScore(this.staticScore());
		unchangeRoads(p.getPos(), whoseMove());
	}

	// 产生当前棋手（whoseMove）的必胜着法
	private Move getWinMove() {
		// 取出当前棋手的4路和5路，存在一条就有必胜着法
		RoadList four = roadTable.getActiveRoads(whoseMove(), 4);
		RoadList five = roadTable.getActiveRoads(whoseMove(), 5);

		if (four.size() > 0) {
			// 取出其中的第一条，生成必胜着法
			Road road = four.get(0);
			// 从4路中肯定能够找到两个空位
			ArrayList<Point> posList = road.findEmptyPos(this);

			return new Move(posList.get(0).getPos(), posList.get(1).getPos());
		}

		// 如果有该棋手的5路
		if (five.size() > 0) {
			// 取出其中的第一条，生成必胜着法
			Road road = five.get(0);

			// 从5路中找到1个空位
			ArrayList<Point> posList = road.findEmptyPos(this);
			int index1 = posList.get(0).getPos();

			int index2 = 0;
			// 在其他空位中，选择一个，作为第二个子的位置
			for (; index2 < SIDE * SIDE; index2++) {
				if (get(index2) == EMPTY && index2 != index1)
					break;
			}

			return new Move(index1, index2);
		}

		return null;
	}

	// 堵一个，攻击另一个；其前提是当前棋盘上只存在对手的一个威胁
	private ArrayList<Move> getSingleBlocks() {
		// vis 标记某个位置是否已经访问过，排除重复
		boolean vis[] = new boolean[SIDE * SIDE];
		for (int i = 0; i < SIDE * SIDE; i++) {
			vis[i] = false;
		}

		// 获取必须堵住的点的候选集合，对手的4路和5路中的空白点
		ArrayList<Point> posList = getThreatPoints(vis);
		// 对空白点进行评分
		Iterator<Point> itr = posList.iterator();
		while (itr.hasNext()) {
			// 对第一个点的候选，均估值为最大值
			itr.next().setScore(Integer.MAX_VALUE);
			;
		}

		// 在第一个点的可选集合的基础上，获取好的两点组合的第二个点的集合，排除已经访问过的点。
		ArrayList<Point> secondPoints = getPotentialPoints(vis);
		posList.addAll(secondPoints);

		return getBlocks(posList);
	}

	// 生成能够堵住对方两个威胁的所有走步
	private ArrayList<Move> getDoubleBlocks() {

		// vis 标记某个位置是否已经访问过，排除重复
		boolean vis[] = new boolean[SIDE * SIDE];
		for (int i = 0; i < SIDE * SIDE; i++) {
			vis[i] = false;
		}

		// 获取必须堵住的点的候选集合，对手的4路和5路中的空白点
		ArrayList<Point> posList = getThreatPoints(vis);
		// 对空白点进行评分
		Iterator<Point> itr = posList.iterator();
		while (itr.hasNext()) {
			// 双威胁时，每个点的评价均默认为最大值，不需要排序
			itr.next().setScore(Integer.MAX_VALUE);
			;
		}

		return getBlocks(posList);

	}

	// 获取必须堵住的点的候选集合，对手的4路和5路中的空白点
	// 尽管候选集中可能存在多个点，但只要选择一个或两个点，就有可能堵住所有威胁。
	private ArrayList<Point> getThreatPoints(boolean[] vis) {
		// 对手的4路和5路的集合
		ArrayList<RoadList> roads = new ArrayList<>();
		// 获得对手的4路路表
		RoadList oppFour = roadTable.getActiveRoads(whoseMove().opposite(), 4);
		if (oppFour.size() != 0)
			roads.add(oppFour);

		// 获得对手的5路路表
		RoadList oppFive = roadTable.getActiveRoads(whoseMove().opposite(), 5);
		if (oppFive.size() != 0)
			roads.add(oppFive);

		// 如果不存在对手的4路和5路，返回空
		if (roads.isEmpty())
			return null;

		// 取出对手的4路和5路中的所有空白点，排除重复的
		return getBlanks(roads, vis);
	}

	// 在当前盘面下，找出能够完全消除对方威胁的走步的集合，
	// 如果对手有三个威胁，则找不到这样的走步
	// posList是对手的4路和5路中的空白点，已去掉重复的。
	// 只有1个威胁时，posList之中，前面的估值为MaxValue的点是所有可能堵住这个威胁的点。
	// 后面估值为0的点，是堵住威胁后，所有可能的选点
	private ArrayList<Move> getBlocks(ArrayList<Point> posList) {
		// 能够完全消除对方威胁的走步的集合
		ArrayList<Move4AI> moves = new ArrayList<>();

		// 试下两个点，所有点的两两组合
		Point[] points = posList.toArray(new Point[posList.size()]);

		// 只有一个威胁时，cntFirst为4路和5路中的空白点的个数；
		// 有两个威胁时，cntFirst = posList.length
		int cntFirst = 0;
		for (int i = 0; i < points.length; i++) {
			if (points[i].getScore() == Integer.MAX_VALUE)
				cntFirst++;
		}

		// 只有一个威胁时，保证第一个点从4路和5路中的空白点中取
		for (int i = 0; i < cntFirst; i++) {
			// 用来破除威胁的第一个点
			int p1 = points[i].getPos();
			for (int j = i + 1; j < points.length; j++) {
				// 用来破除威胁的第二个点
				int p2 = points[j].getPos();
				Move4AI mov = new Move4AI(p1, p2);
				changeRoads(p1, whoseMove());
				changeRoads(p2, whoseMove());
				// 如果这个走步可以消除威胁，则放入候选集
				if (roadTable.noThreats(whoseMove())) {
					mov.setScore(this.staticScore());
					moves.add(mov);
				}
				unchangeRoads(p2, whoseMove());
				unchangeRoads(p1, whoseMove());
			}
		}
		// 根据是Max层还是Min层，选择不同的排序策略
		moves.sort(Move4AI.scoreComparatorDesc);

		ArrayList<Move> results = new ArrayList<>();
		fillMoves(moves, results);

		return results;
	}

	// 从Move4AI的列表，复制到Move列表
	private void fillMoves(ArrayList<Move4AI> src, ArrayList<Move> dest) {
		Iterator<Move4AI> itr = src.iterator();
		while (itr.hasNext()) {
			dest.add(itr.next().getMove());
		}
	}

	// 获取RoadArray列表中的所有路的空白点，排除已经访问过的
	private ArrayList<Point> getBlanks(ArrayList<RoadList> roads, boolean[] vis) {
		// 用来存放所有空格的集合
		ArrayList<Point> posList = new ArrayList<>();

		// 遍历roads中的每一个RoadArray
		Iterator<RoadList> itr = roads.iterator();
		while (itr.hasNext()) {
			RoadList roadArr = itr.next();
			// 遍历RoadArray中的每一条路
			for (int k = 0; k < roadArr.size(); k++) {
				// 取出roadArr中的一条路
				Road road = roadArr.get(k);
				// 找到该路中的空格
				ArrayList<Point> temp = road.findEmptyPos(this, vis);
				// 如果该路中存在空格
				if (temp != null) {
					// 将路中的空格加入到空格集中
					posList.addAll(temp);
				}
			}
		}
		return posList;
	}

	// 获取能够组成一个走步的所有可能的好点，排除已经访问过的点。
	// 从对手的2路和3路中选，从自己的1，2，3路中选空白点
	private ArrayList<Point> getPotentialPoints(boolean[] vis) {

		// 获得对手的2路和3路
		RoadList oppTwo = roadTable.getActiveRoads(whoseMove().opposite(), 2);
		RoadList oppThree = roadTable.getActiveRoads(whoseMove().opposite(), 3);
		ArrayList<RoadList> roads = new ArrayList<>();
		roads.add(oppTwo);
		roads.add(oppThree);

		// 获得我方的1，2，3路
		RoadList meOne = roadTable.getActiveRoads(whoseMove(), 1);
		RoadList meTwo = roadTable.getActiveRoads(whoseMove(), 2);
		RoadList meThree = roadTable.getActiveRoads(whoseMove(), 3);
		roads.add(meOne);
		roads.add(meTwo);
		roads.add(meThree);

		// 获取空白点
		ArrayList<Point> posList = getBlanks(roads, vis);

		// 对空白点进行评分
		Iterator<Point> itr = posList.iterator();

		while (itr.hasNext()) {
			staticScore(itr.next());
		}

		return posList;
	}

	// 随机选择两个点，作为一个走步
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

		// vis 标记某个位置是否已经访问过，排除重复
		boolean vis[] = new boolean[SIDE * SIDE];
		for (int i = 0; i < SIDE * SIDE; i++) {
			vis[i] = false;
		}

		// 获取所有可能的潜力点
		ArrayList<Point> posList = getPotentialPoints(vis);

		// 如果不存在好的潜力点
		if (posList.isEmpty()) {
			// 随机选择两个点，作为一个走步
			return findRadomMove();
		}

		// 对潜力点进行排序
		posList.sort(Point.scoreComparator);
		Point[] points = posList.toArray(new Point[posList.size()]);

		// 计算所有点的估值的平均值
		int sum = 0, average = 0;
		// 中间夹挤预置为数组长度
		int mid = points.length;
		// 所有点的估值的平均值
		for (int i = 0; i < points.length; i++) {
			sum += points[i].getScore();
		}
		average = sum / points.length;
		// 求中位数的位置
		for (int i = 0; i < points.length; i++) {
			// 当所有的点的得分均相同时，则本语句的条件不会成立
			if (points[i].getScore() < average) {
				mid = i;
				break;
			}
		}

		// 生成走步的集合
		ArrayList<Move4AI> moves = new ArrayList<>();
		// 试下两个点，所有点的两两组合，第一个子从候选集中取
		for (int i = 0; i < points.length; i++) {
			// 第二个子从大于中位数的点中取
			for (int j = i + 1; j < mid; j++) {
				// 两个点的组合
				int p1 = points[i].getPos();
				int p2 = points[j].getPos();
				Move4AI mov = new Move4AI(p1, p2);

				// 模拟下棋，得到当前走步的评分
				changeRoads(p1, whoseMove());
				changeRoads(p2, whoseMove());
				mov.setScore(this.staticScore());
				// undo模拟下棋
				unchangeRoads(p2, whoseMove());
				unchangeRoads(p1, whoseMove());

				moves.add(mov);
			}
			mid--; // 优化，中心夹挤
		}
		moves.sort(Move4AI.scoreComparatorDesc);

		// 所有好走步的集合
		ArrayList<Move> results = new ArrayList<>();
		fillMoves(moves, results);
		return results;
	}

	// 投子认输，随便生成一个走步
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

	// 计算威胁的个数
	private int countAllThreats() {

		if (roadTable.noThreats(whoseMove()))
			return 0;

		// vis 标记某个位置是否已经访问过，排除重复
		boolean vis[] = new boolean[SIDE * SIDE];
		for (int i = 0; i < SIDE * SIDE; i++) {
			vis[i] = false;
		}

		// 取出4路和5路中的所有的空白点，排除重复的
		ArrayList<Point> posList = getThreatPoints(vis);
		Point[] points = posList.toArray(new Point[posList.size()]);

		boolean flag = false;
		// 判断是否只有1个威胁
		for (int i = 0; i < points.length; i++) {
			int pos = points[i].getPos();
			// 下一颗当前颜色的棋子
			changeRoads(pos, whoseMove());
			// 如果这个走步可以消除威胁，则放入候选集
			if (roadTable.noThreats(whoseMove())) {
				flag = true;
			}
			unchangeRoads(pos, whoseMove());
			if (flag)
				return 1;
		}

		// 判断是否存在两个威胁
		for (int i = 0; i < points.length - 1; i++) {
			// 用来破除威胁的第一个点
			int p1 = points[i].getPos();
			for (int j = i + 1; j < points.length; j++) {
				// 用来破除威胁的第二个点
				int p2 = points[j].getPos();
				// 下两颗当前颜色的棋子
				changeRoads(p1, whoseMove());
				changeRoads(p2, whoseMove());
				// 如果这个走步可以消除威胁，则放入候选集
				if (roadTable.noThreats(whoseMove())) {
					flag = true;
				}
				unchangeRoads(p2, whoseMove());
				unchangeRoads(p1, whoseMove());
				if (flag)
					return 2;
			}
		}

		// 存在两个以上威胁
		return 3;
	}

	// 根据走步move，修改路表信息
	private void changeRoads(Move move) {
		changeRoads(move.index1());
		changeRoads(move.index2());
	}

	// 撤销对路表信息的修改
	private void unchangeRoads(Move move) {
		unchangeRoads(move.index2());
		unchangeRoads(move.index1());
	}

	// 根据某个落子点，修改路表信息（修改受到影响的有效路的信息）
	private void changeRoads(int pos, PieceColor whoseMove) {
		ArrayList<Road> affectedRoads = roadTable.getAffectedRoads(pos);
		if (affectedRoads.isEmpty())
			return;
		for (Road road : affectedRoads) {
			// 将受到影响的路，从原先的路表中删除
			roadTable.removeRoad(road);
			// 向该路中添加本棋手所下颜色的棋子
			road.addStone(whoseMove);
			// 将新的路，添加到相应的路表中
			roadTable.addRoad(road);
		}
	}

	private void changeRoads(int pos) {
		changeRoads(pos, whoseMove().opposite());
	}

	// 撤销对路表信息的修改
	private void unchangeRoads(int pos, PieceColor whoseMove) {
		ArrayList<Road> affectedRoads = roadTable.getAffectedRoads(pos);

		if (affectedRoads.isEmpty())
			return;

		for (Road road : affectedRoads) {
			// 将受到影响的路，从原先的路表中删除
			roadTable.removeRoad(road);
			// 从该路中删除本棋手所下颜色的棋子
			road.removeStone(whoseMove);
			// 将新的路，添加到相应的路表中
			roadTable.addRoad(road);
		}
	}

	private void unchangeRoads(int pos) {
		unchangeRoads(pos, whoseMove().opposite());
	}

	private RoadTable roadTable = new RoadTable();
}
