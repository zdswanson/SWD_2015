import java.util.ArrayList;

/**
 * This abstract class provides ability to implement a variety of games and provides almost all
 * the necessary functions 
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public abstract class Game {
	/**
	 * home team (team 1) in game
	 */
	private Team homeTeam;
	/**
	 * away team (team 2) in game
	 */
	private Team awayTeam;
	/**
	 * current period in game
	 */
	private int period;
	/**
	 * total periods in game
	 */
	private final int totalPeriods;
	/**
	 * list of all possible scoring methods in game
	 */
	private ArrayList<ScoringMethod> scoringMethods;
	/**
	 * name of periods in game
	 */
	private final String periodName;
	/**
	 * length of one period in game in format HH:MM:SS
	 */
	private final String periodLength;
	
	/**
	 * Creates new Game with the given parameters
	 * 
	 * @param home 				the home team
	 * @param away 				the away team
	 * @param totalPeriods 		number of total periods
	 * @param periodName		name of periods
	 * @param periodLength		length of periods
	 */
	Game(Team home, Team away, int totalPeriods, String periodName, String periodLength){
		homeTeam=home;
		awayTeam=away;
		period=1;
		this.totalPeriods=totalPeriods;
		this.periodName=periodName;
		this.periodLength=periodLength;
		scoringMethods=initializeScoringMethods();
	}
	
	/**
	 * Creates an ArrayList containing all possible scoring methods for this game
	 * 
	 * @return ArrayList of all possible scoring methods in game
	 */
	public abstract ArrayList<ScoringMethod> initializeScoringMethods();/*{
		//override this
		//ArrayList<ScoringMethod> scoringMethods=new ArrayList<ScoringMethod>();
		//scoringMethods.add(new ScoringMethod("Score",1));
		//return scoringMethods;
	}*/
	
	
	/**
	 * Adds points value of method used to score to total of team who scored
	 * 
	 * @param score 	scoring method used to score
	 * @param team		team that scored
	 */
	public void addScore(ScoringMethod score, Team team){
		team.addScore(score.getPoints());
		return;
	}
	
	/**
	 * Returns ArrayList of all possible scoring methods for this game
	 * 
	 * @return ArrayList of scoring methods for game
	 */
	public ArrayList<ScoringMethod> getScoringMethods(){
		return scoringMethods;
	}
	
	/**
	 * Ends period by incrementing current period by 1
	 */
	public void endPeriod(){
		period+=1;
		return;
	}
	
	/**
	 * Returns current period
	 * 
	 * @return current period
	 */
	public int getPeriod(){
		return period;
	}
	
	/**
	 * Returns name of periods in this game
	 * 
	 * @return name of period
	 */
	public String getPeriodName(){
		return periodName;
	}
	
	/**
	 * Returns total number of periods in this game
	 * 
	 * @return total number of periods
	 */
	public int getTotalPeriods(){
		return totalPeriods;
	}
	
	/**
	 * Returns length of periods in this game
	 * @return period length
	 */
	public String getPeriodLength(){
		return periodLength;
	}
	
	/**
	 * Checks if game is over
	 * 
	 * @return true if game is over, false if game is still going
	 */
	public boolean gameOver(){
		if(period>
		totalPeriods){
			return true;
		}
		return false;
	}
	
	/**
	 * Determines who won this game
	 * 
	 * @return winning team, or "Nobody" if tie
	 */
	public String getWinner(){
		if(homeTeam.getScore()>awayTeam.getScore()){
			return homeTeam.getName();
		}
		else if(awayTeam.getScore()>homeTeam.getScore()){
			return awayTeam.getName();
		}
		else{
			return "Nobody";
		}
	}
}
