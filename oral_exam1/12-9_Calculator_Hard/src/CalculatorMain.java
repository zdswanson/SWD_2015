import javax.swing.JFrame;
/**
 * Contains main method to run a Calculator
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class CalculatorMain {
	/**
	 * This method will automatically be called when ArabicToRomanGUIMain is run.
	 * 
	 * @param args command line arguments (none expected)
	 */
	public static void main(String[] args){
		Calculator calculator = new Calculator(); 
	    calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    calculator.setSize(500, 400); 
	    calculator.setVisible(true); 
	}
}
