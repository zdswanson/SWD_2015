import java.awt.GridLayout;
import java.util.InputMismatchException;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * Creates a GUI that converts between arabic and roman numeral systems in real time
 * based on user input
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class ArabicToRomanGUI extends JFrame{
    /**
     * contains arabic I/O box
     */
    private final JPanel arabicPanel;
    /**
     * contains roman I/O box
     */
    private final JPanel romanPanel;
    /**
     * used to input/display arabic numbers
     */
    private final JTextArea arabicBox;
    /**
     * used to input/display roman numbers
     */
    private final JTextArea romanBox;
    /**
     * used to display big = in middle of GUI
     */
    private final JLabel middle;
    /**
     * holds arabic value equivalents for all roman special cases
     */
    private static final int[] arabicValues={1000,  900,500, 400,100,  90, 50,  40, 10,   9,  5,   4,1};
    /**
     * holds all roman special cases by decreasing value
     */
    private static final String[] romanValues={"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
    /**
     * flag variable used to prevent the modification of the current output box from running it's own handler
     */
    private boolean updating;
    
    
    /**
     * Creates new ArabicToRomanGUI
     */
    public ArabicToRomanGUI(){
    	super("ArabicToRomanGUI");
    	arabicPanel=new JPanel();
    	arabicPanel.setLayout(new GridLayout(3,1,5,5));
    	romanPanel=new JPanel();
    	romanPanel.setLayout(new GridLayout(3,1,5,5));
    	JLabel arabicLabel = new JLabel("Arabic: ");
        JLabel romanLabel = new JLabel("Roman: ");
    	middle=new JLabel("=");
    	middle.setFont(new Font("MS UI Gothic", Font.PLAIN, 40));
    	middle.setHorizontalAlignment(JLabel.CENTER);
    	arabicBox = new JTextArea();
    	romanBox = new JTextArea();
    	arabicBox.setText("");
    	romanBox.setText("");
    	romanPanel.add(romanLabel,BorderLayout.NORTH);
        romanPanel.add(romanBox, BorderLayout.CENTER);
        arabicPanel.add(arabicLabel,BorderLayout.NORTH);
        arabicPanel.add(arabicBox, BorderLayout.CENTER);
        
        ArabicHandler arabicHandler = new ArabicHandler();
        RomanHandler romanHandler = new RomanHandler();
        
        romanBox.getDocument().addDocumentListener(romanHandler);
        arabicBox.getDocument().addDocumentListener(arabicHandler);
        
        add(arabicPanel,BorderLayout.NORTH);
        add(middle,BorderLayout.CENTER);
        add(romanPanel,BorderLayout.SOUTH);
    }

    
    /**
     * Implements a custom DocumentEvent handler for any time roman box is modified by the user
     * 
     * @author Zach Swanson
     * @version 1.0
     */
    private class RomanHandler implements DocumentListener{
		@Override
		public void changedUpdate(DocumentEvent event) {
			handleChange(event.getDocument());
		}
	
		@Override
		public void insertUpdate(DocumentEvent event) {
			handleChange(event.getDocument());
	
		}
	
		@Override
		public void removeUpdate(DocumentEvent event) {
			handleChange(event.getDocument());
	
		}
		
		/**
		 * Makes sure the roman box is being used as input, not output. If so, calls calculateArabic. 
		 * Called by changedUpdate, insertUpdate, removeUpdate
		 * 
		 * @param doc Document from object that triggered DocumentEvent
		 */
		public void handleChange(Document doc){
			if(!updating){
				try{
					updating=true;
					String str=calculateArabic(romanBox.getText());
					arabicBox.setText(str);
				}
				catch(InputMismatchException e){
					arabicBox.setText("ERROR: Invalid roman numeral");
				}
				
				updating=false;
			}
		}
		/**
		 * Calculates arabic equivalent to this roman numeral
		 * 
		 * @param roman		numeral to convert
		 * @return			arabic equivalent as string
		 * @throws InputMismatchException Invalid roman numeral
		 */
		private String calculateArabic(String roman) throws InputMismatchException{
		    int indexTrip=0;
		    int countTrip=0;
		    boolean changeTrip=false;;
			int arabic=0;
			roman=roman.toUpperCase();
			
			for(int i=0;i<roman.length();i++){
				changeTrip = false;
				if(roman.length()-i>=2){
					switch(roman.substring(i,i+2)){		//????is this the right substring
					case "CM":
						if (indexTrip < 1){
							arabic+=900;
							i++;
							indexTrip = 4;
							countTrip = 0;
							changeTrip = true;
						}
						else{
							throw new InputMismatchException("1");	
						}
						break;
					case "CD":
						if (indexTrip < 3){
							arabic+=400;
							i++;
							indexTrip = 4;
							countTrip = 0;
							changeTrip = true;
						}
						else{
							throw new InputMismatchException("2");	
						}
						break;
					case "XC":
						if (indexTrip < 5){
							arabic+=90;
							i++;
							indexTrip = 8;
							countTrip = 0;
							changeTrip = true;
						}
						else{
							throw new InputMismatchException("3");	
						}
						break;
					case "XL":
						if (indexTrip < 7){
							arabic+=40;
							i++;
							indexTrip = 8;
							countTrip = 0;
							changeTrip = true;
						}
						else{
							throw new InputMismatchException("4");	
						}
						break;
					case "IX":
						if (indexTrip < 9){
							arabic+=9;
							i++;
							indexTrip = 12;
							countTrip = 0;
							changeTrip = true;
						}
						else{
							throw new InputMismatchException("5");	
						}
						break;
					case "IV":
						if (indexTrip < 11){
							arabic+=4;
							i++;
							indexTrip = 12;
							countTrip = 0;
							changeTrip = true;
						}
						else{
							throw new InputMismatchException("6");	
						}
						break;
					default:
						break;
					}
				}
				if(!changeTrip){
					switch(roman.charAt(i)){
					case 'M':
						if (indexTrip < 0){
							arabic+=1000;
							indexTrip = 0;
							countTrip = 1;
						}
						else if (indexTrip == 0){
							arabic+=1000;
							countTrip++;
						}
						else{
						    throw new InputMismatchException("7");	
						}
						break;
					case 'D':
						if (indexTrip < 2){
							arabic+=500;
							indexTrip = 2;
							countTrip = 1;
						}
						else if (indexTrip == 2){
							arabic+=500;
							countTrip++;
						}
						else{
						    throw new InputMismatchException("8");	
						}
						break;
					case 'C':
						if (indexTrip < 4){
							arabic+=100;
							indexTrip = 4;
							countTrip = 1;
						}
						else if (indexTrip == 4){
							arabic+=100;
							countTrip++;
						}
						else{
						    throw new InputMismatchException("9");	
						}
						break;
					case 'L':
						if (indexTrip < 6){
							arabic+=50;
							indexTrip = 6;
							countTrip = 1;
						}
						else if (indexTrip == 6){
							arabic+=50;
							countTrip++;
						}
						else{
						    throw new InputMismatchException("10");	
						}
						break;
					case 'X':
						if (indexTrip < 8){
							arabic+=10;
							indexTrip = 8;
							countTrip = 1;
						}
						else if (indexTrip == 8){
							arabic+=10;
							countTrip++;
						}
						else{
						    throw new InputMismatchException("11");	
						}
						break;
					case 'V':
						if (indexTrip < 10){
							arabic+=5;
							indexTrip = 10;
							countTrip = 1;
						}
						else if (indexTrip == 10){
							arabic+=5;
							countTrip++;
						}
						else{
						    throw new InputMismatchException("12");	
						}
						break;
					case 'I':
						if (indexTrip < 12){
							arabic+=1;
							indexTrip = 12;
							countTrip = 1;
						}
						else if (indexTrip == 12){
							arabic+=1;
							countTrip++;
						}
						else{
						    throw new InputMismatchException("13");	
						}
						break;
					default:
						throw new InputMismatchException("14");
					}
					if(countTrip>=4){
						throw new InputMismatchException("15");
					}
				}
			}
			return ""+arabic;
		}
    }

    /**
     * Implements a custom DocumentEvent handler for any time arabic box is modified by the user
     * 
     * @author Zach Swanson
     * @version 1.0
     */
    private class ArabicHandler implements DocumentListener{
		@Override
		public void changedUpdate(DocumentEvent event) {
			handleChange(event.getDocument());
		}
	
		@Override
		public void insertUpdate(DocumentEvent event) {
			handleChange(event.getDocument());
	
		}
	
		@Override
		public void removeUpdate(DocumentEvent event) {
			handleChange(event.getDocument());
	
		}
		
		/**
		 * Makes sure the arabic box is being used as input, not output. If so, calls calculateRoman. 
		 * Called by changedUpdate, insertUpdate, removeUpdate
		 * 
		 * @param doc Document from object that triggered DocumentEvent
		 */
		public void handleChange(Document doc){
			if(!updating){
				updating=true;
				String str=calculateRoman(arabicBox.getText());
				romanBox.setText(str);
				updating=false;
			}
		}
		
		/**
		 * Calculates roman numeral equivalent to this arabic number
		 * 
		 * @param arabic	number to convert
		 * @return			roman equivalent as string
		 */
		private String calculateRoman(String arabic){
			if(arabic.equals("")){
				return "";
			}
			String roman="";
			int arabicInt=Integer.parseInt(arabic);
			for(int i=0;i<13;i++){
				int charCount=arabicInt/arabicValues[i];
				if(charCount>0){
					while(charCount>0){
						roman+=romanValues[i];
						arabicInt-=arabicValues[i];
						charCount--;
					}
				}
			}
			return roman;
		}
    }
}
