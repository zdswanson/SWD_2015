import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.Formatter;
import java.util.Scanner;
import javax.swing.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * Creates a chess game client that allows the user to play another player on a different computer in chess
 * 
 * @author Zach Swanson, Amanda Beadle, and Zak Keesee
 * @author Deitel & Associates, Inc. and Pearson Education, Inc.
 */
public class GameClient extends JFrame implements Runnable 
{
	/**
	 * JPanel that contains square indicators that how the rest of the mob is doing
	 */
	private JPanel boardPanel; 
	/**
	 * JPanel that contains boardPanel, for easier displaying
	 */
	private JPanel panel2;
	/**
	 * Holds the timer for the amount of time the client or mob member has to answer
	 */
	private JPanel timerPanel;
	/**
	 * Container for the timer and the mob indicators
	 */
	private JPanel mobAndTimerPanel;
	/**
	 * Array of Squares that represents the mob, it will indicate how many mob members there are,
	 * and after answering the question how many mob members are left in the game.
	 */
	private Square[] mob; 
	/**
	 * JPanel that contains the question
	 */
	private JPanel questionPanel;
	/**
	 * JPanel that contains the question
	 */
	private JPanel answerPanel;
	/**
	 * JTextField that asks the question
	 */
	private JTextField question;
	/**
	 * JLabel that contains an answer one to the question being asked
	 */
	private JLabel answerOne;
	/**
	 * JLabel that contains an answer two to the question being asked
	 */
	private JLabel answerTwo;
	/**
	 * JLabel that contains an answer three to the question being asked
	 */
	private JLabel answerThree;
	/**
	 * JPanel that is the container for the first answer
	 */
	private JPanel answerOnePanel;
	/**
	 * JPanel that is the container for the first answer
	 */
	private JPanel answerTwoPanel;
	/**
	 * JPanel that is the container for the first answer
	 */
	private JPanel answerThreePanel;
	/**
	 * JPanel that contains the score board of the mob
	 */
	private JPanel mobScoreBoardPanel;
	/**
	 * JLabel that contains the leader board for mob
	 */
	private JLabel mobLabel;
	/**
	 * JPanel that contains the score board of the mob
	 */
	private JPanel mobHighScoringPanel;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topOne;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topTwo;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topThree;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topFour;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topFive;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topSix;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topSeven;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topEight;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topNine;
	/**
	 * JLabel that contains a high scoring leader in the mob
	 */
	private JLabel topTen;
	/**
	 * JLabel that contains the client's personal score
	 */
	private JLabel mobPersonalScoreLabel;
	/**
	 * Contains the client's personal score
	 */
	private JLabel personalScoreLabel;
	/**
	 * JPanel that contains the score board of the mob
	 */
	private JPanel mobPersonalScorePanel;
	/**
	 * JLabel that contains the number of mob remaining
	 */
	private JLabel mobRemaining;
	/**
	 * JLabel that contains the number of mob remaining
	 */
	private JLabel oneScore;
	/**
	 * JPanel that contains an label mobRemaining
	 */
	private JPanel mobRemainingPanel;
	/**
	 * Default string of leader board
	 */
	private String[] topTenPeople = {"???-???", "???-???", "???-???", "???-???", "???-???", "???-???", "???-???", "???-???", "???-???", "???-???" };
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
	private String serverHost; 
	/**
	 * This client's color
	 */
	private int myNumber; 
	/**
	 * initializes timer
	 */
	private Timer timer;
	/**
	 * Another container for the timer and it's label
	 */
	private JPanel timerPane;
	/**
	 * JLabel for the timer "seconds"
	 */
	private JLabel label;
	private JLabel oneLabel;
	private int counter; // the duration
    private int delay = 9; // every 1 second
    private String thyme = "";
    /**
     * ActionListener for the timer
     */
    private ActionListener action;
    /**
     * If the client has been eliminated and becomes only an observer
     */
    private boolean eliminated;
	/**
	 * Client's answer
	 */
	private int answer;
	/**
	 * Checks to make sure that the Client 
	 * can't change their answer after it has been clicked
	 */
	private boolean answered;
	/**
	 * Another boolean that helps to make sure that the Client 
	 * can't change their answer after it has been clicked
	 */
	private boolean one;
	private int oneIndex;
	private boolean eliminatedInformed;
	/**
	 * Initializes connection to server, as well as GUI
	 * 
	 * @param host		IP address of server
	 */
	public GameClient()
	{ 
		String host = JOptionPane.showInputDialog(this,"Enter IP (127.0.0.1 for localhost):","172.17.114.153");
		serverHost = host; 
		one=false;
		counter=1500;
		oneIndex=0;
		eliminated=false;
		eliminatedInformed=false;
		
		//creates the container for all of the squares for the mob
		boardPanel = new JPanel(); 
		boardPanel.setLayout(new GridLayout(10, 13, 0, 0));
		//initializes the mob
		mob = new Square[101]; 
		int i=100;
		//mob[0]=new Square(0); 		//the one
		for (i = 100; i > -1; i--) 
		{
				if(i==0){
					for(int j=0;j<21;j++){
						if(j==8){
							boardPanel.add(new JLabel("vs", JLabel.CENTER));
						}
						else{
							boardPanel.add(new JLabel(""));
						}
					}
				}
				mob[i] = new Square(i);
				boardPanel.add(mob[i]); 
				if(i==10){
					boardPanel.add(new JLabel(""));
					boardPanel.add(new JLabel(""));
				}
				//i++;
		} 

		
		//Creates the container for the mob's leader board
		mobScoreBoardPanel = new JPanel(new BorderLayout());
		//creates the outlining border for the mob's leader board
		mobScoreBoardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		//creates the container for the remaining players of the mob
		//holds the status of the game
		mobRemainingPanel = new JPanel(new GridLayout(2,1));
		oneScore = new JLabel();
		oneScore.setText("One's score: ???"); //default
		mobRemainingPanel.add(oneScore);
		mobRemaining = new JLabel();
		mobRemaining.setText("???/??? Remaining\n"); //default
		mobRemainingPanel.add(mobRemaining);
		mobRemainingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		mobScoreBoardPanel.add(mobRemainingPanel, BorderLayout.PAGE_START);
		
		//creates the middle of the leader board that will list the leaders of the mob
		mobHighScoringPanel= new JPanel(new GridLayout(11,1)); 
		mobHighScoringPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		//creating the top mob contestants JLabels
		mobLabel = new JLabel();
		topOne = new JLabel();
		topTwo = new JLabel();
		topThree = new JLabel();
		topFour = new JLabel();
		topFive = new JLabel();
		topSix = new JLabel();
		topSeven = new JLabel();
		topEight = new JLabel();
		topNine = new JLabel();
		topTen = new JLabel();
		//sets default text variables for the leaderboard
		mobLabel.setText("   Mob Leaderboard   ");
		topOne.setText("1. " + topTenPeople[0]);
		topTwo.setText("2. " + topTenPeople[1]);
		topThree.setText("3. " + topTenPeople[2]);
		topFour.setText("4. " + topTenPeople[3]);
		topFive.setText("5. " + topTenPeople[4]);
		topSix.setText("6. " + topTenPeople[5]);
		topSeven.setText("7. " + topTenPeople[6]);
		topEight.setText("8. " + topTenPeople[7]);
		topNine.setText("9. " + topTenPeople[8]);
		topTen.setText("10. " + topTenPeople[9]);
		//Adds all of the top mob contestants names, and scores to the panel that contains them
		mobHighScoringPanel.add(mobLabel);
		mobHighScoringPanel.add(topOne);
		mobHighScoringPanel.add(topTwo);
		mobHighScoringPanel.add(topThree);
		mobHighScoringPanel.add(topFour);
		mobHighScoringPanel.add(topFive);
		mobHighScoringPanel.add(topSix);
		mobHighScoringPanel.add(topSeven);
		mobHighScoringPanel.add(topEight);
		mobHighScoringPanel.add(topNine);
		mobHighScoringPanel.add(topTen);
		
		//adds center of "Mob Leader board" to the mobScoreBoardPanel
		mobScoreBoardPanel.add(mobHighScoringPanel,BorderLayout.CENTER);
		//personal score tracker for the client playing
		mobPersonalScorePanel = new JPanel(new GridLayout(2,1,0,0));
		mobPersonalScorePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		mobPersonalScoreLabel = new JLabel();
		personalScoreLabel=new JLabel(" Your Score:	 ");
		mobPersonalScorePanel.add(personalScoreLabel);
		mobPersonalScoreLabel.setText("?????????");
		mobPersonalScorePanel.add(mobPersonalScoreLabel);
		
		//adds End of "Mob Leader board" to the mobScoreBoardPanel
		mobScoreBoardPanel.add(mobPersonalScorePanel,BorderLayout.PAGE_END);
		
		//Creating the Questions and Answer Containers
		questionPanel = new JPanel(new BorderLayout());
		answerPanel = new JPanel(new GridLayout(3,1,3,3)); 
		//Creating the fields of the questions
		question = new JTextField();
	    question.setText("     ????????????????????????????????????????     "); //default
	    question.setEditable(false);
		questionPanel.add(question,BorderLayout.PAGE_START);
		
		//create a new answer panel that will contain the panel and first answer
		answerOnePanel = new JPanel(new FlowLayout()); 
		answerOnePanel.setBackground(Color.lightGray);
		answerOnePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		//create the label that will contain the answer one
		answerOne = new JLabel();
		answerOne.setText("??????????"); //default
		answerOnePanel.add(answerOne);
		answerOnePanel.addMouseListener( //MouseListener to this first answer Panel to detect user clicks on it
		new MouseAdapter() 
		{
			public void mouseReleased(MouseEvent e)
			{
				setAnswer(1); 			
			} 
		} 
		); 
		//add answer one to answer panel
		answerPanel.add(answerOnePanel, BorderLayout.SOUTH);
		
		//create a new answer panel that will contain the panel and second answer
		answerTwoPanel = new JPanel(new FlowLayout()); 
		answerTwoPanel.setBackground(Color.lightGray);
		answerTwoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		//create the label that will contain the answer two
		answerTwo = new JLabel();
		answerTwo.setText("????? ?????"); //default
		answerTwoPanel.add(answerTwo);
		answerTwoPanel.addMouseListener( //MouseListener to this second answer Panel to detect user clicks on it
				new MouseAdapter() 
				{
					public void mouseReleased(MouseEvent e)
					{
						setAnswer(2); 			
					} 
				} 
				); 
		//add answer two to answer panel
		answerPanel.add(answerTwoPanel);
		
		//create a new answer panel that will contain the panel and third answer
		answerThreePanel = new JPanel(new FlowLayout()); 
		answerThreePanel.setBackground(Color.lightGray);
		answerThreePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		//create the label that will contain the answer three
		answerThree = new JLabel();
		answerThree.setText("??? ??? ???"); //default
		answerThree.setBackground(Color.lightGray);
		answerThreePanel.add(answerThree);
		answerThreePanel.addMouseListener(	//MouseListener to this third answer Panel to detect user clicks on it
				new MouseAdapter() 
				{
					public void mouseReleased(MouseEvent e)
					{
						setAnswer(3); 			
					} 
				} 
				); 
		//add answer three to answer panel
		answerPanel.add(answerThreePanel);
		//Adds the answer panel containing all the answer panels to question panel
		questionPanel.add(answerPanel,BorderLayout.PAGE_END);
		timerPane = new JPanel(new FlowLayout()); 
		label = new JLabel("15.00 sec");
		timerPane.add(label);
		
		//Larger Container for all of the panels such as mobScoreBoardPanel, boardPanel & questionPanel
		panel2 = new JPanel(); // set up panel to contain boardPanel
		panel2.add(mobScoreBoardPanel, BorderLayout.WEST);
		mobAndTimerPanel = new JPanel(new BorderLayout());
		
		timerPanel = new JPanel(new BorderLayout());
		oneLabel=new JLabel("????",JLabel.CENTER);
		timerPanel.add(timerPane,BorderLayout.NORTH);
		timerPanel.add(oneLabel,BorderLayout.CENTER);
		timerPanel.add(questionPanel,BorderLayout.SOUTH);
		mobAndTimerPanel.add(boardPanel,BorderLayout.SOUTH);
		mobAndTimerPanel.add(timerPanel,BorderLayout.SOUTH);
		panel2.add(boardPanel, BorderLayout.EAST); // add board panel
		
		panel2.add(mobAndTimerPanel,BorderLayout.SOUTH);
		
		add(panel2, BorderLayout.CENTER); // add container panel
		
		
		//runs the timer
		action = new ActionListener()
        {   
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if(counter == 0 || answered) //stops if time runs out or if button is pressed (change button pressed to mousehandle)
                {
                    timer.stop();
                    label.setText("You have selected an answer!");
                }
                else
                {
                	double inSec = (double) counter/ 100;
                    label.setText(Double.toString(inSec));
                    counter--;
                    thyme = label.getText();
                }
            }
        };
        
        timer = new Timer(delay, action);
        timer.setInitialDelay(0);

		setSize(300, 225); 
		setVisible(true); 

		startClient();
	}
	/**
	 * sets the answer the user gives
	 * @param answer
	 */
	private void setAnswer(int ans){
		if(!answered&&!eliminated){
			if( !(myNumber == oneIndex && !one) ){
				answered=true;
				answer = ans;
				int time=(int) (Double.parseDouble(thyme)*1000);

				output.format("%d %d\n", answer,time);
				output.flush();
				//sets anyone who answered to yellow
				switch(answer){
				case 1:
					answerOnePanel.setBackground(Color.YELLOW);
					break;
				case 2:
					answerTwoPanel.setBackground(Color.YELLOW);
					break;
				case 3:
					answerThreePanel.setBackground(Color.YELLOW);
					break;
				default:
					System.out.println("ERROR\nERROR\nERROR\nERROR\nERROR\nERROR\nERROR\n");
				}
				
				counter=1500;
			}
		}
		
	}
	/**
	 * Initializes connection with server, as well as input and output, and executes a thread to run the program
	 */
	public void startClient()
	{
		try 	// connect to server and get streams
		{
			// make connection to server
			connection = new Socket(
					InetAddress.getByName(serverHost), 23666);

			// get streams for input and output
			input = new Scanner(connection.getInputStream());
			myNumber = input.nextInt(); // get player's number
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
		for(int i=0;i<=myNumber;i++){
			mob[i].updateColor(1);
		}
		while (true)			//receive and passes messages from server off to processMessage method
		{
			if (input.hasNextLine()){
				processMessage(input.nextLine());
			}
		} 
	}

	/**
	 * Processes input from server
	 * 
	 * @param message		message from server
	 */
	private void processMessage(String message)
	{
		//parses messages from the server
		Scanner messageParser=new Scanner(message);
		messageParser.useDelimiter(":");
		String command="";
		if(messageParser.hasNext()){
			command=messageParser.next();
		}
		
		//determines what command the server sent
		if(command.equals("Update")){
			//updates the mob info
			int playerNum=messageParser.nextInt();
			int newColor=messageParser.nextInt();

			if(playerNum==oneIndex){
				mob[0].updateColor(newColor);
			}
			else if(playerNum<oneIndex){
				mob[playerNum+1].updateColor(newColor);
			}
			else{
				mob[playerNum].updateColor(newColor);
			}
		}
	    else if(command.equals("Right")){
	    	//sets the square to green when right
	    	switch(answer){
			case 1:
				answerOnePanel.setBackground(Color.GREEN);
				break;
			case 2:
				answerTwoPanel.setBackground(Color.GREEN);
				break;
			case 3:
				answerThreePanel.setBackground(Color.GREEN);
				break;
			default:
				System.out.println("ERROR\nERROR\nERROR\nERROR\nERROR\nERROR\nERROR\n");
			}
		}
		else if(command.equals("Wrong")){
			//sets the square to red when wrong
			eliminated=true;
			oneLabel.setText("You have been eliminated");
			int correctAnswer=messageParser.nextInt();
			
			switch(answer){
			case 1:
				answerOnePanel.setBackground(Color.RED);
				break;
			case 2:
				answerTwoPanel.setBackground(Color.RED);
				break;
			case 3:
				answerThreePanel.setBackground(Color.RED);
				break;
			case 0:			//didn't answer
				break;
			default:
				System.out.println("ERROR\nERROR\nERROR\nERROR\nERROR\nERROR\nERROR\n");
			}
			switch(correctAnswer){
			case 1:
				answerOnePanel.setBackground(Color.GREEN);
				break;
			case 2:
				answerTwoPanel.setBackground(Color.GREEN);
				break;
			case 3:
				answerThreePanel.setBackground(Color.GREEN);
				break;
			default:
				System.out.println("ERROR\nERROR\nERROR\nERROR\nERROR\nERROR\nERROR\n");
			}
		}
		else if(command.equals("Question")){
			//sets the panels for a question asked
			answerOnePanel.setBackground(Color.lightGray);
			answerTwoPanel.setBackground(Color.lightGray);
			answerThreePanel.setBackground(Color.lightGray);
			timer=new Timer(delay,action);
			answer=0;
			answered=false;
			String q=messageParser.next();
			String[] answers=new String[3];
			for(int i=0;i<3;i++){
				answers[i]=messageParser.next();
			}
			
			question.setText(q);
			answerOne.setText(answers[0]);
			answerTwo.setText(answers[1]);
			answerThree.setText(answers[2]);
			if(eliminated&&!eliminatedInformed){
				output.format("%d %d\n", 0,0);
				output.flush();
				eliminatedInformed=true;
			}
			timer.start();
		}
		else if(command.equals("OnesTurn")){
			label.setText("One's turn");
		}
		//handles the one's turn
		else if(command.equals("One")){
			one=true;
			label.setText("One's turn");
			
			while(!answered){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			one=false;
		}
		//sets the top ten scoreboard
		else if(command.equals("Scoreboards")){
			label.setText("Waiting for next question");
			int remainingPlayers=messageParser.nextInt();
			int totalPlayers=messageParser.nextInt();
			mobRemaining.setText(remainingPlayers+"/"+totalPlayers+" Remaining\n");
			oneScore.setText("One's score: "+messageParser.nextInt());
			for(int i=0;i<10&&i<totalPlayers;i++){
				topTenPeople[i]=messageParser.next();
			}
			topOne.setText("1. " + topTenPeople[0]);
			topTwo.setText("2. " + topTenPeople[1]);
			topThree.setText("3. " + topTenPeople[2]);
			topFour.setText("4. " + topTenPeople[3]);
			topFive.setText("5. " + topTenPeople[4]);
			topSix.setText("6. " + topTenPeople[5]);
			topSeven.setText("7. " + topTenPeople[6]);
			topEight.setText("8. " + topTenPeople[7]);
			topNine.setText("9. " + topTenPeople[8]);
			topTen.setText("10. " + topTenPeople[9]);
			mobPersonalScoreLabel.setText(messageParser.next());
			
		}
		//sets up a new game, pauses
		else if(command.equals("NewGame")){
			oneIndex=messageParser.nextInt();
			eliminated=false;
			eliminatedInformed=false;
			label.setText("GAME OVER. NEW GAME WILL BEGIN SHORTLY");
			for(int i=0;i<101;i++){
				mob[i].updateColor(0);
			}
			if(oneIndex==myNumber){
				oneLabel.setText("You are the One");
			}
			else{
				oneLabel.setText("You are in the Mob");
			}
		}
		//prompts the client to enter a username
		else if(command.equals("Name")){
			String name = JOptionPane.showInputDialog(this,"Enter Username (max 12 characters):","null");
			if(name==null){
				name="null";
			}
			if(name.length()>12){
				name=name.substring(0,12);
			}
			output.format(name+"\n");
			output.flush();
			if(oneIndex==myNumber){
				oneLabel.setText("You are the One");
			}
			else{
				oneLabel.setText("You are in the Mob");
			}
		}
		else{															//should never reach this
			//do nothing
		}
		messageParser.close();
	}

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
		private Color color;
		
		/**
		 * Creates new Square with the given parameters
		 * 
		 * @param squarePiece		starting piece on square
		 * @param squareLocation	server-format location of square
		 * @param isBlack			background color of square (true if black, false if white)
		 */
		public Square(int player)
		{
			color=Color.BLACK;
		} 

		// return preferred size of Square
		/**
		 * @see javax.swing.JComponent#getPreferredSize()
		 */
		public Dimension getPreferredSize() 
		{ 
			return new Dimension(30, 30);	// return preferred size
		}

		// return minimum size of Square
		/**
		 * @see javax.swing.JComponent#getMinimumSize()
		 */
		public Dimension getMinimumSize() 
		{
			return getPreferredSize();	// return preferred size
		}

		/**
		 * updates the colors of the squares
		 * @param newColor
		 */
		public void updateColor(int newColor){
			switch(newColor){
			case 0:
				color=Color.BLACK;
				break;
			case 1:
				color=Color.BLUE;
				break;
			case 2:
				color=Color.YELLOW;
				break;
			case 3:
				color=Color.RED;
				break;
			case 4:
				color=Color.GREEN;
				break;
			case 5:
				color=Color.GRAY;
				break;
			}
			repaint();
		}
		
		/**
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.setColor(color);				//set color of square
			g.fillRect(0, 0, 29, 29); 		//draw square	
		} 
	}
}