/* Player.java
 * Konane Game System
 * MIT IEEE IAP Programming Competition 2001
 * Paul Pham, ppham@mit.edu
 */

package reference;

import konaneCommon.*;
import java.util.Vector;

public class Player extends konaneCommon.Player {

    public Move makeMove(BoardGrid oldBoard, long allowedTime) {
	Vector moves = oldBoard.getAllMoves(side);
	if (moves.size() > 0) {
	    int moveIndex = (int)(moves.size() * Math.random());
	    Move tempMove = (Move)moves.elementAt(moveIndex);
	    return tempMove;
	}
	else {
	    return new Move(-1, -1, -1, -1, side, "**FORFEIT**");
	}
    }

}
