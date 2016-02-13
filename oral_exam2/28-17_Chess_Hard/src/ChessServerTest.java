// This code was originally from Fig. 28.12: TicTacToeServerTest.java
import javax.swing.JFrame;

/**
 * Creates a GUI containing a ChessServer
 * 
 * @author Zach Swanson
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 */
public class ChessServerTest
{
	/**
	 * This method will automatically be called when ChessClientTest is run.
	 * 
	 * @param args no command line args expected
	 */
	public static void main(String[] args)
	{
		ChessServer application = new ChessServer();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.execute();
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
