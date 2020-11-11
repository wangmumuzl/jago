/**
 * 老师和助教提供的一个AI棋手，作为评价的一个Baseline
 */

package baseline.player;

import static core.board.PieceColor.*;

import java.util.ArrayList;

import core.game.Game;
import core.game.Move;

public class AI extends core.player.AI {

	/** Maximum minimax search depth before going to static evaluation. */
	private static final int MAX_DEPTH = 2;
	/**
	 * A position magnitude indicating a win (for white if positive, black if
	 * negative).
	 */
	private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
	/** A magnitude greater than a normal value. */
	private static final int INFTY = Integer.MAX_VALUE;

	public AI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Move findMove(Move opponentMove) throws Exception {
		
		// 如果opponentMove不为空，(白棋下第一手时，opponentMove为空)
		if (opponentMove != null)
			board.makeMove(opponentMove);
		if (getColor() == WHITE && firstMove) {
			firstMove = false;
			Move move = firstMove();
			board.makeMove(move);
			return move;
		}
		
		findMove(MAX_DEPTH, true, 1, -INFTY, INFTY);
		board.makeMove(_lastFoundMove);
		
		return _lastFoundMove;
	}

	/**
	 * The move found by the last call to one of the ...FindMove methods below.
	 */
	private Move _lastFoundMove;

	/**
	 * Find a move from position BOARD and return its value, recording the move
	 * found in _lastFoundMove iff SAVEMOVE. The move should have maximal value
	 * or have value > BETA if SENSE==1, and minimal value or value < ALPHA if
	 * SENSE==-1. Searches up to DEPTH levels. Searching at level 0 simply
	 * returns a static estimate of the board value and does not set
	 * _lastMoveFound.
	 */
	private int findMove(int depth, boolean saveMove, int sense, int alpha, int beta) {

		Move best = null;
		int bestVal;
		if (board.gameOver()) {	
		  //如果是出现在极大层节点，则返回负值
          return sense == 1 ? -WINNING_VALUE : WINNING_VALUE;	
        }
		
		if (depth == 0) {
			return board.staticScore();
		}
		
		ArrayList<Move> moves = board.getMoves();
		
		if (sense == 1) { // 如果是MaximizingPlayer
			bestVal = -INFTY;
			for (Move mov : moves) {
				board.makeMove(mov);
				int response = findMove(depth - 1, false, -sense, alpha, beta);
				board.undo();
				if (response >= bestVal) {
					best = mov;
					bestVal = response;
					alpha = Math.max(response, alpha);
				}
				if (beta <= alpha) {
					return alpha;
				}
			}
			if (saveMove) {
				_lastFoundMove = best;
			}
			return alpha;
		} else {
			bestVal = INFTY;
			for (Move mov : moves) {
				board.makeMove(mov);
				int response = findMove(depth - 1, false, -sense, alpha, beta);
				board.undo();
				if (response <= bestVal) {
					best = mov;
					bestVal = response;
					beta = Math.min(response, beta);
				}
				if (beta <= alpha) {
					return beta;
				}
			}
			if (saveMove) {
				_lastFoundMove = best;
			}
			return beta;
		}

	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Robot";
	}
	
	@Override
	public void playGame(Game game) {
		super.playGame(game);
		board = new Board4AI(); 
		firstMove = true;
	}

	private Board4AI board = null;
	private boolean firstMove = true;
	
}
