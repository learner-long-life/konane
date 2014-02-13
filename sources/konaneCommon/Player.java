/* Player.java
 * Konane Game System
 * MIT IEEE IAP Programming Competition 2001
 * Paul Pham, ppham@mit.edu
 */

package konaneCommon;

/** The player who makes moves for the team.
 
 * <p>The <code>Player</code> class represents the individual player who
 * competes for a team in the tournament by computing and returning moves
 * to the game engine.</p>
 * <p>Team submissions should extend this abstract class and override the
 * {@link #makeMove(konaneCommon.BoardGrid, long)} method, which is the
 * entry point into team-written code. If a <code>Player</code> needs to
 * run initialization code, the constructor should be overridden but should
 * take no parameters. You should not call the {@link #setSide(byte)} method
 * for any reason. 
 * <p>More details on writing your solution can be found in
 * the <a href="../../readme.txt">readme.txt</a> file.</p>

 * @author Paul Pham
 * @version 1.9, 17 January 2001
 */
public abstract class Player {

    /** Byte representing the side of this <code>Player</code> in the
     * current game. Sides are represented by byte constants defined in
     * {@link konaneCommon.Konane}.
     * You can refer to this variable in your subclass if your algorithm
     * depends on the <code>Player</code>'s side.
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     */
    protected byte side;

    /** Empty constructor.
     * This constructor, which takes no parameters, is necessary for
     * loading and instantiating this class programmatically. If you need
     * to perform initialization for your <code>Player</code>subclass,
     * override this constructor but do not take any parameters.
     */
    public Player() {}

    /** Sets the <code>Player</code>'s side.
     * Sides are represented by byte constants defined in
     * {@link konaneCommon.Konane}. You will never need to call this method.
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     * @param side a byte constant representing the side of the player. 
     */
    public final void setSide(byte side) {
	this.side = side;
    }

    /** The entry point into player execution.
     * <p>Override this method to implement your gameplaying algorithm. Note
     * that <code>allowedTime</code> is initialized at the beginning of each
     * game to the total time allocated to each player for making <em>all</em>
     * of their moves; as in timed chess matches, it is up to the player how
     * to distribute their time among moves.
     * <code>makeMove</code> is called with successively smaller values of
     * <code>allowedTime</code> as the player's time limit is decremented by
     * the time elapsed during its last move. Player who let their
     * <code>allowedTime</code> drop below 0 will be disqualified.
     * Consult the <a href="../../readme.txt">readme.txt</a> file for more
     * details about writing your solution.</p>
     * @param oldBoard the Konane gameboard before you make a move. You do not need to keep track of this board, since an up-to-date board will passed to this method each time you are required to make a move.
     * @param allowedTime the total number of milliseconds you have left in this game to make moves. You do not need to keep track of this time, since it will be passed to this method each time you are required to make a move.
     * @return a valid {@link konaneCommon.Move} or a voluntary forfeit. A null move will count as an error and an involuntary forfeit.
     */
    public abstract Move makeMove(BoardGrid oldBoard, long allowedTime);

}


