package mainPackage;

import java.util.HashMap;

/**
 * Automatically decodes the given barcode to UPC format
 * 
 * @author Zach Swanson
 * @version 1.0
 *
 */
public class UPC_Decode {
	/**
	 * decoded UPC code
	 */
	private String upc;
	/**
	 * barcode passed to the constructor
	 */
	private String barcode;
	/**
	 * contains all possible number encodings (even- and odd-parity) as keys and
	 * their equivalent numbers as values for easy lookup
	 */
	private HashMap<String,Integer> keyMap = new HashMap<>();
	/**
	 * encoded equivalents of each UPC code number, in the format
	 * [upc number][left (0) or right (1) of center]
	 */
	private String[][] key = {{ "0001101","1110010"},{"0011001","1100110"},{"0010011","1101100"},
			{"0111101","1000010"},{"0100011","1011100"},{"0110001","1001110"},{"0101111","1010000"},
			{"0111011","1000100"},{"0110111","1001000"},{"0001011","1110100"}};
	
	/**
	 * Constructs a new UPC_Decode with this barcode and calls populateHashMap, decode, and verifyChecksum
	 * 
	 * @param barcode the barcode to decode
	 */
	UPC_Decode(String barcode){
		this.barcode=barcode;
		upc="";
		populateHashMap();
		try{
			decode();
			boolean check=verifyChecksum();
			if(check){
				System.out.println("Check digit ("+upc.charAt(11)+") is valid");
			}
			else{
				throw new Exception();
			}
		}
		catch(IllegalArgumentException e){
			System.out.println("ERROR: Barcode Invalid");
		}
		catch(Exception e){
			System.out.println("ERROR: Incorrect checksum");
		}
	}
	
	/**
	 * Returns the original barcode
	 * @return this barcode passed to constructor
	 */
	public String getBarcode(){
		return barcode;
	}
	
	/**
	 * Returns the decoded UPC
	 * @return this decoded UPC
	 */
	public String getUPC(){
		return upc.substring(0,11);
	}
	
	/**
	 * Makes sure this barcode is valid length, reverses if the barcode was "scanned" backwards,
	 * decodes the barcode to UPC format
	 */
	private void decode(){
		if(!((barcode.substring(0,3)).equals("101")) || !((barcode.substring(92)).equals("101"))){
			throw new IllegalArgumentException();
		}
		barcode=barcode.substring(3,92);
		
		//check if reversed
		barcode=checkIfBackward(barcode);
		
		//first half
		for(int i=0;i<42;i+=7){
			String code=barcode.substring(i,i+7);
			
			if(keyMap.containsKey(code)){
				upc+=keyMap.get(code);
			}
			else{
				throw new IllegalArgumentException();
			}
		}
		if(!barcode.substring(42,47).equals("01010")){
			throw new IllegalArgumentException();
		}
		for(int i=47;i<89;i+=7){
			String code=barcode.substring(i,i+7);
			
			if(keyMap.containsKey(code)){
				upc+=keyMap.get(code);
			}
			else{
				throw new IllegalArgumentException();
			}
		}
	}
	
	
	/**
	 * Verifies checksum of barcode to ensure there were no errors in reading
	 * 
	 * @return true if barcode checksum is correct, otherwise false
	 */
	private boolean verifyChecksum(){
		char[] charArray = upc.substring(0,11).toCharArray();
		int total = 0;
		for (int i=0;i<11;i+=2){
			total+=(charArray[i]-48);
		}
		total*=3;
		for(int i=1;i<11;i+=2){
			total+=charArray[i]-48;
		}
		total %= 10;
		total=10-total;
		return (total+48==upc.charAt(11));
	}
	
	
	/**
	 * Populates HashMap used for easy lookup while decoding
	 */
	private void populateHashMap(){
		for(int i=0;i<10;i++){
			keyMap.put(key[i][0],i);
			keyMap.put(key[i][1],i);
		}
	}
	
	/**
	 * Checks if barcode was scanned in correct direction, reverses barcode if
	 * not
	 * 
	 * @param barcode the barcode to check 
	 * @return original barcode if correct direction, reversed barcode if
	 * 			barcode was scanned upside down
	 */
	private String checkIfBackward(String barcode){
		String reverseCheck=barcode.substring(0,7);
		char[] codeArr=reverseCheck.toCharArray();
		int value=0;
		for(int i=0;i<7;i++){
			value+=(codeArr[i]-48);
		}
		if((value%2)==1){	//odd parity, not backward
			return barcode;
		}
		//even parity, need to flip
		char[] code=barcode.toCharArray();
		for(int i=0;i<=(barcode.length()/2);i++){
			char temp=code[i];
			code[i]=code[barcode.length()-i];
			code[barcode.length()-i]=temp;
		}
		String reversedBarcode=new String(code);
		return reversedBarcode;
	}
	
}
