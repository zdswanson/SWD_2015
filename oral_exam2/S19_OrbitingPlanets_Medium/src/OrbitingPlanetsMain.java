import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Contains main method to run a SolarSystem (OrbitingPlanets)
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class OrbitingPlanetsMain {
	/**
	 * This method will automatically be called when OrbitingPlanetsMain is run.
	 * 
	 * @param args command line arguments (none expected)
	 */
	public static void main(String[] args){
		JFrame frame=new JFrame("Solar System");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(1000, 1000);
	    frame.setResizable(false);
	    frame.setLayout(new BorderLayout());
	    SolarSystem sys=new SolarSystem();
	    frame.add(sys,BorderLayout.CENTER);
	    frame.setVisible(true); 
	    while(true)									//until program is terminated
        {
            sys.updateLocs();						//call solar system's updateLocs function every 10 milliseconds
            try {
				Thread.sleep(10);					
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }  
	    
	}
}