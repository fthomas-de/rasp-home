/**
 * 
 */
package raspi_desktop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

/**
 * @author fthomas
 *
 */
public class PiConnector {

	/**
	 * 
	 */
	public PiConnector() {
	}

	private String msgFromServer;

	/*
	 * SET A10 / A11 : on / off B20 / B21 : on / off
	 * 
	 * GET
	 * 
	 * lg1 : reootlog lg2 : shutdownlog rst : reboot pi 101 : onlinestatus AS1 :
	 * status A BS2 : status B
	 */
	public void connect(String msgForServer) {
		Users u = new Users();
		String hash = u.getUserID();
		msgForServer = msgForServer
				+ hash;
		Socket socket;
		String address = "192.168.0.111";

		final int PORT = 1892;
		int LOGLEN = 28;

		socket = new Socket();

		SocketAddress sockaddr = new InetSocketAddress(address, PORT);

		try {
			socket.connect(sockaddr, 1000);
			socket.setSoTimeout(1000);

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(msgForServer);
			out.flush();
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			this.msgFromServer = inFromServer.readLine();

			if (!this.msgFromServer.equals("Cookies")) {
				String log1 = "";
				for (int i = 0; i < LOGLEN; i++) {
					log1 = log1 + inFromServer.readLine()
							+ System.getProperty("line.separator");
					;
				}
				log1 = log1 + inFromServer.readLine();
				this.msgFromServer = log1;
				System.out.println("Server:\n'" + msgFromServer + "'");

				socket.close();
			}
		} catch (SocketTimeoutException e) {
			this.msgFromServer = "Client: SocketTimeoutException";

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 
	 */
	public String getResponse() {
		return msgFromServer;
	}

}
