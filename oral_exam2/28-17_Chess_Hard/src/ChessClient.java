// This code was modified extensively by Zach Swanson, originally from Fig. 28.13: TicTacToeClient.java
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * Creates a chess game client that allows the user to play another player on a different computer in chess
 * 
 * @author Zach Swanson
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 */
public class ChessClient extends JFrame implements Runnable 
{
	/**
	 * Displays what color this player is
	 */
	private JTextField idField; 
	/**
	 * Displays client output
	 */
	private JTextArea displayArea; 
	/**
	 * JPanel that contains chessboard
	 */
	private JPanel boardPanel; 
	/**
	 * JPanel that contains boardPanel, for easier displaying
	 */
	private JPanel panel2; 
	/**
	 * Array of Squares that represents the board
	 */
	private Square[][] board; 
	/**
	 * Location of currently selected piece
	 */
	private Square moveToSquare; 
	/**
	 * Attempted move location
	 */
	private Square moveFromSquare; 
	/**
	 * Socket connecting to server
	 */
	private Socket connection; 
	/**
	 * Scanner receiving input from server
	 */
	private Scanner input; 
	/**
	 * Formatter sending output to server
	 */
	private Formatter output; 
	/**
	 * IP of host
	 */
	private String ticTacToeHost; 
	/**
	 * This client's color
	 */
	private String myColor; 
	/**
	 * Boolean determining whether it is this client's turn
	 */
	private boolean myTurn; 
	/**
	 * Color of first client (white player)
	 */
	private final String W_COLOR = "White"; 
	/**
	 * Color of second client (black player)
	 */
	private final String B_COLOR = "Black"; // mark for second client
	/**
	 * Array containing starting order of back row pieces
	 */
	private final static String[] startingOrder={"R","N","B","Q","K","B","N","R"};
	/**
	 * String representation of the piece value of a space with no piece on it
	 */
	private final static String EMPTY_PIECE="  ";

	/**
	 * Initializes connection to server, as well as GUI
	 * 
	 * @param host		IP address of server
	 */
	public ChessClient(String host)
	{ 
		ticTacToeHost = host; 
		displayArea = new JTextArea(4, 30); 
		displayArea.setEditable(false);
		add(new JScrollPane(displayArea), BorderLayout.SOUTH);

		boardPanel = new JPanel(); 
		boardPanel.setLayout(new GridLayout(8, 8, 0, 0));

		board = new Square[8][8]; 
		int i=0;
		
		for (int row = 0; row < board.length; row++) 
		{
			
			for (int column = 0; column < board[row].length; column++) 
			{
				
				String startingPiece=EMPTY_PIECE;
				boolean isBlack;
				if((row+column)%2==0){
					isBlack=false;
				}
				else{
					isBlack=true;
				}
				if(i<8){
					startingPiece="B"+startingOrder[i];
				}
				else if(i<16){
					startingPiece="BP";
				}
				else if(i<48){
					startingPiece=EMPTY_PIECE;
				}
				else if(i<56){
					startingPiece="WP";
				}
				else if(i==59){
					startingPiece="WQ";
				}
				else if(i==60){
					startingPiece="WK";
				}
				else if(i<64){
					startingPiece="W"+startingOrder[i-56];
				}


				board[row][column] = new Square(startingPiece, row * 8 + column,isBlack);
				boardPanel.add(board[row][column]);  
				i++;
			}
		} 

		idField = new JTextField(); 
		idField.setEditable(false);
		add(idField, BorderLayout.NORTH);

		panel2 = new JPanel(); // set up panel to contain boardPanel
		panel2.add(boardPanel, BorderLayout.CENTER); // add board panel
		add(panel2, BorderLayout.CENTER); // add container panel

		setSize(300, 225); 
		setVisible(true); 

		startClient();
	}

	/**
	 * Initializes connection with server, as well as input and output, and executes a thread to run the program
	 */
	public void startClient()
	{
		try // connect to server and get streams
		{
			// make connection to server
			connection = new Socket(
					InetAddress.getByName(ticTacToeHost), 12345);

			// get streams for input and output
			input = new Scanner(connection.getInputStream());
			output = new Formatter(connection.getOutputStream());
		} 
		catch (IOException ioException)
		{
			ioException.printStackTrace();         
		} 

		// create and start worker thread for this client
		ExecutorService worker = Executors.newFixedThreadPool(1);
		worker.execute(this); // execute client
	}

	/** 
	 * Updates GUI with color, initializes myTurn boolean, recieves and processes input from the server until program is terminated
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		myColor = input.nextLine(); // get player's color

		SwingUtilities.invokeLater(
				new Runnable() 
				{         
					public void run()
					{
						// display player's color
						idField.setText("You are \"" + myColor + "\"");
					} 
				} 
				); 

		myTurn = (myColor.equals(W_COLOR)); // determine if this client's turn

		while (true)			//recieve and passes messages from server off to processMessage method
		{
			if (input.hasNextLine())
				processMessage(input.nextLine());
		} 
	}

	/**
	 * Processes input from server
	 * 
	 * @param message		message from server
	 */
	private void processMessage(String message)
	{
		if(message.equals("Choose promotion.")){				//if server is asking for promotion choice
			displayMessage("Choose promotion\n");
			String[] pieceChoices = new String[] {"Queen", "Knight", "Rook", "Bishop"};
		    int choice = JOptionPane.showOptionDialog(null, "Choose what piece you would like to upgrade to:", "Pawn Promotion",
		        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
		        null, pieceChoices, pieceChoices[0]);						//create popup with options
		    //System.out.println(choice);
		    output.format("%d \n", choice);			//send choice back to server
		    output.flush();
		}
		else if (message.equals("Valid move.")) 			//move validated by server
		{
			displayMessage("Valid move, please wait.\n");
			int moveType=input.nextInt();						//gets move type from server to react to special cases
			input.nextLine();				//clears newline
			movePiece(moveFromSquare, moveToSquare,moveType);	//calls movePiece function to implement validated move
			moveFromSquare=null;								//clear to and from for next move
			moveToSquare=null;
		} 
		else if (message.equals("Invalid move, try again")||message.equals("Doesn't resolve check, try again")) 		//if move declared invalid by server
		{
			moveFromSquare=null;								//clear to and from for new move
			moveToSquare=null;
			displayMessage(message + "\n"); 					// display invalid move
			myTurn = true; 										// repeat first down
		}  
		else if (message.equals("Opponent moved")) 								//if opponent made a valid move
		{
			int fromLocation = input.nextInt(); 						//get starting location			
			int toLocation = input.nextInt(); 							//get final location
			int moveType = input.nextInt();								//get move type
			input.nextLine(); 											//skip newline
			int fromRow = fromLocation / 8; 							//calculate starting row
			int fromColumn = fromLocation % 8; 							//calculate starting column
			int toRow = toLocation / 8; 								//calculate final row
			int toColumn = toLocation % 8; 								//calculate final column

			movePiece(board[fromRow][fromColumn],board[toRow][toColumn],moveType); //calls movePiece function to implement move
			displayMessage("Opponent moved. Your turn.\n");
			myTurn = true; 												// now this client's turn
		}  
		else															//should never reach this
			displayMessage(message + "\n"); 							//display the message
	}

	/**
	 * This function is called by movePiece after doing the initial move if the move is a special case
	 * 
	 * @param moveFrom		starting location
	 * @param moveTo		final location
	 * @param typeMove		type of special move
	 */
	private void handleSpecialMoves(Square moveFrom, Square moveTo, int typeMove){
		int toLocation=moveTo.getSquareLocation();
		int toRow = toLocation / 8; 							//calculate final row
		int toColumn = toLocation % 8; 							//calculate column
		if(typeMove==2){										//if en passant take
			board[toRow-1][toColumn].setPiece(EMPTY_PIECE);		//delete taken pawn
			board[toRow+1][toColumn].setPiece(EMPTY_PIECE);		//delete taken pawn (it has to come from one of these spaces and ends in the other one, so it's safe to always clear both)
		}
		if(typeMove==3){										//if castle
			if(toLocation==62){									//white king castle
				board[7][7].setPiece(EMPTY_PIECE);				//move rook
				board[7][5].setPiece("WR");
			}
			else if(toLocation==58){							//white queen castle
				board[7][0].setPiece(EMPTY_PIECE);				//move rook
				board[7][3].setPiece("WR");
			}
			else if(toLocation==6){								//black king castle
				board[0][7].setPiece(EMPTY_PIECE);				//move rook
				board[0][5].setPiece("BR");
			}
			else if(toLocation==2){								//black queen castle
				board[0][0].setPiece(EMPTY_PIECE);				//move rook
				board[0][3].setPiece("BR");
			}
			else{												//should never reach this
				System.out.println("ERROR: CLIENT-SIDE CASTLE ERROR");		//display error
			}
		}
		if(typeMove>=4&&typeMove<8){							//if pawn upgrade (4=queen 5=knight 6=bishop 7=rook)
			upgradePiece(moveTo,typeMove-4);					//pass off to pawn upgrade function 
		}
	}
	
	/**
	 * Passes asynchronous thread to AWT event dispatching thread that displays given message in client output window
	 * 
	 * @param messageToDisplay		message to be displayed
	 */
	private void displayMessage(final String messageToDisplay)
	{
		SwingUtilities.invokeLater(													
				new Runnable() 
				{
					public void run() 
					{
						displayArea.append(messageToDisplay); // updates output
					} 
				} 
				); 
	} 

	/**
	 * Passes asynchronous thread to AWT event dispatching thread that implements the given move on the chessboard GUI
	 * 
	 * @param moveFrom		starting location 
	 * @param moveTo		final location
	 * @param moveType		type of move
	 */
	private void movePiece(final Square moveFrom, final Square moveTo, int moveType)
	{
		SwingUtilities.invokeLater(
				new Runnable() 
				{
					public void run()
					{
						moveTo.setPiece(moveFrom.takePiece()); 					//removes piece from moveFrom and puts it in moveTo
						if(moveType>1){											//if special case
							handleSpecialMoves(moveFrom,moveTo,moveType);		//handle rest of special case
						}
					} 
				} 
				); 
	} 

	/**
	 * Handles pawn upgrade special case passed on from handleSpecialMoves
	 * 
	 * @param moveTo	location of pawn after move
	 * @param choice	choice of upgrade piece
	 */
	private void upgradePiece(Square moveTo, final int choice){
		char colorLetter=moveTo.getPiece().charAt(0);				//gets color letter for upgraded piece
		switch(choice){							//replaces pawn with chosen piece of proper color
		case 0: 								//Queen
			moveTo.setPiece(colorLetter+"Q");
			break;
		case 1:									//Knight
			moveTo.setPiece(colorLetter+"N");
			break;
		case 2:									//Rook
			moveTo.setPiece(colorLetter+"R");
			break;
		case 3:									//Bishop
			moveTo.setPiece(colorLetter+"B");
			break;
		}
	}
	/**
	 * Sends attempted move to server
	 * 
	 * @param fromLocation		initial location of piece
	 * @param toLocation		final location of piece
	 */
	public void sendMove(int fromLocation,int toLocation)
	{
		if(myTurn){												//if it is this client's turn
			output.format("%d %d\n", fromLocation, toLocation); 	//send move to server
			output.flush();
			myTurn = false; 										//other client's turn
		}
	}

	/**
	 * Sets current square for move, location of piece if first click, otherwise location to move piece to
	 * 
	 * @param square	square that was clicked
	 */
	public void setCurrentSquare(Square square)
	{	
		if(moveFromSquare==null){														//if no moveFrom selected, meaning no piece selected
			if(square.getPiece().charAt(0)==myColor.charAt(0)){							//if the selected piece is yours
				moveFromSquare=square;
				displayMessage("You selected a(n) "+square.getPiece().charAt(1)+"\n");
			}
			else{	
				displayMessage("You can only move your own pieces\n");
			}
		}
		else if(!square.equals(moveFromSquare)){										//if the attempted move is not the same square as the piece being moved
			moveToSquare=square;
			sendMove(moveFromSquare.getSquareLocation(),moveToSquare.getSquareLocation());	//send move
		}
		else{																			//otherwise reset choice and try again
			displayMessage("You can't do that, silly\n");
			moveFromSquare=null;
		}
	}





	// private inner class for the squares on the board
	/**
	 * Private inner class implementing the Squares the chessboard is made of
	 * 
	 * @author Zach Swanson
	 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
	 */
	private class Square extends JPanel 
	{
		/**
		 * String containing the piece on the square
		 */
		private String piece; // mark to be drawn in this square
		/**
		 * int containing the server-format location of the square (0-63)
		 */
		private int location; // location of square
		/**
		 * Background color of board square
		 */
		private boolean colorIsBlack;

		/**
		 * Creates new Square with the given parameters
		 * 
		 * @param squarePiece		starting piece on square
		 * @param squareLocation	server-format location of square
		 * @param isBlack			background color of square (true if black, false if white)
		 */
		public Square(String squarePiece, int squareLocation, boolean isBlack)
		{
			piece = squarePiece; 					
			location = squareLocation; 				
			colorIsBlack=isBlack;
			addMouseListener(											//add a mouseListener to this square to detect user clicks on it
					new MouseAdapter() 
					{
						public void mouseReleased(MouseEvent e)
						{
							setCurrentSquare(Square.this); 				//when the user clicks on it, call setCurrentSquare with this Square as the parameter

						} 
					} 
					); 
		} 

		// return preferred size of Square
		/**
		 * @see javax.swing.JComponent#getPreferredSize()
		 */
		public Dimension getPreferredSize() 
		{ 
			return new Dimension(30, 30); // return preferred size
		}

		// return minimum size of Square
		/**
		 * @see javax.swing.JComponent#getMinimumSize()
		 */
		public Dimension getMinimumSize() 
		{
			return getPreferredSize(); // return preferred size
		}

		/**
		 * @return piece on this square
		 */
		public String getPiece(){
			return piece;
		}

		/**
		 * Set piece on this square
		 * 
		 * @param newPiece		piece to place on this square
		 */
		public void setPiece(String newPiece) 
		{ 
			piece = newPiece; 
			repaint(); 
		}

		/**
		 * Removes piece from square
		 * 
		 * @return		String representation of piece
		 */
		public String takePiece() 
		{ 
			String tempPiece=piece;
			piece = EMPTY_PIECE; // set mark of square
			repaint(); // repaint square
			return tempPiece;
		}

		// return Square location
		/**
		 * @return server-format location of square
		 */
		public int getSquareLocation() 
		{
			return location; 
		}

		/**
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.setColor(colorIsBlack ? Color.BLACK : Color.WHITE);				//set color of square
			g.fillRect(0, 0, 29, 29); 											//draw square						
			if(!piece.equals(EMPTY_PIECE)){										//if there is a piece on the square
				g.setColor(piece.charAt(0)=='B' ? Color.BLACK : Color.WHITE);	//set piece's color
				g.fillOval(7, 7, 16, 16);										//draw piece
				g.setColor(Color.RED);											//outline piece in red to distinguish if white on white or black on black
				g.drawOval(7, 7, 16, 16);
				g.setColor(piece.charAt(0)=='B' ? Color.WHITE : Color.BLACK);	//set to opposite color of piece
			}
			g.drawString(""+piece.charAt(1), 11, 20); 							//label piece with proper letter
		} 
	}
}
