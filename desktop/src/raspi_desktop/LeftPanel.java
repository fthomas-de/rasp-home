/**
 * 
 */
package raspi_desktop;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author fthomas
 *
 */
public class LeftPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public LeftPanel(ButtonsPanel buttonsPanel, ControlPanel controlPanel, PiConnector pc) {
		setLayout(new BorderLayout());

		ImageIcon statusIcon = new ImageIcon("img/lampe.png");
		JLabel statusLabel = new JLabel(statusIcon);

		add(buttonsPanel, BorderLayout.NORTH);
		add(controlPanel, BorderLayout.SOUTH);
		add(statusLabel, BorderLayout.CENTER);
		
		Runnable daemon = new StatusDaemon(this);
		Thread thread = new Thread(daemon);
		thread.start();
	}
}
