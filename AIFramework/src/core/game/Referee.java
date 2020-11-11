package core.game;

import core.board.Board;
import core.player.Player;
import core.player.SocketDelegate;
import static core.board.PieceColor.*;

import javax.swing.JOptionPane;

public class Referee {

	private Board _board;
	private Player _black, _white;
	private String endReason; // 棋局结束的原因; F 正常结束；T 超时；
								// E 棋手内部错误；N 非法走步；M 超过最大步数
	private int steps = 0; // 棋局下了多少步

	public Referee(Player black, Player white) {
		_board = new Board();
		_board.clear();
		_black = black;
		_white = white;
	}

	// 轮到谁下棋
	public Player whoseMove() {
		return _board.whoseMove() == WHITE ? _white : _black;
	}

	// 棋局是否结束
	public boolean gameOver() {
		return _board.gameOver();
	}

	// 处理异常
	public void endingGame(String endReason, Player currPlayer, Move currMove) {
		_black.stopTimer();
		_white.stopTimer();
		if (currPlayer instanceof SocketDelegate) {
			((SocketDelegate) currPlayer).sendMove(currMove);
		}
		// F 正常结束；T 超时；E 棋手内部错误；N 非法走步；M 超过最大步数（平局）
		String gameResult = "";
		switch (endReason) {
		case "F":
			gameResult = "胜方：" + getWinner();
			break;
		case "T":
			gameResult = currPlayer.name() + "超时";
			break;
		case "E":
			gameResult = currPlayer.name() + "棋手内部错误";
			break;
		case "N":
			gameResult = currPlayer.name() + "非法走步";
			break;
		case "M":
			gameResult = "平局";
			break;
		default:
			break;
		}
		JOptionPane.showMessageDialog(null, gameResult, "对战结束", JOptionPane.DEFAULT_OPTION);
		this.endReason = endReason;
		recordGame();
	}

	// 是否合法走步
	public boolean legalMove(Move move) {
		// TODO Auto-generated method stub
		return _board.legalMove(move);
	}

	// 记录走步
	public void recordMove(Move move) {
		// TODO Auto-generated method stub
		_board.makeMove(move);
		steps++;
	}

	// 为黑白双方生成棋局报告
	public void recordGame() {
		GameResult result = new GameResult(_black.name(), _white.name(), getWinner(), steps, endReason);
		System.out.println(result.toString());
		// 添加到黑棋和白棋的对局结果列表
		_black.addGameResult(result);
		_white.addGameResult(result);
	}

	private String getWinner() {

		// 如果是超过最大步数而结束，
		if ("M".equalsIgnoreCase(endReason)) {
			return "NONE";
		}

		// // 如果是超时负，
		// if ("T".equalsIgnoreCase(endReason)) {
		// //轮到下棋的那一方，判定为赢家。即，轮到白棋下，则白胜
		// return _board.whoseMove() == WHITE ? _white.name() : _black.name();
		// }

		// 轮到下棋的那一方，判定为输家。即，轮到白棋下，则黑胜，否则，白胜
		return _board.whoseMove() == WHITE ? _black.name() : _white.name();
	}
}
