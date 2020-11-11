package g14;

import java.util.Comparator;

import core.board.PieceColor;

public class Road
{
	public int startposi;
	public int blackcot;
	public int whitecot;
	public int[] info;

	public Road(int _startposi, int _dir)
	{
		startposi = _startposi;
		dir = _dir;
		blackcot = 0;
		whitecot = 0;
	}

	public int colorCounter(PieceColor cl)
	{
		if (cl == PieceColor.BLACK)
			return this.blackcot;
		else if (cl == PieceColor.WHITE)
			return this.whitecot;
		return 0;
	}

	

	public int dir = 0;

	@Override
	public String toString()
	{
		return startposi + "  " + dir + " [" + blackcot + ", " + whitecot + "]";
	}

	public int[] getinfor()
	{
		info = new int[4];
		info[0] = startposi;
		info[1] = dir;
		info[2] = blackcot;
		info[3] = whitecot;
		return info;
	}

	@Override
	public boolean equals(Object obj)
	{
		Road other = (Road) obj;
		if (dir != other.dir)
			return false;
		if (startposi != other.startposi)
			return false;
		return true;
	}

}

class comp implements Comparator<Road>
{

	@Override
	public int compare(Road o1, Road o2)
	{
		return o1.startposi - o2.startposi;
	}

}