
/**
 * Calculates date of Easter on a given year
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class Easter {
	/**
	 * the year for this object
	 */
	private int year;
	/**
	 * computus algorithm intermediate variable
	 */
	private int a;
	/**
	 * computus algorithm intermediate variable
	 */
	private int b;
	/**
	 * computus algorithm intermediate variable
	 */
	private int c;
	/**
	 * computus algorithm intermediate variable
	 */
	private int d;
	/**
	 * computus algorithm intermediate variable
	 */
	private int e;
	/**
	 * computus algorithm intermediate variable
	 */
	private int f;
	/**
	 * computus algorithm intermediate variable
	 */
	private int g;
	/**
	 * computus algorithm intermediate variable
	 */
	private int h;
	/**
	 * computus algorithm intermediate variable
	 */
	private int i;
	/**
	 * computus algorithm intermediate variable
	 */
	private int k;
	/**
	 * computus algorithm intermediate variable
	 */
	private int l;
	/**
	 * computus algorithm intermediate variable
	 */
	private int m;
	/**
	 * the month of Easter in number form
	 */
	private int month;
	/**
	 * the day of Easter
	 */
	private int day;
	/**
	 * the concatenated date of Easter
	 */
	private String date;
	/**
	 * the month name in word form
	 */
	private String monthName;
	
	/**
	 * Constructs a new Easter with the specified year
	 * @param year the year to calculate the date of Easter
	 */
	Easter(int year){
		this.year=year;
	}
	/**
	 * Calculates the date of Easter using algorithm specified at 
	 * <a href="url">https://en.wikipedia.org/wiki/Computus#Meeus_Julian_algorithm</a>
	 * for this year.
	 * 
	 * @return date the string date of Easter on the year 
	 */
	public String findDate(){	//Algorithm taken from https://en.wikipedia.org/wiki/Computus#Meeus_Julian_algorithm
		a=year%19;
		b=(int) Math.floor(year/100);
		c=year%100;
		d=(int) Math.floor(b/4);
		e=b%4;
		f=(int) Math.floor((b+8)/25);
		g=(int) Math.floor((b-f+1)/3);
		h=(((19*a)+b-d-g+15)%30);
		i=(int) Math.floor(c/4);
		k=c%4;
		l=((32+(2*e)+(2*i)-h-k)%7);
		m=(int) Math.floor((a+(11*h)+(22*l))/451);
		month=(int) Math.floor((h+l-(7*m)+114)/31);
		day=((h+l-(7*m)+114)%31)+1;
		
		if(month==3){
			monthName="March";
		}
		else if(month==4){
			monthName="April";
		}
		else{
			monthName="ERROR";
		}
			
		date=""+monthName+" "+day;
		return date;
	}
}
