package g14;

import static core.board.PieceColor.EMPTY;
import static core.game.Move.SIDE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.board.Board;
import core.board.PieceColor;
import core.game.Move;

public class AI extends core.player.AI
{
	public static double attack = 1;// 攻击性（1-2）
	public static double deffend = 1;// 防御性（1-2）

	public static PieceColor[] map;
	{
		map = new PieceColor[SIDE * SIDE];
	}
	/**
	 * 当前颜色
	 */
	public static PieceColor color;

	Board b = new Board();

	@Override
	public Move findMove(Move arg0)
	{
		Random rand = new Random();
		for (int i = 0; i < SIDE * SIDE; i++)
		{
			map[i] = b.get(i);
		}

		color = this.getColor();

		int index1 = 0;
		index1 = Carrior();
		int index2 = 0;
		index2 = Carrior();

		if (index1 != index2 && b.get(index1) == EMPTY && b.get(index2) == EMPTY)
		{
			b.makeMove(new Move(index1, index2));
			return new Move(index1, index2);
		} else
		{
			while (true)
			{
				index1 = rand.nextInt(SIDE * SIDE);
				index2 = rand.nextInt(SIDE * SIDE);

				if (index1 != index2 && b.get(index1) == EMPTY && b.get(index2) == EMPTY)
				{
					Move move = new Move(index1, index2);
					b.makeMove(move);
					return move;
				}
			}
		}
	}

	/**
	 * 遍历地图map找出所有点
	 * 
	 * @return 返回所有点列表
	 */
	public static List<Integer> GetPo(PieceColor[] _map)
	{
		List<Integer> allpo = new ArrayList<>();
		for (int i = 0; i < _map.length; i++)
		{
			PieceColor pieceColor = _map[i];
			if (color.ordinal() == pieceColor.ordinal())
			{
				allpo.add(i);
			} else if (pieceColor.ordinal() == color.opposite().ordinal())
			{
				allpo.add(i);
			}
		}
		return allpo;
	}

	/**
	 * 根据所有路径，找到所有路径上的空白点。存放入vacli最终vacli中存放的是所有路径上的空白点。
	 * 
	 * @param _roli 所有路径
	 * @return 所有空白点列表
	 */
	public static List<Integer> GetVacli(List<Road> _roli)
	{
		List<Integer> _vacli = new ArrayList<>();
		for (Road road : _roli)
			for (Integer integer : pathvac(road))
				if (!_vacli.contains(integer))
					_vacli.add(integer);
		return _vacli;
	}

	/**
	 * 根据所有点列表返回所有路径（用于求路径上的空白位置）
	 */
	public static List<Road> getALLRoad(List<Integer> _numli)
	{
		List<Road> roli = new ArrayList<>();

		List<Road> temproli = new ArrayList<>();
		for (Integer integer : _numli)
		{
			temproli = findRoads(map, integer);
			for (Road road : temproli)
			{
				if (!roli.contains(road))
					roli.add(road);
			}
		}
		return roli;
	}

	/**
	 * 找到路径中的所有EMPTY的位置 返回空白位置列表，赋值给vacli
	 */
	public static List<Integer> pathvac(Road rd)
	{
		List<Integer> templi = new ArrayList<>();
		int pos = rd.startposi;
		int dir = rd.dir;

		int h = pos / 19;
		int w = pos % 19;

		switch (dir)
		{
		case 1:
			for (int i = 0; i < 6; i++)
			{
				if (map[h * 19 + w + i] == PieceColor.EMPTY)
					templi.add(h * 19 + w + i);
			}
			break;
		case 2:
			for (int i = 0; i < 6; i++)
			{
				if (map[(h + i) * 19 + w] == PieceColor.EMPTY)
					templi.add((h + i) * 19 + w);
			}
			break;
		case 3:
			for (int i = 0; i < 6; i++)
			{
				if (map[(h - i) * 19 + w + i] == PieceColor.EMPTY)
					templi.add((h - i) * 19 + w + i);
			}
			break;
		case 4:
			for (int i = 0; i < 6; i++)
			{
				if (map[(h + i) * 19 + w + i] == PieceColor.EMPTY)
					templi.add((h + i) * 19 + w + i);
			}
			break;
		}
		return templi;
	}

	/**
	 * 获得新的地图，同时统计3连、4连、5连, 当在一个现有的map上添加一个点，路径发生的变化有两个方面 1.新节点的引入会添加多条新的路径
	 * 2.原来路径会受影响，会导致点数增加，路径消除（出现了两种颜色）
	 * 
	 * @param map      当前的地图
	 * @param newpoint 添加的点
	 * @return 返回评估值
	 */
	public static int ValueOf_AddPoint(PieceColor[] map, int newpoint)
	{
		PieceColor[] tempmap = map.clone();
		tempmap[newpoint] = color;
		///////////////////////////////
		List<Road> temp_roli = new ArrayList<>();

		List<Integer> temp_POli = GetPo(tempmap);

		for (Integer integer : temp_POli)
		{
			for (Road road : findRoads(tempmap, integer))
			{
				if (!temp_roli.contains(road))
				{
					temp_roli.add(road);
				}
			}
		}
		////////////////////////////////////
		int value = 0;
		for (Road road : temp_roli)
		{
			if (tempmap[road.startposi] == color)
			{
				value += road.colorCounter(color) * road.colorCounter(color)
						* road.colorCounter(color) * attack;
			}
			if (tempmap[road.startposi] == color.opposite())
			{
				value -= road.colorCounter(color.opposite()) * road.colorCounter(color.opposite())
						* road.colorCounter(color.opposite()) * deffend;
			}
		}
//		for (Road road : temp_roli)
//		{
//			if (tempmap[road.startposi] == color)
//			{
//				if (road.colorCounter(color) >= 6)
//					value += 10000;
//				else if (road.colorCounter(color) == 5)
//					value += 8000;
//				else if (road.colorCounter(color) == 4)
//					value += 100;
//				else if (road.colorCounter(color) == 3)
//					value += 20;
//				else if (road.colorCounter(color) == 2)
//					value += 1;
//			}
//			if (tempmap[road.startposi] == color.opposite())
//			{
//				if (road.colorCounter(color.opposite()) >= 6)
//					value -= 10000;
//				else if (road.colorCounter(color.opposite()) == 5)
//					value -= 8000;
//				else if (road.colorCounter(color.opposite()) == 4)
//					value -= 100;
//				else if (road.colorCounter(color.opposite()) == 3)
//					value -= 20;
//				else if (road.colorCounter(color.opposite()) == 2)
//					value -= 1;
//			}
//		}
		///////////////////////////////////
		return value;

	}

	public static List<Integer> vacli;
	{
		vacli = new ArrayList<>();
	}

	public static int Carrior()
	{

		List<Integer> poli = GetPo(map);// 遍历地图map找出所有点
		List<Road> roli = getALLRoad(poli);// 根据点列表返回所有路径（用于求路径上的空白位置）
		vacli = GetVacli(roli);// 根据所有路径，找到所有路径上的空白点。存放入vacli 最终vacli中存放的是所有路径上的空白点。

		int move = -1;// 0--360

		int best = -100000;
		for (Integer integer : vacli)
		{

			int tempbest = ValueOf_AddPoint(map, integer);
			if (tempbest >= best)
			{
				best = tempbest;
				move = integer;
			}
		}

		PieceColor[] tempmap = map.clone();
//		System.out.println("\n-----****"+move+"*******");
		tempmap[move] = color;
		map = tempmap;
		return move;
	}

	/**
	 * 重新定义Road的概念，一条路上如果连子被对手截断，就不能称为一条路。 在棋盘中 找到所有与pos有关的路
	 * 
	 * @param _map 当前的棋盘
	 * @param pos  要判断的点
	 * @return Road列表
	 */
	public static ArrayList<Road> findRoads(PieceColor[] _map, int pos)
	{
		ArrayList<Road> roli = new ArrayList<>();
		PieceColor color = _map[pos];
		PieceColor oppocolor = color.opposite();

		int h = pos / 19;
		int w = pos % 19;

		// 右 反向移动
		for (int i = 0; i < 6; i++)
		{
			if (!((w - i >= 0) && (w - i + 5 < 19)))
				continue;
			Road temp = new Road(h * 19 + w - i, 1);
			CountBW(temp, _map);
			// 只有这条路上没有对手的棋子，就算一条路
			if (temp.colorCounter(oppocolor) == 0)
				roli.add(temp);
		}
		// 下
		for (int i = 0; i < 6; i++)
		{
			if (!((h - i >= 0) && (h - i + 5 < 19)))
				continue;
			Road temp = new Road((h - i) * 19 + w, 2);
			CountBW(temp, _map);
			if (temp.colorCounter(oppocolor) == 0)
				roli.add(temp);
		}
		// 右上

		for (int i = 0; i < 6; i++)
		{
			if (!(0 <= (h + i) && (h + i) < 19 && 0 <= (w - i) && (w - i) < 19 && 0 <= (h + i - 5)
					&& (h + i - 5) < 19 && 0 <= (w - i + 5) && (w - i + 5) < 19))
				continue;
//			System.out.println((h + i) * 19 + w - i);
			Road temp = new Road((h + i) * 19 + w - i, 3);
			CountBW(temp, _map);
			if (temp.colorCounter(oppocolor) == 0)
				roli.add(temp);
		}
		// 右下 反方向
		for (int i = 0; i < 6; i++)
		{
			if (!(0 <= h - i && h - i < 19 && 0 <= w - i && w - i < 19 && 0 <= h - i + 5
					&& h - i + 5 < 19 && 0 <= w - i + 5 && w - i + 5 < 19))
				continue;
			Road temp = new Road((h - i) * 19 + w - i, 4);
			CountBW(temp, _map);
			if (temp.colorCounter(oppocolor) == 0)
				roli.add(temp);
		}
		return roli;
	}

	public static void CountBW(Road e, PieceColor[] _map)
	{
		int pos = e.startposi;
		int h = pos / 19;
		int w = pos % 19;

		switch (e.dir)
		{
		case 1:
			for (int i = 0; i < 6; i++)
			{
				if (_map[h * 19 + w + i] == PieceColor.BLACK)
					e.blackcot++;
				else if (_map[h * 19 + w + i] == PieceColor.WHITE)
					e.whitecot++;
			}
			break;
		case 2:
			for (int i = 0; i < 6; i++)
			{
				if (_map[(h + i) * 19 + w] == PieceColor.BLACK)
					e.blackcot++;
				else if (_map[(h + i) * 19 + w] == PieceColor.WHITE)
					e.whitecot++;
			}
			break;
		case 3:
			for (int i = 0; i < 6; i++)
			{
				if (_map[(h - i) * 19 + w + i] == PieceColor.BLACK)
					e.blackcot++;
				else if (_map[(h - i) * 19 + w + i] == PieceColor.WHITE)
					e.whitecot++;
			}
			break;
		case 4:
			for (int i = 0; i < 6; i++)
			{
				if (_map[(h + i) * 19 + w + i] == PieceColor.BLACK)
					e.blackcot++;
				else if (_map[(h + i) * 19 + w + i] == PieceColor.WHITE)
					e.whitecot++;
			}
			break;

		}
	}

	@Override
	public String name()
	{
		return "g14";
	}

}
