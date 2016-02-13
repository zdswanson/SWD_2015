package mainPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


/**
 * Creates a GUI which creates and displays QR codes based on user input
 * 
 * @author Zach Swanson
 * @version 1.0
 */
public class barcodesGUI extends JFrame implements ActionListener{
	/**
	 * holds the qrInBox and button which are used to get user input
	 */
    private final JPanel qrPanel;
    /**
	 * holds the QR code image
	 */
    private final JPanel imagePanel;
    /**
     * where the user inputs the string they want encoded to QR
     */
    private final JTextField qrInBox;
    /**
     * labels the qrInBox as such
     */
    private final JLabel qrIn;
    /**
     * triggers the ActionListener which causes a new QR code to be created
     */
    private final JButton button;
    
    /**
     * Creates and arranges the GUI
     */
    public barcodesGUI(){
    	super("QR GUI");
        qrPanel=new JPanel();
    	qrIn = new JLabel("URL: ");
    	qrInBox = new JTextField(20);
    	
        button=new JButton("Generate");
        button.addActionListener(this);
        imagePanel=new JPanel();
        qrPanel.add(qrIn,BorderLayout.WEST);
        qrPanel.add(qrInBox,BorderLayout.CENTER);
        qrPanel.add(button, BorderLayout.EAST);
        
        add(imagePanel,BorderLayout.CENTER);
        add(qrPanel,BorderLayout.SOUTH);
    }
    
    
    /**
     * Generates and displays new QR code based on the current input String
     */
    @Override
	public void actionPerformed(ActionEvent e) {
		String url=qrInBox.getText();
		QRCodeWriter writer=new QRCodeWriter();
		try {
			BitMatrix qrCode=writer.encode(url, BarcodeFormat.QR_CODE, 400, 400);
			BufferedImage qrImage=MatrixToImageWriter.toBufferedImage(qrCode);
			JLabel picLabel=new JLabel(new ImageIcon(qrImage));
			imagePanel.removeAll();
			imagePanel.add(picLabel);
			imagePanel.revalidate();
			imagePanel.repaint();
		} catch (WriterException e1) {
			System.out.println("ERROR"+e1);
		}
	}
}