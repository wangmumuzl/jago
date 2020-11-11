package core.game;

import java.util.Observable;
import java.util.Observer;

import core.board.PieceColor;

public class ConsoleRecorder implements Observer{
	private PieceColor _whoseMove = PieceColor.WHITE;
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		Move move = (Move) arg;
	}

}
