/**
 * 
 */
package raspi_desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * @author fthomas
 *
 */
public class Menu extends JMenuBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TextPanel textPanel;

	private JMenu file;
	private JMenu help;

	private JMenuItem open;
	private JMenuItem log1;
	private JMenuItem log2;
	private JMenuItem close;
	private JMenuItem about;

	private PiConnector pc;

	/**
	 * 
	 */
	public Menu(TextPanel textPanel, PiConnector pc) {
		this.textPanel = textPanel;
		this.pc = pc;

		file = new JMenu("File");
		help = new JMenu("Help");

		open = new JMenuItem("open");
		open.addActionListener(this);
		log1 = new JMenuItem("log1");
		log1.addActionListener(this);
		log2 = new JMenuItem("log2");
		log2.addActionListener(this);
		close = new JMenuItem("close");
		close.addActionListener(this);
		about = new JMenuItem("About");
		about.addActionListener(this);

		file.add(open);
		file.add(log1);
		file.add(log2);
		file.add(close);
		help.add(about);

		add(file);
		add(help);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == open) {
			System.out.println("open");
		} else if (e.getSource() == log1) {
			pc.connect("GETlg1");
			String log = pc.getResponse();
			textPanel.replaceTextInPanel(log);

		} else if (e.getSource() == log2) {
			pc.connect("GETlg2");
			String log = pc.getResponse();
			textPanel.replaceTextInPanel(log);

		} else if (e.getSource() == close) {
			System.exit(0);

		} else if (e.getSource() == about) {
			JFrame msg = new JFrame();
			JOptionPane
					.showMessageDialog(
							msg,
							"v.1 20.08.2014\nauthor: Florian Thomas\ngit: https://github.com/trudikampfschaf",
							"About", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
