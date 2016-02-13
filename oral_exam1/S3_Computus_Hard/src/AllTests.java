import static org.junit.Assert.assertArrayEquals;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.junit.Test;

/**
 * JUnit test suite verifying calculated Easter dates from 2000-2099 inclusive
 * @author Zach Swanson
 * @version 1.0
 *
 */
public class AllTests {
	/**
	 * Tests calculated Easter dates from 2000-2099 compared to the dates found here
	 * <a href="url">https://www.census.gov/srd/www/genhol/easter500.html</a>
	 * 
	 * @throws FileNotFoundException if file was not found at specificed location
	 */
	@Test
	public void testThings() throws FileNotFoundException{
		/**
		 * this scanner is used to read in the expected dates from a text file
		 */
		Scanner r=new Scanner(new File("easters2000-2099.txt"));  //https://www.census.gov/srd/www/genhol/easter500.html
		/**
		 * stores expected Easter dates
		 */
		String[] expected=new String[100];
		/**
		 * this int is a counter
		 */
		int i=0;
		while(r.hasNextLine()){
			expected[i]=r.nextLine();
			i++;
		}
		/**
		 * stores calculated easter values
		 */
		String[] actual=new String[100];
		for(int x=2000;x<2100;x++){
			Easter e=new Easter(x);
			actual[x-2000]=e.findDate();
		}
		
		
		assertArrayEquals(expected, actual);
		r.close();
	}
}
