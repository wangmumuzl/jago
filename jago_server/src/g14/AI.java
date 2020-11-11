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
	public static double attack = 1;// �����ԣ�1-2��
	public static double deffend = 1;// �����ԣ�1-2��

	public static PieceColor[] map;
	{
		map = new PieceColor[SIDE * SIDE];
	}
	/**
	 * ��ǰ��ɫ
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
	 * ������ͼmap�ҳ����е�
	 * 
	 * @return �������е��б�
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
	 * ��������·�����ҵ�����·���ϵĿհ׵㡣�����vacli����vacli�д�ŵ�������·���ϵĿհ׵㡣
	 * 
	 * @param _roli ����·��
	 * @return ���пհ׵��б�
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
	 * �������е��б�������·����������·���ϵĿհ�λ�ã�
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
	 * �ҵ�·���е�����EMPTY��λ�� ���ؿհ�λ���б���ֵ��vacli
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
	 * ����µĵ�ͼ��ͬʱͳ��3����4����5��, ����һ�����е�map�����һ���㣬·�������ı仯���������� 1.�½ڵ���������Ӷ����µ�·��
	 * 2.ԭ��·������Ӱ�죬�ᵼ�µ������ӣ�·��������������������ɫ��
	 * 
	 * @param map      ��ǰ�ĵ�ͼ
	 * @param newpoint ��ӵĵ�
	 * @return ��������ֵ
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

		List<Integer> poli = GetPo(map);// ������ͼmap�ҳ����е�
		List<Road> roli = getALLRoad(poli);// ���ݵ��б�������·����������·���ϵĿհ�λ�ã�
		vacli = GetVacli(roli);// ��������·�����ҵ�����·���ϵĿհ׵㡣�����vacli ����vacli�д�ŵ�������·���ϵĿհ׵㡣

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
	 * ���¶���Road�ĸ��һ��·��������ӱ����ֽضϣ��Ͳ��ܳ�Ϊһ��·�� �������� �ҵ�������pos�йص�·
	 * 
	 * @param _map ��ǰ������
	 * @param pos  Ҫ�жϵĵ�
	 * @return Road�б�
	 */
	public static ArrayList<Road> findRoads(PieceColor[] _map, int pos)
	{
		ArrayList<Road> roli = new ArrayList<>();
		PieceColor color = _map[pos];
		PieceColor oppocolor = color.opposite();

		int h = pos / 19;
		int w = pos % 19;

		// �� �����ƶ�
		for (int i = 0; i < 6; i++)
		{
			if (!((w - i >= 0) && (w - i + 5 < 19)))
				continue;
			Road temp = new Road(h * 19 + w - i, 1);
			CountBW(temp, _map);
			// ֻ������·��û�ж��ֵ����ӣ�����һ��·
			if (temp.colorCounter(oppocolor) == 0)
				roli.add(temp);
		}
		// ��
		for (int i = 0; i < 6; i++)
		{
			if (!((h - i >= 0) && (h - i + 5 < 19)))
				continue;
			Road temp = new Road((h - i) * 19 + w, 2);
			CountBW(temp, _map);
			if (temp.colorCounter(oppocolor) == 0)
				roli.add(temp);
		}
		// ����

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
		// ���� ������
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
