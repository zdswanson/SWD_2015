// This code was originally from Fig. 28.12: TicTacToeServerTest.java
import javax.swing.JFrame;

/**
 * Creates a GUI containing a ChessServer
 * 
 * @author Zach Swanson, Amanda Beadle, Zak Keesee
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 */
public class GameServerTest
{
	/**
	 * This method will automatically be called when GameClientTest is run.
	 * 
	 * @param args no command line args expected
	 */
	public static void main(String[] args)
	{
		//creates game server
		GameServer application = new GameServer();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.executeGame();
	} 
}