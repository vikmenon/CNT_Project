package com.uf.cise.sp14.cnt.project.networkDataManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.uf.cise.sp14.cnt.project.protocolManager.messages.Message;
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

	/**
	 * This method sets up the server, and waits for a client to connect to it.
	 */
	public Server(Integer myPort) {
		ApplicationUtils.logServerPortEvent("Creating server on port: " + myPort, myPort);
		port = myPort;
		
		try {
			// Bind a server socket to this port
			serverSocket = new ServerSocket(port);
			
			// Listen for incoming connections
			socket = serverSocket.accept();
			inStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ApplicationUtils.logServerPortEvent("Server on " + port + " has connected to a client.", port);
	}

	/**
     * This message reads a Message sent by the connected client.
     */
	public Message waitForMessage() {
		Message message = null;
		try {
			message = (Message) inStream.readObject();
			ApplicationUtils.logServerPortEvent("Object received = " + message, port);
		} catch (SocketException se) {
			ApplicationUtils.exit(0, "ERR: SocketException was thrown!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cn) {
			cn.printStackTrace();
		}
		
		return message;
	}

	/**
	 * This method tears down the server, and closes the socket bound to the port.
	 */
	public void close() {
		try {
			inStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ApplicationUtils.logServerPortEvent("Server is shutting down.", port);
	}
}
