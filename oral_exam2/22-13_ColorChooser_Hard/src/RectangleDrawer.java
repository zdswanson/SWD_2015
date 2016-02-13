import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Maintains and displays current color swatch
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class RectangleDrawer extends JPanel {
	/**
	 * RGB Color object
	 */
	private Color color;
	/**
	 * R value (0-255)
	 */
	private int red;
	/**
	 * G value (0-255)
	 */
	private int green;
	/**
	 * B value (0-255)
	 */
	private int blue;
	
	/**
	 * Constructor
	 * 
	 * @param r starting R value
	 * @param g starting G value
	 * @param b starting B value
	 */
	public RectangleDrawer(int r,int g,int b){
		red=r;
		green=g;
		blue=b;
		color=new Color(red,green,blue);
	}
	/**
	 * Updates RectangleDrawer's R value and repaints rectangle
	 * 
	 * @param r new R value (0-255)
	 */
	public void setRed(int r){
		red=r;
		repaint();
	}
	/**
	 * Updates RectangleDrawer's G value and repaints rectangle
	 * 
	 * @param g new G value (0-255)
	 */
	public void setGreen(int g){
		green=g;
		repaint();
	}
	/**
	 * Updates RectangleDrawer's B value and repaints rectangle
	 * 
	 * @param b new B value (0-255)
	 */
	public void setBlue(int b){
		blue=b;
		repaint();
	}
	
	/**
	 * Paints rectangle with current Color
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent (Graphics g){
		super.paintComponent(g);
		color=new Color(red,green,blue);
		g.setColor(color);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
