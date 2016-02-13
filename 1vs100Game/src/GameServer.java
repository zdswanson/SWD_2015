// This code was modified extensively by Zach Swanson, originally from the textbook, Fig. 28.11: TicTacToeServer.java
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Creates a Game game server
 * 
 * @author Zach Swanson, Amanda Beadle, Zak Keesee
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 */
/**
 * @author Invicti
 *
 */
public class GameServer extends JFrame 
{	
	/**
	 * Text area used to output each attempted move sent to the server
	 */
	private JTextArea outputArea; 
	/**
	 * Array containing Player objects representing each player
	 */
	private Player[] players; 
	/**
	 * server socket to connect with clients
	 */
	private ServerSocket server; 
	/**
	 * ExecutorService that handles threads of both players
	 */
	private ExecutorService runGame; 
	/**
	 * trip variable to start and stop game server
	 */
	private boolean start;
	/**
	 * integer value of correct answer to current question
	 */
	private int correctAnswer;
	/**
	 * integer value of total number of active players
	 */
	private int totalPlayers;
	/**
	 * ArrayList holding all questions
	 */
	private ArrayList<String> questions = new ArrayList<>();
	/**
	 * Button used to initiate and end game from server side
	 */
	private final JButton startButton;
	/**
	 * boolean representing whether current round is over
	 */
	private boolean gameOver;
	/**
	 * integer value of remaining mob members
	 */
	private int remainingPlayers;
	/**
	 * array representing scoreboard
	 */
	private int[][] scoreboard;
	/**
	 * The One's current score
	 */
	private int onesScore;
	/**
	 * Array used to convert percent of mob remaining into One's score
	 */
	private int[] oneScoreConverter={0,1000,5000,10000,25000,50000,75000,100000,250000,500000,1000000};
	/**
	 * current One's player index
	 */
	private int oneIndex;
	/**
	 * Initializes Game server and GUI to display logs
	 */
	private boolean finalGameOver;
	public GameServer()
	{
		super("1 vs. 100"); 
		start=false;
		gameOver=false;
		finalGameOver=false;
		remainingPlayers=1;
		oneIndex=0;
		
		//gets the questions
		getQuestions();
		// create ExecutorService with a thread for each possible player
		runGame = Executors.newFixedThreadPool(101);
		players = new Player[101]; 

		try
		{
			server = new ServerSocket(23666, 101); 	//set up ServerSocket
		} 
		catch (IOException ioException) 
		{
			ioException.printStackTrace();
			System.exit(1);
		} 

		startButton=new JButton("Start");
		startButton.addActionListener(											//add an ActionListener to this button to detect user clicks on it
					new ActionListener() 
					{
						@Override
						public void actionPerformed(ActionEvent e) {
							if(start){
								start=false;
								startButton.setText("Start");
							}
							else{
								start=true;
								startButton.setText("Stop");
							}
						} 
					} 
					);
		
		outputArea = new JTextArea(); 							//gui's output area
		JScrollPane pane = new JScrollPane(outputArea); 
		add(pane, BorderLayout.CENTER);
		add(startButton, BorderLayout.SOUTH);
		outputArea.setText("Server awaiting connections\n");
		setSize(300, 300); 
		setVisible(true);
		
	}
	
	public void executeGame(){
		totalPlayers=0;
		try {
			server.setSoTimeout(1000);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		while(totalPlayers < 101&&start==false) 			//while game isn't full and game hasnt been started by server
		{
			try // wait for connection, create Player, start runnable
			{
				try{
					players[totalPlayers] = new Player(server.accept(), totalPlayers);		//note: this line will not resolve until player i successfully connects or server.accept times out
					runGame.execute(players[totalPlayers]); 						//execute player[i] thread
					totalPlayers++;														
				}
				catch(SocketTimeoutException e){		//ignore this
				}
			} 
			catch (IOException ioException) 
			{
				ioException.printStackTrace();
				System.exit(1);
			}
		}
		scoreboard=new int[totalPlayers-1][2];			
		remainingPlayers=totalPlayers-1;			//initialize remaining mob members
		if(start==false){							//if game filled up
			while(!start){							//wait for server start command to continue
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		int qNum=0;			//question number in array
		while(start){							//while server hasn't clicked "stop" button
			while(!isGameOver()){					//until current game ends
				pushMessage("Question:"+questions.get(qNum));		//starts round by sending new question to all clients
				correctAnswer=Integer.parseInt(""+questions.get(qNum).charAt(questions.get(qNum).length()-1));		//find correct answer from question string
				
				if(qNum+2>questions.size()){		//if all questions have been asked
					qNum=0;								//reset to beginning (shhhh)
				}
				else{
					qNum++;							//otherwise next question
				}
				try {
					Thread.sleep(15000);						//wait for answers
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				players[oneIndex].sendMessage("One\n");		//inform one it's their turn
				for(int i=0;i<totalPlayers;i++){				//inform everyone else it's the one's turn
					if(i!=oneIndex){
						players[i].sendMessage("OnesTurn\n");
					}
				}
				while(players[oneIndex].awaitingAnswer()){		//while waiting for the One's answer
					try {
						Thread.sleep(100);					//check back every 10th of a second
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				checkAnswers();								//check all answers
				players[oneIndex].resetAwaitingAnswer();	//reset this for later
				updateScoreboard();							//update scoreboard
				try {
					Thread.sleep(5000);						//give clients 5 seconds to see how everyone did and check scoreboard
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				resetColors();								//reset board from green/red back to blue/grey
			}
			displayMessage("Game over\n");				//display game over to server gui			
			resetGame();								//reset everything for next game
		}
		displayMessage("End program.");					//inform server gui final game has ended
		finalGameOver=true;								//used to shut off all Player threads
	}
	/**
	 * This function pushes color updates to all clients to update green squares to blue and red to grey
	 */
	public void resetColors(){							
		for(int i=0;i<totalPlayers;i++){		//to all players
			if(!players[i].isEliminated()){		//if the player isnt eliminated (answered last right/is green rn)
				pushColorChange(i,1);			//make blue
			}
			else{
				pushColorChange(i,5);			//otherwise make grey
			}
		}
	}
	
	public void updateScoreboard(){							//updates all client scoreboards
		int offset=0;										//used to skip the One
		for(int i=0;i<totalPlayers;i++){					
			if(i!=oneIndex){									//if this player isnt the One
				scoreboard[i-offset][1]=i;						//add player number to next spot in scoreboard
				scoreboard[i-offset][0]=players[i].getScore();	//add this player's score
			}
			else{
				offset++;										//otherwise skip One
			}
		}
		Arrays.sort(scoreboard, new Comparator<int[]>(){		//custom comparator used to sort scoreboard array by score while keeping score-player pairings
			@Override
			public int compare(int[] x, int[] y){
				int x0=x[0];
				int y0=y[0];
				return(y0-x0);
			}
		});
		
		onesScore=oneScoreConverter[(10-((int)(10*((double)remainingPlayers)/((double)totalPlayers-1))))];		//calculate % of mob eliminated and determines One's score based on %
		
		
		String scoreboardMsg="Scoreboards:"+remainingPlayers+":"+(totalPlayers-1)+":"+onesScore;				//begins concatenation of scoreboard update message
		for(int i=0;i<10&&i<totalPlayers-1;i++){
			scoreboardMsg+=":"+players[scoreboard[i][1]].getName()+"-"+scoreboard[i][0];						//adds top ten or all if <10 player-score pairings 
		}
		for(int i=0;i<totalPlayers;i++){						//sends general scoreboard update message to each Player to add their own score
			players[i].sendScoreboardUpdate(scoreboardMsg);
		}
		
	}
	
	public void pushColorChange(int player,int newColor){		//pushes color change to given player to every client
		for(int z=0;z<totalPlayers;z++){
			players[z].updateSquareColor(player,newColor);
		}
		
	}
	
	public void pushMessage(String message){					//pushes given message to all clients
		for(int z=0;z<totalPlayers;z++){
			players[z].sendMessage(message);
		}
	}
	
	public void checkAnswers(){									//checks everyone's answer
		for(int z=0;z<totalPlayers;z++){
			if(!players[z].isEliminated()){						//if they're eliminated, ignore their answer
				players[z].checkAnswer();
			}
		}
	}

	/**
	 * Passes asynchronous thread to AWT event dispatching thread that displays given message in server log window
	 * 
	 * @param messageToDisplay	message to display
	 */
	private void displayMessage(final String messageToDisplay)
	{
		SwingUtilities.invokeLater(						
				new Runnable() 
				{
					public void run() // updates outputArea
					{
						outputArea.append(messageToDisplay); // add message
					} 
				} 
				); 
	} 

	private void resetGame(){										//resets everything between games
		int tempLoc=scoreboard[0][1];				//gets player number of highest score player
		for(int i=0;i<totalPlayers-1;i++){							//finds highest scoring player who isn't eliminated, otherwise it defaults to the highest scoring player set above
			if(!players[scoreboard[i][1]].isEliminated()){
				tempLoc=scoreboard[i][1];			//highest scoring player who hasn't been eliminated
				i=110;
			}
		}
		
		oneIndex=tempLoc;						//make this player the new One
		pushMessage("NewGame:"+oneIndex);		//tell everyone
		try {
			Thread.sleep(7500);					//give everyone some time between games
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		remainingPlayers=totalPlayers-1;		//reset remaining mob players
		
		for(int i=0;i<totalPlayers;i++){	//for all players
			pushColorChange(i,1);				//reset squares to blue
			players[i].resetScore();			//resets scores to 0
			players[i].resetEliminated();		//resets eliminated state
			players[i].resetAwaitingAnswer();	//resets awaiting answer
		}
		gameOver=false;				//resets game having ended
		updateScoreboard();			//clear scoreboard
	}
	
	/**
	 * Determines if game has ended
	 * 
	 * @return	true if game is over, false if not
	 */
	public boolean isGameOver()
	{
		
		if(remainingPlayers==0){		//if all mob is eliminated
			gameOver=true;
		}
		if(players[oneIndex].isEliminated()){		//if one is eliminated
			gameOver=true;
		}
		return gameOver; 
	}
	
	/**
	 * gets the questions from a file and shuffles them
	 */
	private void getQuestions(){
		String data = "";
		
		try {
			//reads file
			FileReader file = new FileReader("questions.txt");
			BufferedReader br = new BufferedReader(file);
   	 		while((data = br.readLine()) != null){
   	 			questions.add(data);
   	 		}
   	 		file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		//shuffles file
		Collections.shuffle(questions);
	}

	// private inner class Player manages each Player as a runnable
	/**
	 * Player-specific Runnable object that maintains connection and handles I/O to it's client 
	 * 
	 * @author Zach Swanson
	 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
	 */
	private class Player implements Runnable 
	{
		/**
		 * Socket connecting to client
		 */
		private Socket connection; 
		/**
		 * Scanner receiving input from client
		 */
		private Scanner input; 
		/**
		 * Formatter sending output to client
		 */
		private Formatter output; 
		/**
		 * Player number of this Player object
		 */
		private int playerNumber; 
		/**
		 * Boolean whether thread is currently suspended
		 */
		private boolean awaitingAnswer; 
		/**
		 * this player's answer
		 */
		private int answer;
		/**
		 * whether this player has been eliminated this game
		 */
		private boolean eliminated;
		/**
		 * score of this player
		 */
		private int score;
		/**
		 * name of this player
		 */
		private String name;
		/**
		 * points from last round
		 */
		private int tempPoints;

		/**
		 * Initializes Player object and I/O
		 * 
		 * @param socket
		 * @param number
		 */
		public Player(Socket socket, int number)
		{
			playerNumber = number; // store this player's number
			connection = socket; // store socket for client
			answer=0;	
			eliminated=false;
			awaitingAnswer=true;
			score=0;
			name="";
			tempPoints=0;

			try // obtain streams from Socket
			{
				input = new Scanner(connection.getInputStream());
				output = new Formatter(connection.getOutputStream());
			} 
			catch (IOException ioException) 
			{
				ioException.printStackTrace();
				System.exit(1);
			} 
		}

		
		
		public void updateSquareColor(int player,int newColor){		//sends square color update to client
			output.format("Update:%d:%d\n", player,newColor);
			output.flush();
		}
		
		public void addScore(int points){
			score+=points;
		}
		
		public void resetScore(){
			score=0;
		}
		
		public int getScore(){
			return score;
		}
		
		public void sendMessage(String message){
			output.format(message+"\n");
			output.flush();
		}
		
		public void checkAnswer(){				//checks answer of this client and informs client of verdict
			if(answer==correctAnswer){
				addScore(tempPoints);
				output.format("Right\n");													//send client type of move
				output.flush(); 
				pushColorChange(playerNumber,4);
			}
			else{
				output.format("Wrong:%d\n",correctAnswer);
				output.flush();
				eliminated=true;
				if(playerNumber!=oneIndex){
					remainingPlayers--;
				}
				pushColorChange(playerNumber,3);
			}
			answer=0;
			tempPoints=0;
		}
		

		/** 
		 * Runs thread for this Player
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run()
		{
			try 
			{
				displayMessage("Player " + playerNumber + " connected\n");		//display color
				output.format("%d\n",playerNumber); 							//complete handshake
				output.flush(); 
				pushColorChange(playerNumber,1);		//change color to blue/active
				
				output.format("Name\n");		//ask for name
				output.flush();
				while(!input.hasNext());		//wait for response
				name=input.next();				//get name
				
				
				
				while(!finalGameOver){			//while server has not ended games
					while (!eliminated) 				//while game not over. 
					{
						int tempAnswer = 0; 			//initialize move locations to impossible location
						int points = 0;
						if (input.hasNext()){					//if new move. NOTE: this is a blocking call
							tempAnswer = input.nextInt(); 		//get move locations
							points=input.nextInt();
						}
						if(tempAnswer>=1&&!eliminated){			//if non-negative move locations (possible)
							pushColorChange(playerNumber,2);
							awaitingAnswer=false;
							
							// check for valid move
							answer=tempAnswer;
							tempPoints=points;
						}
					} 
					while(!isGameOver()){				//if this player has been eliminated, wait until the game is over
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					eliminated=false;
				}
			} 
			finally
			{
				try
				{
					connection.close(); 	//close connection to client
				} 
				catch (IOException ioException) 
				{
					ioException.printStackTrace();
					System.exit(1);
				} 
			} 
		}
		public boolean awaitingAnswer(){
			return awaitingAnswer;
		}
		
		public boolean isEliminated(){
			return eliminated;
		}
		
		public void resetEliminated(){
			eliminated=false;
			
		}
		
		public void resetAwaitingAnswer(){
			awaitingAnswer=true;
		}
		
		public String getName(){
			return name;
		}
		
		public void sendScoreboardUpdate(String msg){		//concatenate this client's place and score to scoreboard message and send to client
			if(playerNumber==oneIndex){
				sendMessage(msg+":You're the one, silly");
			}
			else{
				int place=totalPlayers;
				for(int i=0;i<totalPlayers-1;i++){
					if(scoreboard[i][1]==playerNumber){
						place=i+1;
						i=101;
					}
				}
				sendMessage(msg+":"+place+". "+name+"-"+score);
			}
		}
	}
}