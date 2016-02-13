package mainPackage;

/**
 * Automatically encodes the given UPC code to barcode format
 * 
 * @author Zach Swanson
 * @version 1.0
 *
 */
public class UPC_Encode {
	/**
	 * original UPC code passed to constructor
	 */
	final private String originalUPC;
	/**
	 * copy of the UPC code that is modified as the encoding occurs
	 */
	private String upc;
	/**
	 * barcode checksum
	 */
	private int checkSum;
	/**
	 * encoded barcode
	 */
	private String barcode;
	/**
	 * holds the encoded equivalents of each UPC code number, in the format
	 * [upc number][left (0) or right (1) of center]
	 */
	private String[][] key = {{ "0001101","1110010"},{"0011001","1100110"},{"0010011","1101100"},
			{"0111101","1000010"},{"0100011","1011100"},{"0110001","1001110"},{"0101111","1010000"},
			{"0111011","1000100"},{"0110111","1001000"},{"0001011","1110100"}};
	
	
	/**
	 * Constructs a new UPC_Encode with this UPC and calls encode
	 * @param upc the UPC to encode
	 */
	UPC_Encode(String upc){
			this.upc=upc;
			originalUPC=upc;
			upc="";
			try{
				encode();
			}
			catch(IllegalArgumentException e){
				System.out.println("ERROR: zipcode Invalid");
			}
		}

	
	/**
	 * Makes sure this UPC is valid length, then computes checksum and encodes to barcode format
	 * @throws IllegalArgumentException UPC code is invalid length
	 */
	private void encode() throws IllegalArgumentException{
		upc = upc.replaceAll("\\D", "");
		if(upc.length()!=11){
			throw new IllegalArgumentException();
		}
		checkSum = computeChecksum();
		upc += checkSum;
		barcode = "101";
		for (int i = 0; i < 6; i++) {
			barcode += key[upc.charAt(i) - 48][0];
		}
		barcode+="01010";
		for (int i = 6; i < 12; i++) {
			barcode += key[upc.charAt(i) - 48][1];
		}
		barcode += "101";
	}

	/**
	 * Computes checksum for this UPC code
	 * @return computed single digit checksum
	 */
	private int computeChecksum() {
		char[] charArray = upc.toCharArray();
		int total = 0;
		for (int i=0;i<11;i+=2){
			total+=(charArray[i]-48);
		}
		total*=3;
		for(int i=1;i<11;i+=2){
			total+=charArray[i]-48;
		}
		total %= 10;
		return 10 - total;
	}

	/**
	 * Returns the encoded barcode
	 * @return this barcode encoded in 01 form
	 */
	public String getBarcode() {
		return barcode;
	}
	
	/**
	 * Returns the original UPC
	 * @return this UPC passed to constructor
	 */
	public String getUPC() {
		return originalUPC;
	}

}
