/**
 * 
 */
package raspi_desktop;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton clearButton;
	private JButton exitButton;
	private TextPanel textPanel;

	public ButtonsPanel() {

		setLayout(new FlowLayout());

		clearButton = new JButton("Clear");
		exitButton = new JButton("Exit");

		clearButton.addActionListener(this);
		exitButton.addActionListener(this);

		add(clearButton);
		add(exitButton);
	}

	public void setTextPanel(TextPanel textPanel) {
		this.textPanel = textPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JButton buttonPressed = (JButton) e.getSource();

		if (buttonPressed.equals(clearButton)) {
			textPanel.clearTextInPanel();
		}

		else if (buttonPressed.equals(exitButton)) {
			System.exit(0);
		}

	}
}