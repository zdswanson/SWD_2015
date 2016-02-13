package mainPackage;

/**
 * Automatically decodes the given barcode to correct postnet format
 * 
 * @author Zach Swanson
 * @version 1.0
 *
 */
public class PostnetDecode {
	/**
	 * decoded postnet zip
	 */
	private String zip;
	/**
	 * barcode checksum
	 */
	private int checkSum;
	/**
	 * barcode passed to the constructor
	 */
	private String barcode;
	/**
	 * holds the value of a 1 at each index and is used to calculate the encoded 
	 * zip number based on this modified binary encoding
	 */
	private int[] valueKey={7,4,2,1,0};
	
	/**
	 * Constructs a new PostnetDecode with this barcode and calls decode, verfiyChecksum, and formatZip
	 * @param barcode the barcode to decode
	 */
	PostnetDecode(String barcode){
		this.barcode=barcode;
		zip="";
		int tempLength=barcode.length();
		if(tempLength==32||tempLength==52||tempLength==62){
			try{
				decode();
				verifyChecksum();
				formatZip();
			}
			catch(IllegalArgumentException e){
				System.out.println("ERROR: Barcode Invalid");
			}
			catch(Exception e){
				System.out.println("ERROR: Incorrect checksum");
			}
		}
		else{
			System.out.println("ERROR: invalid length");
		}
	}
	
	/**
	 * Encodes this zip to barcode format
	 * @throws IllegalArgumentException corrupted/invalid barcode
	 */
	private void decode() throws IllegalArgumentException{
		barcode=barcode.substring(1,barcode.length()-1);
		for(int i=0;i<barcode.length();i+=5){
			String code=barcode.substring(i,i+5);
			char[] newCode=code.toCharArray();
			int value=0;
			for(int j=0;j<5;j++){
				value+=(newCode[j]-48)*valueKey[j];
			}
			if(value<12){
				if(value==11){
					zip+="0";
				}
				else{
					zip+=value;
				}
			}
			else{
				throw new IllegalArgumentException();
			}
		}
	}
	/**
	 * Verifies checksum of barcode to ensure there were no errors in reading
	 * 
	 * @throws Exception checksum was incorrect; barcode corrupt
	 * */
	private void verifyChecksum() throws Exception{
		checkSum=(zip.charAt(zip.length()-1))-48;
		zip=zip.substring(0,zip.length()-1);
		
		char[] charArray = zip.toCharArray();
		int total=0;
		for(char x:charArray){
			total+=x-48;
		}
		total%=10;
		total=10-total;
		if(total==checkSum){
			System.out.println("checksum verified");
		}
		else{
			throw new Exception();
		}
	}
	/**
	 * Adds proper dashes to zip depending on version
	 */
	private void formatZip(){
		int length=zip.length();
		if(length>5){
			zip=zip.substring(0,5)+"-"+zip.substring(5);
			if(length>9){
				zip=zip.substring(0,10)+"-"+zip.substring(10);
			}
		}
	}
	/**
	 * Prints decoded zip to console
	 */
	public void displayZip(){
		System.out.println(zip+" checksum: "+checkSum);
	}
	/**
	 * Returns the formatted zip
	 * @return this formatted zip
	 */
	public String getZip(){
		return zip;
	}
	/**
	 * Returns the original barcode
	 * @return this barcode passed to constructor
	 */
	public String getBarcode(){
		return barcode;
	}
}
