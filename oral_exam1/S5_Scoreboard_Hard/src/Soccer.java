import java.util.ArrayList;

/**
 * Implements the game of soccer by extending Game
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class Soccer extends Game{
	
	/**
	 * Creates new Soccer with given home and away teams
	 * 
	 * @param home 	the home team
	 * @param away	the away team
	 */
	Soccer(Team home, Team away){
		super(home,away,2,"half","45:00");
		
		
	}
	
	@Override
	public ArrayList<ScoringMethod> initializeScoringMethods(){
		ArrayList<ScoringMethod> scoringMethods=new ArrayList<ScoringMethod>();
		scoringMethods.add(new ScoringMethod("goal",1));
		return scoringMethods;
	}
}
