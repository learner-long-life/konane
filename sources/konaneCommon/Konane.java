/* Konane.java
 * Konane Game System
 * MIT IEEE IAP Programming Competition 2001
 * Paul Pham, ppham@mit.edu
 */

package konaneCommon;

/** Container class for game constants and utility methods.

 * The <code>Konane</code> class contains public constants and
 * methods used in all game packages to validate and execute
 * {@link konaneCommon.Move}s, construct {@link konaneCommon.BoardGrid}s,
 * and faciliate communication between the Konane client and server.

 * @author Paul Pham
 * @version 1.9, 1/7/2001
 */
public class Konane {

    /** Constant representing the white side in a game.
     * Represents either the white player or a position on a
     * {@link konaneCommon.BoardGrid} occupied by a white token.
     */
    public final static byte WHITE = 0x0F;

    /** Constant representing the black side in a game.
     * Represents either the black player or a position on a
     * {@link konaneCommon.BoardGrid} occupied by a black token.
     */
    public final static byte BLACK = ~WHITE; // WHITE = ~BLACK

    /** Constant representing no side.
     * Represents an empty position on a {@link konaneCommon.BoardGrid}.
     */
    public final static byte NONE  = 3;

    /** Constant representing an invalid side.
     * This value is returned if an invalid position is requested in a
     * {@link konaneCommon.BoardGrid}.
     */
    public final static byte ERROR = 0;

    /** Maximum allowable number of columns in a board.
     */
    public final static byte MAX_COL = 127;

    /** Maximum allowable number of rows in a board.
     */
    public final static byte MAX_ROW = 127;

    /** Bytecode for the server sending player names to a client.
     */
    public final static byte NAME           = 0;

    /** Bytecode for a client acknowledging player names from the server.
     */
    public final static byte NAME_ACK       = 1;

    /** Bytecode for the server sending player times from a client.
     */
    public final static byte TIME           = 2;

    /** Bytecode for a client acknowledging players times from the server.
     */
    public final static byte TIME_ACK       = 3;

    /** Bytecode for the server signaling a client to begin a turn.
     */
    public final static byte BEGIN_TURN     = 4;

    /** Bytecode for a client acknowledging a signal to begin a turn.
     */
    public final static byte BEGIN_TURN_ACK = 5;

    /** Bytecode for the server signaling a client to end a turn.
     * This bytecode is only sent if a client's player has exceeded its
     * time limit.
     */
    public final static byte END_TURN       = 6;

    /** Bytecode for a client acknowledging a signal to end a turn. */
    public final static byte END_TURN_ACK   = 7;

    /** Bytecode for the server sending {@link konaneCommon.BoardGrid}
     *	dimensions to a client. */
    public final static byte BOARD          = 8;

    /** Bytecode for the client acknowledging {@link konaneCommon.BoardGrid}
     * dimensions from the server. */
    public final static byte BOARD_ACK      = 9;

    /** Bytecode for the server sending an entire
     *{@link konaneCommon.BoardGrid} to a client. */
    public final static byte BOARD_SYNC     = 10;

    /** Bytecode for a client acknowledging an entire
     * {@link konaneCommon.BoardGrid} from the server. */
    public final static byte BOARD_SYNC_ACK = 11;

    /** Bytecode for the server sending a {@link konaneCommon.Move} to a
     * client. */
    public final static byte MOVE           = 12;

    /** Bytecode for a client acknolweding a {@link konaneCommon.Move} from
     * the server. */
    public final static byte MOVE_ACK       = 13;

    /** Bytecode for either the client or the server requesting a disconnect.
     */
    public final static byte DISCONNECT     = 14;

    /** Bytecode for the server signaling a client to reset itself for a
     * new game. */
    public final static byte RESET          = 15;

    /** Maximum length in bytes of a packet from the server to the client. */
    public static int MAX_SERVER_PACKET_LENGTH = 1 + 5 + 22;

    /** Maximum length in bytes of a packet from the client to the server. */
    public final static int MAX_CLIENT_PACKET_LENGTH = 1 + 5 + 22;

    /** TCP port number used for communication between the server and the
     * white client. */
    public final static int WHITE_PORT_NUMBER = 2222;

    /** TCP port number used for communication between the server and the
     * black client. */
    public final static int BLACK_PORT_NUMBER = 2223;

    /** Time in milliseconds that the server waits for a response from a
     * client before aborting communications.
     */
    public final static long TIMEOUT = 22222;

    /** Determines whether game error messages are displayed.
     * If <code>verbose</code> is true, all game error messages (not
     * just program errors) are printed to standard out and any GUI
     * text areas in the server and clients.
     */
    public static boolean verbose = true;

    /** Private construction to prevent instantiation.
     */
    private Konane() {}

    /** Translates a byte constant into a string.<br>
      * {@link konaneCommon.Konane#WHITE} => "WHITE"<br>
      * {@link konaneCommon.Konane#BLACK} => "BLACK"<br>
      * {@link konaneCommon.Konane#NONE} => "NONE"<br>
      * @param side One of the byte constants defined in {@link konaneCommon.Konane} representing a side.
      * @return The <code>String</code> representation of a byte constant representing a side.
      */
    public static String sideToString(byte side) {
	switch (side) {
	case WHITE:
	    return "WHITE";
	case BLACK:
	    return "BLACK";
	case NONE:
	    return "NONE";
	default:
	    return "ERROR";
	}
    }

}
