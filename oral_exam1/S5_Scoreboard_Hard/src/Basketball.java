import java.util.ArrayList;

/**
 * Implements the game of basketball by extending Game
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class Basketball extends Game{
	
	/**
	 * Creates new Basketball with given home and away teams
	 * 
	 * @param home 	the home team
	 * @param away	the away team
	 */
	Basketball(Team home, Team away){
		super(home,away,2,"quarter","12:00");
		
		
	}
	
	@Override
	public ArrayList<ScoringMethod> initializeScoringMethods(){
		ArrayList<ScoringMethod> scoringMethods=new ArrayList<ScoringMethod>();
		scoringMethods.add(new ScoringMethod("three-pointer",3));
		scoringMethods.add(new ScoringMethod("two-pointer",2));
		scoringMethods.add(new ScoringMethod("free throw",1));
		return scoringMethods;
	}
}
