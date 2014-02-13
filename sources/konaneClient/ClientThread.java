/* ClientThread.java
 * Konane Game System
 * MIT IEEE/ACM IAP Programming Competition 2001
 * Paul Pham, ppham@mit.edu
 */

package konaneClient;

import java.net.*;
import java.io.*;
import konaneCommon.*;

/** Thread for game client operatons.

 * <p>The <code>ClientThread</code> class serves as a container for a
 * particular {@link konaneCommon.Player} object during a game, managing
 * and communicating moves with the game server.

 * @author Paul Pham, ppham@mit.edu
 * @version 1.13, 21 January 2001
 */
final class ClientThread extends commonThread.ServerThread {

    /** Package name to load for this client's {@link konaneCommon.Player}. */
    private static String name;

    /** Total time in milliseconds left for this client's
     * {@link konaneCommon.Player} in the current game. */
    private long timeLeft;

    /** System time at which a move began in milliseconds. */
    private long timeBegan;

    /** Time elapsed in milliseconds during a move. */
    private long transitTime;

    /** The local copy of board state for this client. This state is updated
     * by moves from this client's {@link konaneCommon.Player} and from the
     * opposing {@link konaneCommon.Player} through the server. */
    private BoardGrid boardGrid;

    /** The {@link konaneCommon.Move} returned by the player. */
    private Move playerMove;

    /** The {@link konaneCommon.Player} for this client. */
    private Player player;

    /** The parent client for this thread. */
    private KonaneClient parent;

    /** Flag signalling when a {@link konaneCommon.Player} has finished making
     * a move. */
    private boolean moveFinished = false;

    /** Constructor that accepts a <code>serverSocket</code> on which to
     * listen for a connection and a reference to the parent client. */
    public ClientThread(ServerSocket serverSocket, KonaneClient parent) {
	super(serverSocket, Konane.MAX_SERVER_PACKET_LENGTH);
	this.parent = parent;
    }

    /** Listens for a connection on the <code>serverSocket</code> member.
     */
    protected boolean getConnection() {
	print("Waiting for connection...");
	return super.getConnection();
    }

    /** Sends a signal to disconnect to the server. */
    protected void sendDisconnect() {
	output(new byte[] {Konane.DISCONNECT}, 1);
	print("Closing connection to server.");
	super.sendDisconnect();
    }

    /** Handles data received from the server by parsing the header byte.
     */
    protected void handleBuffer(byte[] recvBuffer, int count) {
	byte[] sendBuffer = new byte[Konane.MAX_CLIENT_PACKET_LENGTH];
	switch (recvBuffer[0]) {

	case Konane.DISCONNECT:
	    print("Connection closed by server.");
	    terminate = true;
	    break;

	case Konane.NAME:
	    name = new String(recvBuffer, 1, count - 1);
	    print("Received name: " + name);
	    PlayerClassLoader classLoader = new PlayerClassLoader();
	    try {
		player =
		    (Player)classLoader.loadClass(name +
						  ".Player").newInstance();
		player.setSide(parent.side);
	    }
	    catch (Exception e) {
		String error = "ClientThread::handleBuffer: " + e.toString();
		if (Konane.verbose) {
		    print(error);
		}
		System.err.println(error);
	    }
	    print("Player loaded.");
	    sendBuffer[0] = Konane.NAME_ACK;
	    output(sendBuffer, 1);
	    break;

	case Konane.TIME:
	    timeLeft = 0;
	    timeLeft |= (0xFF00000000000000L & (recvBuffer[1] << 56));
	    timeLeft |= (0x00FF000000000000L & (recvBuffer[2] << 48));
	    timeLeft |= (0x0000FF0000000000L & (recvBuffer[3] << 40));
	    timeLeft |= (0x000000FF00000000L & (recvBuffer[4] << 32));
	    timeLeft |= (0x00000000FF000000L & (recvBuffer[5] << 24));
	    timeLeft |= (0x0000000000FF0000L & (recvBuffer[6] << 16));
	    timeLeft |= (0x000000000000FF00L & (recvBuffer[7] << 8));
	    timeLeft |= (0x00000000000000FFL & recvBuffer[8]);
	    print("Received time: " + timeLeft);
	    sendBuffer[0] = Konane.TIME_ACK;
	    output(sendBuffer, 1);
	    break;

	case Konane.BEGIN_TURN:
	    print("Beginning turn. Time left: " + timeLeft);
	    Move playerMove = makeMove();
	    sendBuffer[0] = Konane.BEGIN_TURN_ACK;
	    sendBuffer[1] = (byte)playerMove.getInitialCol();
	    sendBuffer[2] = (byte)playerMove.getInitialRow();
	    sendBuffer[3] = (byte)playerMove.getFinalCol();
	    sendBuffer[4] = (byte)playerMove.getFinalRow();
	    String temp = playerMove.getComment();
		
	    for (int i = 0; i < temp.length(); i++) {
		sendBuffer[i + 5] = (byte)temp.charAt(i);
	    }
	    output(sendBuffer, temp.length() + 5);
	    print("Finishing turn. Time left: " + timeLeft);
	    break;

	case Konane.END_TURN:
	    print("Forced to end turn.");
	    sendBuffer[0] = Konane.END_TURN_ACK;
	    output(sendBuffer, 1);
	    break;

	case Konane.BOARD:
	    boardGrid = new BoardGrid(recvBuffer[1], recvBuffer[2]);
	    print("Received board dimensions: " + recvBuffer[1] + " cols, " +
		  recvBuffer[2] + " rows.");
	    sendBuffer[0] = Konane.BOARD_ACK;
	    output(sendBuffer, 1);
	    break;

	case Konane.BOARD_SYNC:
	    byte[][] tokens = new byte[recvBuffer[1]][recvBuffer[2]];
	    boardGrid = new BoardGrid(tokens, recvBuffer[1], recvBuffer[2]);
	    print("Received board sync: \n" + boardGrid.toString());
	    sendBuffer[0] = Konane.BOARD_SYNC_ACK;
	    output(sendBuffer, 1);
	    break;

	case Konane.MOVE:
	    String comment = new String(recvBuffer, 6, count - 6);
	    Move tempMove = new Move(recvBuffer[1], recvBuffer[2],
				     recvBuffer[3], recvBuffer[4],
				     recvBuffer[5], comment);
	    print("Received move: " + tempMove.toString());
	    if (boardGrid.makeMove(tempMove)) {
		print("Move successful. New board: \n" + boardGrid.toString());
		sendBuffer[0] = Konane.MOVE_ACK;
		output(sendBuffer, 1);
	    }
	    else {
		print("Move failed. Please send a valid move.");
	    }
	    break;
	case Konane.RESET:
	    print("Resetting client.");
	    parent.signalReset();
	    break;
	}
	
    }

    /** Displays messages on the GUI's textArea. */
    protected void print(String message) {
	KonaneClient.print(message);
    }

    /** Instantiates a <code>PlayerThread</code> and calls
     * {@link konaneCommon.Player@makeMove(konaneCommon.BoardGrid, long)}.
     * Performs necessary cleanup and display of debugging information.
     */
    private synchronized Move makeMove() {
        playerMove = null;
	moveFinished = false;
	PlayerThread playerThread = new PlayerThread(this);
	playerThread.start();

	try {
	    wait(timeLeft);
	}
	catch(InterruptedException e) {
	    System.err.println(e.toString());
	}

	if (!moveFinished) {
	    print("Player exceeded time limit.");
	    return new Move(-1, -1, -1, -1, parent.side, "**TIME**");
	}
	else {
	    timeLeft -= transitTime;
	    if (timeLeft < 0) {
		print("Player exceeded time limit.");
		return new Move(-1, -1, -1, -1, parent.side, "**TIME**");
	    }
	    else if (playerMove == null) {
		print("Player returned a null move.");
		return new Move(-1, -1, -1, -1, parent.side, "**ERROR**");
	    }
	    else if (playerMove.isForfeitMove()) {
		print("Player has forfeited.");
		return playerMove;
	    }
	    else if (!boardGrid.makeMove(playerMove)) {
		print("Player returned an invalid move.");
		return new Move(-1, -1, -1, -1, parent.side, "**ERROR**");
	    }
	}

	print("Player's move: " + playerMove.toString());
	print("  in " + transitTime + " milliseconds.");
	return playerMove;

    }

    /** Signals the parent <code>ClientThread</code> that the current
     * {@link konaneCommon.ClientThread.PlayerThread} is done making a
     * move.
     */
    private synchronized void wakeUp() {
	notifyAll();
    }

    /** Inner class of <code>ClientThread</code> that runs a
     * {@link konaneCommon.Player} in a separate thread of execution,
     * times it, and signals its completion to the <code>ClientThread</code>
     */
    public class PlayerThread extends Thread {

	private ClientThread parent;

	public PlayerThread(ClientThread parent) {
	    this.parent = parent;
	}

	public synchronized void run() {
	    try {
		timeBegan = System.currentTimeMillis();
		playerMove = player.makeMove(boardGrid.copy(), timeLeft);
		transitTime = System.currentTimeMillis() - timeBegan;
	    }
	    catch (Exception e) {
		System.err.println(e.toString());
		playerMove = null;
	    }
	    moveFinished = true;
	    parent.wakeUp();
	}

    }

}
