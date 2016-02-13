// This code was modified extensively by Zach Swanson, originally from the textbook, Fig. 28.11: TicTacToeServer.java
import java.awt.BorderLayout;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Creates a chess game server
 * 
 * @author Zach Swanson
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 */
public class ChessServer extends JFrame 
{
	/**
	 * Array of strings containing the pieces at all locations on the board in the format "(piece color)(piece type)" ie "WK" is white king
	 */
	private String[] board = new String[64]; 
	/**
	 * Array of strings containing the game state before this turn. Used to make sure the player moves out of check if they were threatened
	 */
	private String[] previousBoard=new String[64];	
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
	 * Tracks which player's move it is
	 */
	private int currentPlayer; 
	/**
	 * constant player value for first (white) player
	 */
	private final static int PLAYER_W = 0; 
	/**
	 * constant player value for second (black) player
	 */
	private final static int PLAYER_B = 1; 
	/**
	 * Array of colors at PLAYER_ value index
	 */
	private final static String[] COLORS = {"White", "Black"}; 
	/**
	 * ExecutorService that handles threads of both players
	 */
	private ExecutorService runGame; 
	/**
	 * Lock used to enforce player turns
	 */
	private Lock gameLock; 
	/**
	 * Lock condition for other player connecting
	 */
	private Condition otherPlayerConnected; 
	/**
	 * Lock condition used when trading turns
	 */
	private Condition otherPlayerTurn; // to wait for other player's turn
	/**
	 * Array containing starting order of back row pieces
	 */
	private final static String[] startingOrder={"R","N","B","Q","K","B","N","R"};
	/**
	 * Array containing various special case flag variables
	 */
	private int[] flags={1,1,1,1,-1};		//white king castle(1 possible, 0 not possible), white queen castle possible, black king castle possible, black queen castle, en passant flag (location of en passant square, otherwise -1)
	/**
	 * Array containing temporary values of flags in case move is reverted
	 */
	private int[] tempFlags=new int[5];		
	/**
	 * String representation of the piece value of a space with no piece on it
	 */
	private final static String EMPTY_PIECE="  ";
	/**
	 * Array of booleans storing whether each player is currently in check
	 */
	private boolean[] inCheck={false,false};  //0 is white, 1 is black
	
	
	/**
	 * Initializes chess server and GUI to display logs
	 */
	public ChessServer()
	{
		super("Tic-Tac-Toe Server"); 

		// create ExecutorService with a thread for each player
		runGame = Executors.newFixedThreadPool(2);
		gameLock = new ReentrantLock(); // create lock for game

		// condition variable for both players being connected
		otherPlayerConnected = gameLock.newCondition();

		// condition variable for the other player's turn
		otherPlayerTurn = gameLock.newCondition();      


		for(int i=0;i<8;i++){				//initializing board with proper pieces
			board[i]="B"+startingOrder[i];
		}
		for(int i=8;i<16;i++){
			board[i]="BP";
		}
		for(int i=16;i<48;i++){
			board[i]=EMPTY_PIECE;
		}
		for(int i=48;i<56;i++){
			board[i]="WP";
		}
		for(int i=56;i<64;i++){
			board[i]="W"+startingOrder[i-56];
		}
		board[60]="WK";
		board[59]="WQ";
		players = new Player[2]; 
		currentPlayer = PLAYER_W; 		//initializes first player to 0 (white)

		try
		{
			server = new ServerSocket(12345, 2); 	//set up ServerSocket
		} 
		catch (IOException ioException) 
		{
			ioException.printStackTrace();
			System.exit(1);
		} 

		outputArea = new JTextArea(); 
		JScrollPane pane = new JScrollPane(outputArea); 
		add(pane, BorderLayout.CENTER);
		outputArea.setText("Server awaiting connections\n");

		setSize(300, 300); 
		setVisible(true); 
	}

	/**
	 * Initializes both player connections before proceeding
	 */
	public void execute()
	{
		// wait for each client to connect
		for (int i = 0; i < players.length; i++) 
		{
			try // wait for connection, create Player, start runnable
			{
				players[i] = new Player(server.accept(), i);		//note: this line will not resolve until player i successfully connects
				runGame.execute(players[i]); 						//execute player[i] thread
			} 
			catch (IOException ioException) 
			{
				ioException.printStackTrace();
				System.exit(1);
			} 
		}
		System.out.println("before 1");
		gameLock.lock(); 							//lock game to signal player X's thread
		System.out.println("after 1");
		try
		{
			System.out.println("inside 1");
			players[PLAYER_W].setSuspended(false); 	//resume player X
			otherPlayerConnected.signal(); 			//wake up player X's thread
			System.out.println("inside after 1");
		} 
		finally
		{
			System.out.println("endf 1");
			gameLock.unlock(); 						//unlock game after signalling player X
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

	/**
	 * Determines if move is valid, and if so, implements move. Also handles enforcement of Player turns
	 * 
	 * @param fromLocation		starting location of piece
	 * @param toLocation		location piece is attempting to move to
	 * @param player			number of player attempting move
	 * @return					type of move, 0 if invalid
	 */
	public int validateAndMove(int fromLocation,int toLocation, int player)
	{
		while (player != currentPlayer){				//while not current player, must wait for turn
			gameLock.lock(); 							//lock game to wait for other player to go

			try 
			{
				otherPlayerTurn.await(); 				//wait for signal of player's turn
			} 
			catch (InterruptedException exception)
			{
				exception.printStackTrace();
			} 
			finally
			{
				gameLock.unlock(); 						//unlock game after waiting
			} 
		} 

		
		if(isOccupied(fromLocation)){					//if there is a piece at the from location
			String piece=board[fromLocation];
			
			if(isValidMoveStyle(fromLocation,toLocation,piece)){						//if it is a possible move for that type of piece
				int messageTrip=isActuallyValidMove(fromLocation,toLocation,piece);		//check if it is actually a valid move
				if(messageTrip<=0){														//if 0 or -1, move is not valid
					return messageTrip;
				}
				
				if(messageTrip>0){														//if move is >0, valid move. 
					if(piece.charAt(1)!='P'){	//only pawns can en passant, if the piece isnt a pawn clear en passant
						tempFlags[4]=-1;
					}
					board[toLocation]=piece;											//move piece to new location
					board[fromLocation]=EMPTY_PIECE;
				}																	//special case handling
				if(messageTrip==2){														//en passant take
					if(currentPlayer==0){												//remove taken pawn
						board[toLocation+8]=EMPTY_PIECE;
					}
					else{
						board[toLocation-8]=EMPTY_PIECE;
					}
				}
				else if(messageTrip==3){												//castle
					if(tempFlags[0]==2){			//white king castle
						board[63]=EMPTY_PIECE;		//move rook
						board[61]="WR";
						tempFlags[0]=0;
					}
					else if(tempFlags[1]==2){		//white queen castle
						board[56]=EMPTY_PIECE;		//move rook
						board[59]="WR";
						tempFlags[1]=0;
					}
					else if(tempFlags[2]==2){		//black king castle
						board[7]=EMPTY_PIECE;		//move rook
						board[5]="BR";
						tempFlags[2]=0;
					}
					else if(tempFlags[3]==2){		//black queen castle
						board[0]=EMPTY_PIECE;		//move rook
						board[3]="BR";
						tempFlags[3]=0;
					}
				}
				if(inCheck[currentPlayer]){											//if player was previously in check
					for(int i=0;i<64;i++){											//see if new move gets them out of check
						if(board[i].equals(COLORS[currentPlayer].charAt(0)+"K")){	//finds player's king
							if(isThreatened(i,currentPlayer)){						//if still in check
								//System.out.println("UNDO MOVE; DOESN'T RESOLVE CHECK");
								board=Arrays.copyOf(previousBoard, 64);				//undo move
								return -1;						
							}
							else{													//move resolved check
								inCheck[currentPlayer]=false;						//player no longer in check
							}
							i=64;													//exit for loop, found the piece being looked for
						}
					}
				}
				flags=Arrays.copyOf(tempFlags, 5);			//if the move is kept, update permanent flags
				
				if(messageTrip==4){													//pawn promotion
					int choice=players[currentPlayer].chooseUpgrade(toLocation);
					messageTrip=4+choice;
				}
				
				currentPlayer = (currentPlayer + 1) % 2; 										//change current player
				players[currentPlayer].otherPlayerMoved(fromLocation,toLocation,messageTrip);	// let other player know that move occurred
				
				for(int i=0;i<64;i++){												//see if this places other player in check
					if(board[i].equals(COLORS[currentPlayer].charAt(0)+"K")){		//finds other player's king
						if(isThreatened(i,currentPlayer)){							//if king is threatened, player is in check
							inCheck[currentPlayer]=true;
						}
						else{
							inCheck[currentPlayer]=false;
						}
						i=64;		//exit for loop, found the piece being looked for
					}
				}
				
				gameLock.lock(); 					//lock game to signal other player to go
				try 
				{
					otherPlayerTurn.signal(); 		//signal other player to continue
				} 
				finally
				{
					gameLock.unlock(); 				//unlock game after signaling
				} 
				return messageTrip; 				//notify player that move was valid
			}
		}
		return 0; 			//notify player that move was invalid
	}
	
	/**
	 * Determines if path between two given locations is unblocked
	 * 
	 * @param fromLoc	first location
	 * @param toLoc		second location
	 * @return			true if unblocked (possible), false if blocked
	 */
	public boolean pathClear(int fromLoc, int toLoc){
		//row * 8 + column = loc
		int fromRow = fromLoc / 8; 	
		int fromCol = fromLoc % 8; 
		int toRow = toLoc / 8;
		int toCol = toLoc % 8;
		
		if(fromRow==toRow){							//if horizontal move
			int moveDir=(fromCol>toCol ? -1 : 1);	//determines if needs to move left or right
			int i=fromCol+moveDir;					//first space beyond fromLoc
			while(i!=toCol){						//until target location is reached
				int loc=fromRow * 8 + i;				//calculate location
				if(isOccupied(loc)){					//if the location is occupied
					return false;						//the path is blocked
				}
				i+=moveDir;								//otherwise move to next space
			}
			return true;							//if no spots were occupied, return true
		}
		else if(fromCol==toCol){					//if vertical move
			int moveDir=(fromRow>toRow ? -1 : 1);
			int i=fromRow+moveDir;
			while(i!=toRow){
				int loc=i * 8 + fromCol;
				if(isOccupied(loc)){
					return false;
				}
				i+=moveDir;
			}
			return true;
		}
		else{											//if diagonal move
			int colMoveDir=(fromCol>toCol ? -1 : 1);
			int rowMoveDir=(fromRow>toRow ? -1 : 1);
			int i=fromRow+rowMoveDir;
			int j=fromCol+colMoveDir;
			while(i!=toRow&&j!=toCol){
				int loc=i * 8 + j;
				if(isOccupied(loc)){
					return false;
				}
				i+=rowMoveDir;
				j+=colMoveDir;
			}
			return true;
		}
	}

	/**
	 * Determines if the given location is threatened by any opposing pieces
	 * 
	 * @param loc		location to test
	 * @param player	player potentially being threatened
	 * @return			true if space is threatened, false otherwise
	 */
	public boolean isThreatened(int loc, int player){		//player checking if threatened for, 0 is white, 1 is black
		for(int i=0;i<64;i++){								//iterate through entire board
			if((board[i].charAt(0)=='B'&&player==0)||(board[i].charAt(0)=='W'&&player==1)){		//if opponent's piece
				if(isValidMoveStyle(i,loc,board[i])){			//if it could possibly move to this location
					switch(board[i].charAt(1)){					//determine if it can take a piece at this location
					case 'R':
					case 'B':
					case 'Q':
						if(pathClear(i,loc)){			//can take if path is clear
							return true;
						}
						break;
					case 'N':						//can always take if it can move to square
						return true;
					case 'K':
						int fromRow = i / 8; 		//row * 8 + column = loc
						int fromCol = i % 8; 
						int toRow = loc / 8;
						int toCol = loc % 8;
						int changeRow=Math.abs(fromRow-toRow);
						int changeCol=Math.abs(fromCol-toCol);
						if(!(changeRow>1||changeCol>1)){		//avoids accidentally allowing castle takes
							return true;
						}
						break;
					case 'P':
						fromRow = i / 8; 		//row * 8 + column = loc
						fromCol = i % 8; 
						toRow = loc / 8;
						toCol = loc % 8;
						changeRow=Math.abs(fromRow-toRow);
						changeCol=Math.abs(fromCol-toCol);
						if(changeRow==1||changeCol==1){			//pawn can only take diagonally
							return true;
						}
						break;
					default:										//should never reach this
						System.out.println("ERROR: Invalid piece");
					}
				}
			}
		}
		return false;					//otherwise return false
	}
	
	/**
	 * Determines if a move is actually valid, once it is determined that it is a valid move type for the given piece
	 * 
	 * @param fromLoc		starting location of piece
	 * @param toLoc			final location
	 * @param piece			piece string
	 * @return				move type, 0=invalid, 1=true, 2=en passant, 3=castle, 4-7=pawn upgrade
	 */
	public int isActuallyValidMove(int fromLoc, int toLoc,String piece){			
		tempFlags=Arrays.copyOf(flags,5);							//create temporary copy of flags to modify
		previousBoard=Arrays.copyOf(board,64);						//create copy of board in current state in case need to revert
		int fromRow = fromLoc / 8; 		//row * 8 + column = loc
		int fromCol = fromLoc % 8; 
		int toRow = toLoc / 8;
		int toCol = toLoc % 8;
		int changeRow=Math.abs(fromRow-toRow);
		int changeCol=Math.abs(fromCol-toCol);
		
		switch(piece.charAt(1)){							//switch based on piece type
		case 'R':
			if(pathClear(fromLoc,toLoc)){
				if(!(board[toLoc].charAt(0)=='W'&&currentPlayer==0)&&!(board[toLoc].charAt(0)=='B'&&currentPlayer==1)){		//if not taking own piece
					if(fromLoc==0){			//black queen side rook
						tempFlags[3]=0;			//black queen side castle no longer possible
					}
					else if(fromLoc==7){	//black king side rook
						tempFlags[2]=0;			//this castle no longer possible
					}
					else if(fromLoc==56){	//white queen side rook
						tempFlags[1]=0;			//this castle no longer possible
					}
					else if(fromLoc==63){	//white king side rook
						tempFlags[0]=0;			//this castle no longer possible
					}
					return 1;			//return valid move
				}
			}
			break;
		case 'B':
		case 'Q':
			if(pathClear(fromLoc,toLoc)){
				if(!(board[toLoc].charAt(0)=='W'&&currentPlayer==0)&&!(board[toLoc].charAt(0)=='B'&&currentPlayer==1)){
					return 1;
				}
			}
			break;
		case 'K':
			if(changeCol==2){		//castle attempt
				if(toLoc==62&&tempFlags[0]==1){		//white king-side and castle is still possible
					if(pathClear(fromLoc,63)&&!inCheck[0]&&!isThreatened(61,0)&&!isThreatened(62,0)){		//no pieces between king&rook, and not moving out of, through, or into check
						tempFlags[0]=2;				//change flag to 2 to indicate this castle happened
						tempFlags[1]=0;				//white queen castle no longer possible
						return 3;				//return castle move
					}
				}
				else if(toLoc==58&&tempFlags[1]==1){	//white queen castle and castle is still possible
					if(pathClear(fromLoc,56)&&!inCheck[0]&&!isThreatened(59,0)&&!isThreatened(58,0)){		//no pieces between king&rook, and not moving out of, through, or into check
						tempFlags[1]=2;
						tempFlags[0]=0;
						return 3;
					}
				}
				else if(toLoc==6&&tempFlags[2]==1){		//black king castle
					if(pathClear(fromLoc,7)&&!inCheck[1]&&!isThreatened(5,1)&&!isThreatened(6,1)){			//no pieces between king&rook, and not moving out of, through, or into check
						tempFlags[2]=2;
						tempFlags[3]=0;
						return 3;
					}
				}
				else if(toLoc==2&&tempFlags[3]==1){		//black queen castle
					if(pathClear(fromLoc,0)&&!inCheck[1]&&!isThreatened(3,1)&&!isThreatened(2,1)){			//no pieces between king&rook, and not moving out of, through, or into check
						tempFlags[3]=2;
						tempFlags[2]=0;
						return 3;
					}
				}
			}
			else if(!(board[toLoc].charAt(0)=='W'&&currentPlayer==0)&&!(board[toLoc].charAt(0)=='B'&&currentPlayer==1)){		//make sure not taking own piece
				if(!isThreatened(toLoc,currentPlayer)){		//can't move into check
					if(currentPlayer==0){		//if white player
						tempFlags[0]=0;				//white castles no longer possible because white king moved
						tempFlags[1]=0;
					}
					else{
						tempFlags[2]=0;
						tempFlags[3]=0;
					}
					return 1;
				}
			}
			break;
		case 'N':
			if(!(board[toLoc].charAt(0)=='W'&&currentPlayer==0)&&!(board[toLoc].charAt(0)=='B'&&currentPlayer==1)){
				return 1;
			}
			break;
		case 'P':			
			
			if(changeRow==2){						//if double first move
				if(currentPlayer==1){
					int loc=16+fromCol;				//intermediate (skipped) space location
					if(isOccupied(loc)||isOccupied(toLoc)){		//if target space and intermediate space are occupied
						return 0;									//move not possible
					}									//otherwise
					tempFlags[4]=loc;		//en passant take location, if other player wants to take this piece
					return 1;				
				}
				else{
					int loc=40+fromCol;
					if(isOccupied(loc)||isOccupied(toLoc)){
						return 0;
					}
					tempFlags[4]=loc;		//en passant take location
					return 1;
				}
			}
			else if(changeCol==0&&!isOccupied(toLoc)){				//if single forward move and space isn't occupied
				tempFlags[4]=-1;		//can't be taken en passant
				if(toRow==0||toRow==7){		//if pawn just reached other side of board
					return 4;					//pawn promotion
				}
				return 1;
			}
			else if(changeCol==1){			//if pawn take
				if((board[toLoc].charAt(0)=='W'&&currentPlayer==1)||(board[toLoc].charAt(0)=='B'&&currentPlayer==0)){
					tempFlags[4]=-1;		//can't be taken en passant
					if(toRow==0||toRow==7){		//if pawn just reached other side of board
						return 4;					//pawn promotion
					}
					return 1;
				}
				else if(toLoc==tempFlags[4]){		//en passant take
					return 2;
				}
			}
			break;
		default:			//should never reach this
			break;
		}
		return 0;			//otherwise, invalid
	}
	
	/**
	 * Determines if move is possible for given piece type
	 * 
	 * @param fromLoc		starting location of piece
	 * @param toLoc			attempted move location
	 * @param piece			value of piece
	 * @return				true if possible, false otherwise
	 */
	public boolean isValidMoveStyle(int fromLoc, int toLoc,String piece){
		int fromRow = fromLoc / 8; 
		int fromCol = fromLoc % 8; 
		int toRow = toLoc / 8;
		int toCol = toLoc % 8;
		int changeRow=Math.abs(fromRow-toRow);
		int changeCol=Math.abs(fromCol-toCol);
		
		if(fromLoc==toLoc){		//also handled client-side
			return false;
		}
		
		switch(piece.charAt(1)){		//switch based on piece type
		case 'R':		//rook
			if(fromRow==toRow||fromCol==toCol){		//linear
				return true;
			}
			break;
		case 'B':		//bishop
			if(changeRow-changeCol==0){		//diagonal
				return true;
			}
			break;
		case 'K':		//king
			if(!(changeRow>1||changeCol>1)){	//one in any direction or
				return true;
			}
			if(changeRow==0&&changeCol==2){		//castle
				if(fromRow==0||fromRow==7){			//if in end row
					return true;
				}
			}
			break;
		case 'Q':		//queen
			if(fromRow==toRow||fromCol==toCol||changeRow-changeCol==0){		//linear or diagonal
				return true;
			}
			break;
		case 'N':		//knight
			if(changeRow+changeCol==3&&changeRow<3&&changeCol<3){		//2 in one direction, 1 perpendicular
				return true;
			}
			break;
		case 'P':		//pawn
			if((piece.equals("BP")&&(toRow-fromRow==1))||(piece.equals("WP")&&(toRow-fromRow==-1))){	//if moving forward one
				if(changeCol<2){				//if forward or one diagonal
					return true;
				}
			}
			else if((fromRow==1&&piece.equals("BP")||fromRow==6&&piece.equals("WP"))&&changeRow==2&&changeCol==0){	//or if double first move 
				return true;
			}
			break;
		default:			//should never reach this
			System.out.println("\\\\\\ERROR:   NOT A VALID PIECE SOMETHING HAS GONE HORRIBLY HORRIBLY WRONG  ///ERROR");
			return false;
		}
		return false;		//otherwise false
	}
	
	/**
	 * Determines if given location has a piece on it
	 * 
	 * @param location	location to check
	 * @return			true if occupied, false if not
	 */
	public boolean isOccupied(int location)
	{
		if (board[location].equals(EMPTY_PIECE))
			return false; // location is unoccupied
		else
			return true; // location is occupied
	}

	/**
	 * Unimplemented method. If there is a checkmate, the losing player will not be able to make any valid moves and therefore the game will stop, but winner is not calculated or announced
	 * 
	 * @return	always false
	 */
	public boolean isGameOver()
	{
		/*boolean whiteWin=true;
		boolean blackWin=true;
		for(int i=0;i<64;i++){
			if(board[i]=="WK"){
				blackWin=false;
			}
			if(board[i]=="BK"){
				whiteWin=false;
			}
		}
		if(whiteWin||blackWin){
			return true;
		}*/
		return false; // this is left as an exercise
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
		 * Player's color ("White" or "Black")
		 */
		private String color; 
		/**
		 * Boolean whether thread is currently suspended
		 */
		private boolean suspended = true; 

		/**
		 * Initializes Player object and I/O
		 * 
		 * @param socket
		 * @param number
		 */
		public Player(Socket socket, int number)
		{
			playerNumber = number; // store this player's number
			color = COLORS[playerNumber]; // specify player's color
			connection = socket; // store socket for client

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

		/**
		 * Sends opponent's move to client
		 * 
		 * @param fromLocation		starting location of piece
		 * @param toLocation		final location of piece
		 * @param moveType			type of move
		 */
		public void otherPlayerMoved(int fromLocation,int toLocation, int moveType)
		{
			output.format("Opponent moved\n");
			//output.format("%d\n", location); // send location of move
			output.format("%d %d %d\n", fromLocation,toLocation, moveType);
			output.flush(); // flush output
		}
		
		/**
		 * Asks client what type of piece they would like for pawn upgrade
		 * 
		 * @param toLocation		location of pawn being upgraded
		 * @return					player's choice
		 */
		public int chooseUpgrade(int toLocation){
			output.format("Choose promotion.\n");
			output.flush(); 
			int upgrade=input.nextInt();
			input.nextLine();
			if(upgrade==-1){		//if window was closed before input, default to queen
				upgrade=0;
			}
			switch(upgrade){		//upgrade pawn to appropriate piece
		    case 0: 	//Queen
		    	board[toLocation]=color.charAt(0)+"Q";
		    	break;
		    case 1:		//Knight
		    	board[toLocation]=color.charAt(0)+"N";
		    	break;
		    case 2:		//Rook
		    	board[toLocation]=color.charAt(0)+"R";
		    	break;
		    case 3:		//Bishop
		    	board[toLocation]=color.charAt(0)+"B";
		    	break;
		    }
			return upgrade;		//return choice to pass to other player
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
				displayMessage("Player " + color + " connected\n");		//display color
				output.format("%s\n", color); 							//send client their color
				output.flush(); 

				if (playerNumber == PLAYER_W) 					//if first player
				{
					output.format("%s\n%s", "Player X connected",
							"Waiting for another player\n");
					output.flush(); // flush output
					System.out.println("before 2");
					gameLock.lock(); 							//lock game to  wait for second player
					System.out.println("after 2");
					try 
					{
						System.out.println("inside 2");
						while(suspended)
						{
							System.out.println("in while 2");
							otherPlayerConnected.await(); 				//wait for second player
							System.out.println("in while after 2");
						} 
						System.out.println("after while 2");
					}  
					catch (InterruptedException exception) 
					{
						exception.printStackTrace();
					} 
					finally
					{
						System.out.println("end 2");
						gameLock.unlock(); 						//unlock game after second player
					} 

					output.format("Other player connected. Your move.\n"); 	//inform client other player connected
					output.flush(); 
				} 
				else
				{														//else this object is second player
					output.format("Player O connected, please wait\n");		//display that second player connected
					output.flush(); 
				} 

				while (!isGameOver()) 				//while game not over. NOTE: This is an infinite loop as isGameOver is unimplemented
				{
					int fromLocation = -1; 			//initialize move locations to impossible location
					int toLocation=-1;
					int moveType=0;

					if (input.hasNext()){					//if new move
						fromLocation = input.nextInt(); 		//get move locations
						toLocation = input.nextInt();
					}
					if(fromLocation>=0&&toLocation>=0){			//if non-negative move locations (possible)
						moveType=validateAndMove(fromLocation,toLocation, playerNumber);		//check move, implement if valid, get move type
						/*for(int i=0;i<64;i++){
							if((i%8)==0){
								System.out.println();
							}
							System.out.print(board[i]+" ");
						}
						System.out.println();*/
						// check for valid move
						if (moveType>0) 			//if valid move type
						{
							displayMessage("\nfrom location: " + fromLocation+" to location: "+toLocation);		//display move in log
							output.format("Valid move.\n"); 													//notify client of valid move
							output.format("%d\n", moveType);													//send client type of move
							output.flush(); 
						} 
						else 						//move was invalid
						{
							if(moveType==-1){		//if invalid because of unresolved check
								displayMessage("\nstill in check, from location: " + fromLocation+" to location: "+toLocation);
								output.format("Doesn't resolve check, try again\n");
								output.flush(); 
							}
							else{					//if invalid for other reason
								displayMessage("\nattempted from location: " + fromLocation+" to location: "+toLocation);
								output.format("Invalid move, try again\n");
								output.flush(); // flush output
							}
						} 
					}
					
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

		/**
		 * Set suspended status of this Player
		 * 
		 * @param status whether suspended or not
		 */
		public void setSuspended(boolean status)
		{
			suspended = status; 
		}
	}
}