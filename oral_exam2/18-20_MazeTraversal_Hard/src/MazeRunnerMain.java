import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This program recursively solves 12x12 mazes
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class MazeRunnerMain {
	/**
	 * Recursively solves a hard-coded maze from a hard-coded start point
	 * 
	 * @param args 		no args expected
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Scanner r=new Scanner(new File("sampleMaze.txt"));
		char[][] maze1=new char[12][12];
		for(int y=0;y<12;y++){
			String temp=r.next();
			for(int x=0;x<12;x++){
				maze1[x][y]=temp.charAt(x);
			}
		}
		if(mazeTraversal(0,10,maze1)){
			System.out.println("\n\nMaze solved!");
		}
		else{
			System.out.println("\n\nThis maze has no solution");
		}
		
		for(int i=0;i<12;i++){			//pretties up final solution by replacing 'o's (failed paths) back to '.'
			for(int j=0;j<12;j++){
				if(maze1[j][i]=='o'){
					maze1[j][i]='.';
				}
			}
		}
		
		
		for(int i=0;i<12;i++){			//display maze
			for(int j=0;j<12;j++){
				System.out.print(maze1[j][i]);
			}
			System.out.println();
		}
	}
	
	/**
	 * Recursively solves given 12x12 maze
	 * 
	 * @param x		x-coordinate of current space
	 * @param y		y-coordinate of current space
	 * @param maze 	character array of maze being solved
	 * @return true if this is a solution of the maze, otherwise false
	 */
	public static boolean mazeTraversal(int x, int y, char[][] maze){
		maze[x][y]='X';					//set current location to visited
		System.out.println("\n");
		for(int i=0;i<12;i++){			//display maze
			for(int j=0;j<12;j++){
				System.out.print(maze[j][i]);
			}
			System.out.println();
		}
		if(x==0&&maze[1][y]=='X'||x==11&&maze[10][y]=='X'||y==0&&maze[x][1]=='X'||y==11&&maze[x][10]=='X'){	//maze is solved
			return true;
		}
		if(x==0){								//first step cases to prevent array index out of bounds (left wall)
			if(maze[x+1][y]=='.'){					//if only possible direction of movement is a possible move
				return mazeTraversal(x+1,y,maze);	//recursively call function on that coordinate
			}
			else{
				return false;						//otherwise this isn't a solveable maze
			}
		}
		if(x==11){									//right wall
			if(maze[x-1][y]=='.'){
				return mazeTraversal(x-1,y,maze);
			}
			else{
				return false;
			}
		}
		if(y==0){									//top wall
			if(maze[x][y+1]=='.'){
				return mazeTraversal(x,y+1,maze);
			}
			else{
				return false;
			}
		}
		if(y==11){									//bottom wall
			if(maze[x][y-1]=='.'){
				return mazeTraversal(x,y-1,maze);
			}
			else{
				return false;
			}
		}
		
		
		
		if(maze[x][y+1]=='.'){					//if down is a possible path
			if(mazeTraversal(x,y+1,maze)){		//recursively call function on that coordinate, if true
				return true;					//		pass on true (solved) state
			}
			else{								//else replace 'X' in space with an 'o' indicating this path
				maze[x][y+1]='o';				//has been tried and isn't a solution
			}
		}
		
		if(maze[x][y-1]=='.'){					//up
			if(mazeTraversal(x,y-1,maze)){
				return true;
			}
			else{
				maze[x][y-1]='o';
			}
		}
		
		if(maze[x+1][y]=='.'){					//right
			if(mazeTraversal(x+1,y,maze)){
				return true;
			}
			else{
				maze[x+1][y]='o';
			}
		}
		
		if(maze[x-1][y]=='.'){					//left
			if(mazeTraversal(x-1,y,maze)){
				return true;
			}
			else{
				maze[x-1][y]='o';
			}
		}
		return false;							//if none of the return trues was reached, return false
		
	}
}
