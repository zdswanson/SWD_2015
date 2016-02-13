import javax.swing.JFrame;
/**
 * Contains main method to run a ColorChooser
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class ColorChooserMain {
	/**
	 * This method will automatically be called when ColorChooserMain is run.
	 * 
	 * @param args command line arguments (none expected)
	 */
	public static void main(String[] args){
		JFrame frame=new JFrame("MyColorChooser");
		ColorChooser colorChooser = new ColorChooser(); 
		frame.add(colorChooser);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(500, 400); 
	    frame.setVisible(true); 
	}
}