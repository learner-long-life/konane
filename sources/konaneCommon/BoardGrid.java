/* BoardGrid.java
 * Konane Game System
 * MIT IEEE IAP Programming Competition 2001
 * Paul Pham, ppham@mit.edu
 */

package konaneCommon;

import java.util.Vector;

/** The Konane gameboard.

 * <p>The <code>BoardGrid</code> represents a Konane gameboard by maintaining
 * a rectangular array of token positions, simply called "tokens" in
 * the documentation below. Tokens may be either white (i.e. a white token
 * occupies that position), black (i.e. a black token occupies that position),
 * or none (i.e. the position is empty). Each of these values (white, black,
 * and none) are called "sides" and are used to in validating and executing
 * moves. Sides are defined as byte constants in
 * {@link konaneCommon.Konane}.</p>

 * @author Paul Pham
 * @version 1.16, 17 January 2001
 */
public class BoardGrid {

    /** Two-dimensional array of bytes representing tokens on a Konane
     * gameboard.
     */
    public byte[][] tokens;

    /** The width of the gameboard in columns.
     * This number must be less than or equals to
     * {@link konaneCommon.Konane#MAX_COL}.
     */
    protected int width;

    /** The height of the gameboard in columns.
     * This number must be less than or equals to
     * {@link konaneCommon.Konane#MAX_ROW}.
     */
    protected int height;

    /** Empty default constructor. Frees subclass constructors from calling
     * the super method.
     */
    public BoardGrid() {};

    /** Constructor that accepts the width and height of the board.
     * This constructor initializes a board to the given <code>width</code>
     * and <code>height</code>, fills the board with alternating token values
     * of <code>Konane.WHITE</code> and <code>Konane.BLACK</code>, and
     * removes two initial tokens by calling
     * {@link #removeInitialTokens()}. If <code>width</code> is 
     * greater than {@link konaneCommon.Konane#MAX_COL} it is truncated to
     * the maximum allowable value. If <code>height</code> is greater than
     * {@link konaneCommon.Konane#MAX_ROW}, it is also truncated to the maximum
     * allowable value.
     */
    public BoardGrid(int width, int height) {
	this.width = ((width > Konane.MAX_COL) ? Konane.MAX_COL : width);
	this.height = ((height > Konane.MAX_ROW) ? Konane.MAX_ROW : height);
	tokens = new byte[width][height];
	init();
	removeInitialTokens();
    }

    /** Constructor that accepts an array of tokens to copy, along with the
     * width and height of the board. Used in cloning the board and in
     * resynchronizing boards between the server and its clients. If you need
     * to clone a board, call {@link #copy()} instead.
     */
    public BoardGrid(byte[][] tokens, int width, int height) {
	this.width = ((width > Konane.MAX_COL) ? Konane.MAX_COL : width);
	this.height = ((height > Konane.MAX_ROW) ? Konane.MAX_ROW : height);
	this.tokens = new byte[this.width][this.height];
	for (int i = 0; i < this.width; i++) {
	  for (int j = 0; j < this.height; j++) {
	    this.tokens[i][j] = tokens[i][j];
	  }
	}
    }

    /** Returns a new instance of the board with the same <code>width</code>,
     * <code>height</code>, and <code>tokens</code> values.
     */
    public BoardGrid copy() {
	return new BoardGrid(tokens, width, height);
    }

    /** Initializes the board.
     * <p>Fills the grid of spaces with alternative black and white tokens,
     * with a white token in the upper-left corner.</p>
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     */
    public void init() {
	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		if (((i + j) % 2) == 1) {
		    tokens[i][j] = Konane.BLACK;
		}
		else {
		    tokens[i][j] = Konane.WHITE;
		}
	    }
	}
    }

    /** Removes two tokens from the center of a gameboard at the
     * beginning of a game.
     * <p>The two tokens are chosen by selecting the middle
     * row of the board and the two middle columns, as calculated below:<br>
     * <code>middle_row = (height - 1) / 2</code><br>
     * <code>middle_column_1 = (height - 1) / 2</code><br>
     * <code>middle_column_2 = middle_column_1 + 1</code></p>
     * @return an array of integers representing the positions of the removed
     * tokens. The array is of the form <code>[column of first token, row of
     * both tokens].
     */
    public int[] removeInitialTokens() {
	int col = (width - 1) / 2;
	int row = (height - 1) / 2;
	tokens[col][row] = Konane.NONE;
	tokens[col + 1][row] = Konane.NONE;
	return (new int[] { col, row });
    }

    /** Returns the width of the board (the number of columns).
     */
    public int getWidth() {
	return width;
    }

    /** Returns the height of the board (the number of rows).
     */
    public int getHeight() {
	return height;
    }

    /** Retrieves the side, or color, of a token at the given position.
      * <p>First checks to see if this is a valid position in the board; if it
      * is, returns one of three constants defined in
      * {@link konaneCommon.Konane}.</p>
      * @see konaneCommon.Konane#WHITE
      * @see konaneCommon.Konane#BLACK
      * @see konaneCommon.Konane#NONE
      * @param col the column of the desired position, numbered from 0.
      * @param row the row of the desired position, numbered from 0.
      * @return side of the token at the given position, or {@link konaneCommon.Konane#ERROR} if the position is invalid.
      */
    public byte getTokenAt(int col, int row) {
	if (isValidPosition(col, row)) {
	    return tokens[col][row];
	}
	else
	    return Konane.ERROR;
    }

    /** Determines whether the given position is within this board.
     * @param col the column of the desired position, numbered from 0.
     * @param row the row of the desired position, numbered from 0.
     * @return true if the given <code>col</code> and <code>row</code> are
     * valid positions in the board; false otherwise.
     */
    public boolean isValidPosition(int col, int row) {
	return ((col >= 0) && (col <= width - 1) &&
		(row >= 0) && (row <= height - 1));
    }

    /** Determines if the given side is valid.
     * @param side the side to validate.
     * @return true if the given <code>side</code> is a valid side, i.e., one of the byte constants defined in {@link konaneCommon.Konane}.
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     * @see konaneCommon.Konane#NONE
     */
    public static boolean isValidSide(byte side) {
	return ((side == Konane.WHITE) || (side == Konane.BLACK) ||
		(side == Konane.NONE));
    }

    /** Determines whether the move from the given initial position to the
     * final position for the given side on this board is valid.
     * @param initialCol column number of the initial position, numbered from 0.
     * @param initialRow row number of the initial position, numbered from 0.
     * @param finalCol column number of the final position, numbered from 0.
     * @param finalRow row number of the final position, numbered from 0.
     * @return true if a valid move can be made from the initial position to the final position for the given <code>side</code>.
     */
    public boolean isValidMove(int initialCol, int initialRow,
			       int finalCol, int finalRow, byte side) {
	byte initialToken = getTokenAt(initialCol, initialRow);
	byte finalToken = getTokenAt(finalCol, finalRow);

	// Checks if side is valid.
	if (!isValidSide(side)) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   Konane.sideToString(side) +
				   " not a valid side.");
	    }
	    return false;
	}

	// Checks if initial position is valid.
	if (!isValidPosition(initialCol, initialRow)) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   "error at (" + initialCol + "," +
				   initialRow + ")--not valid position.");
	    }
	    return false;
	}

	// Checks if final position is valid.
	if (!isValidPosition(finalCol, finalRow)) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   "error at (" + finalCol + "," +
				   finalRow + ")--not valid position.");
	    }
	    return false;
	}

	// Checks if initial position and final position are the same.
	if (initialCol == finalCol && initialRow == finalRow) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   "same initial and final token.");
	    }
	    return false;
	}

	// Checks if move is in a straight-line.
	if (initialCol != finalCol && initialRow != finalRow) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   "not a straight-line move.");
	    }
	    return false;
	}

	// Checks if token at initialPosition is of the correct side.
	if (initialToken != side) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   "error at (" + initialCol + "," +
				   initialRow + ")--initial token is " +
				   Konane.sideToString(initialToken) +
				   ", expected " +
				   Konane.sideToString((byte)side));
	    }
	    return false;
	}

	// Checks if final token is empty.
	if (finalToken != Konane.NONE) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   "error at (" + finalCol + "," +
				   finalRow + ")--final token is not empty.");
	    }
	    return false;
	}

	// Vertical move
	if (initialCol == finalCol) {
	    // Checks if move changes position by an even number of squares.
	    if (((finalRow - initialRow) % 2) == 0) {

		// "Down" move, in the direction of increasing row numbers.
		if (initialRow < finalRow) {

		    for (int i = initialRow + 1; i < finalRow; i += 2) {

			byte jumpedToken = getTokenAt(initialCol, i);
			byte landedToken = getTokenAt(initialCol, i + 1);

			if (jumpedToken != ~side) {
			    if (Konane.verbose) {
				System.err.println("BoardGrid::isValidMove()" +
				"--error at (" + initialCol + "," + i +
				")--jumped position is " +
				Konane.sideToString(jumpedToken) +
				", expected " +
				Konane.sideToString((byte)~side));
			    }
			    return false;
			}

			if (landedToken != Konane.NONE) {
			    if (Konane.verbose) {
				System.err.println("BoardGrid::isValidMove()" +
				"--error at (" + initialCol + "," + i +
				")--jumped position is " +
				Konane.sideToString(jumpedToken) +
				", expected NONE.");
			    }
			    return false;
			}

		    }
		    return true;
		}

		// "Up" move, in the direction of decreasing row numbers.
		else { 

		    for (int i = initialRow - 1; i > finalRow; i -= 2) {

			byte jumpedToken = getTokenAt(initialCol, i);
			byte landedToken = getTokenAt(initialCol, i - 1);

			if (jumpedToken != ~side) {
			    if (Konane.verbose) {
				System.err.println("BoardGrid::isValidMove()" +
				"--error at (" + initialCol + "," + i +
				")--jumped position is " +
				Konane.sideToString(jumpedToken) +
				", expected " +
				Konane.sideToString((byte)~side));
			    }
			    return false;
			}

			if (landedToken != Konane.NONE) {
			    if (Konane.verbose) {
				System.err.println("BoardGrid::isValidMove()" +
				"--error at (" + initialCol + "," + i +
				")--jumped position is " +
				Konane.sideToString(jumpedToken) +
				", expected NONE.");
			    }
			    return false;
			}

		    }
		    return true;
		}
	    }
	    // Else move does not change position by an even number of spaces.
	    else {
		if (Konane.verbose) {
		    System.err.println("BoardGrid::isValidMove()==" +
				       "error at (" + initialCol + "," +
				       initialRow + ")--vertical jump does " +
				       "not cover an even number of spaces.");
		}
		return false;
	    }
	}
	// Else horizontal move
	else {
	    // Checks if move changes position by an even number of spaces
	    if (((finalCol - initialCol) % 2) == 0) {
		// "Right" move, in the direction of increasing column numbers.
		if (initialCol < finalCol) {

		    for (int i = initialCol + 1; i < finalCol; i += 2) {

			byte jumpedToken = getTokenAt(i, initialRow);
			byte landedToken = getTokenAt(i + 1, initialRow);

			if (jumpedToken != ~side) {
			    if (Konane.verbose) {
				System.err.println("BoardGrid::isValidMove()" +
				"--error at (" + i + "," + initialRow +
				")--jumped position is " +
				Konane.sideToString(jumpedToken) +
				", expected " +
				Konane.sideToString((byte)~side));
			    }
			    return false;
			}

			if (landedToken != Konane.NONE) {
			    if (Konane.verbose) {
				System.err.println("BoardGrid::isValidMove()" +
				"--error at (" + i + "," + initialRow +
				")--jumped position is " +
				Konane.sideToString(jumpedToken) +
				", expected NONE.");
			    }
			    return false;
			}

		    }
		    return true;
		}
		// "Left" move, in the direction of decreasing column numbers
		else { 

		    for (int i = initialCol - 1; i > finalCol; i -= 2) {

			byte jumpedToken = getTokenAt(i, initialRow);
			byte landedToken = getTokenAt(i - 1, initialRow);

			if (jumpedToken != ~side) {

			    if (Konane.verbose) {
				System.err.println("BoardGrid::isValidMove()" +
				"--error at (" + initialCol + "," + i +
				")--jumped position is " +
				Konane.sideToString(jumpedToken) +
				", expected " +
				Konane.sideToString((byte)~side));
			    }
			    return false;
			}

			if (landedToken != Konane.NONE) {
			    if (Konane.verbose) {
				System.err.println("BoardGrid::isValidMove()" +
				"--error at (" + i + initialRow +
				")--jumped position is " +
				Konane.sideToString(jumpedToken) +
				", expected NONE.");
			    }
			    return false;
			}

		    }
		    return true;
		}
	    }
	    // Else move does not change position by an even number of spaces
	    else {
		if (Konane.verbose) {
		    System.err.println("BoardGrid::isValidMove()==" +
		    "error at (" + initialCol + "," +
		    initialRow + ")--horizontal jump" +
		    "doesn't cover an even number of spaces.");
		}
		return false;
	    }
	}
    }	    

    /** Determines whether the move from the given initial position to the
     * final position for the given side on this board is valid.
     * <p>Decodes <code>move</code> into its data members and calls the
     * previous version of {@link #isValidMove(int, int, int, int, byte)}.</p>
     * @param initialCol column number of the initial position, numbered from
     * 0.
     * @param initialRow row number of the initial position, numbered from 0.
     * @param finalCol column number of the final position, numbered from 0.
     * @param finalRow row number of the final position, numbered from 0.
     * @return true if a valid move can be made from the initial position to
     * the final position for the {@link konaneCommon.Move}'s
     * <code>side</code>.
     */ 
    public boolean isValidMove(Move move) {
	if (move != null) {
	    return (isValidMove(move.getInitialCol(), move.getInitialRow(),
				move.getFinalCol(), move.getFinalRow(),
				move.getSide()));
	}
	else {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--null move");
	    }
	    return false;
	}
    }
	
    /** Returns all possible {@link konaneCommon.Move}s from the given
     * initial position for the given <code>side</code>.
     * <code>side</code> should be one of the byte constants defined in
     * {@link konaneCommon.Konane}.
     * @param initialCol column number of the initial position, numbered from
     * 0.
     * @param initialRow row number of the initial position, numbered from 0.
     * @return a vector of all possible moves from the initial position for
     * the given <code>side</code>.
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     */
    public Vector getMoves(int initialCol, int initialRow, byte side) {

	byte initialToken = getTokenAt(initialCol, initialRow);
	Vector moves = new Vector();

	// Checks if side is valid.
	if (!isValidSide(side)) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   Konane.sideToString(side) +
				   " not a valid side.");
	    }
	    return moves;
	}

	// Checks if position is valid.
	if (!isValidPosition(initialCol, initialRow)) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   "error at (" + initialCol + "," +
				   initialRow + ")--not valid position.");
	    }
	    return moves;
	}

	// Checks if initial token is of the correct side.
	if (initialToken != side) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::isValidMove()--" +
				   "error at (" + initialCol + "," +
				   initialRow + ")--initial token is " +
				   Konane.sideToString(initialToken) +
				   ", expected " +
				   Konane.sideToString(side));
	    }
	    return moves;
	}


	// "Down" moves, in the direction of increasing row numbers
	for (int i = initialRow + 1; i < (height - 1); i += 2) {
	    if ((getTokenAt(initialCol, i) == ~side) &&
		(getTokenAt(initialCol, i + 1) == Konane.NONE)) {
		Move newMove = new Move(initialCol, initialRow,
					initialCol, i + 1, side, "");
		moves.addElement(newMove);
	    }
	    else {
		break;
	    }
	}
	// "Up" moves, in the direction if decreasing row numbers.
	for (int i = initialRow - 1; i > 0; i -= 2) {
	    if ((getTokenAt(initialCol, i) == ~side) &&
		(getTokenAt(initialCol, i - 1) == Konane.NONE)) {
		Move newMove = new Move(initialCol, initialRow,
					initialCol, i - 1, side, "");
		moves.addElement(newMove);
	    }
	    else {
		break;
	    }
	}

	// "Right" moves, in the direction of increasing column numbers.
	for (int i = initialCol + 1; i < (width - 1); i += 2) {
	    if ((getTokenAt(i, initialRow) == ~side) &&
		(getTokenAt(i + 1, initialRow) == Konane.NONE)) {
		Move newMove = new Move(initialCol, initialRow,
					i + 1, initialRow, side, "");
		moves.addElement(newMove);
	    }
	    else {
		break;
	    }
	}

	// "Left" moves, in the direction of decreasing column numbers.
	for (int i = initialCol - 1; i > 0; i -= 2) {
	    if ((getTokenAt(i, initialRow) == ~side) &&
		(getTokenAt(i - 1, initialRow) == Konane.NONE)) {
		Move newMove = new Move(initialCol, initialRow,
					i - 1, initialRow, side, "");
		moves.addElement(newMove);
	    }
	    else {
		break;
	    }
	}

	return moves;
    }

    /** Returns a vector of all possible moves on the board for the given
     * <code>side</code>. */
    public Vector getAllMoves(byte side) {
	Vector moves = new Vector();
	for (int i = 0; i < width; i++) {
	    for (int j = 0; j < height; j++) {
		byte token = tokens[i][j];
		if (token == side) {
		    Vector tempMoves = getMoves(i, j, side);
		    for (int k = 0; k < tempMoves.size(); k++) {
			moves.addElement(tempMoves.elementAt(k));
		    }
		}
	    }
	}
	return moves;
    }

    /** Executes the given move on the board.
     * <p>Checks to see if the {@link konaneCommon.Move} is valid by calling
     * {@link #isValidMove(konaneCommon.Move)}. If it is, then the board's
     * tokens are changed to reflect the move.</p>
     * @param move the desired move to execute. 
     * @return true if move is successful, false otherwise.
     */
    public boolean makeMove(int initialCol, int initialRow,
			    int finalCol, int finalRow, byte side) {

	// Checks if move is valid.
	if (!isValidMove(initialCol, initialRow, finalCol, finalRow,
			 side)) {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::move()--invalid move from (" +
				   initialCol + "," + initialRow + ") to (" +
				   finalCol + "," + finalRow +
				   ") for side " + Konane.sideToString(side));

	    }
	    return false;
	}

	// Sets final position to the correct side.
	tokens[finalCol][finalRow] = side;
	// Empties the initial position.
	tokens[initialCol][initialRow] = Konane.NONE;

	// Vertical move
	if (initialCol == finalCol) {
	    // "Down" jump, in the direction of increasing row numbers.
	    if (initialRow < finalRow) {
		for (int i = initialRow + 1; i < finalRow; i++) {
		    tokens[initialCol][i] = Konane.NONE;
		}
	    }
	    else { 
		// "Up" jump, in the direction of decreasing row numbers.
		for (int i = initialRow - 1; i > finalRow; i--) {
		    tokens[initialCol][i] = Konane.NONE;
		}
	    }

	}
	// Horizontal move
	else {
	    // "Right" move, in the direction of increasing column numbers.
	    if (initialCol < finalCol) {
		for (int i = initialCol + 1; i < finalCol; i++) {
		    tokens[i][initialRow] = Konane.NONE;
		}
	    }
	    // "Left" move, in the direction of decreasing column numbers.
	    else {
		for (int i = initialCol - 1; i > finalCol; i--) {
		    tokens[i][initialRow] = Konane.NONE;
		}
	    }

	}
	return true;
    }

    /** Executes the given move on the board.
     * <p>Checks to see if the {@link konaneCommon.Move} is valid by calling
     * {@link #isValidMove(konaneCommon.Move)}. If it is, then the board's
     * tokens are changed to reflect the move.
     * Decodes <code>move</code> and calls the previous version of
     * {@link #makeMove(int, int, int, int, byte)}.</p>
     * @param move the desired move to execute. 
     * @return true if move is successful, false otherwise.
     */
    public boolean makeMove(Move move) {
	if (move != null) {
	    return makeMove(move.getInitialCol(), move.getInitialRow(),
			    move.getFinalCol(), move.getFinalRow(),
			    move.getSide());
	}
	else {
	    if (Konane.verbose) {
		System.err.println("BoardGrid::makeMove()--null move");
	    }
	    return false;
	}
    }

    /** Returns a grid of ASCII characters representing the current
     * state of the board. One row is printed per line with the columns
     * separated by spaces. 'W' represents a white token, 'B' represents
     * a black token, and '_' represents a blanks space.
     * @return a <code>String</code> representation of the board.
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     * @see konaneCommon.Konane#NONE
     */
    public String toString() {
	String temp = new String();
	for (int i = 0; i < height; i++) {
	    for (int j = 0; j < width; j++) {
		byte token = tokens[j][i];
		if (token == Konane.WHITE) {
		    temp += "W ";
		}
		else if (token == Konane.BLACK) {
		    temp += "B ";
		}
		else {
		    temp += "_ ";
		}
	    }
	    temp += "\n";
	}
	return temp;
    }

}
