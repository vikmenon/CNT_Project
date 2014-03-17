package com.uf.cise.sp14.cnt.project.networkDataManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.uf.cise.sp14.cnt.project.protocolManager.messages.Message;
import com.uf.cise.sp14.cnt.project.util.ApplicationUtils;
 
/**
 * @author Rahul
 *
 * This class reaches out to peers and sets up connections.
 */
public class Client {
	private String targetHost;
	private Integer targetPort;
	
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
 
    /**
     * This method sets up the connection to the targeted peer.
     */
    public Client(String host, Integer port) {
		ApplicationUtils.printLine("Creating client to: " + host + ":" + port + "...");
    	targetHost = host;
    	targetPort = port;
    	
    	// Keep trying to connect to server until we succeed.
        while (!isConnected) {
            try {
                socket = new Socket(targetHost, targetPort);
    			outputStream = new ObjectOutputStream(socket.getOutputStream());
                isConnected = true;
            } catch (SocketException se) {
                se.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
		ApplicationUtils.printLine("Client has connected to the server: " + targetHost + ":" + targetPort);
    }
 
    /**
     * This method sends out a Message to the targeted peer.
     */
    public void sendMessage(Message message) {
		try {
			ApplicationUtils.printLine("Object to be written = " + message);
			outputStream.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 
    /**
     * This method tears down the connection to the targeted peer.
     */
    public void close() {
		try {
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        ApplicationUtils.printLine("Client is shutting down.");
    }
}
