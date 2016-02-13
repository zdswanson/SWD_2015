import java.util.ArrayList;

/**
 * Implements the game of hockey by extending Game
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class Hockey extends Game{
	
	/**
	 * Creates new Hockey with given home and away teams
	 * 
	 * @param home 	the home team
	 * @param away	the away team
	 */
	Hockey(Team home, Team away){
		super(home,away,3,"period","20:00");
		
		
	}
	
	@Override
	public ArrayList<ScoringMethod> initializeScoringMethods(){
		ArrayList<ScoringMethod> scoringMethods=new ArrayList<ScoringMethod>();
		scoringMethods.add(new ScoringMethod("goal",1));
		return scoringMethods;
	}
}
