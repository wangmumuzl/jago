package s17020031086.player;

import core.board.PieceColor;
import core.game.Game;
import core.game.Move;

import java.util.ArrayList;

public class AI extends core.player.AI {
	// 搜索的深度
	private static final int MAX_DEPTH = 2;
	/* 记录一下行棋的序列 */
	ArrayList<Move4AI> moveOrder = new ArrayList<>();

	public AI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Move findMove(Move opponentMove) {
		if (opponentMove == null) {
			Move move = firstMove();
			board.makeMove(move);
			return move;
		} else {
			board.makeMove(opponentMove);
		}

		bestMove = board.findwinMoves();
		if (bestMove != null) {
			board.makeMove(bestMove);
			return bestMove;
		}

		// 记录我方颜色
		color = board.whoseMove();
		// 因为有findwinmoves的原因 所以从搜素到第三层开始
		moveOrder.clear();
		for (int i = 3; i <= 21; i += 2) {
			if (DTSS(i)) {
				board.makeMove(bestMove);

				return bestMove;
			}

		}

		alphaBeta(-Integer.MAX_VALUE, Integer.MAX_VALUE, MAX_DEPTH);

		if (bestMove == null) {
			ArrayList<Move4AI> moves = board.findGenerateMoves();
			moves.sort(Move4AI.scoreComparator);
			bestMove = moves.get(0);
		}
		board.makeMove(bestMove);
		return bestMove;
	}

	boolean DTSS(int depth) {

		// depth为0，搜索达到最大深度还没有找到连续双迫着的情况，return false；
		if (depth == 0)
			return false;
		// 当我方行棋时
		if (color == board.whoseMove()) {
			// 如果对方对于我方存在威胁，但是我方对于对方没有威胁
			if (board.countAllThreats(color) > 0 && board.countAllThreats(color.opposite()) == 0)
				return false;

			// 找到我方行棋成为双迫着的所有着法
			ArrayList<Move4AI> movesList = board.findDoubleThreats();
			for (Move4AI move : movesList) {
				board.makeMove(move);
				moveOrder.add(move);
				boolean flag = DTSS(depth - 1);
				moveOrder.remove(moveOrder.size() - 1);
				board.undoMove(move);
				// 根据算法，存在即可返回true
				if (flag)
					return true;
			}
			return false;
		}
		// 对方行棋时
		else {
			// 如果堵不住我方对于对方的威胁
			if (board.countAllThreats(board.whoseMove()) >= 3) {
				bestMove = moveOrder.get(0);
				return true;
			}
			// 找到对方用来堵的所有着法
			ArrayList<Move4AI> movesList = board.findDoubleBlocks();
			for (Move4AI move : movesList) {
				board.makeMove(move);
				moveOrder.add(move);
				boolean flag = DTSS(depth - 1);
				moveOrder.remove(moveOrder.size() - 1);
				board.undoMove(move);
				// 根据算法，必须全部可以出现双迫着，否则 搜索失败
				if (!flag)
					return false;
			}
			return true;
		}

	}

	public int alphaBeta(int alpha, int beta, int depth) {
		int value, best = -Integer.MAX_VALUE;
		// 如果棋局结束或当前节点为叶子节点则返回评估值
		if (board.gameOver() || depth <= 0) {
			int evaluateScore = EvaluationFunction.evaluateChessStatus(color, board.getRoadTable());
			return evaluateScore;
		}
		ArrayList<Move4AI> moves = null;
		int threats = board.countAllThreats(board.whoseMove());
		if (threats == 0) {
			moves = board.findGenerateMoves();
		} else if (threats == 1) {
			moves = board.findSingleBlocks();
		} else if (threats == 2) {
			moves = board.findDoubleBlocks();
		} else {
			moves = board.findTripleBlocks();
		}
		// 启发式排序
		moves.sort(Move4AI.scoreComparator);
		for (Move4AI move : moves) {
			board.makeMove(move);
			value = -alphaBeta(-beta, -alpha, depth - 1);
			board.undoMove(move);
			if (value > best) {
				best = value;
				if (best > alpha) {
					alpha = best;

				}
				if (value >= beta) {
					break;
				}
			}
			if (depth == MAX_DEPTH && value >= best) {
				board.makeMove(move);
				color = color.opposite();
				moveOrder.clear();
				// 加一步反向DTSS搜索 防止自己防御失误
				if (!DTSS(5)) {
					bestMove = move;
				}

				color = color.opposite();
				board.undoMove(move);

			}
		}
		return alpha;
	}

	/**
	 * The move found by the last call to one of the ...FindMove methods below.
	 */
	private Move bestMove;

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "杨国鑫";
	}

	@Override
	public void playGame(Game game) {
		super.playGame(game);
		board = new Board4AI();
	}

	// 自己保有的棋盘
	private Board4AI board = null;
	PieceColor color;
}
