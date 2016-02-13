import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * Creates a GUI that implements a basic calculator
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class Calculator extends JFrame implements ActionListener {
	/**
	 * contains an array of all buttons on calculator
	 */
	private final JButton[] buttons;
	/**
	 * contains an array of all labels for buttons on calculator
	 */
	private static final String[] numbers={"7","8","9","/","4","5","6","*","1","2","3","-","0",".","=","+"};
    /**
     * holds all buttons on calculator
     */
    private final JPanel calculatorPanel;
    /**
     * displays calculator output
     */
    private final JTextField outputBox;
    /**
     * newly input number
     */
    private double curNum;
    /**
     * previous number, such as 1 in 1+2, or previous result
     */
    private double previousNum;
    /**
     * contains the most recent operation performed
     */
    private String operation;
    /**
     * flag variable to make sure there is only one decimal in any number
     */
    private boolean decTrip;
    /**
     * holds newly inputted numbers while they are being entered
     */
    private String inputString;
    /**
     * flag variable to allow proper behavior directly following an equals operation
     */
    private boolean baseTrip;
    
    /**
     * Creates new Calculator
     */
    public Calculator(){
    	super("Calculator");
    	baseTrip=true;
    	operation="";
    	inputString="";
    	calculatorPanel=new JPanel();
    	calculatorPanel.setLayout(new GridLayout(4,4,5,5)); 
    	buttons = new JButton[16];
    	for (int i = 0; i < 16; i++)
        {
           buttons[i] = new JButton(numbers[i]);
           buttons[i].addActionListener(this); // register listener
           calculatorPanel.add(buttons[i]); // add button to JFrame
        }
    	outputBox = new JTextField();
        outputBox.setEditable(false); 
        Font f = new Font("MS UI Gothic", Font.PLAIN, 40);
        outputBox.setFont(f);
        outputBox.setHorizontalAlignment(JTextField.RIGHT);
        add(outputBox, BorderLayout.NORTH);
    	add(calculatorPanel, BorderLayout.CENTER);
    }
    
    /**
     * Handles all button pushes and implements calculator logic
     * 
     * @param event the event that triggered the ActionHandler
     */
    @Override
    public void actionPerformed(ActionEvent event)
    { 
    	System.out.println(event.getActionCommand());
    	String input=event.getActionCommand();
    	switch(input){
    	case "0":
    	case "1":
    	case "2":
    	case "3":
    	case "4":
    	case "5":
    	case "6":
    	case "7":
    	case "8":
    	case "9":
    		inputString+=input;
    		outputBox.setText(inputString);
    		break;
    	case ".":
    		if(!decTrip){
    			inputString+=input;
    			decTrip=true;
    		}
    		break;
    		
    	case "+":
    	case "-":
    	case "/":
    	case "*":
    		if(inputString.equals(".")){
    			inputString="";
    			decTrip=false;
    		}
    		if(baseTrip){
    			if(!inputString.equals("")){
    				//System.out.println(inputString+" "+inputString.length());
    				curNum=Double.parseDouble(inputString);
    			}
    			operation=input;
    			inputString="";
    			decTrip=false;
    			baseTrip=false;
    		}
    		else{
    			if(!inputString.equals("")){
    				previousNum=Double.parseDouble(inputString);
    				curNum=executeOperation(operation,curNum,previousNum);
	    			inputString="";
	    			decTrip=false;
	    		}
    			operation=input;
    		}
    		outputBox.setText(""+curNum);
			break;
    	case "=":
    		if(inputString.equals(".")){
    			inputString="";
    			decTrip=false;
    		}
    		if(baseTrip){
    			if(inputString.equals("")){
    				if(operation!=""){
    					curNum=executeOperation(operation,curNum,previousNum);
    				}
    			}
    			else{
    				curNum=Double.parseDouble(inputString);
    				operation="";
    			}
    			
    		}
    		else{
    			if(!inputString.equals("")){
	    			previousNum=Double.parseDouble(inputString);
	    			curNum=executeOperation(operation,curNum,previousNum);
	    			baseTrip=true;
    			}
    		}
    		inputString="";
    		outputBox.setText(""+curNum);
    		break;
    	
    	}
    }

    /**
     * Performed desired operation on given numbers
     * 
     * @param operation 	operation to be performed
     * @param curNum 		second number in operation
     * @param prevNum 		first number in operation
     * @return				resulting value of desired operation
     */
    private double executeOperation(String operation, double curNum, double prevNum){
    	switch(operation){
		case "+":
			curNum+=previousNum;
			break;
		case "-":
			curNum-=previousNum;
			break;
		case "/":
			curNum/=previousNum;
			break;
		case "*":
			curNum*=previousNum;
			break;
		}
    	return curNum;
    }
}
