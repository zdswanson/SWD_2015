import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * Creates a solar system of up to 8 independently rotating planets around a central sun
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class SolarSystem extends JPanel{
	/**
	 * Array containing all planets for easy painting, including sun
	 */
	private Planet[] planets;
	/**
	 * Number of planets, also used while adding planets to give differing orbits
	 */
	private int count;
	/**
	 * ExecutorService used to handle individual Planet threads
	 */
	private ExecutorService executor;
	/**
	 * Array containing colors to paint each planet
	 */
	private static final Color[] planetColors={Color.YELLOW,Color.RED,Color.ORANGE,Color.WHITE,Color.GREEN,Color.BLUE,Color.MAGENTA,Color.PINK,Color.CYAN};
	
	/**
	 * Constructor, creates a solar system with a sun and up to 8 planets
	 */
	public SolarSystem(){
		executor=Executors.newCachedThreadPool();
		addMouseListener(
	            new MouseAdapter()
	            {
	                @Override
	                public void mouseClicked(MouseEvent e)						//Adds a planet to the next orbit when user clicks, up to 8 planets
	                {
	            		if(count<9){
	            			planets[count]=new Planet(count,1000,1000,20);		//creates new Planet in next orbit
	            			executor.execute(planets[count]);					//creates thread to run new Planet
	            			count++;
	            		}}});
		
		planets=new Planet[9];
		//System.out.println(getWidth()+" "+getHeight());
		planets[0]=new Planet(0,1000,1000,30);
		executor.execute(planets[0]);							//creates thread for sun
		count=1;									//sun is considered planet 0 for simplicity
		setBackground(Color.BLACK);
	}
	
	/**
	 * Repaints solar system with current Planet locations
	 */
	public void updateLocs(){
		repaint();
	}
	
	
	/**
	 * Iterates through all planets, gets their current locations, and displays them
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		for(int i=0;i<count&&i<9;i++){
			g.setColor(planetColors[i]);
			g.fillOval(planets[i].getX(), planets[i].getY(), planets[i].getSize(), planets[i].getSize());
		}
		
	}
}
