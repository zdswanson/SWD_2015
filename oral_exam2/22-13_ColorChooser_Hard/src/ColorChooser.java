import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Creates JPanel that contains sliders and text fields for R G and B values, allowing the user to modify
 * either slider or box and displaying the value of the slider in the associated box. Also displays a color 
 * swatch of the current color at the bottom of the panel
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class ColorChooser extends JPanel implements ChangeListener,ActionListener{
	/**
	 * Slider controlling R value
	 */
	private final JSlider redSlider;
	/**
	 * Text field controlling/displaying R value
	 */
	private final JTextField redField;
	/**
	 * Label showing which slider/text field is for R value
	 */
	private final JLabel redLabel;
	/**
	 * Current R value
	 */
	private int red;
	/**
	 * Slider controlling G value
	 */
	private final JSlider greenSlider;
	/**
	 * Text field controlling/displaying G value
	 */
	private final JTextField greenField;
	/**
	 * Label showing which slider/text field is for G value
	 */
	private final JLabel greenLabel;
	/**
	 * Current G value
	 */
	private int green;
	/**
	 * Slider controlling B value
	 */
	private final JSlider blueSlider;
	/**
	 * Text field controlling/displaying B value
	 */
	private final JTextField blueField;
	/**
	 * Label showing which slider/text field is for B value
	 */
	private final JLabel blueLabel;
	/**
	 * Current B value
	 */
	private int blue;
	/**
	 * Color swatch displaying current RGB color
	 */
	private RectangleDrawer rectangle;
	
	/**
	 * Constructor instantiating JPanel
	 */
	public ColorChooser(){
		this.setLayout(new GridLayout(4,3));
		redSlider=new JSlider(0,255);
		greenSlider=new JSlider(0,255);
		blueSlider=new JSlider(0,255);
		red=redSlider.getValue();
		green=greenSlider.getValue();
		blue=blueSlider.getValue();
		redField=new JTextField(""+red);
		greenField=new JTextField(""+green);
		blueField=new JTextField(""+blue);
		redLabel=new JLabel("Red:");
		greenLabel=new JLabel("Green:");
		blueLabel=new JLabel("Blue:");
		
		redSlider.addChangeListener(this);
		greenSlider.addChangeListener(this);
		blueSlider.addChangeListener(this);
		
		redField.addActionListener(this);
		greenField.addActionListener(this);
		blueField.addActionListener(this);
		
		rectangle=new RectangleDrawer(red,green,blue);
		
		this.add(redLabel);
		this.add(redSlider);
		this.add(redField);
		this.add(greenLabel);
		this.add(greenSlider);
		this.add(greenField);
		this.add(blueLabel);
		this.add(blueSlider);
		this.add(blueField);
		this.add(new JLabel(""));		//used to center the color swatch at the bottom by filling the bottom left GridLayout spot with nothing
		this.add(rectangle);
		
	}

	/**
     * Handles user input into any of the text boxes
     * 
     * @param e the event that triggered the ActionListener
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			red=Integer.parseInt(redField.getText());
			green=Integer.parseInt(greenField.getText());
			blue=Integer.parseInt(blueField.getText());
			if(red>255||red<0||green>255||green<0||blue>255||blue<0){
				red=redSlider.getValue();
				green=greenSlider.getValue();
				blue=blueSlider.getValue();
				throw new IllegalArgumentException();
			}
		}
		catch(NumberFormatException err){
			JOptionPane.showMessageDialog(this,"Enter an integer between 0-255");
		}
		catch(IllegalArgumentException err){
			JOptionPane.showMessageDialog(this,"Enter an integer between 0-255");
		}
		
		redSlider.setValue(red);
		greenSlider.setValue(green);
		blueSlider.setValue(blue);
		
		rectangle.setRed(red);
		rectangle.setGreen(green);
		rectangle.setBlue(blue);
	}

	/**
     * Handles user change of any of the sliders
     * 
     * @param arg0 the event that triggered the ChangeListener
     */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		red=redSlider.getValue();
		green=greenSlider.getValue();
		blue=blueSlider.getValue();
		
		redField.setText(""+red);
		greenField.setText(""+green);
		blueField.setText(""+blue);
		
		rectangle.setRed(red);
		rectangle.setGreen(green);
		rectangle.setBlue(blue);
	}
}
