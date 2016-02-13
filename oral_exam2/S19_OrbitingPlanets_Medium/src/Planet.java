import java.awt.Point;

/**
 * Creates a Runnable Planet object that autonomously orbits around a given point at a given radius
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class Planet implements Runnable{
	/**
	 * Orbit number (for example Sun=0, Mercury=1, Venus=2, and so on)
	 */
	private final int orbit;
	/**
	 * Angular velocity in radians
	 */
	private double speed;
	/**
	 * Distance between orbits
	 */
	private static final int ORBIT_SIZE=50;
	/**
	 * Current location in space
	 */
	private Point loc; //ensures both are updated simultaneously
	/**
	 * X-coordinate of orbited point
	 */
	private int centerX;
	/**
	 * Y-coordinate of orbited point
	 */
	private int centerY;
	/**
	 * Total angular distance traveled
	 */
	private double radians;
	/**
	 * Diameter of Planet
	 */
	private int size;
	
	/**
	 * Creates new Planet
	 * 
	 * @param o			orbit number of this Planet
	 * @param width		width of window (divided by 2 to get orbited point)
	 * @param height	height of window (divided by 2 to get orbited point)
	 * @param size		diameter of planet
	 */
	public Planet(int o,int width,int height,int size){
		this.size=size;
		orbit=o*ORBIT_SIZE;
		centerX=(width/2)-(size/2);
		centerY=(height/2)-(size/2);
		radians=Math.PI*(1.5);
		loc=new Point(centerX,centerY+orbit);
		speed=Math.PI/((orbit+1)*4);
	}
	
	/**
	 * Calls update function even 10 milliseconds until thread is terminated
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(true){
			update();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds angular velocity to current angular location, calculates the new Cartesian location, and updates location
	 */
	private void update(){
		radians+=speed;
		int newX=(int) ((orbit*Math.cos(Math.PI*2*radians))+centerX);
		int newY=(int) ((orbit*Math.sin(Math.PI*2*radians))+centerY);
		loc.setLocation(newX,newY);
	}
	
	/**
	 * Returns this planet's current x-coordinate
	 * 
	 * @return x-coordinate
	 */
	public int getX(){
		return (int) loc.getX();
	}
	
	/**
	 * Returns this planet's current y-coordinate
	 * 
	 * @return y-coordinate
	 */
	public int getY(){
		return (int) loc.getY();
	}
	
	/**
	 * Returns diameter of this planet
	 * 
	 * @return diameter of planet
	 */
	public int getSize(){
		return size;
	}
}
