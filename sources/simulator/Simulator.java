/* Simulator.java
 * Konane Game System
 * MIT IEEE IAP Programming Competition 2001
 * Paul Pham, ppham@mit.edu
 */

package simulator;

import konaneCommon.*;

/** Simulator **/
final class Simulator {

    private byte      won;
    private BoardGrid boardGrid;

    private String    whiteName;
    private Player    whitePlayer;
    private long      whiteTime;

    private String    blackName;
    private Player    blackPlayer;
    private long      blackTime;

    private boolean   moveFinished;
    private long      timeBegan;
    private long      transitTime;
    private Move      playerMove;
    private long      originalTime; // arjunrn

    public Simulator(int boardWidth, int boardHeight, long playerTime,
		     String whiteName, String blackName) {
	won = Konane.NONE;
	System.out.println("Initializing " + boardWidth + " by " +
			   boardHeight + " board.");
	boardGrid = new BoardGrid(boardWidth, boardHeight);
	System.out.println("\n" + boardGrid.toString() + "\n");
	System.out.println("Time for each player is " + playerTime +
			   " milliseconds.");
	this.originalTime = playerTime; //arjunrn
	this.whiteTime = playerTime;
	this.blackTime = playerTime;
	this.whiteName = whiteName;
	this.blackName = blackName;
	whitePlayer = loadPlayer(whiteName, Konane.WHITE);
	blackPlayer = loadPlayer(blackName, Konane.BLACK);
	System.out.println("Beginning game.\n\n");
    }

    private Player loadPlayer(String playerName, byte playerSide) {
	ClassLoader classLoader = new PlayerClassLoader();
	System.out.println("Trying to load " +
			   Konane.sideToString(playerSide) + " player: " +
			   playerName);
	Player player = null;
	try {
	    player = (Player)classLoader.loadClass(playerName +
						   ".Player").newInstance();
	}
	catch (Exception e) {
	    System.out.println(e.toString());
	    System.exit(1);
	}
	player.setSide(playerSide);
	System.out.println(Konane.sideToString(playerSide) +
			   " player loaded.");
	return player;
    }

    public synchronized Result playGame() {
	boolean terminate = false;
	int moveCount = 0;
	while (true) {

	    //WHITE's move
	    System.out.println("-----------------------------------");
	    System.out.println("Beginning WHITE's turn. Time left: " +
			       whiteTime);
	    System.out.println(boardGrid.toString());
	    moveFinished = false;
	    PlayerThread whiteThread = new PlayerThread(whitePlayer,
							whiteTime, this);
	    whiteThread.start();

	    try {
		wait(blackTime);
	    }
	    catch(InterruptedException e) {
		System.err.println(e.toString());
	    }

	    if (!moveFinished) {
		terminate = true;
		System.out.println("WHITE exceeded time limit.");
	    }
	    else {
		whiteTime -= transitTime;
		if (whiteTime < 0) {
		    terminate = true;
		    System.out.println("WHITE exceeded time limit.");
		}
		else if (!boardGrid.makeMove(playerMove)) {
		    System.out.println("WHITE returned an invalid move.");
		    terminate = true;
		}
	    }

	    if (terminate) {
		System.out.println("Terminating WHITE player.");
		System.out.println("BLACK won.");
		whiteThread = null;
		System.gc();
		won = Konane.BLACK;
		break;
	    }

	    System.out.println("WHITE's move: " + playerMove.toString());
	    System.out.println("  in " + transitTime + " milliseconds.");

	    if (boardGrid.getAllMoves(Konane.BLACK).size() == 0) {
		System.out.println("WHITE won.");
		won = Konane.WHITE;
		break;
	    }

	    //BLACK's move
	    System.out.println("-----------------------------------");
	    System.out.println("Beginning BLACK's turn. Time left: " +
			       blackTime);
	    System.out.println(boardGrid.toString());
	    moveFinished = false;
	    PlayerThread blackThread = new PlayerThread(blackPlayer,
							blackTime, this);
	    blackThread.start();

	    try {
		wait(blackTime);
	    }
	    catch(InterruptedException e) {
		System.err.println(e.toString());
	    }

	    if (!moveFinished) {
		terminate = true;
		System.out.println("BLACK exceeded time limit.");
	    }
	    else {
		blackTime -= transitTime;
		if (blackTime < 0) {
		    terminate = true;
		    System.out.println("BLACK exceeded time limit.");
		}
		else if (!boardGrid.makeMove(playerMove)) {
		    System.out.println("BLACK returned an invalid move.");
		    terminate = true;
		}
	    }

	    if (terminate) {
		System.out.println("Terminating BLACK player.");
		System.out.println("WHITE won.");
		blackThread = null;
		System.gc();
		won = Konane.WHITE;
		break;
	    }

	    System.out.println("BLACK's move: " + playerMove.toString());
	    System.out.println("  in " + transitTime + " milliseconds.");

	    if (boardGrid.getAllMoves(Konane.WHITE).size() == 0) {
		System.out.println("BLACK won.");
		won = Konane.BLACK;
		break;
	    }

	    moveCount++;
	}
	System.out.println("Game lasted " + moveCount + " moves.");

	return new  Result(this.originalTime, this.boardGrid.getWidth(), 
			   this.boardGrid.getHeight(), this.whiteName,
			   this.whiteTime, this.blackName,
			   this.blackTime,
			   this.won, moveCount) ;
       
    }

    public synchronized void wakeUp() {
	notifyAll();
    }

    public static void main(String[] args) {
	System.out.println("\nKonane Simulator, " +
			   "MIT IEEE IAP Programming Contest 2001\n");
	Simulator simulator = new Simulator(new Integer(args[0]).intValue(),
					    new Integer(args[1]).intValue(),
					    new Long(args[2]).longValue(),
					    args[3], args[4]);
	simulator.playGame();
    }

    private final class PlayerThread extends Thread {

	private Player player;
	private long playerTime;
	private Simulator parent;

	public PlayerThread(Player player, long playerTime,
			    Simulator parent) {
	    this.player = player;
	    this.playerTime = playerTime;
	    this.parent = parent;
	}

	public synchronized void run() {
	    try {
		timeBegan = System.currentTimeMillis();
		playerMove = player.makeMove(boardGrid.copy(), playerTime);
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







