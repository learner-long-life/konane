README file for MIT IEEE IAP Programming Contest 2001
Revision 7, 25 January 2001

_______________________________________________________________________________
ChangeLog

Rev. 1 (7 Jan 2001) -  Initial release
Rev. 2 (8 Jan 2001) -  Changed use of Athena 1.1.7 JDK to 1.3.0 JDK
                         (Section 1).
                       Added simulator sources/classes and konaneCommon sources
                         to the directory structure (Section 2).
                       Added instructions for running the simulator 
                         (Section 4).
                       Clarified submission procedures. (Section 4).
Rev. 3 (13 Jan 2001) - Code changes:
                         Move.java - Constructor handles null comments.
                         BoardGrid.java - copy() method added to clone a board.
                           New constructor added to accept a grid of tokens.
                         Simulator.java - WhitePlayerThread and 
                           BlackPlayerThread merged into PlayerThread.
                           Timing moved to PlayerThread.run() to be more
                             accurate.
                         PlayerClassLoader.java - New class to make player 
                             loading more backward compatible.
                       Added reference to Robert Ragno's cksum port to Win32.
                         (Section 1).
                       Added PlayerClassLoader.java to directory structure
                         (Section 2).
                       Clarified allowedTime parameter in Player.makeMove
                         method (Section 3).                       
                       Added new section describing the technical details of
                         the tournament. (Section 5).
                       All files converted to UNIX-mode text files.
Rev. 4 (17 Jan 2001) - Code changes:
                         BoardGrid.java - empty constructor added.
                           width and height are protected, tokens is public.
                       Moved PlayerClassLoader.java to konaneCommon from
                           simulator package (Section 2).
                       Added ServerThread.java and ClientThread.java to the
                         directory structure (Section 2).
                       Added question about Sun's Solaris JVM 1.3 (Section 5).
Rev. 5 (19 Jan 2001) - Revised submission format (Section 4).
Rev. 6 (24 Jan 2001) - Code changes:
                         Simulator.java - replaced busy-wait thread with
                           wait/notifyAll mechanism.
                         ClientThread.java/ServerThread.java - minor fixed to
                           remove redundancies, incorporated changes from
                           Simulator.java.
                       Compiler? But I... - changes to the compilers used in
                         the tournament (Sections 1 and 4).
                       Added Pool.java, Parser.java, and Result.java to
                         simulator package and directory structure (Section 2).
                       Modified FAQs to address hardware, time limits, board
                         sizes, simulator bug, and tournament conditions
                         (Section 6).
Rev. 7 (25 Jan 2001) - Augmented submission format (Section 4).
                       Changed board size specifications (Section 5).
_______________________________________________________________________________

0. What is this README file?
1. What software tools do I need?
2. What is the default directory structure?
3. How do I write my solution?
4. How do I compile, test, and submit my solution?
5. What are the technical details of the tournament?
6. Where can I find help?

_______________________________________________________________________________
0. What is this README file?

This file contains a description of the files and directories provided to
you in this contest, instructions for developing and submitting
your solution, and pointers to other useful resources.

Note the revision number and revision date at the top of this document, as
contest files are subject to change. You should check the contest website at

http://web.mit.edu/ieee/iap/www

for the most recent revision of this README file as well as contest deadlines,
tournament rules, and other administrative matters.

_______________________________________________________________________________
1. What software tools do I need?

The programming contest will be conducted entirely in the Java language.
Teams are free to use any development platform, but their solutions will
be expected to compile and run with the latest Java Development Kit (JDK)
and Java Runtime Enviroment (JRE) on Athena (v1.3.0); specifically, we will
be using IBM's high-performance Java compiler called jikes with Sun's 1.3
runtime libraries located in <jdkpath>/jre/lib/rt.jar to compile contest
solutions.

All solutions that compile using Sun's javac compiler will also
compile with jikes, but more quickly and efficiently. More information
regarding jikes can be found at
http://oss.software.ibm.com/developerworks/opensource/jikes/project.
We are also considering using IBM's JVM on Linux for the tournament, pending
more thorough testing. None of these decisions, however, will affect your
current development with Sun tools.

Also, the cksum utility in GNU textutils will be used to calculate
checksums of each team's source code. Hence, it is recommended that contestants
use Athena workstations for development if possible. To use JDK 1.3.0 on
Athena instead of the default 1.1.7, type the following at the shell prompt:

     athena% add java
     athena% setenv PATH /mit/java_v1.3.0/bin:$PATH

To use the jikes compiler, type the following at the shell prompt:

     athena% add jikes

and add the following to your classpath:

     /mit/java_v1.3.0/jre/rt.jar

The contest organizers recommend that contestants develop in either Linux,
Solaris, and Microsoft Windows. Sun's JDK and JRE can be downloaded for
Windows and Solaris from http://java.sun.com. The GNU Compiler for Java
is available for Linux at http://sources.redhat.com/java; if you use GCJ,
be sure to generate platform-independent class files.

The source for textutils (which contains cksum), and other GNU tools
can be downloaded from ftp://aeneas.mit.edu in the pub/gnu directory and
compiled in a POSIX-compliant environment. In Microsoft operating systems,
Robert Ragno's port of cksum to Win32 is available from
http://web.mit.edu/ieee/iap/www/cksum.exe. If you prefer to work in a 
POSIX-compliant environment within Windows, including standard GNU tools like
make, cvs, and cksum, we recommend the free Cygwin environment from Redhat
at http://sources.redhat.com/cygwin/.

_______________________________________________________________________________
2. What is the default directory structure?

The directory structure of the contest files is shown below with a brief
description of each item. Unjarring the file konane.jar will produce this
directory structure rooted at the "konane" directory. Note that you do not
have to use this directory structure; however, the rest of the examples in
this file will refer to this default structure.

 konane/                              Top-level working directory.
   |
   |--README                          This file.
   |
   |--specifications.html             A copy of the online specifications,
   |                                  including contest deadlines and
   |                                  tournament rules.
   |
   |--classes/                        Class file directory, contains bytecode
   |    |                             output of compilation by package
   |    |
   |    |--konaneCommon/              Common contest package
   |    |  |                          See the HTML documentation for details.
   |    |  |
   |    |  |--BoardGrid.class         Class representing a Konane gameboard.
   |    |  |--Konane.class            Container class for constants.
   |    |  |--Move.class              Class representing a move on a gameboard.
   |    |  |--Player.class	      Class representing an individual player.
   |    |  ---PlayerClassLoader.class Class that loads a player.
   |    |
   |    |--reference/                 Reference implementation package
   |    |    |
   |    |    ---Player.class          Reference player extending
   |    |                             konaneCommon.Player.
   |    |
   |    |--simulator/                 Simulator class files for playing one
   |    |                             implementantion against another.
   |    |
   |    ---<your_team_package>/       Class directory for your team package.
   |          |                       This directory does not exist initially,
   |          |                       but will be created by the Java compiler
   |          |                       the first time you compile your sources.
   |          |                       See Section 3 of this README file
   |          |                       for details.
   |          |
   |          |--Player.class         Your player class extending 
   |          |                       konaneCommon.Player. This is the entry
   |          |                       point for your code and is required.
   |          |                       See Section 3 of this README file for
   |          |                       details.
   |          |
   |          ---<...>                Any other classes that you define.
   |
   |--docs/                           Base directory for HTML documentation.
   |    |
   |    ---index.html                 Top-level index file for documental.
   |
   ---sources/                        Contains Java source files by package.
        |
        |--konaneServer/              Package directory for the game server.
        |    |
        |    ---ServerThread.java     Source file for the game server thread.
        |
        |--konaneClient/              Package directory for the game client.
        |    |
        |    ---ClientThread.java     Source file for the game client thread.
        |
        |--konaneCommon/              Package directory for the common
        |   |                         Konane classes. See the HTML
        |   |                         documentation for details.
        |   |
        |   |--BoardGrid.java         Source file for a Konane gameboard.
        |   |--Konane.java            Source file for Konane constants.
        |   |--Move.java              Source file for a move on the gameboard.
        |   |--Player.java            Source file for a Konane player.
        |   ---PlayerClassLoader.java Source file for player class loader.
        |
        |--reference/                 Package directory for reference
        |    |                        implementation sources.
        |    |
        |    ---Player.java           Source file for reference player
        |                             implementation.
        |
        |--simulator/                 Package directory for the simulator to
        |   |                         test one implementation against another.
        |   |
        |   |--Simulator.java         Source file for simulator classes.
        |   |--Parser.java            Parser class for pool-matchup text file.
        |   |--Pool.java              Class encapsulating a preliminary pool.
        |   ---Result.java            Result of a game.
        |
        ---<your_team_package>/       Source directory for your team package.
              |                       You need to create this directory
              |                       yourself and give it the package name
              |                       that you submitted to the contest
              |                       organizers at 6370@mit.edu. See Section 3
              |                       of this README file for details.
              |
              |--Player.java          Source file for your player class
              |                       extending konaneCommon.Player. See
              |                       Section 3 of this README file
              |
              ---<...>                Any additional packages, directories,
                                      or source files.

_______________________________________________________________________________
3. How do I write my solution?

   a) First you must choose a unique name for your team and your team's
      package. The name should be of the form teamname_xx, where teamname
      is a string of 22 or less alphanumeric characters (A-Z, a-z, 0-9), and
      xx is your assigned team number.

      Your team's contact person should submit this name to the contest
      organizers at 6370@mit.edu as soon as possible. In our examples, we'll
      use the name honig_22. Consult the reference implementation in
      konane/sources/reference for an example.

   b) Create a directory in konane/sources named honig_22. All of your
      source files should be kept in this directory and should have the .java
      filename extension.

   c) One of your source files must be named Player.java and define the public
      class Player in package honig_22 that extends konaneCommon.Player. The
      class declaration should be begin with:

      public class Player extends konaneCommon.Player {

   d) You should override the Player.makeMove method which takes in a
      gameboard and a time limit in milliseconds. See the HTML documentation
      and the reference implementation for details on how to use the
      konaneCommon classes, especially the convenience methods in the
      BoardGrid class. The Player.makeMove method is the entry point into your
      code. The time limit represents the total time remaining for *all*
      moves; it is up to the Player how to allocate this time among its moves.
      Each successive call to Player.makeMove will receive smaller and smaller
      values of allowedTime as it is decremented by the times of previous
      moves.

   e) Define any other classes or packages and create any additional
      subdirectories you need in the honig_22 directory. You should not
      spawn any new threads of execution in any of your code; in general,
      this means none of your classes should extend java.lang.Thread or
      implement the Runnable interface.

   Review this checklist to make sure that all your code complies with the
   contest specifications:

      1) The team submission should define a package with a unique name that
         they should submit to the contest organizers at 6370@mit.edu.
      2) The team package should include a class named Player that extends
         konaneCommon.Player.
      3) The Player class should override the Player.makeMove method, which is
         the entry point into team-written code.
      4) The team-written source code should not define any new threads of
         execution.
      5) The team-written source code should not interface with non-Java code,
         compiled native libraries, precompiled Java bytecode, or any Java
         code not written by the team explicitly for this particular contest.
         Specifically, use of Java's Reflection classes, class loading,
         file system operations, or network operations will not be possible.
      6) Team submissions should consist of team-written source code in a
         jar file named after the team and a checksum of the source files.
         See the accompany specifications.html file or
	 http://web.mit.edu/ieee/iap/www for submission deadlines.
         See Section 4 for details on preparing your submissions.

_______________________________________________________________________________
4. How do I compile, test, and submit my solution?

Make sure your JDK binaries, and GNU cksum are in your path. If you are using
Athena, this is the case by default. Type the corresponding commands in you
shell prompt from within the "konane" top-level directory.

The following instructions assume you have used the default directory
structure described in Section 1. If you use a different structure or have
multiple packages or subdirectories, adjust the instruction accordingly.

To compile your player, type:

      javac -d classes -classpath classes sources/honig_22/*.java

To compile the konaneCommon package, type:

      javac -d classes -classpath classes sources/konaneCommon/*.java

To compile the reference implementation, type:

      javac -d classes -classpath classes sources/reference/*.java

To compile the simulator, type:

      javac -d classes -classpath classes sources/simulator/*.java

If you would like to use the jikes compiler, replaced every instance of
'javac' above with 'jikes', without the single quotes. Also add the following
path to your classpath:

      /mit/java_v1.3.0/jre/lib/rt.jar

Note that you do not have to use the jikes compiler; every solution that
compiles with Sun's javac will also compile with jikes using the same
runtime libraries. However, jikes will be the compiler used to compile your
solutions for the tournament, and you may find it faster and easier to use
than the Sun compiler.

To test any two players against each other, type the following and follow
the onscreen prompts:

      java -cp classes simulator.Simulator <width> <height> <time> \
          <white_package> <black_package>

      where <width> is the desired number of columns in your board,
      <height> is the desired number of rows, <time> is the total time
      for each player in milliseconds, <white_package> is the name of the
      package of the white player, and <black_package> is the name of the
      package of the black player. You may wish to redirect the simulator's
      standard out stream to a log file for later analysis.

Note that unlike previous versions of the simulator, from Revision 6 onwards,
the Simulator class is the basis for the actual game engine, although client
and server functions will be separated for efficiency and security.

To pack your solution into a jar file for submission, type:

      jar cvf honig_22.jar -C sources/honig_22 .

Note that the trailing '.' means the current directory, not the end of a
sentence :)

To generate the checksums of your source files, type:

      cksum sources/honig_22/*.java > honig_22.cksum

There are *two* submission deadlines, which both follow the same guidelines
listed below. The preliminary submissions have already passed. Check the
contest website at http://web.mit.edu/ieee/iap/www for the most recent
schedule.

Your submission consists of two files:
  1) your jar file (honig_22.jar), which contains only your team's source code.
     You should jar all files in the directory structure rooted in your
     package directory (sources/honig_22).
  2) your checksum file (honig_22.cksum), which is a text file containing the
     output of the GNU cksum command on all source files in your jar file.

Your submission will be complete when you mail a copy these files to
6370@mit.edu. The subject of the e-mail should be
"6.370 E-MAIL SUBMISSION: TEAM XX".

We also reccomend that you place the jar file in your Public directory at the
same time as you mail us your submission; and mail us this URL along with your
jar and checksum file. Do not modify the jar file after this. This will help us
immensely in error recovery.

Please observe the following guidelines strictly.

1) DO NOT SUBMIT COMPILED BYTECODE. We do not want to measure compiler
   differences. Nor do we want to run out of locker space.
2) Mail only ONE jar file and ONE checksum file per team to 6370@mit.edu.
3) Make sure your mail client identifies your jar file as a binary file and
   not a text file. This is the main cause for corrupt jar files received
   as submissions.
4) Ensure that your jar file unjars to produce the correct directory hierarchy.
   Your jar file should unjar to produce:
       Player.java
       <OtherClass.java>*
       <sub-packages>*
   Do not include enclosing directories..

You will be notified when your submission has been tested against our 
specifications.

_______________________________________________________________________________
5. What are the technical details of the tournament?

   Q) Why is the reference player so slow on Athena JDK 1.3?
   A) This problem was due to the busywait loop used in Simulator and the
      combination of the Solaris 1.3 JVM and Solaris's lack of preemptive
      multitasking. The Simulator (and ClientThread) now both use a wait/
      notifyAll mechanism. Thanks to Chris Peikert for pointing this out.

   Q) What hardware will be used to run my Player?
   A) Your players will be run within client JVMs on the Dell Linux machines
      found in many Athena clusters. You may use these machines to test your
      players under tournament conditions.

   Q) What size will the board be during the tournament?
   A) As described in the online specs, the board size for the prelims was a 
      randomly chosen rectangle with width and height between 7 and 14 units,
      inclusive.

      For the elimination rounds, The board size will be a randomly
      chosen rectangle with a width between 10 to 15 columns and a height
      between 10 to 15 rows, inclusive, with the caveat that odd x odd boards
      are disallowed. 

      You may choose to write your algorithm
      to scale well for any board size, or you may choose to optimize it for 
      certain board sizes. Currently we are going to vary the board size from
      game to game during the tournament.

      Thanks to Prof. Ernst, Jacky Mallett, Siddique Khan and Egon Pasztor.

   Q) How long will each Player's time limit be during the tournament?
   A) The time limit will be on the order of 2-5 minutes, or about 222222
      milliseconds.

   Q) Will my Player be able to save state, and if so, how much?
   A) Yes, each Player is loaded and instantiated at the beginning of each
      game and remains persistent throughout the game. The JVM hosting your
      Player will be running with a limited heap size on the order of 32MB;
      all your state must be handled in RAM, as operations on the local file
      system or over a network will not be allowed.

   Q) When will the source code for the game engine be released?
   A) The full source for the game engine and graphics routines will probably
      not be ready for public release until after the tournament is over.
      The relevant classes of the game engine (dealing with player loading
      and timing) are currently being released in konane.jar.

_______________________________________________________________________________
6. Where can I find help?

If you have a technical question about these specifications, first see
if your question can be answered by this README file or by the online
specifications. Check the 6.370 contest website for the most recent
information and revision of this README file.

If you have an administrative question about the contest, go to the
website directly:

http://web.mit.edu/ieee/iap/www

If your questions are still unanswered, e-mail the contest organizers at
6370@mit.edu.

There is also a joint mailing list of both teams and organizers at
6370-discuss@mit.edu. You can add yourself to this list in Athena by typing:

athena% blanche 6370-discuss -a <username>

The contest organizers assume you have a basic knowledge of Java and/or
have reference materials to consult. If you have a question about the Java
programming language, you might want to try Sun's Java forums and
tutorials at:

http://java.sun.com

_______________________________________________________________________________


