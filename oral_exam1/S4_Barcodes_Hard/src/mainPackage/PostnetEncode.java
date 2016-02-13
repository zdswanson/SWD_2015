package mainPackage;

/**
 * Automatically encodes the given postnet zip code to barcode format
 * 
 * @author Zach Swanson
 * @version 1.0
 *
 */
public class PostnetEncode {
	/**
	 * original zipcode passed to constructor
	 */
	final private String originalZip;
	/**
	 * copy of the zipcode that is modified as the decoding occurs
	 */
	private String zip;
	/**
	 * barcode checksum
	 */
	private int checkSum;
	/**
	 * encoded barcode
	 */
	private String barcode;
	/**
	 * holds the encoded equivalents of each zipcode number at that
	 * number's index
	 */
	private String[] key={"11000","00011","00101","00110","01001","01010","01100","10001","10010","10100"};
	
	/**
	 * Constructs a new PostnetEncode with this zipcode and calls encode
	 * @param zip the zipcode to encode
	 */
	PostnetEncode(String zip){
		this.zip=zip;
		originalZip=zip;
		barcode="";
		try{
			encode();
		}
		catch(IllegalArgumentException e){
			System.out.println("ERROR: zipcode Invalid");
		}
	}
	
	/**
	 * Makes sure this zipcode is valid length, then computes checksum and encodes to barcode format
	 * @throws IllegalArgumentException zipcode is not a valid length
	 */
	private void encode() throws IllegalArgumentException{
		zip=zip.replaceAll("\\D", "");
		if(zip.length()!=5&&zip.length()!=9&&zip.length()!=11){
			throw new IllegalArgumentException();
		}
		checkSum=computeChecksum();
		zip+=checkSum;
		barcode="1";
		for(int i=0;i<zip.length();i++){
			barcode+=key[zip.charAt(i)-48];
		}
		barcode+="1";
	}
	
	/**
	 * Computes checksum for this zipcode
	 * @return computed single digit checksum
	 */
	private int computeChecksum(){
		char[] charArray = zip.toCharArray();
		int total=0;
		for(char x:charArray){
			total+=x-48;
		}
		total%=10; 
		return 10-total;
	}
	
	/**
	 * Prints barcode to console in .| and 01 format
	 */
	public void displayBarcode(){
		System.out.println();
		for(int i=0;i<barcode.length();i++){
			if(barcode.charAt(i)=='1'){
				System.out.print("|");
			}
			else{
				System.out.print(".");
			}
		}
		System.out.println("\n"+barcode);
	}
	
	
	/**
	 * Returns the original zip
	 * @return this zip passed to constructor
	 */
	public String getZip(){
		return originalZip;
	}
	
	/**
	 * Returns the encoded barcode
	 * @return this barcode encoded in 01 form
	 */
	public String getBarcode(){
		return barcode;
	}
}
