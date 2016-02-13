//import java.util.Random;
import java.util.Scanner;

/**
 * Generates written check amounts for given monetary values up to $1000.00
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class CheckWriter {
	
	/**
	 * contains word equivalent of numbers up to 20 at index (number-1) (does not contain 0)
	 */
	private final static String[] onesConverter={"one","two","three","four","five","six","seven","eight","nine","ten","eleven","twelve","thirteen","fourteen","fifteen","sixteen","seventeen","eighteen","nineteen"};
	/**
	 * contains word equivalent of multiples of 10 up to 100 at index (number/10)-1 (does not contain 0)
	 */
	private final static String[] tensConverter={"ten","twenty","thirty","forty","fifty","sixty","seventy","eighty","ninety"};

	/**
	 * Main prompts user for monetary value between 0 and 1000 exclusive
	 * @param args no expected args
	 */
	public static void main(String[] args) {
		//Random rand=new Random();								
		//double value=(double) rand.nextInt(100000)/100;		//randomly generates a monetary value in range to test
		Scanner r=new Scanner(System.in);
		double value=0;
		while(value!=-1){
			System.out.print("Enter check value (-1 to quit): ");
			value=r.nextDouble();
			System.out.println(value+" = "+convert(value));
		}
	}
	
	/**
	 * Computes the written equivalent of the given monetary value
	 * 
	 * @param val monetary value to be converted
	 * @return writtenVal written equivalent of val
	 */
	public static String convert(double val){
		StringBuilder check=new StringBuilder();
		
		if(val>1000||val<=0){			//if value is invalid
			if(val==-1){				//if quit value
				return "Goodbye";
				
			}
			System.out.println("ERROR: invalid amount");		//otherwise inform user of invalid input
			return "ERROR: invalid amount";
		}		
		int wholeVal=(int) val;												//integer portion of val
		if(wholeVal/100>0){													//if val's hundreds digit is nonzero (if val>99)
			check.append(onesConverter[(wholeVal/100)-1]+" hundred ");		//append hundreds equivalent
			wholeVal-=(wholeVal/100)*100;									//remove hundreds amount from value
		}
		if(wholeVal>19){													//if remaining value is greater than 20 (because of teens special cases)
			check.append(tensConverter[(wholeVal/10)-1]+" ");				//append tens equivalent
			wholeVal-=(wholeVal/10)*10;										//remove tens amount from value
		}
		if(wholeVal>0){														//if integer portion of value is still >0
			check.append(onesConverter[(wholeVal)-1]+" ");					//append equivalent ones/teens value
			wholeVal-=(wholeVal);											//remove that amount from value
		}
		double centVal=val-(int) val;								//cent portion of val
		if((int) val>0){											//if val's integer portion was nonzero
			check.append("and ");									//append "and" for formatting purposes
		}
		else{														//otherwise append "zero and" to empty string
			check.append("zero and ");
		}
		check.append((int) Math.round(centVal*100)+"/100");			//append cents as fraction /100
		return check.toString();									//return translated string
	}

}
