/**
 * 
 */
package raspi_desktop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author fthomas
 *
 */
public class ControlPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PiConnector pc;

	private JButton restartButton;

	/**
	 * 
	 */
	public ControlPanel(TextPanel textPanel, PiConnector pc) {
		this.pc = pc;

		setLayout(new FlowLayout());

		restartButton = new JButton("Restart");
		restartButton.addActionListener(this);
		
		add(restartButton, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton buttonPressed = (JButton) e.getSource();

		if (buttonPressed.equals(restartButton)) {
			pc.connect("GETrst");
		}
	}
}
