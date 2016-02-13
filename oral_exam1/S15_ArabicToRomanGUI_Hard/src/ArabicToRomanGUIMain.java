import javax.swing.JFrame;

/**
 * Contains main method to run an ArabicToRomanGUI
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class ArabicToRomanGUIMain {
	
	/**
	 * This method will automatically be called when ArabicToRomanGUIMain is run.
	 * 
	 * @param args command line arguments (none expected)
	 */
	public static void main(String[] args){
		ArabicToRomanGUI gui=new ArabicToRomanGUI();
	    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    gui.setSize(500, 200); 
	    gui.setResizable(false);
	    gui.setVisible(true); 
	}
}
