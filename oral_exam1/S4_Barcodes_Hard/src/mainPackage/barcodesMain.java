package mainPackage;
import javax.swing.JFrame;
import java.util.Scanner;


/**
 * Main driver class that tests PostnetDecode, PostnetEncode, UPC_Decode 
 * UPC_Encode, and barcodesGUI (QR encode)
 * 
 * @author Zach Swanson
 * @version 1.0
 *
 */
public class barcodesMain {
	
	/**
	 * This method will automatically be called when barcodesMain is run.
	 * @param args Command line arguments (none expected)
	 */
	public static void main(String[] args){
		Scanner r=new Scanner(System.in);
		System.out.println("Postnet: ");
		System.out.print("Enter valid postnet code to encode: ");
		String str=r.nextLine();
		//PostnetEncode e=new PostnetEncode("55555-1237-99");
		PostnetEncode e=new PostnetEncode(str);
		System.out.println(e.getZip());
		e.displayBarcode();
		System.out.print("Enter valid postnet barcode to decode: ");
		str=r.nextLine();
		PostnetDecode q=new PostnetDecode(str);
		//PostnetDecode q=new PostnetDecode(e.getBarcode());
		q.displayZip();
		
		
		
		System.out.println("\n\nUPC-A: ");
		System.out.print("Enter valid UPC-A code to encode (without checksum, 11 digits): ");
		str=r.nextLine();
		UPC_Encode f=new UPC_Encode(str);
		//UPC_Encode f=new UPC_Encode("01254667375");
		System.out.println(f.getBarcode());
		
		System.out.print("Enter valid UPC-A barcode to decode: ");
		//UPC_Decode g=new UPC_Decode(f.getBarcode());
		str=r.nextLine();
		UPC_Decode g=new UPC_Decode(str);
		System.out.println("UPC: "+g.getUPC());
		
		
		
		barcodesGUI gui=new barcodesGUI();
	    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    gui.setSize(427, 480); 
	    gui.setVisible(true); 
	}
}
