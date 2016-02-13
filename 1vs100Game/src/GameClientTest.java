// This code was originally from Fig. 28.14: TicTacToeClientTest.java
import javax.swing.JFrame;

/**
 * Creates a GUI containing a GameClient
 * 
 * @author Zach Swanson, Amanda Beadle, and Zak Keesee
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 */
public class GameClientTest
{
	/**
	 * This method will automatically be called when GameClientTest is run.
	 * 
	 * @param args IP of GameServer, if empty defaults to 127.0.0.1
	 */
	public static void main(String[] args)
	{
		GameClient application; // declare client application
		// if no command line args
		application = new GameClient(); // localhost
		application.setSize(650, 510);
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	} 
}