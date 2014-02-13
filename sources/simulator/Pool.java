package simulator;

import java.io.*;
import java.util.*;

public class Pool {

    private Parser parser;
    private Vector results = new Vector();
 
    public Pool(String file) {
	try {
	    this.parser = new Parser(new File(file));
	} catch (IOException e) {
	    System.out.println (e.toString());
	}

    }	

    /** generate a random dimensioned integer **/
    private int dimension() {
        int size = (int) ( 5 * Math.random());
        return (7 +size);
    }


    /** plays two packages against one another 3 times, varying
     * board dimensions and starting package
     **/
    private void playTwoInPool(String player1, String player2) {
	String p1, p2;
	boolean even;
	Simulator sim;
	Result result;
	Vector v = new Vector();
	for (int i = 0; i < 3; i ++) {
	    even = ( i != 1);
	    if (even) {
		p1 = player1;
		p2 = player2;
	    } else {
		p1 = player2;
		p2 = player1;
	    }
	    System.out.println();
	    System.out.println("------------- New Game -------------");
	    System.out.println();
	    sim = new Simulator( this.dimension(), this.dimension(),
				 120000, p1, p2);
	    result = sim.playGame();
	    this.results.addElement(result);
	    v.addElement(result);
	    this.sleep(1000);
	}
	this.sleep(5000);
	System.out.println("----------- Head-to-Head Results ------------");
	for (int i = 0; i < v.size(); i++) 
	    System.out.println(v.elementAt(i).toString());
 
    }
	 
    private void sleep(long  millis) {
	try {
	    Thread.sleep(millis);
	} catch (InterruptedException e) {
	}
    }
    /** @returns a Vector containing Simulator.Results **/
    public Vector run() {

	while (this.parser.hasMoreLines()) {
	    Parser.Line players = this.parser.nextLine();
	    this.playTwoInPool(players.stringAt(0), players.stringAt(1));
	}
	
	//System.out.println("----------- Pool Results ------------");
	//for (int i = 0; i < this.results.size(); i++) 
	//    System.out.println(this.results.elementAt(i).toString());
	
	return this.results;
    }   
	    

    /** main demonstrates usage of the pool; it requires a file containing 
	pairings
    **/
    public static void main(String[] args) {
	if (args.length == 0)
	    return;

	Pool p = new Pool(args[0]);

	p.run();
    }
    

    
}




