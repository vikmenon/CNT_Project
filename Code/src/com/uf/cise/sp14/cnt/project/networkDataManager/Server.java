package com.uf.cise.sp14.cnt.project.networkDataManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.uf.cise.sp14.cnt.project.protocolManager.messages.HandshakeMsg;
 
/**
 * @author Rahul
 *
 */
public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inStream = null;
 
    public Server() {}
 
    /**
     * 
     */
    public void communicate() {
        try {
            serverSocket = new ServerSocket(4445);
            socket = serverSocket.accept();
            System.out.println("Connected");
            inStream = new ObjectInputStream(socket.getInputStream());
 
            HandshakeMsg msg  = ( HandshakeMsg) inStream.readObject();
            System.out.println("Object received = " + msg);
            socket.close();
 
        } catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
        }
    }
 
    /**
     * @param args
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.communicate();
    }
}