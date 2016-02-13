import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Contains main which calculates date of Easter on a given year, then
 * calculates the number of times Easter falls on each possible date in one 
 * 5,700,000 year cycle
 * 
 * @author Zach Swanson
 * @version 1.0
 *
 */
public class ComputusMain {
	
	/**
	 * Main calculates date of Easter on a given year, calculates total number each
	 * date of Easter appears in a complete cycle
	 * @param args no expected args
	 */
	public static void main(String args[]) {
		System.out.println("Easy: ");
		Scanner r = new Scanner(System.in);
		int year;
		try{
			System.out.print("Enter year: ");
			year = r.nextInt();
			Easter e = new Easter(year);
			System.out.println(e.findDate() + ", " + year);
		}
		catch(InputMismatchException e){
			System.out.println("Invalid input (integer expected): "+e);
		}

		System.out.println("Medium: computing...");
		TreeMap<String, Integer> h = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				int sc1 = 0;
				int sc2 = 0;
				if (s1.charAt(0) == 'A') {
					sc1 += 35;
				}
				if (s2.charAt(0) == 'A') {
					sc2 += 35;
				}
				sc1 += Integer.parseInt(s1.substring(6));
				sc2 += Integer.parseInt(s2.substring(6));
				return sc1 - sc2;
			}
		});

		for (int i = 1; i <= 5700000; i++) {
			Easter f = new Easter(i);
			String date = f.findDate();
			if (h.containsKey(date)) {
				h.put(date, (h.get(date)) + 1);
			} else {
				h.put(date, 1);
			}

		}
		for (Entry<String, Integer> q : h.entrySet()) {
			System.out.println(q);
		}
		r.close();
	}
}
