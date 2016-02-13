import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Implements a menu and scoreboards for a variety of games
 * 
 * @author Zach Swanson
 * @version 1.0
 *
 */
public class Scoreboard {
	/**
	 * Creates and runs a scoreboard for a variety of games
	 * 
	 * @param args command line arguments (none expected)
	 */
	public static void main(String args[]){
		Scanner r=new Scanner(System.in);
		int trip=0;
		boolean err=true;
		Game game=null;
		while(err){
			try{
				System.out.print("\nSelect Sport (by number): \n"
						+ "  1)   Football\n"
						+ "  2)   Soccer\n"
						+ "  3)   Basketball\n"
						+ "  4)   Hockey\n"
						+ "Enter choice: ");
				trip=r.nextInt();
				r.nextLine();
				if(trip>4||trip<1){
					throw new IndexOutOfBoundsException();
				}
				err=false;
			}
			catch(IndexOutOfBoundsException e){
				System.out.println("Invalid Option, try again");
				//r.nextLine();
			}
			catch(InputMismatchException e){
				System.out.println("Please enter the menu item number");
				r.nextLine();
			}
		}
		
		
		if(trip>0){
			System.out.println("ENTER TEAMS");
			System.out.print("Home: ");
			Team team1=new Team(r.nextLine());
			System.out.print("Away: ");
			Team team2=new Team(r.nextLine());
			System.out.println();
			
			switch(trip){
				case 1:
					game=new Football(team1,team2);
					break;
				case 2:
					game=new Soccer(team1,team2);
					break;
				case 3:
					game=new Basketball(team1,team2);
					break;
				case 4:
					game=new Hockey(team1,team2);
					break;
			}
			
			while(!game.gameOver()){
				err=true;
				int menuCount=0;
				while(err){
					menuCount=1;
					//PRINT MENU
					try{
						
						ArrayList<ScoringMethod> scoringMethods=game.getScoringMethods();
						/* Displays dynamically generated scoreboard containing all available scoring methods for both teams */
						
						System.out.println(team1.getName()+" - "+team1.getScore()+", "+team2.getName()+" - "+team2.getScore());
						System.out.println("Current "+game.getPeriodName()+": "+game.getPeriod());
						
						System.out.println("Menu: ");
						for(int i=0;i<(game.getScoringMethods().size()	);i++){
							System.out.println("  "+menuCount+") "+team1.getName()+" "+game.getScoringMethods().get(i).getName());
							menuCount++;
						}
						for(int i=0;i<(game.getScoringMethods().size());i++){
							System.out.println("  "+menuCount+") "+team2.getName()+" "+game.getScoringMethods().get(i).getName());
							menuCount++;
						}
						System.out.println("  "+menuCount+") End "+game.getPeriodName());
						
						System.out.print("Enter choice: ");
						
						trip=r.nextInt();
						
						if(trip>((scoringMethods.size()*2)+1)||trip<1){
							throw new IndexOutOfBoundsException();
						}
						
						err=false;
					}
					catch(IndexOutOfBoundsException e){
						System.out.println("Invalid Option, try again "+e);
						r.nextLine();
					}
					catch(InputMismatchException e){
						System.out.println("Please enter the menu item number");
						r.nextLine();
					}
				}
				
				if(trip==menuCount){
					game.endPeriod();
				}
				else if(trip>((menuCount-1)/2)){
					trip-=(menuCount-1)/2;
					team2.addScore(game.getScoringMethods().get(trip-1).getPoints());
				}
				else{
					team1.addScore(game.getScoringMethods().get(trip-1).getPoints());
				}
				
				
			}
			System.out.println("\n\nGame is over.");
			System.out.println("Final Score: "+team1.getName()+" - "+team1.getScore()+", "+team2.getName()+" - "+team2.getScore());
			String winner=game.getWinner();
			if(winner.equals("Nobody")){
				System.out.println("Tie");
				//System.out.println(winner+" wins :(");
			}
			else{
				System.out.println(winner+" wins!");
			}
		}
		r.close();
	}
}
