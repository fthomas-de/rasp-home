package raspi_desktop;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusDaemon implements Runnable {

	private PiConnector pc;
	private JPanel leftPanel;

	private Boolean status = false;

	public StatusDaemon(JPanel leftPanel) {
		this.leftPanel = leftPanel;
		this.pc = new PiConnector();
	}

	@Override
	public void run() {
		while (true) {
			pc.connect("GET101");
			String response = pc.getResponse();
			System.out.println(response);
			if (response.equals("Cookies")) {
				if (!status) {
					status = true;
					ImageIcon statusIcon = new ImageIcon("img/lampeon.png");
					JLabel statusLabel = new JLabel(statusIcon);

					leftPanel.remove(2);
					leftPanel.revalidate();
					leftPanel.repaint();
					leftPanel.add(statusLabel, BorderLayout.CENTER);
				}
			} else {
				if (status) {
					status = false;
					System.out.println("else");
					ImageIcon statusIcon = new ImageIcon("img/lampe.png");
					JLabel statusLabel = new JLabel(statusIcon);

					leftPanel.remove(2);
					leftPanel.revalidate();
					leftPanel.repaint();
					leftPanel.add(statusLabel, BorderLayout.CENTER);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
