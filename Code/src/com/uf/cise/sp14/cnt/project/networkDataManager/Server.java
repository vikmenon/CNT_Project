package com.uf.cise.sp14.cnt.project.networkDataManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.uf.cise.sp14.cnt.project.protocolManager.messages.HandshakeMsg;
import com.uf.cise.sp14.cnt.project.util.ApplicationUtils;

/**
 * @author Rahul
 * 
 * This class listen for incoming connections from peers.
 */
public class Server {
	private Integer port;

	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private ObjectInputStream inStream = null;

	public Server(Integer myPort) {
		ApplicationUtils.logServerPortEvent("Creating server on port: " + myPort, myPort);
		port = myPort;
	}

	/**
     *
     */
	public void waitForHandshake() {
		try {
			/* Listen for incoming connections. */
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();

			ApplicationUtils.printLine("Server has connected to a client.");
			ApplicationUtils.logServerPortEvent("Server on " + port + " has connected to a client.", port);

			inStream = new ObjectInputStream(socket.getInputStream());

			HandshakeMsg msg = (HandshakeMsg) inStream.readObject();

			ApplicationUtils.printLine("Object received = " + msg);
			ApplicationUtils.logServerPortEvent("Object received = " + msg, port);

			socket.close();
		} catch (SocketException se) {
			ApplicationUtils.exit(0, "ERR: SocketException was thrown!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cn) {
			cn.printStackTrace();
		}
	}
}
