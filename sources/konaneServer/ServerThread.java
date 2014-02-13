/* ServerThread.java
 * Konane Game System
 * MIT IEEE/ACM IAP Programming Competition 2001
 * Paul Pham, ppham@mit.edu
 */

package konaneServer;

import java.net.*;
import java.io.*;
import konaneCommon.*;

/** Thread for game server operatons.

 * <p>The <code>ServerThread</code> class controls game and tournament
 * parameters and coordinates games between clients. It initializes and
 * manages the clients by sending out various command "packets" that instruct
 * the client to reset itself, load a player, synchronize its time, begin
 * a turn, end a turn, and synchronize its board.

 * @author Paul Pham, ppham@mit.edu
 * @version 1.12, 17 January 2001
 */
final class ServerThread extends commonThread.ClientThread {

    /** TCP socket of the white client. */
    private Socket whiteSocket;

    /** Input stream of the {@link #whiteSocket}. */
    private DataInputStream whiteInputStream;

    /** Output stream of the {@link #whiteSocket}. */
    private DataOutputStream whiteOutputStream;

    /** Package name of the white {@link konaneCommon.Player}. */
    private String whiteName;

    /** Hostname or IP address of the white client. */
    private String whiteHostname;

    /** TCP socket of the white client. */
    private Socket blackSocket;

    /** Input stream of the {@link #whiteSocket}. */
    private DataInputStream blackInputStream;

    /** Output stream of the {@link #whiteSocket}. */
    private DataOutputStream blackOutputStream;

    /** Package name of the white {@link konaneCommon.Player}. */
    private String blackName;

    /** Hostname or IP address of the white client. */
    private String blackHostname;

    /** Total time for each {@link konaneCommon.Player} to make all its moves
     * in this game. */
    private long totalTime;

    /** The side of the {@link konaneCommon.Player} who has won this game.
     * Sides are defined as byte constants in {@link konaneCommon.Konane}.
     * Its value is initially {@link konaneCommon.Konane#NONE} to indicate
     * that the game is still being played and no one has won yet.
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     * @see konaneCommon.Konane#NONE
     */
    private byte won;

    /** Constructor which accepts the names of the white and black players'
     * package, the hostnames/IP addresses of the white and black clients,
     * and the total time allocated to each player for this game.
     */
    public ServerThread(String whiteName, String blackName,
			String whiteHostname, String blackHostname,
			long totalTime) {
	super(null, 0, Konane.MAX_SERVER_PACKET_LENGTH);
	this.whiteName = whiteName;
	this.blackName = blackName;
	this.whiteHostname = whiteHostname;
	this.blackHostname = blackHostname;
	this.totalTime = totalTime;
	won = Konane.NONE;
    }

    /** Established a connection to both the white and black clients. */
    protected boolean getConnection() {
	print("Connecting to clients...");
	try {
	    whiteSocket = new Socket(whiteHostname, Konane.WHITE_PORT_NUMBER);
	    print("Connected to white client.");
	    whiteInputStream =
		new DataInputStream(whiteSocket.getInputStream());
	    whiteOutputStream =
		new DataOutputStream(whiteSocket.getOutputStream());
	    blackSocket = new Socket(blackHostname, Konane.BLACK_PORT_NUMBER);
	    print("Connected to black client.");
	    blackInputStream =
		new DataInputStream(blackSocket.getInputStream());
	    blackOutputStream =
		new DataOutputStream(blackSocket.getOutputStream());
	    return true;
	}
	catch (IOException e) {
	    System.err.println("GameThread::connect(): " + e.toString());
	    return false;
	}
    }

    /** Returns the side of the player who won.
     * Sides are defined as byte constants in {@link konaneCommon.Konane}.
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     * @see konaneCommon.Konane#NONE
     * @return Side of the player who won as a byte constant.
     */
    public byte getWon() {
	return won;
    }

    /** Sends package names to the white and black clients and waits for
     * acknowledgments.
     */
    private void sendNames() {
	int count;
	long time;
	byte[] recvBuffer = new byte[1];
	byte[] sendBuffer = new byte[Konane.MAX_SERVER_PACKET_LENGTH];
	sendBuffer[0] = Konane.NAME;

	for (int i = 1; i <= whiteName.length(); i++) {
	    sendBuffer[i] = (byte)whiteName.charAt(i - 1);
	}
	outputToWhiteClient(sendBuffer, whiteName.length() + 1);
	print("Sending WHITE name: " + whiteName);

	try {
	    count = 0;
	    time = System.currentTimeMillis();
	    while ((whiteInputStream.available() == 0) &&
		   (System.currentTimeMillis() < time + Konane.TIMEOUT));
	    while ((whiteInputStream.available() > 0) && (count < 1)) {
		count += whiteInputStream.read(recvBuffer, count,
					       whiteInputStream.available());
	    }
	    if (recvBuffer[0] == Konane.NAME_ACK) {

	    }
	}
	catch (IOException e) {
	    String error = "ServerThread::sendNames: " + e.toString();
	    if (Konane.verbose) {
		print(error);
	    }
	    System.err.println(error);
	}

	for (int i = 1; i <= blackName.length(); i++) {
	    sendBuffer[i] = (byte)blackName.charAt(i - 1);
	}
	outputToBlackClient(sendBuffer, blackName.length() + 1);
	print("Sending BLACK name: " + blackName);

	try {
	    count = 0;
	    time = System.currentTimeMillis();
	    while ((blackInputStream.available() == 0) &&
		   (System.currentTimeMillis() < time + Konane.TIMEOUT));
	    while ((blackInputStream.available() > 0) && (count < 1)) {
		count += blackInputStream.read(recvBuffer, count,
					       blackInputStream.available());
	    }
	    if (recvBuffer[0] == Konane.NAME_ACK) {

	    }
	}
	catch (IOException e) {
	    String error = "ServerThread::sendNames: " + e.toString();
	    if (Konane.verbose) {
		print(error);
	    }
	    System.err.println(error);
	}

    }

    /** Sends total playing time to both white and black clients and waits
     * for acknowledgments.
     */
    private void sendTimes() {
	int count = 0;
	long time;
	byte[] recvBuffer = new byte[1];
	byte[] sendBuffer = new byte[9];
	sendBuffer[0] = Konane.TIME;
	sendBuffer[1] = (byte)(0xFF & (totalTime >> 56));
	sendBuffer[2] = (byte)(0xFF & (totalTime >> 48));
	sendBuffer[3] = (byte)(0xFF & (totalTime >> 40));
	sendBuffer[4] = (byte)(0xFF & (totalTime >> 32));
	sendBuffer[5] = (byte)(0xFF & (totalTime >> 24));
	sendBuffer[6] = (byte)(0xFF & (totalTime >> 16));
	sendBuffer[7] = (byte)(0xFF & (totalTime >> 8));
	sendBuffer[8] = (byte)(0xFF & totalTime);
	outputToWhiteClient(sendBuffer, 9);
	print("Sending WHITE total time: " + totalTime);

	try {
	    count = 0;
	    time = System.currentTimeMillis();
	    while ((whiteInputStream.available() == 0) &&
		   (System.currentTimeMillis() < time + Konane.TIMEOUT));
	    while ((whiteInputStream.available() > 0) && (count < 1)) {
		count += whiteInputStream.read(recvBuffer, count,
					       whiteInputStream.available());
	    }
	    if (recvBuffer[0] == Konane.TIME_ACK) {

	    }
	}
	catch (IOException e) {
	    String error = "ServerThread::sendSides: " + e.toString();
	    if (Konane.verbose) {
		print(error);
	    }
	    System.err.println(error);
	}

	outputToBlackClient(sendBuffer, 9);
	print("Sending BLACK total time: " + totalTime);

	try {
	    count = 0;
	    time = System.currentTimeMillis();
	    while ((blackInputStream.available() == 0) &&
		   (System.currentTimeMillis() < time + Konane.TIMEOUT));
	    while ((blackInputStream.available() > 0) && (count < 1)) {
		count += blackInputStream.read(recvBuffer, count,
					       blackInputStream.available());
	    }
	    if (recvBuffer[0] == Konane.TIME_ACK) {

	    }
	}
	catch (IOException e) {
	    String error = "ServerThread::sendSides: " + e.toString();
	    if (Konane.verbose) {
		print(error);
	    }
	    System.err.println(error);
	}

    }

    /** Sends {@link konaneCommon.BoardGrid} dimensions to both the white
     * and black clients and waits for acknowledgments.
     */
    private void sendBoardGrids() {
	int count = 0;
	long time;
	byte[] recvBuffer = new byte[1];
	byte[] sendBuffer = new byte[3];
	sendBuffer[0] = Konane.BOARD;
	sendBuffer[1] = (byte)KonaneServer.boardGrid.getWidth();
	sendBuffer[2] = (byte)KonaneServer.boardGrid.getHeight();
	outputToWhiteClient(sendBuffer, 3);
	print("Sending WHITE board dimensions: " + sendBuffer[1] + " cols, " +
	      sendBuffer[2] + " rows.");

	try {
	    count = 0;
	    time = System.currentTimeMillis();
	    while ((whiteInputStream.available() == 0) &&
		   (System.currentTimeMillis() < time + Konane.TIMEOUT));
	    while ((whiteInputStream.available() > 0) && (count < 1)) {
		count += whiteInputStream.read(recvBuffer, count,
					       whiteInputStream.available());
	    }
	    if (recvBuffer[0] == Konane.BOARD_ACK) {

	    }
	}
	catch (IOException e) {
	    String error = "ServerThread::sendBoard: " + e.toString();
	    if (Konane.verbose) {
		print(error);
	    }
	    System.err.println(error);
	}

	outputToBlackClient(sendBuffer, 3);
	print("Sending BLACK board dimensions: " + sendBuffer[1] + " cols, " +
	      sendBuffer[2] + " rows.");

	try {
	    count = 0;
	    time = System.currentTimeMillis();
	    while ((blackInputStream.available() == 0) &&
		   (System.currentTimeMillis() < time + Konane.TIMEOUT));
	    while ((blackInputStream.available() > 0) && (count < 1)) {
		count += blackInputStream.read(recvBuffer, count,
					       blackInputStream.available());
	    }
	    if (recvBuffer[0] == Konane.BOARD_ACK) {

	    }
	}
	catch (IOException e) {
	    String error = "ServerThread::sendBoard: " + e.toString();
	    if (Konane.verbose) {
		print(error);
	    }
	    System.err.println(error);
	}

    }

    /** Sends the entire {@link konaneCommon.BoardGrid#tokens} member of
     * the current gameboard to resynchronize both clients' local gameboards.
     * Also waits for acknowledgments from each client.
     */
    private void syncBoardGrids() {
	print("Sending boardgrid: " + KonaneServer.boardGrid.toString());
	int count;
	long time;
	DataInputStream inputStream;
	byte[] recvBuffer = new byte[1];
	byte[] sendBuffer = new byte[Konane.MAX_SERVER_PACKET_LENGTH];
	sendBuffer[0] = Konane.BOARD_SYNC;
	sendBuffer[1] = (byte)KonaneServer.boardGrid.getWidth();
	sendBuffer[2] = (byte)KonaneServer.boardGrid.getHeight();
	int index = 3;
	for (int i = 0; i < KonaneServer.boardGrid.getHeight(); i++) {
	    for (int j = 0; j < KonaneServer.boardGrid.getWidth(); j++) {
		sendBuffer[index++] =
		    KonaneServer.boardGrid.getTokenAt(j,i);
	    }
	}

	outputToWhiteClient(sendBuffer, index);
	print("Syncing WHITE's board: \n" + KonaneServer.boardGrid);

	try {
	    count = 0;
	    time = System.currentTimeMillis();
	    while ((whiteInputStream.available() == 0) &&
		   (System.currentTimeMillis() < time + Konane.TIMEOUT));
	    while ((whiteInputStream.available() > 0) && (count < 1)) {
		count += whiteInputStream.read(recvBuffer, count,
					       whiteInputStream.available());
	    }
	    if (recvBuffer[0] == Konane.BOARD_SYNC_ACK) {

	    }
	}
	catch (IOException e) {
	    String error = "ServerThread::sendSides: " + e.toString();
	    if (Konane.verbose) {
		print(error);
	    }
	    System.err.println(error);
	}

	outputToBlackClient(sendBuffer, index);
	print("Syncing BLACK's board: \n" + KonaneServer.boardGrid);

	try {
	    count = 0;
	    time = System.currentTimeMillis();
	    while ((blackInputStream.available() == 0) &&
		   (System.currentTimeMillis() < time + Konane.TIMEOUT));
	    while ((blackInputStream.available() > 0) && (count < 1)) {
		count += blackInputStream.read(recvBuffer, count,
					       blackInputStream.available());
	    }
	    if (recvBuffer[0] == Konane.BOARD_SYNC_ACK) {

	    }
	}
	catch (IOException e) {
	    String error = "ServerThread::sendSides: " + e.toString();
	    if (Konane.verbose) {
		print(error);
	    }
	    System.err.println(error);
	}
    }

    /** Sends a {@link konaneCommon.Move} to the appropriate client based
     * on the side of the {@link konaneCommon.Move}. Sides are defined as
     * byte constants in {@link konaneCommon.Konane}.
     * @see konaneCommon.Konane#WHITE
     * @see konaneCommon.Konane#BLACK
     * @see konaneCommon.Konane#NONE
     */
    private void sendMove(Move move) {
	int count = 0;
	long time;
	byte[] recvBuffer = new byte[1];
	byte[] sendBuffer = new byte[Konane.MAX_SERVER_PACKET_LENGTH];
	sendBuffer[0] = Konane.MOVE;
	sendBuffer[1] = (byte)move.getInitialCol();
	sendBuffer[2] = (byte)move.getInitialRow();
	sendBuffer[3] = (byte)move.getFinalCol();
	sendBuffer[4] = (byte)move.getFinalRow();
	sendBuffer[5] = move.getSide();
	String comment = move.getComment();
	for (int i = 6; i < comment.length() + 6; i++) {
	    sendBuffer[i] = (byte)comment.charAt(i - 6);
	}

	if (sendBuffer[5] == Konane.BLACK) {
	    outputToWhiteClient(sendBuffer, comment.length() + 6);
	    print("Sending move to WHITE.");
	    try {
		count = 0;
		time = System.currentTimeMillis();
		while ((whiteInputStream.available() == 0) &&
		       (System.currentTimeMillis() < time + Konane.TIMEOUT));
		while ((whiteInputStream.available() > 0) && (count < 1)) {
		    count +=
			whiteInputStream.read(recvBuffer, count,
					      whiteInputStream.available());
		}
		if (recvBuffer[0] == Konane.MOVE_ACK) {
		    
		}
	    }
	    catch (IOException e) {
		String error = "ServerThread::sendMove: " + e.toString();
		if (Konane.verbose) {
		    print(error);
		}
		System.err.println(error);
	    }
	}
	else if (sendBuffer[5] == Konane.WHITE) {
	    print("Sending move to BLACK.");
	    outputToBlackClient(sendBuffer, comment.length() + 6);
	    try {
		count = 0;
		time = System.currentTimeMillis();
		while ((blackInputStream.available() == 0) &&
		       (System.currentTimeMillis() < time + Konane.TIMEOUT));
		while ((blackInputStream.available() > 0) && (count < 1)) {
		    count +=
			blackInputStream.read(recvBuffer, count,
					      blackInputStream.available());
		}
		if (recvBuffer[0] == Konane.MOVE_ACK) {
		    
		}
	    }
	    catch (IOException e) {
		String error = "ServerThread::sendBoard: " + e.toString();
		if (Konane.verbose) {
		    print(error);
		}
		System.err.println(error);
	    }
	}
    }

    /** Sends bytes to the white client. */
    private void outputToWhiteClient(byte[] buffer, int bufferLength) {
	if (whiteOutputStream != null) {
	    try {
		whiteOutputStream.write(buffer, 0, bufferLength);
		whiteOutputStream.flush();
	    }
	    catch (IOException e) {
		String error = "ServerThread::outputToWhiteClient: " +
		    e.toString();
		if (Konane.verbose) {
		    print(error);
		}
		System.err.println(error);
	    }
	}
    }

    /** Sends bytes to the black client. */
    private void outputToBlackClient(byte[] buffer, int bufferLength) {
	if (blackOutputStream != null) {
	    try {
		blackOutputStream.write(buffer, 0, bufferLength);
		blackOutputStream.flush();
	    }
	    catch (IOException e) {
		String error = "ServerThread::outputToBlackClient: " +
		    e.toString();
		if (Konane.verbose) {
		    print(error);
		}
		System.err.println(error);
	    }
	}
    }

    /** Displays test on the GUI textArea. */
    protected void print(String message) {
	KonanePanel.print(message);
    }

    /** Sends a disconnect signal to both the white and black clients. */
    protected void sendDisconnect() {
	byte[] sendBuffer = { Konane.DISCONNECT };
	if (whiteSocket != null) {
	    outputToWhiteClient(sendBuffer, 1);
	    try {
		whiteInputStream.close();
		whiteOutputStream.close();
		whiteSocket.close();
		whiteSocket = null;
	    }
	    catch (IOException e) {
		System.err.println(e.toString());
	    }
	}
	if (blackSocket != null) {
	    outputToBlackClient(sendBuffer, 1);
	    try {
		blackInputStream.close();
		blackOutputStream.close();
		blackSocket.close();
		blackSocket = null;
	    }
	    catch (IOException e) {
		System.err.println(e.toString());
	    }
	}
    }

    public void sendReset() {
	byte[] sendBuffer = { Konane.RESET };
	outputToWhiteClient(sendBuffer, 1);
	outputToBlackClient(sendBuffer, 1);
    }

    /** Begins a new game. Sends initialization packets to the white
     * and black clients, then alternates between white and black moves.
     */
    public void run() {
	if (!getConnection()) {
	    return;
	}
	sendNames();
	sendTimes();
	sendBoardGrids();
	
	int count;
	byte[] recvBuffer = new byte[Konane.MAX_CLIENT_PACKET_LENGTH];
	byte[] sendBuffer = { Konane.BEGIN_TURN };
	while (!terminate && won == Konane.NONE) {

	    //WHITE MOVE
	    count = 0;
	    outputToWhiteClient(sendBuffer, 1);
	    print("Beginning WHITE's turn.");
	    try {
		while (whiteInputStream.available() == 0);
		while ((whiteInputStream.available() > 0) &&
		       (count < Konane.MAX_CLIENT_PACKET_LENGTH)) {
		    count += whiteInputStream.read(recvBuffer, count,
						whiteInputStream.available());
		}
		if (recvBuffer[0] == Konane.BEGIN_TURN_ACK) {
		    String temp = new String(recvBuffer, 5, count - 5);
		    Move tempMove = new Move((int)recvBuffer[1],
					     (int)recvBuffer[2],
					     (int)recvBuffer[3],
					     (int)recvBuffer[4],
					     Konane.WHITE, temp);
		    print("Received: " + tempMove.toString());
		    if (tempMove.isErrorMove() || tempMove.isForfeitMove()) {
			if (tempMove.isErrorMove()) {
			    print("WHITE returned an invalid move.");
			}
			else {
			    print("WHITE forfeited.");
			}
			print("BLACK won.");
			won = Konane.BLACK;
			break;
		    }
		    else {
			KonaneServer.boardGrid.makeMove(tempMove);
			if (KonaneServer.boardGrid.getAllMoves(Konane.BLACK).size() == 0) {
			    print("WHITE won.");
			    won = Konane.WHITE;
			    break;
			}
			else {
			    sendMove(tempMove);
			}
		    }
		}
		else {
		    System.err.println("Client returned invalid reply");
		}
	    }
	    catch (IOException e) {
		System.err.println(e.toString());
	    }
	    
	    // BLACK MOVE
	    count = 0;
	    outputToBlackClient(sendBuffer, 1);
	    print("Beginning BLACK's turn.");
	    try {
		while (blackInputStream.available() == 0);
		while ((blackInputStream.available() > 0) &&
		       (count < Konane.MAX_CLIENT_PACKET_LENGTH)) {
		    count += blackInputStream.read(recvBuffer, count,
						blackInputStream.available());
		}
		if (recvBuffer[0] == Konane.BEGIN_TURN_ACK) {
		    String temp = new String(recvBuffer, 5, count - 5);
		    Move tempMove = new Move((int)recvBuffer[1],
					     (int)recvBuffer[2],
					     (int)recvBuffer[3],
					     (int)recvBuffer[4],
					     Konane.BLACK, "");
		    print("Received: " + tempMove.toString());
		    if (tempMove.isErrorMove() || tempMove.isForfeitMove()) {
			if (tempMove.isErrorMove()) {
			    print("BLACK returned an invalid move.");
			}
			else {
			    print("BLACK forfeited.");
			}
			print("WHITE won.");
			won = Konane.WHITE;
			break;
		    }
		    else {
			KonaneServer.boardGrid.makeMove(tempMove);
			if (KonaneServer.boardGrid.getAllMoves(Konane.WHITE).size() == 0) {
			    print("BLACK won.");
			    won = Konane.BLACK;
			    break;
			}
			else {
			    sendMove(tempMove);
			}
		    }
		}
		else {
		    System.err.println("Client returned invalid reply");
		}
	    }
	    catch (IOException e) {
		System.err.println(e.toString());
	    }
	}
	sendReset();
    }

}
