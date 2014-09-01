/**
 * 
 */
package raspi_desktop;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * @author fthomas
 *
 */
public class MainFrame extends JFrame {

	/**
	 * private objects
	 */
	private static final long serialVersionUID = 1L;
	private ButtonsPanel buttonsPanel;
	private TextPanel textPanel;
	private ControlPanel controlPanel;
	private LeftPanel leftPanel;
	
	/**
	 * http://www.caveofprogramming.com/frontpage/articles/java-swing-gui-
	 * tutorial/beginning-swing/
	 */
	public MainFrame() {
		super("Mylilraspi Mate");
		setLayout(new BorderLayout());
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("img/icon.png").getImage());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		// create components
		PiConnector pc = new PiConnector();
		
		buttonsPanel = new ButtonsPanel();
		textPanel = new TextPanel();
		controlPanel = new ControlPanel(textPanel, pc);
		
		setJMenuBar(new Menu(textPanel, pc));
		buttonsPanel.setTextPanel(textPanel);
		
		leftPanel = new LeftPanel(buttonsPanel, controlPanel, pc);
		
		// add components
		add(leftPanel, BorderLayout.WEST);
		add(textPanel, BorderLayout.CENTER);

		setVisible(true);
	}
}
