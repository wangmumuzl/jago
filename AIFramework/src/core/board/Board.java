package core.board;

import static core.board.PieceColor.*;
import static core.game.Move.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import core.game.Move;


/** A Connect6 board.  The squares are labeled by column (a char value between
 *  'A' and 'S') and row (also a char value between 'A' and 'S').
 *  
 *  Moves on this board are denoted by Moves.
 *  @author Jianliang Xu
 */
public class Board implements Observer{
	//ĳ���ĸ�ǰ�������λ���������˷���  = -ǰ������
	//ǰ�������£� �ң����£����ϣ�  {col������row����}
	public static final int FORWARD[][] = {
		{0, 1}, {1, 0}, {1, 1}, {1, -1}
	};
    /** A new, cleared board at the start of the game. */
    public Board() {
        _board = new PieceColor[SIDE * SIDE];
        clear();
    }

    /** A copy of B. */
    public Board(Board b) {
        _board = new PieceColor[SIDE * SIDE];
        internalCopy(b);
    }

    /** Clear me to my starting state, with pieces in their initial
     *  positions. */
    public void clear() {
        _whoseMove = WHITE;
        _gameOver = false;

        for (int i = 0; i < SIDE * SIDE; i++) {
            _board[i] = EMPTY;
        }
        //�ڷ����µ�һ���ӣ�Ĭ��Ϊ���̵���Ԫ
        _board[Move.index('J', 'J')] = BLACK;
        moveList.clear();
    }

    /** Copy B into me. */
    public void copy(Board b) {
        internalCopy(b);
    }

    /** Copy B into me. */
    private void internalCopy(Board b) {
        _gameOver = b.gameOver();
        _whoseMove = b.whoseMove();
        for (int i = 0; i < SIDE * SIDE; i++) {
            _board[i] = b.get(i);
        }
    }

    /** Return true iff the game is over: i.e., after the last move, 
     *  there is at lease one connect6 of the last player. */
    public boolean gameOver() {
    	if (moveList.isEmpty()) return false;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
    	Move lastMove = moveList.get(moveList.size() - 1);
    	
    	if (isWin(lastMove.col0(), lastMove.row0()) || isWin(lastMove.col1(), lastMove.row1())) {
			_gameOver = true;
			return _gameOver;
		}
    	else {
    		return false;
    	}
    }
    
    //�ӵ�(startCol, startRow)��ʼ�����ŷ���dir��ǰ��len��λ�ã�������õ���ɫ��ͬ�����ӵĸ���
    //�ø��� <= len.
    private int lengthConnected(char startCol, char startRow, int dir, int len) {
    	
    	int myLen = 0;
    	//��(startCol, startRow)�����ӵ���ɫ
    	PieceColor myColor = _whoseMove.opposite();
		for (int j = 0; j < len; j++) {
			char tempCol = (char)(startCol + FORWARD[dir][0] * j);
			char tempRow = (char)(startRow + FORWARD[dir][1] * j);
			if (Move.validSquare(tempCol, tempRow) && this.get(tempCol, tempRow) == myColor) {
				myLen++;
			}
			else {
				break;
			}
		}
		return myLen;
    }
    
    //�����ڵ�(col��row)�����Ӻ󣬸������Ƿ��ʤ
    private boolean isWin(char col, char row) {
    	
    	for (int dir = 0; dir < 4; dir++) {
    		//�ӵ�ǰ����dir����������ͬ��ɫ�ĵ�ĸ�����������ǰ��
    		int len = lengthConnected(col, row, dir, 6);
    		//�Ѿ�����
    		if (len == 6) {
    			return true;
    		}
    		else {
    			//���򣬴ӵ�ǰ�㣬��dir����ķ������˺�6-len�������λ��
    			char startCol = (char)(col - FORWARD[dir][0] * (6 -len));
    			char startRow = (char)(row - FORWARD[dir][1] * (6 -len));
    			//������ǺϷ��ĵ㣬������鿴��һ������
    			if (!Move.validSquare(startCol, startRow)) {
    				continue;
    			}
    			int tempLen = 6 - len;
    			len = lengthConnected(startCol, startRow, dir, tempLen);
    			//�������
        		if (len == tempLen) {
        			return true;
        		}
        		else {
        			continue;
        		}
    		}
    	}
    	return false;
    }

    /** Return the current contents of square C R, where 'A' <= C <= 'S',
     *  and 'A' <= R <= 'S'.  */
    public PieceColor get(char c, char r) {
        assert validSquare(c, r);
        return _board[index(c, r)];
    }

    /** Return the current contents of the square at linearized index K. */
    public PieceColor get(int k) {
        assert validSquare(k);
        return _board[k];
    }

    /** Set square(C, R) to V, where 'A' <= C <= 'S', and 'A' <= R <= 'S'. */
    protected void set(char c, char r, PieceColor v) {
        assert validSquare(c, r);
        set(index(c, r), v);
    }

    /** Set square(K) to V, where K is the linearized index of a square. */
    protected void set(int k, PieceColor v) {
        assert validSquare(k);
        _board[k] = v;
    }

    /** Return true iff MOV is legal on the current board. */
    public boolean legalMove(Move mov) {
    	return validSquare(mov.index1()) && get(mov.index1()) == EMPTY && 
    			validSquare(mov.index2()) && get(mov.index2()) == EMPTY &&
    			mov.index1() != mov.index2();	//ͬһ�߲��������������ڲ�ͬ��
    }

    /** Return the color of the player who has the next move.  The
     *  value is arbitrary if gameOver(). */
    public PieceColor whoseMove() {
        return _whoseMove;
    }

    /** Perform the move C0R0-C1R1.*/
    public void makeMove(char c0, char r0, char c1, char r1) {
        makeMove(new Move(c0, r0, c1, r1));
    }

    /** Make the Move MOV on this Board, assuming it is legal. */
    public void makeMove(Move mov) {
        assert legalMove(mov);
        
        moveList.add(mov);
        //System.out.println(_whoseMove + ": " + "move " + mov.toString());
        set(mov.col0(), mov.row0(), _whoseMove);
        set(mov.col1(), mov.row1(), _whoseMove);
        
        _whoseMove = _whoseMove.opposite();
    }

    /** Undo the last move, if any. */
    public void undo() {
        Move mov = moveList.remove(moveList.size() - 1);
        undo(mov);
        _whoseMove = _whoseMove.opposite();
    }

    public void undo(Move mov) {
        set(mov.col0(), mov.row0(), EMPTY);
        set(mov.col1(), mov.row1(), EMPTY);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /** Return a text depiction of the board. If LEGEND, supply row and
     *  column numbers around the edges. */
    public String toString(boolean legend) {
    	StringBuffer strBuff = new StringBuffer();
    	
    	strBuff.append("  ");
    	for (int i = 0; i < SIDE; i++)
    		strBuff.append((char)('A' + i));
    	strBuff.append("\n");
    	
    	for (int i = 0; i < SIDE * SIDE; i++) {
    		if (i % SIDE == 0) 
    			strBuff.append((char)('A' + i / SIDE) + " ");
    		if (_board[i] == EMPTY) {
    			strBuff.append("-");
    		}
    		else if (_board[i] == BLACK) {
    			strBuff.append("x");
    		}
    		else {
    			strBuff.append("o");
    		}
    		if ((i+1) % SIDE == 0)
    			strBuff.append("\n");
    	}
        return strBuff.toString();  // FIXME
    }
    
    public void draw() {
    	System.out.print(this.toString(true));
    }
    
    public void showMoves() {
    	Iterator<Move> itr = moveList.iterator();
    	PieceColor turn = WHITE;
    	while (itr.hasNext()) {
    		System.out.println(turn.toString() + ": " + itr.next().toString());
    		turn = turn.opposite();
    	}
    }
    
    /** Recording the movelist into a file*/
    public void record(File record) {
    	
    }

    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** Player that is on move. */
    private PieceColor[] _board;

    /** Set true when game ends. */
    private boolean _gameOver;

    private ArrayList<Move> moveList = new ArrayList<>();

    @Override
    public boolean equals(Object b) {
        if (((Board) b)._whoseMove != _whoseMove || ((Board) b)._gameOver != _gameOver) {
            return false;
        }
        for (int i = 0; i < SIDE * SIDE; i++){
            if (((Board) b)._board[i] != _board[i]) {
                return false;
            }
        }
        return true;
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		this.makeMove((Move) arg1);
		this.draw();
	}
}
