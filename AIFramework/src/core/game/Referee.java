package core.game;

import core.board.Board;
import core.player.Player;
import core.player.SocketDelegate;
import static core.board.PieceColor.*;

import javax.swing.JOptionPane;

public class Referee {

	private Board _board;
	private Player _black, _white;
	private String endReason; // ��ֽ�����ԭ��; F ����������T ��ʱ��
								// E �����ڲ�����N �Ƿ��߲���M ���������
	private int steps = 0; // ������˶��ٲ�

	public Referee(Player black, Player white) {
		_board = new Board();
		_board.clear();
		_black = black;
		_white = white;
	}

	// �ֵ�˭����
	public Player whoseMove() {
		return _board.whoseMove() == WHITE ? _white : _black;
	}

	// ����Ƿ����
	public boolean gameOver() {
		return _board.gameOver();
	}

	// �����쳣
	public void endingGame(String endReason, Player currPlayer, Move currMove) {
		_black.stopTimer();
		_white.stopTimer();
		if (currPlayer instanceof SocketDelegate) {
			((SocketDelegate) currPlayer).sendMove(currMove);
		}
		// F ����������T ��ʱ��E �����ڲ�����N �Ƿ��߲���M �����������ƽ�֣�
		String gameResult = "";
		switch (endReason) {
		case "F":
			gameResult = "ʤ����" + getWinner();
			break;
		case "T":
			gameResult = currPlayer.name() + "��ʱ";
			break;
		case "E":
			gameResult = currPlayer.name() + "�����ڲ�����";
			break;
		case "N":
			gameResult = currPlayer.name() + "�Ƿ��߲�";
			break;
		case "M":
			gameResult = "ƽ��";
			break;
		default:
			break;
		}
		JOptionPane.showMessageDialog(null, gameResult, "��ս����", JOptionPane.DEFAULT_OPTION);
		this.endReason = endReason;
		recordGame();
	}

	// �Ƿ�Ϸ��߲�
	public boolean legalMove(Move move) {
		// TODO Auto-generated method stub
		return _board.legalMove(move);
	}

	// ��¼�߲�
	public void recordMove(Move move) {
		// TODO Auto-generated method stub
		_board.makeMove(move);
		steps++;
	}

	// Ϊ�ڰ�˫��������ֱ���
	public void recordGame() {
		GameResult result = new GameResult(_black.name(), _white.name(), getWinner(), steps, endReason);
		System.out.println(result.toString());
		// ��ӵ�����Ͱ���ĶԾֽ���б�
		_black.addGameResult(result);
		_white.addGameResult(result);
	}

	private String getWinner() {

		// ����ǳ����������������
		if ("M".equalsIgnoreCase(endReason)) {
			return "NONE";
		}

		// // ����ǳ�ʱ����
		// if ("T".equalsIgnoreCase(endReason)) {
		// //�ֵ��������һ�����ж�ΪӮ�ҡ������ֵ������£����ʤ
		// return _board.whoseMove() == WHITE ? _white.name() : _black.name();
		// }

		// �ֵ��������һ�����ж�Ϊ��ҡ������ֵ������£����ʤ�����򣬰�ʤ
		return _board.whoseMove() == WHITE ? _black.name() : _white.name();
	}
}
