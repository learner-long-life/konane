/* Simulator.java
 * Konane Game System
 * MIT IEEE IAP Programming Competition 2001
 * Arjun Narayanswamy, arjunrn@mit.edu
 */

package simulator;

import konaneCommon.*;

public final class Result {
	
    private long totalTime;
    private int boardWidth;
    private int boardHeight;
	
    private String whiteName;
    private long whiteTimeLeft;

    private String blackName;
    private long blackTimeLeft;
	
    private  byte won = Konane.NONE;
    private int movesMade; // number of moves made

    public Result(long totalTime, int boardWidth, int boardHeight,
		  String whiteName, long whiteTimeLeft,
		  String blackName, long blackTimeLeft,
		  byte won, int movesMade) {
	this.totalTime = totalTime;
	this.boardWidth = boardWidth;
	this.boardHeight = boardHeight;
	    
	this.whiteName = whiteName;
	this.whiteTimeLeft = whiteTimeLeft;
	    
	this.blackName = blackName;
	this.blackTimeLeft = blackTimeLeft;

	this.won = won;
	this.movesMade = movesMade;
    }

    public long totalTime() {
	return this.totalTime();
    }
       
    public int boardWidth() {
	return this.boardWidth;
    }

    public int boardHeight() {
	return this.boardHeight();
    }

    public byte sideWon() {
	return this.won;
    }

    public String packageWon() {
	switch (this.won) {
	case Konane.WHITE:
	    return whiteName;
	case Konane.BLACK:
	    return blackName;
	default:
	    return null;
	}
    }

    public int movesMade() {
	return this.movesMade;
    }

    public String whiteName() {
	return this.whiteName;
    }
 
    public long whiteTimeLeft() {
	return this.whiteTimeLeft;
    }

    public String blackName() {
	return this.blackName;
    }
 
    public long blackTimeLeft() {
	return this.blackTimeLeft;
    }
       
    public String toString() {
	return (this.boardWidth + " x " + this.boardHeight + " Board. " +
		this.totalTime + " milliseconds.\n" +
		this.whiteName + " playing WHITE. " + this.blackName + 
		" playing BLACK.\n" + packageWon() + " wins in " +
		this.movesMade + " moves.\n" +
		"WHITE time left: " + this.whiteTimeLeft + "\n" +
		"BLACK time left: " + this.blackTimeLeft + "\n");
    }
       
}
