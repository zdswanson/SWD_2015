/**
 * Contains all necessary information for a scoring method
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class ScoringMethod{
	/**
	 * name of this scoring method
	 */
	private final String name;
	/**
	 * number of points gained by this scoring method
	 */
	private final int points;
	
	
	/**
	 * Creates new ScoringMethod with given name worth the given number of points
	 * 
	 * @param name		name of this scoring method
	 * @param points	number of points this scoring method is worth
	 */
	ScoringMethod(String name, int points){
		this.points=points;
		this.name=name;
	}
	/**
	 * Returns number of points gained by this scoring method
	 * 
	 * @return	points gained
	 */
	public int getPoints(){
		return points;
	}
	
	/**
	 * Returns name of scoring method
	 * 
	 * @return name of scoring method
	 */
	public String getName(){
		return name;
	}
}
