/** 
 * Parser.java <br>
 * Konane Game System <br>
 * MIT IEEE/ACM IAP Programming Competition 2001 <br>
 * Emina Torlak, emina@mit.edu <br>
 **/
package simulator;
import java.io.*;
import java.util.*;

/**
 * Class Parser parses a newline/whitespace delimited text file of strings
 * The strings on a line should be separated by a whitespace and the lines separated
 * by a newline.  E.g. 
 *   white1 black1 <br>
 *   white2 black2 <br>
 * There should be no extraneous empty lines.
 **/
public final class Parser {
    private Line current;
    
    /** 
     * @requires file != null
     * @effects constructs a new Parser object 
     * @throws IOException if <file> is either an invalid filename, an
     *         unreadable file or an empty file
     **/
    public Parser(File file) throws IOException	{
	if (file.canRead()) 
	    { 
		current = new Line(new Vector(),null);
		parseFile(new BufferedReader(new FileReader(file)));
	    }
	else
	    throw new IOException("File " + file.getAbsolutePath() + " cannot be parsed.");
    }
    
    private Line makeLine(String names) {
	StringTokenizer tokenizer = new StringTokenizer(names," ",false);
	Vector v = new Vector();
	
	while (tokenizer.hasMoreTokens()) {
	    v.addElement(tokenizer.nextToken());
	}
	
	return new Line(v,null);
    }
    
    private void parseFile(BufferedReader reader) throws IOException {
	Line temp1, temp2;
	String names = "";
	int line = 0;
	
	if ((names = reader.readLine()) != null) 
	    {
		line++;
		try 
		    { 
			temp1 = makeLine(names); 
			current.next = temp1; 
		    }
		catch (IllegalArgumentException e) 
		    { 
			throw new IOException("Error on line " + line + " (" + names + "): " + e.getMessage());
		    };
		
		while ((names = reader.readLine()) != null) 
		    {	
			line++;
			try
			    {
				temp2 = makeLine(names);
				temp1.next = temp2;
				temp1 = temp2;
			    }
			catch (IllegalArgumentException e) 
			    { 
				throw new IOException("Error on line " + line + " (" + names + "): " + e.getMessage());
			    };
		    }
	    }
	
	reader.close();
    }
    
    /** @return true if there are more Lines; false otherwise **/
    public boolean hasMoreLines() {
	return current.hasNext(); }
    
    /** 
     * @return the next Line
     * @throws NoSuchElementException if there are no more Lines left
     **/
    public Line nextLine() {
	if (current.hasNext()) 
	    {
		current = current.next;
		return current;
	    }
	else
	    throw new NoSuchElementException();
    }
    
    /** 
     * Class Line is a simple record class that packages the strings on a line <br>
     * @specfield strings : <String>* // strings on a line
     * @endspec	 
     **/
    public static class Line {
	public final Vector items;

	Line next;
	
	Line(Vector v, Line next) {
	    this.items = v;
	    this.next = next;
	}	
	
	public String stringAt(int index) {
	    return (String) this.items.elementAt(index);
	}

	/** @return true if there are more lines after this; otherwise returns false. **/
	boolean hasNext() {
	    if (next == null)
		return false;
	    else	
		return true;
	}
    }
    
    /** main demonstrates usage of the parser; it requires a text file with atleast two strings per line **/
    public static void main(String[] args) {
	if (args.length == 0)
	    return;
	try
	    {
		Parser parser = new Parser(new File(args[0]));
		
		while (parser.hasMoreLines())
		    {
			Parser.Line Line = parser.nextLine();
			System.out.println(Line.stringAt(0) + " vs. " + Line.stringAt(1));
		    }
	    }
	catch (IOException e) 
	    {
		e.printStackTrace();
	    }
    }
    
}

