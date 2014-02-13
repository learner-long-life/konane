/* Move.java
 * Konane Game System
 * MIT IEEE IAP Programming Competition 2001
 * Paul Pham, ppham@mit.edu
 */

package konaneCommon;

import java.io.Serializable;

/** A move on a Konane gameboard.

 * <p>The <code>Move</code> class represents a move from an initial position to
 * a final position on a <code>BoardGrid</code>. A position is defined by its
 * row and column, numbered beginning with 0, as described in
 * {@link konaneCommon.BoardGrid}. The move
 * also records to which side it belongs, black or white; sides are defined
 * as byte constants in {@link konaneCommon.Konane}. Finally, the move includes
 * an optional comment as a <code>String</code>. You may use this comment to
 * debug your algorithm, baffle your friends, and confound your enemies!
 * Comments can be made visible during the tournament at the organizers'
 * discretion.</p>

 * @author Paul Pham
 * @version 1.14, 17 January 2001
 */
public class Move {

    /** The initial column number of a move, numbered from 0.
     */
    private int initialCol;
    /** The initial row number of a move, numbered from 0.
     */
    private int initialRow;
    /** The final column number of a move, numbered from 0.
     */
    private int finalCol;
    /** The final row number of a move, numbered from 0.
     */
    private int finalRow;
    /** The side which made the move, either black or white; sides are
     * defined as byte constants in {@link konaneCommon.Konane}.
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     */
    private byte side;
    /** Optional comment as a <code>String</code>, limited to 22 characters.
     */
    private String comment;

    /** Default constructor that accepts the initial position, final position,
     * and side of a move, along with an optional comment. The positions
     * consist of a column and row number, beginning with 0, as defined in
     * {@link konaneCommon.BoardGrid}. The side is a byte constant, either
     * {@link konaneCommon.Konane#WHITE} or {@link konaneCommon.Konane#BLACK},
     * indicating to which side the move belongs. The comment is limited to
     * a string of 22 or less characters from [A-Z,a-z,0-9]. Null comments will
     * result in a comment of the empty string.
     */
    public Move(int initialCol, int initialRow,
		int finalCol, int finalRow, byte side, String comment) {
	this.initialCol = initialCol;
	this.initialRow = initialRow;
	this.finalCol = finalCol;
	this.finalRow = finalRow;
	this.side = side;
	if (comment == null) {
	    this.comment = "";
	}
	else {
	    this.comment = (comment.length() > 22) ? comment.substring(0,22) :
		                                     comment;
	}
    }

    /** Returns the initial column of the move. */
    public int getInitialCol() {
	return initialCol;
    }

    /** Returns the initial row of the move. */
    public int getInitialRow() {
	return initialRow;
    }

    /** Returns the final column of the move. */
    public int getFinalCol() {
	return finalCol;
    }

    /** Returns the final row of the move. */
    public int getFinalRow() {
	return finalRow;
    }

    /** Returns the side of the move. */
    public byte getSide() {
	return side;
    }

    /** Returns the comment of the move. */
    public String getComment() {
	return comment;
    }

    /** Determines if a move represents a forfeit.
     * @return true if <code>comment</code> equals <code>**FORFEIT**</code>,
     * false otherwise. */
    public boolean isForfeitMove() {
	return (comment.equals("**FORFEIT**"));
    }

    /** Determines if a move represents an error.
     * @return true if <code>comment</code> equals <code>**ERROR**</code>,
     * false otherwise. */
    public boolean isErrorMove() {
	return (comment.equals("**ERROR**"));
    }

    /** Determines if the move has exceeded the player's time limit.
     * @return true if <code>comment</code> equals <code>**TIME**</code>,
     * false otherwise. */
    public boolean isTimeMove() {
	return (comment.equals("**TIME**"));
    }

    /** Converts the move to a string.
     * The string takes the form: <br>
     * (&lt;initialCol&gt;,&lt;initialRow&gt;) to (&lt;finalCol&gt;,&lt;finalRow&gt;) for &lt;side&gt;
     * with <comment> */
    public String toString() {
	return (String)("(" + initialCol + "," + initialRow + ") to (" +
			finalCol + "," + finalRow + ") for " +
			Konane.sideToString(side) + " with " + comment);
    }

}
