package com.uf.cise.sp14.cnt.project.networkDataManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.uf.cise.sp14.cnt.project.protocolManager.MessageGenerator;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.HandshakeMsg;
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
 
    public Client(String host, Integer port) {
		ApplicationUtils.printLine("Creating client to: " + host + ":" + port);
    	targetHost = host;
    	targetPort = port;
    }
 
    /**
     * This method sends out a Handshake message to the targeted peer.
     */
    public void sendHandshake(Integer peerID) {
    	// Keep trying to connect to server until we succeed.
        while (!isConnected) {
            try {
                socket = new Socket(targetHost, targetPort);
                isConnected = true;
                
                ApplicationUtils.printLine("Client has connected to the server: " + targetHost + ":" + targetPort);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                
                HandshakeMsg msg = MessageGenerator.getHandshakeMessage(peerID);

                ApplicationUtils.printLine("Object to be written = " + msg);
                outputStream.writeObject(msg);
                
                socket.close();
            } catch (SocketException se) {
                se.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ApplicationUtils.printLine("Write of Object is complete!");
    }
}
