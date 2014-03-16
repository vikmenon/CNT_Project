package com.uf.cise.sp14.cnt.project.networkDataManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.uf.cise.sp14.cnt.project.protocolManager.MessageGenerator;
import com.uf.cise.sp14.cnt.project.protocolManager.messages.HandshakeMsg;
 
/**
 * @author Rahul
 *
 */
public class Client {
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
 
    public Client() {
 
    }
 
    /**
     * 
     */
    public void communicate() {
 
        while (!isConnected) {
            try {
                socket = new Socket("localHost", 4445);
                System.out.println("Connected");
                isConnected = true;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                MessageGenerator msgGen = new MessageGenerator();
                //TODO for testing only
                HandshakeMsg msg = msgGen.getHandshakeMessage(1234);
                System.out.println("Object to be written = " + msg);
                outputStream.writeObject(msg);
 
 
            } catch (SocketException se) {
                se.printStackTrace();
                // System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    /**
     * @param args
     */
    public static void main(String[] args) {
        Client client = new Client();
        client.communicate();
    }
}