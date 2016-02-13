// This code was originally from Fig. 28.14: TicTacToeClientTest.java
import javax.swing.JFrame;

/**
 * Creates a GUI containing a ChessClient
 * 
 * @author Zach Swanson
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 */
public class ChessClientTest
{
	/**
	 * This method will automatically be called when ChessClientTest is run.
	 * 
	 * @param args IP of ChessServer, if empty defaults to 127.0.0.1
	 */
	public static void main(String[] args)
	{
		ChessClient application; // declare client application

		// if no command line args
		if (args.length == 0)
			application = new ChessClient("127.0.0.1"); // localhost
		else
			application = new ChessClient(args[0]); // use args
		application.setSize(500, 500);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	} 
}

/**************************************************************************
 * (C) Copyright 1992-2014 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
