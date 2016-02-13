import java.util.ArrayList;

/**
 * Implements the game of football by extending Game
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class Football extends Game{
	
	/**
	 * Creates new Football with given home and away teams
	 * 
	 * @param home 	the home team
	 * @param away	the away team
	 */
	Football(Team home, Team away){
		super(home,away,4,"quarter","15:00");
		
		
	}
	
	
	//I don't need a javadoc here?
	@Override
	public ArrayList<ScoringMethod> initializeScoringMethods(){
		ArrayList<ScoringMethod> scoringMethods=new ArrayList<ScoringMethod>();
		scoringMethods.add(new ScoringMethod("touchdown",6));
		scoringMethods.add(new ScoringMethod("field goal",3));
		scoringMethods.add(new ScoringMethod("extra-point",1));
		scoringMethods.add(new ScoringMethod("two-point conversion",2));
		scoringMethods.add(new ScoringMethod("safety",2));
		return scoringMethods;
	}
}
