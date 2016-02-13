/**
 * Contains tteam-specific information including name and score
 * 
 * @author Zach Swanson
 * @version 1.0
 *
 */
public class Team{
	/**
	 * name of this team
	 */
	private final String team;
	/**
	 * current score of this team
	 */
	private int score;
	
	/**
	 * Constructs new Team with given name
	 * @param team name of team
	 */
	Team(String team){
		this.team=team;
		score=0;
	}
	
	/**
	 * Returns name of this team
	 * @return team name
	 */
	public String getName(){
		return team;
	}
	
	/**
	 * Returns current score of this team
	 * @return current score
	 */
	public int getScore(){
		return score;
	}
	
	/**
	 * Adds given points to current total
	 * @param points amount of points to add
	 */
	public void addScore(int points){
		score+=points;
		return;
	}
}
